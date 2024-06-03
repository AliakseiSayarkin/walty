#!/bin/bash
set -x
yum update -y

#docker
amazon-linux-extras install docker -y
service docker start
systemctl enable docker
usermod -a -G docker ec2-user
sudo chmod 666 /var/run/docker.sock

#docker compose
sudo wget "https://github.com/docker/compose/releases/download/1.29.2//docker-compose-$(uname -s)-$(uname -m)" -O /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo wget "https://github.com/docker/compose/releases/download/$(curl -s https://api.github.com/repos/docker/compose/releases/latest | grep -oP '"tag_name": "\K(.*)(?=")')/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose