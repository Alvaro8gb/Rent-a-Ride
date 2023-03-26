function refreshValue(idNewVal, idShowVal) {
    let val = $(idNewVal).val();
    $(idShowVal).text(val);
    //console.log("Precio" + val);
}

function processSearch(response){

}
function searchFilter(){
    console.log($("#searchV").val());

    let params = {"available" : $("#available").val(), "price": $("#priceRange").val()};

    console.log(params);
    // Spring Security lo añade en formularios html, pero no en Ajax
    params[config.csrf.name] = config.csrf.value;
    
    //return go(config.rootUrl + "/booking/"+ idVehicle, 'GET', params)
    //    .then(response => processSearch(response) )
    //    .catch(e => console.log(e));

}
