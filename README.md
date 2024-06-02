# sap-commerce-cloud-management-api-gui

A simple JavaFX GUI for managing [SAP (Hybris) Commerce Cloud (TM)](https://www.sap.com/products/crm/commerce-cloud.html)

<img width="832" alt="7" src="https://github.com/nicolabeghin/sap-commerce-cloud-api-gui/assets/2743637/a80a909e-9b26-4c2c-bdbd-545428c1392c">
<img width="480" alt="3" src="https://github.com/nicolabeghin/sap-commerce-cloud-api-gui/assets/2743637/0f256603-384a-4c91-9106-3fb97f003deb">
<img width="480" alt="1" src="https://github.com/nicolabeghin/sap-commerce-cloud-api-gui/assets/2743637/fb1fb911-5ee1-4892-a20c-5f6943eeb6f9">

## What it is
[SAP Commerce Cloud API](https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/452dcbb0e00f47e88a69cdaeb87a925d/66abfe678b55457fab235ce8039dda71.html?locale=en-US)
provides an out-of-the-box way to manage builds and deployments.

* Trigger a build
* Find a list of available builds
* Find the details for a specific build
* Download build logs
* Trigger a deployment
* Find a list of deployments
* Find the details for a specific deployment
* Get the options for canceling a deployment and, if necessary, cancel a deployment

A [CLI](https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/9116f1cfd16049c3a531bfb6a681ff77/8acde53272c64efb908b9f0745498015.html?locale=en-US) is
provided but no GUI:
this is where `sap-commerce-cloud-management-api-gui` comes into play.

## Configuration
<img width="338" alt="8_2" src="https://github.com/nicolabeghin/sap-commerce-cloud-api-gui/assets/2743637/3c8092d7-4871-49d4-966e-4a2f5d8c7669">

### How to retrieve Cloud Portal API token

To authenticate against the Cloud Portal, generate an API token from the Cloud Portal,
see [Generating API Tokens](https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/0fa6bcf4736c46f78c248512391eb467/b5d4d851cbd54469906a089bb8dd58d8.html?locale=en-US).

### How to retrieve Cloud Portal subscription
You can find the subscription code in the Cloud Portal URL. It appears after `subscription/` in the URL.

## Technical
* network calls have been generated through [Swagger codegen](https://swagger.io/docs/open-source-tools/swagger-codegen/) from [OpenAPI](https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/452dcbb0e00f47e88a69cdaeb87a925d/66abfe678b55457fab235ce8039dda71.html?locale=en-US)
specifications 
* networking based on [Retrofit](https://square.github.io/retrofit/) + [OkHttp3](https://square.github.io/okhttp/)
* it's possible to enable network tracing