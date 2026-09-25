@echo off
title RideLink - Fare and Payment Service (Port 8084)
set "ROOT_DIR=%~dp0.."
echo ===================================================
echo Starting RideLink Fare and Payment Service on Port 8084...
echo Database: fare_db (MS SQL Server)
echo ===================================================
call "%ROOT_DIR%\mvnw.cmd" -pl fare-payment-service spring-boot:run
pause
