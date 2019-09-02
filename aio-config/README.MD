# SMP, Connector and Gateway Docker Compose

This is a simple docker-compose that downloads the latest containers for SMP, Connector and Gateway TOOP Components and starts them by default on:
- SMP Port 80
- Connector Port 8083
- Gateway Port 8082

The configuration files are stored by default on /toop-dir. On a new installation, you should run this once which will create the configuration files and folders in that directory. 

All of the above can be changed by editing the docker-compose.yml file.