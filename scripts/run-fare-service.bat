@echo off
title RideLink - Fare and Payment Service (Port 8084)
cd /d "%~dp0\..\fare-payment-service"
echo ===================================================
echo Starting RideLink Fare and Payment Service on Port 8084...
echo Database: fare_db (MS SQL Server)
echo ===================================================
call mvn spring-boot:run
pause
