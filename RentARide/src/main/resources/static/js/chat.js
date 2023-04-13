console.log("Chats");
ws.receive = (m) => {
    console.log(m);
    $(".chatBody").append(
        '<div class="col-12 chatMessage my-2 py-2 chatReceiver">'+
        'Mensaje recibido: '+ m["text"] +
        '</div>');
    console.log("Adios");   
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
    

    return go(config.rootUrl + "/user/"+ idReceptor + "/msg", 'POST', params)
        .then(response => console.log(response) )
        .catch(e => console.log(e));
    
}

function viewChat(idReceptor){

    let chatContainer = `
    <div class="container chat" id="chat">
        <div class="row chatHeader py-3">
            <div class="col-12">
                Respondiendo a usuario con id= ${idReceptor}
            </div>
        </div>

        <div class="row chatBody p-3 d-flex h-100">
                    
        </div>

        <div class="row chatInput py-3">
            <div class="col-10">
                <div class="input-group">
                    <input type="text" class="form-control border-0" name="message" id="message"
                        placeholder="Escribe tu mensaje">
                    <span class="input-group-append px-2 d-flex align-items-center attachmentGroup">
                        <label role="button" for="attachment"><i class="fa-solid fa-paperclip"></i></label>
                    </span>
                </div>
            </div>
            <div class="col-2">
                <button id="sendmsg" class="btn btn-secondary" onclick="sendMsg(${idReceptor})"
                    type="submit">Enviar</button>
            </div>
        </div>
    </div>
`   ;

    $("#chatcontainer").html(chatContainer);

}
