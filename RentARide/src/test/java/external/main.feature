Feature: Prueba principal, en nuestro caso que un cliente busque un coche 

Background:
    * url baseUrl
    * call read('login.feature@login_como_a')

Scenario: Busqueda de un audi
    Given driver baseUrl
    And input('#vehicle', 'audi')
    When submit().click("#button-search")
    Then waitForUrl(baseUrl + '/vehicle/search')

Scenario: Buscar coche por id
    * driver baseUrl + '/vehicle/4'
    * path '/vehicle/4'
    * method get
    * status 200
    * match response contains 'BMW Z4' 
    * driver.screenshot()

    