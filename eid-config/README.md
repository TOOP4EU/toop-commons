# eID configuration

This folder contains some documentation on how to setup the eID and EIDAS infrastructure in the playground

## eid-freedonia (9086)

Base Docker image:

https://hub.docker.com/r/endimion13/toop-eid

The source configuration was forked to https://github.com/TOOP4EU/toopEid

Based on: https://github.com/uaegeani4mlab/LEPS-APIs/tree/master/eIDAS-SP-WebApp-2.0

Start container:
docker-compose --file docker-compose.yml up -d --force-recreate

Stop container:
docker-compose --file docker-compose.yml stop

See https://docs.docker.com/compose/gettingstarted/ for docker-compose rules
 
The configuration for the node resides in folder `/configEidas-toop` on the machine

## eidas-freedonia (9087)

Explicit Tomcat at /var/lib/tomcat8/webapps

3 WARs deployed:
* EidasNode
* IdP
* SP

Special Tomcat startup parameters (setenv.sh):
* -DLOG_HOME=/var/log/tomcat8/eidas
* -DEIDAS_CONFIG_REPOSITORY=/opt/eidas/tomcat/
* -DSPECIFIC_CONNECTOR_CONFIG_REPOSITORY=/opt/eidas/tomcat/specificConnector/
* -DSPECIFIC_PROXY_SERVICE_CONFIG_REPOSITORY=/opt/eidas/tomcat/specificProxyService/
* -DSP_CONFIG_REPOSITORY=/opt/eidas/tomcat/sp/
* -DIDP_CONFIG_REPOSITORY=/opt/eidas/tomcat/idp/
* -DSPECIFIC_CONFIG_REPOSITORY=/opt/eidas/tomcat/specific/

Tomcat endorsed directory contains:
* resolver-2.9.1.jar
* serializer.jar
* xalan-2.7.2.jar
* xercesImpl-2.11.0.jar
* xml-apis-1.4.01.jar
 