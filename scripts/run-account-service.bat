@echo off
title RideLink - Account Service (Port 8081)
set "ROOT_DIR=%~dp0.."
echo ===================================================
echo Starting RideLink Account Service on Port 8081...
echo Database: account_db (MS SQL Server)
echo ===================================================
call "%ROOT_DIR%\mvnw.cmd" -pl account-service spring-boot:run
pause
