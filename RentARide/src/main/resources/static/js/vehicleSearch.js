$(document).ready(function(){
    $("#searchV").keyup(function(){
        var filtro = $(this).val();

        fetch('/vehicle/searchByName?filtro=' + filtro)
        .then(response => {
            if (!response.ok) {
            throw new Error('Error con la peticion por Ajax');
            }
            return response.json();
        })
        .then(data => {
            var tarjetasCoche = [];
            data["data"].forEach(c =>{
                tarjetasCoche.push(
                '<div class="tarjetaVehiculo">' +
                    '<!-- Listado de coches -->' +
                    '<div class="row mb-2 border lightBlueBorder rounded">' +
                        '<div class="col-sm-3 col-md-3 col-lg-3 col-xl-3">' +
                            '<img class="img-fluid" src="/img/'+ c.imagePath + '"' +
                                'alt="Imagen coche">' +
                        '</div>' +
                        '<div class="my-auto col-sm-6 col-md-6 col-lg-6 col-xl-6">' +
                            '<p class="my-auto fw-bold fs-5">'+ c.brand + " " +c.modelName+ '</p>' +
                            '<p>'+ c.license +'</p>' +
                        '</div>' +
                        '<div class="my-auto col-sm-3 col-md-3 col-lg-3 col-xl-3">' +
                           '<div class="row">' +
                                '<a href="#" class="textColor1 text-decoration-none">Modificar</a>' +
                            '</div>' +
                            '<div class="row">' +
                                '<a href="#viewBooking" onclick="viewBooking('+ c.id +')"' +
                                    'class="textColor1 text-decoration-none">Consultar reserva</a>' +
                            '</div>' +
                        '</div>' +
                    '</div>' +
                '</div>' +
            '</div>');
            });
            $('.tarjetaVehiculo').remove();
            $('#paginaGestionFlota').append(tarjetasCoche);
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });
});