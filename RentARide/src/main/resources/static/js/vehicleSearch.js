$(document).ready(function(){
    $("#searchV").keyup(function(){
        aplicarFiltros();
    });

    $("#tipoDeCombustible").on("change", function(){
        aplicarFiltros();
    });

    $("#numeroPlazas").on("change", function(){
        aplicarFiltros();
    });

    $("#cambio").on("change", function(){
        aplicarFiltros();
    });

    $("#rangoPrecio").ionRangeSlider({
        type: "double",
        grid: true,
        min: 0,
        max: 250,
        from: 0,
        to: 250,
        postfix: "€",
        skin: "round",
        onFinish: function (data) {
            aplicarFiltros();
        }
    });
});

function aplicarFiltros(){
    const filtro = new RegExp($("#searchV").val().toLowerCase());
    const combustible = $("#tipoDeCombustible").val();
    const plazas = $("#numeroPlazas").val();
    const cambio = $("#cambio").val();
    const precio = $("#rangoPrecio");
    
    $(".tarjetaVehiculo").each(function(){
        const ls = $(this).attr("reservas")
        if(filtro.test($(this).attr("nombre").toLowerCase()) && 
        (combustible === $(this).attr("combustible") || combustible === "Todos") &&
        (plazas === $(this).attr("plazas") || plazas === "Todos") &&
        (cambio === $(this).attr("cambio") || cambio === "Todos") &&
        (precio.data("from") <= $(this).attr("precio") && precio.data("to") >= $(this).attr("precio"))){
            $(this).show();
        }
        else{
            $(this).hide();
        }
    });
}

ws.receive = (m) => {
    console.log(m);
}