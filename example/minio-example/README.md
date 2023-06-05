### deployment

```sh
docker-compose -f ./docker/docker-compose.yml up
```

```sh
docker run -p 9000:9000 -p 9090:9090 \
     --net=host \
     --name minio \
     -d --restart=always \
     -e "MINIO_ACCESS_KEY=minioadmin" \
     -e "MINIO_SECRET_KEY=minioadmin" \
     -v ./minio/data:/data \
     -v ./minio/config:/root/.minio \
     minio/minio server \
     /data --console-address ":9090" -address ":9000"
```