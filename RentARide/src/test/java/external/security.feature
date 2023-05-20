Feature: Prueba de seguridad 

Background:
    * url baseUrl

Scenario: Ver las paginas que puede entrar un cliente sin resgitrar 
   Given path baseUrl + '/vehicle/search'
   When method GET
   Then status 200

   Given path baseUrl + '/vehicle/1/pic'
   When method GET
   Then status 200

   Given path baseUrl + '/vehicle/1'
   And path id = 456
   When method GET
   Then status 200


Scenario: Intentar acceder a cosas que no se pueden no estando registrado
   Given def urlLogin = baseUrl + '/login'
   Given driver baseUrl + '/vehicle/carsManagement'
   When method GET
   Then waitForUrl(urlLogin)

   Given driver baseUrl + '/incidencias/export'
   When method GET
   Then waitForUrl(urlLogin)

   Given driver baseUrl + '/incidencias/listar'
   When method GET
   Then waitForUrl(urlLogin)

   Given driver baseUrl + '/user/userList'
   When method GET
   Then waitForUrl(urlLogin)

   Given driver baseUrl + '/user/1/delete'
   When method GET
   Then waitForUrl(urlLogin)

   Given driver baseUrl + '/user/1/modify'
   When method GET
   Then waitForUrl(urlLogin)


Scenario: Intentar acceder a cosas que no se pueden siendo usuarios
   * call read('login.feature@login_c')
   Given driver baseUrl + '/vehicle/carsManagement'
   When method GET
   Then script('document.querySelector("#status").value = "403";')

   Given driver baseUrl + '/incidencias/export'
   When method GET
   * script('document.querySelector("#status").value = "403";')

   Given driver baseUrl + '/incidencias/listar'
   When method GET
   * script('document.querySelector("#status").value = "403";')

   Given driver baseUrl + '/user/userList'
   When method GET
   * script('document.querySelector("#status").value = "403";')

   Given driver baseUrl + '/user/1/delete'
   When method GET
   * script('document.querySelector("#status").value = "403";')

   Given driver baseUrl + '/user/1/modify'
   When method GET
   * script('document.querySelector("#status").value = "403";')
