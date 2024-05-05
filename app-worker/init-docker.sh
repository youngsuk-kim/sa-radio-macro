#!/bin/bash

cd ..


# 서버 IP 주소 리스트
SERVERS=("3.36.86.236" "43.201.150.174")

# PEM 키 파일 위치
PEM_KEY_PATH="/Users/bread/Downloads/jun.pem"


# Docker 설치 및 실행 스크립트
INSTALL_SCRIPT='
sudo apt-get update &&
sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common &&
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - &&
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" &&
sudo apt-get update &&
sudo apt-get install -y docker-ce docker-ce-cli containerd.io &&
sudo systemctl start docker &&
sudo systemctl enable docker
'

# 각 서버에 SSH를 통해 Docker 설치 명령 실행
for SERVER in "${SERVERS[@]}"
do
    echo "Attempting to install Docker on $SERVER..."
    ssh -i "$PEM_KEY" -o StrictHostKeyChecking=no ubuntu@$SERVER "$INSTALL_SCRIPT" &&
    echo "Docker installed successfully on $SERVER" ||
    echo "Failed to install Docker on $SERVER"
done


