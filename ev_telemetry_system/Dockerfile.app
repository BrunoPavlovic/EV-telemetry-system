FROM amd64/eclipse-temurin:21

WORKDIR /app

COPY ./ev_telemetry_app/target/ev_telemetry_app-1.1.0-jar-with-dependencies.jar /app/ev_telemetry_app-1.1.0.jar
COPY ./docker-entrypoint.app.sh /docker-entrypoint.sh

COPY ./ev_telemetry_app/NWTiS_DZ1_CS.txt /app/NWTiS_DZ1_CS.txt
COPY ./ev_telemetry_app/NWTiS_DZ1_R1.txt /app/NWTiS_DZ1_R1.txt
COPY ./ev_telemetry_app/NWTiS_DZ1_R2.txt /app/NWTiS_DZ1_R2.txt
COPY ./ev_telemetry_app/NWTiS_DZ1_R3.txt /app/NWTiS_DZ1_R3.txt
COPY ./ev_telemetry_app/NWTiS_DZ1_R4.txt /app/NWTiS_DZ1_R4.txt
COPY ./ev_telemetry_app/NWTiS_DZ1_PK.txt /app/NWTiS_DZ1_PK.txt

RUN chmod -R 777 /docker-entrypoint.sh

EXPOSE 8080
    
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["app"]
