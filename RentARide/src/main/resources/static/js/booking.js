
function processBooking(response){
    console.log(response);

    const datos = JSON.stringify(response);
    const tabla = document.getElementById("content-booking");
    const encabezado = tabla.createTHead();
    const filaEncabezado = encabezado.insertRow();
    for (let dato of datos) {
    const fila = tabla.insertRow();
    for (let key in dato) {
        if (!encabezado.rows[0].cells.namedItem(key)) {
        const celdaEncabezado = filaEncabezado.insertCell();
        celdaEncabezado.innerHTML = key;
        }
        const celda = fila.insertCell();
        celda.innerHTML = dato[key];
  }

  $("#modalAlquiler").modal("show");

  console.log("Fnish");

}

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






    