#!/bin/sh

ls -al
docker load -i grocery_backend.tar
docker run -d -p 8080:8080 --name grocery_container grocery_backend:latest
