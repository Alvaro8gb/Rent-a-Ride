Feature: Modificar vehiculo

#
#  Este test sirve para confirmar que se modifica un vehiculo correctamente

Background:
    * url baseUrl

  Scenario: modificacion vehiculo correcto
    * call read('login.feature@login_b')
    Given def createFormURL = baseUrl + '/vehicle/carsManagement'
    Given driver createFormURL
    * click('#modificarVehiculo1')
    * delay(2000)
    And script('document.querySelector("#modelo1").value = "A3 con turbo";')
    And script('document.querySelector("#cv1").value = "200";')
    * delay(2000)
    * driver.screenshot()
    * click("#confirm-reservar1")
    * delay(2000)
    * driver.screenshot()
    
