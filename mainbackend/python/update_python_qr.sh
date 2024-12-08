#!/bin/sh

cd /tmp;
ls -al;
sudo docker ps;
sudo docker stop grocery_container_python || true;
sudo docker load -i grocery_python_qr.tar;
sudo docker run --rm -d -p 8084:8084 --name grocery_container_python grocery_python_qr:latest;
