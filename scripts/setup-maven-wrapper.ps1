# RideLink - Setup Maven Wrapper Script
# Downloads and initializes official Apache Maven Wrapper for zero-config building

$ErrorActionPreference = "Stop"
$MavenVersion = "3.9.9"
$WrapperDir = "$PSScriptRoot\..\.mvn\wrapper"

Write-Host "Creating Maven Wrapper directory at $WrapperDir..." -ForegroundColor Cyan
New-Item -ItemType Directory -Path $WrapperDir -Force | Out-Null

$PropertiesContent = @"
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MavenVersion/apache-maven-$MavenVersion-bin.zip
wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar
"@

Set-Content -Path "$WrapperDir\maven-wrapper.properties" -Value $PropertiesContent -Encoding ASCII

$JarUrl = "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
$JarPath = "$WrapperDir\maven-wrapper.jar"

if (-not (Test-Path $JarPath)) {
    Write-Host "Downloading maven-wrapper.jar from Apache Maven Repository..." -ForegroundColor Cyan
    try {
        Invoke-WebRequest -Uri $JarUrl -OutFile $JarPath
        Write-Host "maven-wrapper.jar downloaded successfully." -ForegroundColor Green
    } catch {
        Write-Warning "Could not download wrapper jar automatically. Ensure internet connection is active or install Maven globally."
    }
}

Write-Host "Maven wrapper setup complete." -ForegroundColor Green
