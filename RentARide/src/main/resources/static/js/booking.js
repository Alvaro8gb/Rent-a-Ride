function processBooking(json){
    console.log(json);

    let table = document.getElementById("content-booking");

    $("#id-car").text(json["id"]["vehicleID"]);
    $("#precio-book").text(json["price"] + "€");
    $("#modalAlquiler").modal("show");

    console.log(table);
    console.log("Finish");

}

function viewBooking(idVehicle) {
    let params = {idVehicle: idVehicle};
    // Spring Security lo añade en formularios html, pero no en Ajax
    params[config.csrf.name] = config.csrf.value;
    
    // petición en sí

    return go(config.rootUrl + "/booking/"+ idVehicle, 'GET', params)
        .then(response => processBooking(response) )
        .catch(e => console.log(e));
    
}






    