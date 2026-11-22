param(
    [int]$LotId = 1
)

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

Write-Host "=== V6 - Service atelier indisponible ===" -ForegroundColor Cyan

$machinesResult = Invoke-DemoRequest -Method GET -Uri "$AtelierUrl/machines"
if ($machinesResult.Status -ne 200) {
    Write-Host "Impossible de recuperer une machine avant l'arret (HTTP $($machinesResult.Status))." -ForegroundColor Red
    exit 1
}
$machines = $machinesResult.Body | ConvertFrom-Json
$machine = @($machines | Where-Object { $_.statut -eq "DISPONIBLE" } | Select-Object -First 1)
if ($machine.Count -eq 0) {
    Write-Host "Aucune machine DISPONIBLE avant l'arret. Lancez 01-normal.ps1 ou creez une machine." -ForegroundColor Red
    exit 1
}
$machineId = [long]$machine[0].id
Write-Host "Machine disponible memoiree pour le scenario : id=$machineId"

Write-Host ""
Read-Host "ARRETEZ MAINTENANT service-atelier, puis appuyez sur Entree"

Write-Host "GET du detail pendant l'indisponibilite : Retry + Circuit Breaker peuvent prendre plusieurs secondes..." -ForegroundColor Yellow
$detailResult = Invoke-DemoRequest -Method GET -Uri "$ProductionUrl/lots/$LotId/detail"
Write-Host "GET /lots/$LotId/detail -> HTTP $($detailResult.Status), temps=$($detailResult.Seconds)s"
Write-Host $detailResult.Body

if ($detailResult.Status -eq 200) {
    try {
        $detail = $detailResult.Body | ConvertFrom-Json
        Write-Host "Repli tolérant vérifié : machineNom=$($detail.machineNom), controleurNom=$($detail.controleurNom), controleurEmail=$($detail.controleurEmail)" -ForegroundColor Green
    } catch { Write-Host "Le corps du detail n'est pas lisible comme JSON." -ForegroundColor Yellow }
}

Write-Host ""
Write-Host "Executions de creation strictes avec 6 clés différentes :" -ForegroundColor Yellow
for ($i = 1; $i -le 6; $i++) {
    $key = [guid]::NewGuid().ToString()
    $body = @{
        machineId = $machineId
        controleurId = 3
        lignes = @(@{ rawMaterialId = 1; quantite = 1 })
    }
    $result = Invoke-DemoRequest -Method POST -Uri "$ProductionUrl/lots/new" -Headers @{ idempotencyKey = $key } -Body $body
    Write-Host "Creation $i -> HTTP $($result.Status), temps=$($result.Seconds)s"
    Write-Host $result.Body
}

