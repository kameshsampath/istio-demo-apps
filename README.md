# Java microservices + Istio on Kubernetes

There are three different and super simple microservices in this system and they are chained together in the following
sequence:

```
customer → preference → recommendation
```

## Required Tools

- helm
- kubectl
- Java
- maven
-

## Deploy Application

### Customer

```shell
helm install --upgrade $PROJECT_HOME/charts/customer
```

### Preference

```shell
helm install --upgrade $PROJECT_HOME/charts/preference
```

### Recommendation

### V1

```shell
helm install --upgrade $PROJECT_HOME/charts/recommendation
```

### V2

```shell
helm install --upgrade $PROJECT_HOME/charts/recommendation --set image.tag="2.0.0"
```
