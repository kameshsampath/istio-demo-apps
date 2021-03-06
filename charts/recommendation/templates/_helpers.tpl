{{/*
Expand the name of the chart.
*/}}
{{- define "recommendation.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Application Version labels
*/}}
{{- define "recommendation.version" -}}
{{- if .Values.image.tag }}
{{- $appVersion :=  semver .Values.image.tag }}
{{- printf "v%d" $appVersion.Major }}
{{- else }}
{{- $appVersion :=  semver "1.0.0" }}
{{- printf "v%d" $appVersion.Major }}
{{- end}}
{{- end }}

{{- define "recommendation.versionLabels" -}}
version: {{ include "recommendation.version" .  }}
{{- end }}


{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "recommendation.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "recommendation.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "recommendation.labels" -}}
helm.sh/chart: {{ include "recommendation.chart" . }}
{{ include "recommendation.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "recommendation.selectorLabels" -}}
app.kubernetes.io/name: recommendation
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "recommendation.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "recommendation.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

