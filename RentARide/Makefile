# Makefile para arrancar aplicación Spring Boot

# Variable que indica el comando para arrancar la aplicación
SPRING_BOOT_RUN = mvn spring-boot:run
SPRING_BOOT_TEST_INTERNAL = mvn test -Dtest=InternalRunner
SPRING_BOOT_TEST_EXTERNAL = mvn test -Dtest=ExternalRunner
CLEAN_KARATE = rm target/karate.log
CLEAN_APP_LOGS = rm app.log

# Regla por defecto (la primera regla en el archivo Makefile)
all: run

# Regla para arrancar la aplicación
run:
    $(SPRING_BOOT_RUN)

test:
	$(SPRING_BOOT_TEST_EXTERNAL)
    $(SPRING_BOOT_TEST_INTERNAL)

clean:
    $(CLEAN_KARATE)
    $(CLEAN_APP_LOGS)
    mvn clean


# Regla para imprimir la ruta relativa del archivo Makefile
#print_relative_path:
#    @echo "La ruta relativa del archivo Makefile es: $(@D)"