@echo off
title RideLink - Ride Management Service (Port 8083)
cd /d "%~dp0\..\ride-service"
echo ===================================================
echo Starting RideLink Ride Management Service on Port 8083...
echo Database: ride_db (MS SQL Server)
echo Interservice Calls: Driver Service (8082), Fare Service (8084)
echo ===================================================
call mvn spring-boot:run
pause
