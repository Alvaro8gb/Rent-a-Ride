function processBooking(json){
    console.log(json);

    if (json["data"] == "False"){
        $("#content-booking").text("No info");
    }
    else{
        $("#id-car").text(json["id"]["vehicleID"]);
        $("#precio-book").text(json["price"] + "€");
        $("#in-date").text(json["id"]["in_date"]);    
        $("#out-date").text(json["id"]["in_date"]);    

    }

    $("#modalAlquiler").modal("show");
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






    