@echo off
title RideLink - Unit Test Runner
cd /d "%~dp0\.."
echo ===================================================
echo Running Unit Tests across all 4 RideLink Services...
echo ===================================================
call mvn clean test
pause
