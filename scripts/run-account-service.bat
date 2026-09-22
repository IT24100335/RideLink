@echo off
title RideLink - Account Service (Port 8081)
cd /d "%~dp0\..\account-service"
echo ===================================================
echo Starting RideLink Account Service on Port 8081...
echo Database: account_db (MS SQL Server)
echo ===================================================
call mvn spring-boot:run
pause
