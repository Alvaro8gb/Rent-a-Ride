Feature: Prueba principal, en nuestro caso que un cliente busque un coche y lo reserve 

Background:
    * url baseUrl

Scenario: Busqueda de un audi sin identificarse
    Given def a3Url = baseUrl + '/vehicle/1' 
    Given driver baseUrl
    And input('#vehicle', 'A3')
    When submit().click("#button-search")
    Then waitForUrl(baseUrl + '/vehicle/search')
    When driver.click("a#A3")
    Then match driver.url == a3Url
    Then driver.screenshot()


Scenario: Buscar coche por id estando logeado y reservarlo
    * def bmwUrl = baseUrl + '/vehicle/4'
    * call read('login.feature@login_como_c')
    * driver bmwUrl
    * method get
    * waitForUrl(bmwUrl)
    * status 200
    * match response contains 'BMW Z4' 
    * driver.screenshot()
    * click('button#reservar')
    * delay(1000)
    * script('document.querySelector("#inDate").value = "2023-04-01";')
    * script('document.querySelector("#outDate").value = "2023-04-05";')
    * delay(1000)
    * driver.screenshot()

#Scenario: Ver reserva
#    * def historyUrl = baseUrl + '/booking/history/'
#    * call read('login.feature@login_como_c')
#    * driver historyUrl
#    * method get
#    * waitForUrl(historyUrl)
#    * driver.screenshot()
#    * def divElements = karate.extractAll(htmlBody, '<div id="tickets"></div>')
#    * match divElements[*].length > 0

Scenario: Ver perfil
    * call read('login.feature@login_como_b')
    * driver baseUrl
    * driver.click("a#profile")
    * waitForUrl(baseUrl + '/user/profile')
    * driver.screenshot()
