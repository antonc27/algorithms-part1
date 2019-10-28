#!/bin/bash

CLASS=$1
PARAMS=$2
javac -cp .:*:.lift/algs4.jar $CLASS.java && java -cp .:*:.lift/algs4.jar $CLASS $PARAMS