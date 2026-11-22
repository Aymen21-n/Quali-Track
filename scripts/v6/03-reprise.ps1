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

Write-Host "=== V6 - Reprise du service atelier ===" -ForegroundColor Cyan
Read-Host "RELANCEZ service-atelier et attendez son réenregistrement dans Eureka, puis appuyez sur Entree"

$machinesCheck = Invoke-DemoRequest -Method GET -Uri "$AtelierUrl/machines"
if ($machinesCheck.Status -ne 200) {
    Write-Host "Lecture des machines impossible actuellement (HTTP $($machinesCheck.Status))." -ForegroundColor Yellow
} else {
    $machines = $machinesCheck.Body | ConvertFrom-Json
    $available = @($machines | Where-Object { $_.statut -eq "DISPONIBLE" })
    if ($available.Count -eq 0) {
        Write-Host "Aucune machine DISPONIBLE apres redémarrage : la base H2 est probablement vide." -ForegroundColor Yellow
        Read-Host "CRÉEZ une machine DISPONIBLE avec POST http://localhost:8081/machines, puis appuyez sur Entree"
    } else {
        Write-Host "Machines DISPONIBLE disponibles : $($available.Count)"
    }
}

$success = $false
for ($attempt = 1; $attempt -le 8; $attempt++) {
    $machinesResult = Invoke-DemoRequest -Method GET -Uri "$AtelierUrl/machines"
    $machineId = $null
    if ($machinesResult.Status -eq 200) {
        try {
            $machines = $machinesResult.Body | ConvertFrom-Json
            $machine = @($machines | Where-Object { $_.statut -eq "DISPONIBLE" } | Select-Object -First 1)
            if ($machine.Count -gt 0) { $machineId = [long]$machine[0].id }
        } catch { $machineId = $null }
    }

    if ($null -eq $machineId) {
        Write-Host "Essai $attempt/8 : aucune machine DISPONIBLE trouvée, HTTP métier attendu : 400/503" -ForegroundColor Yellow
    } else {
        $body = @{
            machineId = $machineId
            controleurId = 3
            lignes = @(@{ rawMaterialId = 1; quantite = 1 })
        }
        $key = [guid]::NewGuid().ToString()
        $result = Invoke-DemoRequest -Method POST -Uri "$ProductionUrl/lots/new" -Headers @{ idempotencyKey = $key } -Body $body
        Write-Host "Essai $attempt/8, machineId=$machineId -> HTTP $($result.Status), temps=$($result.Seconds)s"
        Write-Host $result.Body

        if ($result.Status -eq 201) {
            $success = $true
            Write-Host "Reprise confirmee : creation retournee en HTTP 201." -ForegroundColor Green
            break
        }
    }

    if ($attempt -lt 8) {
        Write-Host "Attente de 10 secondes avant le prochain essai..."
        Start-Sleep -Seconds 10
    }
}

if (-not $success) {
    Write-Host "Aucune création 201 obtenue après 8 essais." -ForegroundColor Red
    exit 1
}

