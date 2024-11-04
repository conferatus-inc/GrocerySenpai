#!/bin/bash

cd $(dirname "$0") # go inside
uvicorn src.app:app --host localhost --port 8887 --reload
cd $(pwd) # go back
