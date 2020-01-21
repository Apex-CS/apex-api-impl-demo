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
