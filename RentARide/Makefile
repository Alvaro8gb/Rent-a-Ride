# Makefile para arrancar aplicación Spring Boot
MAVEN_CMD=mvn
SPRING_BOOT_RUN = $(MAVEN_CMD) spring-boot:run
SPRING_BOOT_TEST_INTERNAL = $(MAVEN_CMD) test -Dtest=InternalRunner
SPRING_BOOT_TEST_EXTERNAL = $(MAVEN_CMD) test -Dtest=ExternalRunner
CLEAN_KARATE = rm target/karate.log
CLEAN_APP_LOGS = rm app.log
CLEAN = $(MAVEN_CMD) clean
VERIFY = $(MAVEN_CMD) verify

# Regla por defecto (la primera regla en el archivo Makefile)
all: run

# Regla para arrancar la aplicación
run:
	$(SPRING_BOOT_RUN)
setup: 
	cp -r  ./iwdata/ /tmp/
# Regla para ejecutar todas las verificaciones de código
verify:
	$(VERIFY)

# Regla para ejecutar los tests
test:
	$(SPRING_BOOT_TEST_EXTERNAL)

# Reglas para limpiar archivos cache y logs
clean:
	$(CLEAN)

clean-logs:
	$(CLEAN_KARATE)
	$(CLEAN_APP_LOGS)

deploy:
	echo "Recuerda cambiar el app.properties y poner el puerto 80"
	make setup

# Regla para imprimir la ruta relativa del archivo Makefile
#print_relative_path:
#    @echo "La ruta relativa del archivo Makefile es: $(@D)"