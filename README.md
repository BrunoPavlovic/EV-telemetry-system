# EV-telemetry-system

The system is designed to simulate driving an electric vehicle through a city. The simulation of electric vehicle driving is based on real data recorded in CSV files.

## Setting up project

  1. Open project from File system and load all projects in your IDE.
  2. Run command **mvn clean install** on parent directory (ev_telemetry_system).
  3. On parent directory run **docker-compose up -d --build** to start servers for databases, app project and REST service.
  4. Go to /opt/payara6-web/glassfish/bin and start payara server with **./startserv**
  5. In directory ev_telemetry_klijenti run command **mvn cargo:redeploy -P ServerEE-local** to install client on payara.
