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

Write-Host "=== V6 - Erreurs métier ===" -ForegroundColor Cyan

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

$tests = @(
    @{ Nom = "Machine inexistante"; Machine = 999999; Material = 1; Quantite = 1 },
    @{ Nom = "Matiere inexistante"; Machine = $machineId; Material = 999999; Quantite = 1 },
    @{ Nom = "Stock insuffisant"; Machine = $machineId; Material = 1; Quantite = 999999 }
)

foreach ($test in $tests) {
    $body = @{
        machineId = $test.Machine
        controleurId = 3
        lignes = @(@{ rawMaterialId = $test.Material; quantite = $test.Quantite })
    }
    $key = [guid]::NewGuid().ToString()
    $result = Invoke-DemoRequest -Method POST -Uri "$ProductionUrl/lots/new" -Headers @{ idempotencyKey = $key } -Body $body
    Write-Host "$($test.Nom) -> HTTP $($result.Status), temps=$($result.Seconds)s"
    Write-Host $result.Body
}

