#!/bin/bash

# auth_code 배열
auth_codes=(
    "7795221L"
    "5T349ZCZ"
    "8687U12O"
    "9U61MK9N"
    "447X77CY"
    "35679RT8"
    "297M673P"
    "5E99868E"
    "9718D892"
    "38848Z3S"
    "53369M3B"
    "76878832"
    "8797299N"
    "34468J28"
    "82345698"
    "14RSTS9V"
    "593978QV"
    "8269PU9M"
    "5611CUOH"
    "38917ZHL"
    "86716F72"
    "5681H8MN"
    "1479H68B"
    "75352ZMZ"
    "45718Z33"
    "3351Z7VC"
    "734869EO"
    "3S8NY729"
    "58582D19"
    "2689MH7B"
    "8631573H"
)

# 기본 cURL 설정
url="https://api.example.com/endpoint"
contentType="application/x-www-form-urlencoded"

# 각 auth_code에 대해 요청 실행
for auth_code in "${auth_codes[@]}"
do
    curl -X POST "$url" \
        -H "Content-Type: $contentType" \
        -d "auth_code=$auth_code&other_param=value" \
        -o "output_${auth_code}.txt"
    echo "Request with auth_code $auth_code completed."
done