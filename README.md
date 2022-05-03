# rest-client Project

docker login

./mvnw clean package

kubectl create ns stork
kubectl apply -f target/kubernetes/kubernetes.yml -n stork 


Problems:

```shell
io.fabric8.kubernetes.client.KubernetesClientException: Failure executing: GET at: https://10.96.0.1/api/v1/endpoints?fieldSelector=metadata.name%3Drest-service. Message: Forbidden!Configured service account doesn't have access. Service account may have been revoked. endpoints "rest-service" is forbidden: User "system:serviceaccount:stork-demo:default" cannot list resource "endpoints" in API group "" at the cluster scope.

```
To solve this error:
- Modify the namespace in the [infrastructure/fabric8-rbac.yaml](infrastructure/clusterrolebinding.yaml) file
- Run the following commands:

```shell
kubectl apply -f infrastructure/clusterrole-endpoints-reader.yaml
kubectl apply -f infrastructure/clusterrolebinding.yaml
```

