#!/bin/bash
set -o errexit

if [ "$1" = 'servisi' ]; then
	java_vm_parameters="-Dfile.encoding=UTF-8"
    java ${java_vm_parameters} -jar /servisi/ev_telemetry_servisi-1.0.0-jar-with-dependencies.jar &
    
    wait
else
	exec "$@"
fi