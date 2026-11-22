param(
    [int]$LotId = 0
)

$ProductionUrl = "http://localhost:8082"
$AtelierUrl = "http://localhost:8081"

function Invoke-DemoRequest {
    param(
        [string]$Method,
        [string]$Uri,
        [object]$Body = $null,
        [hashtable]$Headers = @{}
    )

    $stopwatch = [System.Diagnostics.Stopwatch]::StartNew()
    $status = 0
    $responseBody = ""

    try {
        $jsonBody = $null
        if ($null -ne $Body) {
            $jsonBody = $Body | ConvertTo-Json -Depth 10
        }
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
            } catch {
                $responseBody = $_.Exception.Message
            }
        } else {
            $responseBody = $_.Exception.Message
        }
    }

    $stopwatch.Stop()
    [pscustomobject]@{
        Status = $status
        Seconds = [math]::Round($stopwatch.Elapsed.TotalSeconds, 3)
        Body = $responseBody
    }
}

Write-Host "=== V6 - Scenario normal ===" -ForegroundColor Cyan

$machinesResult = Invoke-DemoRequest -Method GET -Uri "$AtelierUrl/machines"
if ($machinesResult.Status -ne 200) {
    Write-Host "Impossible de lire les machines (HTTP $($machinesResult.Status))." -ForegroundColor Red
    Write-Host $machinesResult.Body
    exit 1
}

try {
    $machines = $machinesResult.Body | ConvertFrom-Json
} catch {
    Write-Host "Reponse machines invalide." -ForegroundColor Red
    exit 1
}

$machine = @($machines | Where-Object { $_.statut -eq "DISPONIBLE" } | Select-Object -First 1)
if ($machine.Count -eq 0) {
    Write-Host "Aucune machine DISPONIBLE trouvee sur $AtelierUrl/machines." -ForegroundColor Red
    exit 1
}

$machineId = [long]$machine[0].id
$idempotencyKey = [guid]::NewGuid().ToString()
$createBody = @{
    machineId = $machineId
    controleurId = 3
    lignes = @(@{ rawMaterialId = 1; quantite = 1 })
}

Write-Host "Machine disponible choisie : id=$machineId"
Write-Host "Cle d'idempotence generee : $idempotencyKey"
$createResult = Invoke-DemoRequest -Method POST -Uri "$ProductionUrl/lots/new" -Headers @{ idempotencyKey = $idempotencyKey } -Body $createBody
Write-Host "POST /lots/new -> HTTP $($createResult.Status), temps=$($createResult.Seconds)s"
Write-Host $createResult.Body

if ($createResult.Status -ne 201) {
    exit 1
}

try {
    $createdLot = $createResult.Body | ConvertFrom-Json
    $LotId = [int]$createdLot.id
} catch {
    if ($LotId -le 0) {
        Write-Host "Impossible d'extraire l'identifiant du lot cree." -ForegroundColor Red
        exit 1
    }
}

$detailResult = Invoke-DemoRequest -Method GET -Uri "$ProductionUrl/lots/$LotId/detail"
Write-Host "GET /lots/$LotId/detail -> HTTP $($detailResult.Status), temps=$($detailResult.Seconds)s"
Write-Host $detailResult.Body

if ($detailResult.Status -eq 200) {
    try {
        $detail = $detailResult.Body | ConvertFrom-Json
        Write-Host "machineNom=$($detail.machineNom), controleurNom=$($detail.controleurNom), controleurEmail=$($detail.controleurEmail)" -ForegroundColor Green
    } catch {
        Write-Host "Detail JSON invalide." -ForegroundColor Yellow
    }
}

