-- ==========================================================
-- RideLink Platform - MS SQL Database Initialization Script
-- Module: IT3130 Application Development
-- ==========================================================
-- This script creates the 4 independent persistence boundaries
-- required by the microservices architecture.
-- Run this script in SQL Server Management Studio (SSMS) or sqlcmd.
-- ==========================================================

USE master;
GO

-- 1. Account Service Database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'account_db')
BEGIN
    CREATE DATABASE [account_db];
    PRINT 'Database [account_db] created successfully.';
END
ELSE
BEGIN
    PRINT 'Database [account_db] already exists.';
END
GO

-- 2. Driver & Vehicle Service Database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'driver_db')
BEGIN
    CREATE DATABASE [driver_db];
    PRINT 'Database [driver_db] created successfully.';
END
ELSE
BEGIN
    PRINT 'Database [driver_db] already exists.';
END
GO

-- 3. Ride Management Service Database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'ride_db')
BEGIN
    CREATE DATABASE [ride_db];
    PRINT 'Database [ride_db] created successfully.';
END
ELSE
BEGIN
    PRINT 'Database [ride_db] already exists.';
END
GO

-- 4. Fare & Payment Service Database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'fare_db')
BEGIN
    CREATE DATABASE [fare_db];
    PRINT 'Database [fare_db] created successfully.';
END
ELSE
BEGIN
    PRINT 'Database [fare_db] already exists.';
END
GO

PRINT '==========================================================';
PRINT 'All 4 RideLink microservice databases are ready!';
PRINT '==========================================================';
GO
