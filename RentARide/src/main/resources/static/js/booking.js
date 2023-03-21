
function viewBooking(idVehicle) {
    let params = {idVehicle: idVehicle};
    // Spring Security lo añade en formularios html, pero no en Ajax
    params[config.csrf.name] = config.csrf.value;
    // petición en sí
    
    let info = document.getElementById("modal-content");

    console.log("Ados");
    return go(config.rootUrl + "/booking/"+ idVehicle, 'GET', params)
        .then(d => info.textContent = "Adios" )
        .catch(() => "Error showing booking");

    
}






    