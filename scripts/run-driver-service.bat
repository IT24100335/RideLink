@echo off
title RideLink - Driver and Vehicle Service (Port 8082)
cd /d "%~dp0\..\driver-vehicle-service"
echo ===================================================
echo Starting RideLink Driver and Vehicle Service on Port 8082...
echo Database: driver_db (MS SQL Server)
echo ===================================================
call mvn spring-boot:run
pause
