#!/bin/sh

cd /tmp;
ls -al;
sudo docker ps;
sudo docker stop grocery_container || true;
sudo docker load -i grocery_backend.tar;
sudo docker run --rm -d -p 8080:8080 --name grocery_container grocery_backend:latest;
