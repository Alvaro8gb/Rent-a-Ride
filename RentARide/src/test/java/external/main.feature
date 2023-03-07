Feature: Prueba principal, en nuestro caso que un cliente busque un coche 

Background:
    * url baseUrl

#Scenario: Busqueda de un audi sin identificarse
#    Given driver baseUrl
#    And input('#vehicle', 'audi')
#    When submit().click("#button-search")
#    Then waitForUrl(baseUrl + '/vehicle/search')
#
Scenario: Buscar coche por id logeado
    * call read('login.feature@login_como_a')
    * driver baseUrl + '/vehicle/4'
    * path '/vehicle/4'
    * method get
    * status 200
    * match response contains 'BMW Z4' 
    * driver.screenshot()
    * submit().click("#reservar")
    * driver.screenshot()
    * submit().click("#confirm-reservar")
