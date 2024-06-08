#!/bin/bash
set -o errexit

if [ "$1" = 'app' ]; then
	java_vm_parameters="-Dfile.encoding=UTF-8"
	
	echo "Pokrecem CentralniSustav"
    java ${java_vm_parameters} -cp /app/ev_telemetry_app-1.1.0.jar edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.CentralniSustav /app/NWTiS_DZ1_CS.txt &

    echo "Pokrecem PosluziteljRadara"
    java ${java_vm_parameters} -cp /app/ev_telemetry_app-1.1.0.jar edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara /app/NWTiS_DZ1_R1.txt &
    java ${java_vm_parameters} -cp /app/ev_telemetry_app-1.1.0.jar edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara /app/NWTiS_DZ1_R2.txt &
    java ${java_vm_parameters} -cp /app/ev_telemetry_app-1.1.0.jar edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara /app/NWTiS_DZ1_R3.txt &
    java ${java_vm_parameters} -cp /app/ev_telemetry_app-1.1.0.jar edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.PosluziteljRadara /app/NWTiS_DZ1_R4.txt &
            
    echo "Pokrecem PosluziteljKazni"
    java ${java_vm_parameters} -cp /app/ev_telemetry_app-1.1.0.jar edu.unizg.foi.nwtis.bpavlovic20.vjezba_07_dz_2.posluzitelji.PosluziteljKazni /app/NWTiS_DZ1_PK.txt &

    wait
else
	exec "$@"
fi