#!/bin/bash

printf "
CREATE DATABASE projectc;
USE projectc;

CREATE TABLE IF NOT EXISTS Users (
    user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username TEXT NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Routes (
    route_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    start TEXT NOT NULL,
    end TEXT NOT NULL,
    user_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Stops (
    stop_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    town TEXT NOT NULL,
    name TEXT NOT NULL,
    stopareacode TEXT NOT NULL,
    longitude TEXT NOT NULL,
    latitude TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Times (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    route_id INT NOT NULL,
    timeofarival TEXT NOT NULL,
    timeofstart TEXT
);
" | mysql -u root
