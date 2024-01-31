```shell

kubectl apply -f leader-role.yml
kubectl apply -f leader-rolebinding.yml

./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=org/kubernetes-leader-election-example
```