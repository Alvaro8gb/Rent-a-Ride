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
                '<div class="tarjetaVehiculo" combustible="' + c.fuel + '" plazas="' + c.seats + '>' +
                    '<!-- Listado de coches -->' +
                    '<div class="row mb-2 border lightBlueBorder rounded">' +
                        '<div class="col-sm-3 col-md-3 col-lg-3 col-xl-3">' +
                            '<img class="img-fluid" src="vehicle/'+ c.id + '/pic"' +
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

$(document).ready(function(){
    $("#tipoDeCombustible").on("change", function(){
        var combustible = $(this).val();
        if(combustible != "Todos"){
            $(".tarjetaVehiculo").each(function(){
                console.log(combustible);
                console.log($(this).attr("combustible"));
                if(combustible != $(this).attr("combustible")){
                    $(this).hide();
                }
                else{
                    $(this).show();
                }
            });
        }
        else{
            $(".tarjetaVehiculo").each(function(){
                $(this).show();
            });
        }
    });
});

$(document).ready(function(){
    $("#numeroPlazas").on("change", function(){
        var plazas = $(this).val();
        if(plazas != "Todos"){
            $(".tarjetaVehiculo").each(function(){
                console.log(plazas);
                console.log($(this).attr("plazas"));
                if(plazas != $(this).attr("plazas")){
                    $(this).hide();
                }
                else{
                    $(this).show();
                }
            });
        }
        else{
            $(".tarjetaVehiculo").each(function(){
                $(this).show();
            });
        }
    });
});

function refreshValue(idNewVal, idShowVal) {
    let val = $(idNewVal).val();
    $(idShowVal).text(val);
}