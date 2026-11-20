<!--- Deploy -->

# Deploy helm chart

## Introduction

This chart bootstraps a deployment on a [Kubernetes](https://kubernetes.io) cluster using [Helm](https://helm.sh) package manager.

## Prerequisites

The code was tested on **Kubernetes cluster** (v1.21.11) with **Istio** (1.12.6)
> It is possible to use other versions, but it hasn't been tested

### Operation system

The code works in Debian-based Linux (Debian 10 and Ubuntu 20.04) and Windows WSL 2. Also, it works but is not guaranteed in Google Cloud Shell. All other operating systems, including macOS, are not verified and supported.

### Packages

Packages are only needed for installation from a local computer.

- **HELM** (version: v3.7.1 or higher) [helm](https://helm.sh/docs/intro/install/)
- **Kubectl** (version: v1.21.0 or higher) [kubectl](https://kubernetes.io/docs/tasks/tools/#kubectl)

## Installation

Before installing deploy Helm chart you need to set variables in **values.yaml** file using any code editor. Some of the values are prefilled, but you need to specify some values as well. You can find more information about them below.

### Global variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**global.domain** | your domain, ex `example.com`. Not used by this chart, but may be used in different deployment setups. Use `global.hostname` for the route host | string | - | no
**global.hostname** | host the `HTTPRoute` attaches to when `httpRoute.hostnames` is empty, ex `osdu.example.com` | string | `""` | only if `httpRoute.enabled` is true and `httpRoute.hostnames` is empty
**global.onPremEnabled** | whether on-prem is enabled | boolean | false | yes
**global.limitsEnabled** | whether CPU and memory limits are enabled | boolean | true | yes

### Configmap variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**data.logLevel** | logging level | string | `ERROR` | yes
**data.entitlementsHost** | Entitlements service host | string | `http://entitlements` | yes

### Deployment variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**data.image** | path to the image in a registry | string | - | yes
**data.requestsCpu** | amount of requests CPU | string | `10m` | yes
**data.requestsMemory** | amount of requests memory| string | `350Mi` | yes
**data.limitsCpu** | CPU limit | string | `1` | only if `global.limitsEnabled` is true
**data.limitsMemory** | memory limit | string | `1G` | only if `global.limitsEnabled` is true
**data.serviceAccountName** | name of kubernetes service account | string | `crs-catalog` | yes
**data.imagePullPolicy** | when to pull the image | string | `IfNotPresent` | yes

### Configuration variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**conf.appName** | Service name | string | `crs-catalog` | yes

### ISTIO variables

| Name | Description | Type | Default |Required |
|------|-------------|------|---------|---------|
**istio.proxyCPU** | CPU request for Envoy sidecars | string | 10m | yes
**istio.proxyCPULimit** | CPU limit for Envoy sidecars | string | 200m | yes
**istio.proxyMemory** | memory request for Envoy sidecars | string | 100Mi | yes
**istio.proxyMemoryLimit** | memory limit for Envoy sidecars | string | 256Mi | yes
**istio.auth.basePath** | Base context path used to expand versioned public paths in the `ALLOW` AuthorizationPolicy | string | `/api/crs/catalog` | yes
**istio.auth.versions** | API versions appended after `basePath` when expanding relative (non leading-`/`) entries in `disable` | list | `[v1]` | yes
**istio.auth.disable** | Public paths allowed without a JWT (Swagger UI, api-docs, health). Entries with a leading `/` are emitted verbatim; entries without are expanded to `{basePath}/{version}/{path}` for each version. Rendered as `to.operation.paths` in the `action: ALLOW` policy | list | see `values.yaml` | yes

### Routing (HTTPRoute)

The chart owns the routing for the service. It renders a Gateway API `HTTPRoute`, disabled by default. The chart no longer ships an Istio `VirtualService`.

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|----------|
**httpRoute.enabled** | render the `HTTPRoute` | boolean | `false` | yes
**httpRoute.name** | route name. When empty the name is `<release>-route`, which differs from any route of the same name defined outside this chart | string | `""` | no
**httpRoute.parentRefs** | gateways the route attaches to | list | `cimpl-gateway` in `istio-system` | yes
**httpRoute.hostnames** | hosts the route serves. When empty, `global.hostname` is used | list | `[]` | one of `httpRoute.hostnames` or `global.hostname`
**httpRoute.routes** | one rule per entry, see below | list | `/api/crs/catalog/` | yes

The render fails when `httpRoute.enabled` is true and both `httpRoute.hostnames` and `global.hostname` are empty, so the route is never exposed on all hosts.

Each entry of `httpRoute.routes` accepts:

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|----------|
**path** | path to match | string | - | yes
**pathType** | match type, `PathPrefix` or `Exact` | string | `PathPrefix` | no
**serviceName** | backend Service name | string | `conf.appName` | no
**port** | backend Service port | integer | `80` | no
**filters** | Gateway API filters, for example `URLRewrite` | list | - | no

Example, a route with a rewrite and another port:

```yaml
httpRoute:
  enabled: true
  hostnames:
    - osdu.example.com
  routes:
    - path: /api/crs/catalog/
    - path: /other/
      port: 9002
      filters:
        - type: URLRewrite
          urlRewrite:
            path:
              type: ReplacePrefixMatch
              replacePrefixMatch: /
```

### Install the helm chart

Run this command from within this directory:

```console
helm install gc-crs-catalog-deploy .
```

To expose the service through the Gateway API:

```console
helm install gc-crs-catalog-deploy . --set httpRoute.enabled=true --set global.hostname=osdu.example.com
```

To check the rendered route without installing:

```console
helm template gc-crs-catalog-deploy . --set httpRoute.enabled=true --set global.hostname=osdu.example.com -s templates/httproute.yaml
```

## Uninstalling the Chart

To uninstall the helm release:

```console
helm uninstall gc-crs-catalog-deploy
```

[Move-to-Top](#deploy-helm-chart)
