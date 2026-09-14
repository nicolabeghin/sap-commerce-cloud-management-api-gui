# sap-commerce-cloud-management-api-gui

A simple cross-platform JavaFX GUI for managing [SAP (Hybris) Commerce Cloud (TM)](https://www.sap.com/products/crm/commerce-cloud.html)

<img width="832" alt="7" src="https://github.com/nicolabeghin/sap-commerce-cloud-api-gui/assets/2743637/a80a909e-9b26-4c2c-bdbd-545428c1392c">

## Disclaimer

**This is an unofficial, community-driven project and is not affiliated with, endorsed by, or supported by SAP SE or its affiliates.**

This software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.

Use this tool at your own risk. The authors assume no responsibility for any issues that may arise from its use.

## What it is
[SAP Commerce Cloud API](https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/452dcbb0e00f47e88a69cdaeb87a925d/66abfe678b55457fab235ce8039dda71.html?locale=en-US)
provides an out-of-the-box way to manage builds and deployments.

* Trigger a build
* Find a list of available builds
* Download build logs
* Trigger a deployment
* Find a list of deployments
* Get the options for canceling a deployment and, if necessary, cancel a deployment
* Enable and disable maintenance mode for a given endpoint

A [CLI](https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/9116f1cfd16049c3a531bfb6a681ff77/8acde53272c64efb908b9f0745498015.html?locale=en-US) is
provided but no GUI:
this is where `sap-commerce-cloud-management-api-gui` comes into play.

### Existing builds
* **Double-click** any row to see full build details (version, timestamps, branch, created by, etc.)

<img width="620" height="528" alt="image" src="https://github.com/user-attachments/assets/7f0e9662-5199-4b75-9e44-4ed017c3beeb" />


### New build
* Trigger a build from a Git branch or tag, with an optional auto-suggested build name
* Optionally deploy automatically after the build completes

<img width="480" alt="1" src="https://github.com/nicolabeghin/sap-commerce-cloud-api-gui/assets/2743637/fb1fb911-5ee1-4892-a20c-5f6943eeb6f9">

### Deployments
* Deploy with configurable strategy and DB update mode
* **Double-click** any row to see full deployment details

<img width="620" height="528" alt="image" src="https://github.com/user-attachments/assets/92c88d1e-80b0-473c-9f8b-9ff5aa01f415" />

### Endpoints
* View all endpoints for the selected environment, including their current maintenance state
* **Schedule maintenance mode**: pick a start and end date/time; the app will enable and disable maintenance automatically at the scheduled times
* **Disable maintenance mode** immediately for endpoints currently in maintenance
* Cancel a pending maintenance schedule at any time

<img width="620" height="528" alt="image" src="https://github.com/user-attachments/assets/33891b2a-6c00-48d8-8160-fc9f571d2cc2" />
<img width="620" height="528" alt="image" src="https://github.com/user-attachments/assets/851b7804-fa66-4f9f-967b-551684abd84d" />

### Scheduled activities
* View all scheduled activities for the selected environment
* **Double-click** any row to see full activity details (type, status, timestamps, notes, etc.)
* **New Activity**: schedule a new activity by selecting the activity type (`HIBERNATE_COMMERCE_ENVIRONMENT`, `WAKE_UP_COMMERCE_ENVIRONMENT`) and a date/time
* **Cancel Activity**: cancel a selected activity (enabled only for activities that are not already cancelled or cancelling)

<img width="620" height="528" alt="image" src="https://github.com/user-attachments/assets/e8a25a80-b1dd-44af-80c7-0c270f92467e" />


## Credentials
At first start you'll be prompted for
* **Client ID** and **Client secret**: OAuth2 credentials — see [Creating a Technical User](https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/0c2050f6d31f49ddb6eba18509060ae5/57bef96f18034193af93d2cc36f6d526.html?locale=en-US&version=LATEST).
* **Subscription code**: found in the Cloud Portal URL after `subscription/`

<img width="620" height="528" alt="image" src="https://github.com/user-attachments/assets/2bec80c7-a0c9-413b-a088-fab471ad5efa" />


## Requirements
* Java 8 or higher

## How to run
Download the precompiled releases for your platform at https://github.com/nicolabeghin/sap-commerce-cloud-api-gui/releases

### OSX Gatekeeper

<img width="260" height="298" alt="image" src="https://github.com/user-attachments/assets/8b5b30a5-a08a-4514-8049-065e8c66e304" />
<img width="723" height="396" alt="image" src="https://github.com/user-attachments/assets/3d1cf888-8ccc-4105-8630-dec4e17b2b2d" />

## How to run from source code
    
    git@github.com:nicolabeghin/sap-commerce-cloud-management-api-gui.git
    ./gradlew run  

## Technical
* networking based on [Retrofit](https://square.github.io/retrofit/) + [OkHttp3](https://square.github.io/okhttp/)
* it's possible to enable network tracing

### Code generation
Network calls have been generated through [Swagger codegen](https://swagger.io/docs/open-source-tools/swagger-codegen/) from [OpenAPI](https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/452dcbb0e00f47e88a69cdaeb87a925d/66abfe678b55457fab235ce8039dda71.html?locale=en-US)
  specifications

    java -jar swagger-codegen-cli-3.0.57.jar generate \
        -i commerce-cloud-management-api.yaml \
        -l java \
        --api-package com.sap.cx.commercecloud.management.openapi.api \
        --model-package com.sap.cx.commercecloud.management.openapi.model \
        --invoker-package com.sap.cx.commercecloud.management.openapi \
        --group-id com.nbeghin.ccv2.api.gui \
        --artifact-id sap-commerce-cloud-api-gui \
        --artifact-version 0.0.1-SNAPSHOT \
        --library retrofit2 \
        -o retrofit2
