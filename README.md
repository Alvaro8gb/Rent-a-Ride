# IW
`Propuesta`: Aplicación de alquiler de vehiculos implementada eficientemente para empresas de renting.

Enlace para figma [link](https://www.figma.com/file/UY1222Ks3zLpMzkwTa3BFu/IW?node-id=0%3A1&t=WUMRbF9RT37ijLMI-1)


## Funcionalidades implementadas
- Gestion de usuarios (90%) vistas: Perfil, Crear, Borrar, Logear
- Gestion de coches (70%)
- Gestion de chats (solo front, web sockets estamos en ello) (50%) vistas: chats_entratantes + chat desplegable
- Gestión de flota (80%)
    1. Vista de calendario administracion (0%)
    2. Busquedas en gestion de flota (70%)
- Gestion de incidentes(0%)

## Imagenes de coches y usuarios 
Al ser editables no estan en el static del proyecto
Estan ubicadas en la capeta [iwdata](./RentARide/iwdata/)

## Docs

- Estilos [css](./Presentacion/css/)
- Imagenes [img](./Presentacion/img/)
- Propuesta [index](./Presentacion/index.html)


## Base de datos

De momento se carga todo desde import.sql, mas adelante habra que hacer una prueba automatica que carge todo desde fichero csv 
este fichero habra que crear una clase para que lo cree.

## Logs
Para debugear se lleva la INFO al fichero app.log

## DB

[designer_link](https://dbdesigner.page.link/XSEH3FhGSRpSoBXG9)
![Db shema](Presentacion/img/db.jpg)

## Develop

- [edit](https://guacamole.containers.fdi.ucm.es/)
- [view](https://vm34.containers.fdi.ucm.es/)