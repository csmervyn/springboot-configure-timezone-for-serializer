#!/bin/bash
echo "Start to build and push arm64 images to docker hub"
docker buildx build  -t csmervyn718/springboot-java-project-template:latest --platform=linux/arm64 . --push
echo "ARM64 Images push to docker hub success"
echo "Start to build and push amd64 images to docker hub"
docker buildx build  -t csmervyn718/springboot-java-project-template:latest --platform=linux/amd64 . --push
echo "AMD64 Images push to docker hub success"
