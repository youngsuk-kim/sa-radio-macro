#!/bin/bash

# 인스턴스의 IP 주소나 호스트명 목록
HOSTS=("15.165.119.94" "13.124.210.105" "43.202.31.180" "3.36.86.236" "43.201.150.174")

# SSH 접속에 사용할 PEM 키 파일 위치
PEM_KEY_PATH="/Users/bread/Downloads/jun.pem"


# Docker 컨테이너 중지 및 제거 스크립트
STOP_SCRIPT='
sudo docker system prune
sudo docker stop radio-server
sudo docker remove radio-server
'


# 각 호스트에 대해 SSH를 통해 Docker 컨테이너를 중지하고 제거
for HOST in "${HOSTS[@]}"
do
    echo "Stopping and removing container on $HOST"
    ssh -i $PEM_KEY_PATH -o StrictHostKeyChecking=no ubuntu@$HOST "$STOP_SCRIPT"
done

echo "Containers have been stopped and removed from all hosts."