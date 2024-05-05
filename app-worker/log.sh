#!/bin/bash

# 인스턴스의 IP 주소나 호스트명 목록
HOSTS=(
"15.165.119.94"
"13.124.210.105"
"43.202.31.180"
"3.36.86.236"
"43.201.150.174"
)

# SSH 접속에 사용할 PEM 키 파일 위치
PEM_KEY_PATH="/Users/bread/Downloads/jun.pem"

# Docker 컨테이너의 최근 20줄 로그를 보는 스크립트
DOCKER_SCRIPT='
echo "Displaying last 20 lines of logs for radio-server..."
sudo docker logs --tail 50 radio-server
'

# 각 호스트에 대해 SSH를 통해 Docker 컨테이너의 로그를 출력
for HOST in "${HOSTS[@]}"
do
    echo "Connecting to $HOST..."
    ssh -i $PEM_KEY_PATH -o StrictHostKeyChecking=no ubuntu@$HOST "$DOCKER_SCRIPT"
done
