#!/bin/bash

sudo gradle clean
sudo gradle bootJar -Pprofile=live
sudo docker build --platform linux/amd64 --no-cache -t breadsuk/macro-broker:latest .
sudo docker push breadsuk/macro-broker:latest

# 인스턴스의 IP 주소나 호스트명 목록
HOSTS=("54.180.131.37")

# SSH 접속에 사용할 PEM 키 파일 위치
PEM_KEY_PATH="/Users/bread/Downloads/jun.pem"

# Docker 컨테이너 실행 및 IP 포트 출력 스크립트
DOCKER_SCRIPT='
echo "Pulling the latest Docker image..."
echo "Stopping and removing the old container if it exists..."
echo "Starting a new container with live profile..."
sudo docker stop broker-server
sudo docker rm broker-server
sudo docker run -d -p 8082:8082 -e SPRING_PROFILES_ACTIVE=live --name broker-server breadsuk/macro-broker:latest
echo "Container running on $(hostname -I | cut -d" " -f1):8082"
'

# 각 호스트에 대해 SSH를 통해 Docker 컨테이너를 실행하고 IP 포트를 출력
for HOST in "${HOSTS[@]}"
do
    echo "Deploying on $HOST"
    ssh -i $PEM_KEY_PATH -o StrictHostKeyChecking=no ubuntu@$HOST "$DOCKER_SCRIPT"
done

echo "Deployment completed."

