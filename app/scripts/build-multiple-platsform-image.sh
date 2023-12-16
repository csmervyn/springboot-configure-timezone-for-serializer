#!/bin/bash
echo "Please Login your docker hub 👉"
docker login
echo "Start to build and push images to docker hub"
docker buildx build  -t csmervyn718/springboot-java-project-template:latest --platform=linux/arm64,linux/amd64 . --push
echo "Images push to docker hub success ✅"
