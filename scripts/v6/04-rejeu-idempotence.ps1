$ProductionUrl = "http://localhost:8082"
$AtelierUrl = "http://localhost:8081"

function Invoke-DemoRequest {
    param([string]$Method, [string]$Uri, [object]$Body = $null, [hashtable]$Headers = @{})
    $stopwatch = [System.Diagnostics.Stopwatch]::StartNew()
    $status = 0
    $responseBody = ""
    try {
        $jsonBody = $null
        if ($null -ne $Body) { $jsonBody = $Body | ConvertTo-Json -Depth 10 }
        $response = Invoke-WebRequest -Method $Method -Uri $Uri -Headers $Headers -Body $jsonBody -ContentType "application/json" -TimeoutSec 20
        $status = [int]$response.StatusCode
        $responseBody = [string]$response.Content
    } catch {
        if ($_.Exception.Response -ne $null) {
            $status = [int]$_.Exception.Response.StatusCode
            try {
                $stream = $_.Exception.Response.GetResponseStream()
                $reader = New-Object System.IO.StreamReader($stream)
                $responseBody = $reader.ReadToEnd()
            } catch { $responseBody = $_.Exception.Message }
        } else { $responseBody = $_.Exception.Message }
    }
    $stopwatch.Stop()
    [pscustomobject]@{ Status = $status; Seconds = [math]::Round($stopwatch.Elapsed.TotalSeconds, 3); Body = $responseBody }
}

Write-Host "=== V6 - Rejeu d'idempotence ===" -ForegroundColor Cyan

$before = Invoke-DemoRequest -Method GET -Uri "$ProductionUrl/lots"
if ($before.Status -ne 200) {
    Write-Host "Impossible de compter les lots avant la création (HTTP $($before.Status))." -ForegroundColor Red
    exit 1
}
$countBefore = @($before.Body | ConvertFrom-Json).Count

$machinesResult = Invoke-DemoRequest -Method GET -Uri "$AtelierUrl/machines"
if ($machinesResult.Status -ne 200) {
    Write-Host "Impossible de lire les machines (HTTP $($machinesResult.Status))." -ForegroundColor Red
    exit 1
}
$machines = $machinesResult.Body | ConvertFrom-Json
$machine = @($machines | Where-Object { $_.statut -eq "DISPONIBLE" } | Select-Object -First 1)
if ($machine.Count -eq 0) {
    Write-Host "Aucune machine DISPONIBLE trouvée." -ForegroundColor Red
    exit 1
}

$machineId = [long]$machine[0].id
$key = "v6-idempotence-fixed"
$body = @{
    machineId = $machineId
    controleurId = 3
    lignes = @(@{ rawMaterialId = 1; quantite = 1 })
}

$create = Invoke-DemoRequest -Method POST -Uri "$ProductionUrl/lots/new" -Headers @{ idempotencyKey = $key } -Body $body
Write-Host "Création initiale -> HTTP $($create.Status), temps=$($create.Seconds)s"
Write-Host $create.Body
if ($create.Status -ne 201) { exit 1 }

for ($i = 1; $i -le 2; $i++) {
    $replay = Invoke-DemoRequest -Method POST -Uri "$ProductionUrl/lots/new" -Headers @{ idempotencyKey = $key } -Body $body
    Write-Host "Rejeu $i avec EXACTEMENT la clé $key -> HTTP $($replay.Status), temps=$($replay.Seconds)s"
    Write-Host $replay.Body
}

$after = Invoke-DemoRequest -Method GET -Uri "$ProductionUrl/lots"
if ($after.Status -ne 200) {
    Write-Host "Impossible de compter les lots après les rejoues (HTTP $($after.Status))." -ForegroundColor Red
    exit 1
}
$countAfter = @($after.Body | ConvertFrom-Json).Count

Write-Host "Nombre de lots avant : $countBefore"
Write-Host "Nombre de lots après  : $countAfter"
if ($countAfter -eq $countBefore + 1) {
    Write-Host "Absence de doublon confirmée : un seul lot ajouté." -ForegroundColor Green
} else {
    Write-Host "Compte inattendu : un risque de doublon existe ou l'état initial a changé." -ForegroundColor Red
}

