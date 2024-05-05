#!/bin/bash

sudo gradle clean
sudo gradle bootJar -Pprofile=live
sudo docker build --platform linux/amd64 --no-cache -t breadsuk/sa-radio-macro-server:test .
sudo docker push breadsuk/sa-radio-macro-server:test

# 인스턴스의 IP 주소나 호스트명 목록
HOSTS=("15.165.119.94" "13.124.210.105" "43.202.31.180" "3.36.86.236" "43.201.150.174")

# SSH 접속에 사용할 PEM 키 파일 위치
PEM_KEY_PATH="/Users/bread/Downloads/jun.pem"

# Docker 컨테이너 실행 및 IP 포트 출력 스크립트
DOCKER_SCRIPT='
echo "Pulling the latest Docker image..."
sudo docker pull breadsuk/sa-radio-macro-server:test
echo "Stopping and removing the old container if it exists..."
sudo docker stop radio-server
sudo docker rm radio-server
echo "Starting a new container with live profile..."
sudo docker run -d -p 8080:8080 -e SPRING_PROFILES_ACTIVE=live --name radio-server breadsuk/sa-radio-macro-server:test
echo "Container running on $(hostname -I | cut -d" " -f1):8080"
'

# 각 호스트에 대해 SSH를 통해 Docker 컨테이너를 실행하고 IP 포트를 출력
for HOST in "${HOSTS[@]}"
do
    echo "Deploying on $HOST"
    ssh -i $PEM_KEY_PATH -o StrictHostKeyChecking=no ubuntu@$HOST "$DOCKER_SCRIPT"
done

echo "Deployment completed on all hosts."

