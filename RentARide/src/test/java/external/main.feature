Feature: Prueba principal, en nuestro caso que un cliente busque un coche y lo reserve 

Background:
    * url baseUrl

Scenario: Busqueda de un audi sin identificarse
    Given driver baseUrl
    And input('#vehicle', 'audi')
    When submit().click("#button-search")
    Then waitForUrl(baseUrl + '/vehicle/search')
    Then driver.screenshot()


Scenario: Buscar coche por id estando logeado y reservarlo
    * call read('login.feature@login_como_a')
    * driver baseUrl + '/vehicle/4'
    * path '/vehicle/4'
    * method get
    * waitForUrl( baseUrl + '/vehicle/4')
    * status 200
    * match response contains 'BMW Z4' 
    * driver.screenshot()
    * click('button#reservar')
    * delay(1000)
    * script('document.querySelector("#inDate").value = "2023-04-01";')
    * script('document.querySelector("#outDate").value = "2023-04-05";')
    * delay(1000)
    * driver.screenshot()
    * click("button#confirm-reservar")
