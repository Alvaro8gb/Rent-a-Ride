Feature: Test para la creacion de un nuevo vehiculo

Background:
    * url baseUrl

Scenario: Creacion del vehiculo
    * call read('login.feature@login_b')
    Given def createFormURL = baseUrl + '/createVehicle' 
    Given driver createFormURL
    Then driver.screenshot()
    And input('#marca', 'Seat') 
    And input('#modelo', 'Leon')  
    And input('#anio', '2023')  
    And input('#combustible', 'Gasolina') 
    And input('#consumo', '345') 
    And input('#cambio', 'Manual')  
    And input('#puertas', '3') 
    And input('#plazas', '5')  
    And input('#cv', '56')  
    And input('#matricula', '4567-DDD') 
    And input('#autonomia', '234') 
    And input('#recogida', 'Madrid')  
    And input('#precio', '234')
    Then driver.screenshot()
    When submit().click("#createVehicleButton")
    Then driver.screenshot()
    Then waitForUrl(createFormURL)
    Then match driver.url == createFormURL



