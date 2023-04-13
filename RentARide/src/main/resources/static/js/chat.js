console.log("Chats");
ws.receive = (m) => {
    console.log(m);
    $(".chatBody").append(
        '<div class="col-12 chatMessage my-2 py-2 chatReceiver">'+
        'Mensaje recibido: '+ m["text"] +
        '</div>');   
} 


function sendMsg(idReceptor) {
    let msg = $("#message").val();
    $("#message").val("");
    console.log(msg);
    let params = {message: msg};
    // Spring Security lo añade en formularios html, pero no en Ajax
    params[config.csrf.name] = config.csrf.value;

    $(".chatBody").append(
        '<div class="col-12 chatMessage my-2 py-2 chatSender">'+
        msg + '</div>');  
    
    // petición en sí

    return go(config.rootUrl + "/user/"+ idReceptor + "/msg", 'POST', params)
        .then(response => console.log(response) )
        .catch(e => console.log(e));
    
}
