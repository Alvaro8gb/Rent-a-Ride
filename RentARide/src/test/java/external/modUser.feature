Feature: Perfil usuario 

Background:
    * url baseUrl

Scenario: Modificar perfil usuario cliente
    * call read('login.feature@login_c')
    Given def userProfURL = baseUrl + '/user/profile' 
    Given driver userProfURL 
    When submit().click("#btmodificar")
    Then driver.screenshot()
    Then input('#correo_perfil', 'pepito@gmail.com')  
    Then input('#dni_perfil', '53995807H') 
    Then input('#apellido_perfil', 'mi apellido')  
    Then driver.screenshot()
    When submit().click("#confirm-modificar")
    Then driver.screenshot()
    Then waitForUrl(userProfURL)
    Then match driver.url == userProfURL