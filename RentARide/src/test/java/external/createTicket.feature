Feature: create ticket

#
#  Este test sirve para confirmar que se realiza la creación de una incidencia correctamente

Background:
    * url baseUrl

  Scenario: creacion ticket correcto
    * call read('login.feature@login_b')
    Given def createFormURL = baseUrl + '/booking/history'
    Given driver createFormURL
    * click('#report-2')
    And script('document.querySelector("#gravity").value = "Alta";')
    And script('document.querySelector("#text").value = "Ayuda! Iba a 200km/h por la M40 y el motor ha empezado a echar humo. Creo que se esta quemando el coche y tengo a mi hijo y a mi chimpancé dentro.";')
    * delay(2000)
    * click("#ok")
    * driver.screenshot()
    Given def createFormURL1 = baseUrl + '/incidencias/'
    Given driver createFormURL1
    * delay(2000)
    * driver.screenshot()
    
