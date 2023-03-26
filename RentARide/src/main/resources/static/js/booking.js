function processBooking(json){
    console.log(json);
    let car = json["data"];

    if (!car){
        $("#content-booking").text("No info");
    }
    else{
        $("#precio-book").text(car["price"] + "€");
        $("#in-date").text(car["id"]["in_date"]);    
        $("#out-date").text(car["id"]["in_date"]);    

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






    