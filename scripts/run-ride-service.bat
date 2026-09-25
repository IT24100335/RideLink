@echo off
title RideLink - Ride Management Service (Port 8083)
set "ROOT_DIR=%~dp0.."
echo ===================================================
echo Starting RideLink Ride Management Service on Port 8083...
echo Database: ride_db (MS SQL Server)
echo Interservice Calls: Driver Service (8082), Fare Service (8084)
echo ===================================================
call "%ROOT_DIR%\mvnw.cmd" -pl ride-service spring-boot:run
pause
