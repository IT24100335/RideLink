@echo off
title RideLink - Unit Test Runner
set "ROOT_DIR=%~dp0.."
echo ===================================================
echo Running Unit Tests across all 4 RideLink Services...
echo ===================================================
call "%ROOT_DIR%\mvnw.cmd" clean test
pause
