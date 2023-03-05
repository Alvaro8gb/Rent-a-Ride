Feature: Prueba principal, en nuestro caso que un cliente busque un coche 

Background:
    url baseUrl
    def util = Java.type('karate.KarateTests')

Scenario: Busqueda de un audi
    Given driver baseUrl + '/login'
    And input('#username', 'a')
    And input('#password', 'aa')
    When submit().click(".form-signin button")
    Given driver baseUrl
    And input('#vehicle', 'audi')
    When submit().click("#button-search")
    Then waitForUrl(baseUrl + '/vehicle/search')
