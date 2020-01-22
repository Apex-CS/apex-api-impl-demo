# apex-api-impl-demo

# FIS API - APEX - IMPL - DEMO

## Description

The APEX API IMPL DEMO covers the implementation of the APEX API with the DEMO core.

### Manual correction in response EAVEventList_v1_0_SearchEventRes.xslt :

1. Since the EAV Search Response provides all event details, we need to get the current/latest event details. Hence added 'position() = 1' in the xslt to fetch the latest event which is displayed at the top in the SOAP response.

2. Corresponding transactionId is mapped to their trail deposits.

3. Fixed decimal value for properties trialDeposit amount and transferAmount using the format-number method. (Ex : format-number(ns3:Amt/ns1:Amt,'#0.00')).

### Dockerfile Version

`api-apex-demo-server-${IMPL pom.xml Version}.jar`

### Helm Chart Name

`fis-api-spi-apex-demo-dev-impl`

# Questions

## Charts
- Is expected to have a local or cloud kubernetes environment or can we run as local spring boot?
- can we have a helm example file?
## Deployments
- Could you please define the deployment process?
- Are we going to be able to connect to your CI/CD or are we going to have our own  CI/CD?

## Integrations
- There's a lot of proprietary classes needed for Integration Flow, Can we assume them as mock classes or will they be provided with a repo access?
- More information about Endpoint Configurer, FIsDynamicHostDestinationProvider and Error Channel needed.
- Can you describe the test process for spring integration with the project?
- Could we assume all data as mock, or are you going to provide access or dummy data?
## Other
- Can we assume the service as REST?
- Are diagrams expected for documentation?
- For documentation purposes, where is the starting point for call Product service ( From Mobile App, web portal, another WS )
- https://github.com/instrumenta/openapi2jsonschema is the correct URL for tool?
- jsd2xsd tool, where could we download? https://github.com/ethlo/jsons2xsd ? 
- Are you using OpenShift as cloud provider?



## Product API estimate
#### Best case scenario

| Activity                    	|  Time 	|
|-----------------------------	|:-----:	|
| Read Requirements           	| 8 hrs 	|
| InputContract - swagger     	| 4 hrs 	|
| Integration/Detailed Spec   	| 8 hrs 	|
| Schemas generate            	| 2 hrs 	|
| Mapping                     	| 4 hrs 	|
| IntegrationFlow Source code 	| 8 hrs 	|
| Test                        	| 8 hrs 	|
| Deployment                  	| 8 hrs 	|
| <b>Total                  	| <b>50 hrs 	|





