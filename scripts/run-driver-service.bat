@echo off
title RideLink - Driver and Vehicle Service (Port 8082)
set "ROOT_DIR=%~dp0.."
echo ===================================================
echo Starting RideLink Driver and Vehicle Service on Port 8082...
echo Database: driver_db (MS SQL Server)
echo ===================================================
call "%ROOT_DIR%\mvnw.cmd" -pl driver-vehicle-service spring-boot:run
pause
