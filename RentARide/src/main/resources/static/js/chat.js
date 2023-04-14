
class Chat {
    constructor(title, idReceptor, username, msgs) {
        this.title = title;
        this.idReceptor = idReceptor;
        this.username = username;
        console.log(username);
        this.msgs = msgs;
    }

    html() {
        let container = `<div class="container chat" id="chat">`;

        // HEADER
        // container += this.header(); Esto no va y nose porque 
        container += `
                <div class="row chatHeader py-3">
                    <div class="col-12">
                        ${this.title}
                    </div>
                </div>
                `

        // BODY

        container += `<div class="row chatBody p-3 d-flex h-100">`;

        for (let i = 0; i < this.msgs.length; i++) {

            let msg = this.msgs[i];
            if (msg.to == this.username) container += Chat.msgSend(msg.text);
            else container += Chat.msgRecv(msg.text);
            
        }

        container += `</div>`;

        // INPUT 
        container += `
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
                    <button id="sendmsg" class="btn btn-secondary" onclick="sendMsg(${this.idReceptor})"
                        type="submit">Enviar</button>
                </div>
            </div>
        `;

        container += `</div>`;

        return container;
    }

    static msgSend(text) {
        return `
        <div class="col-12 chatMessage my-2 py-2 chatSender">
            ${text}
        </div>`;
    }

    static msgRecv(text) {
        return `
        <div class="col-12 chatMessage my-2 py-2 chatReceiver">
            ${text}
        </div>`;
    }

}


ws.receive = (m) => {
    console.log("Mensaje Recibido: \n" + m);
    $(".chatBody").append(Chat.msgRecv(m["text"]));
}

function sendMsg(idReceptor) {
    let msg = $("#message").val();
    $("#message").val("");
    console.log(msg);
    let params = { message: msg };

    // Spring Security lo añade en formularios html, pero no en Ajax
    params[config.csrf.name] = config.csrf.value;

    $(".chatBody").append(Chat.msgSend(msg));

    return go(config.rootUrl + "/message/user/" + idReceptor, 'POST', params)
        .then(response => console.log(response))
        .catch(e => console.log(e));

}

function acceptMsg(idMessage, idReceptor, username) {

    console.log("Acepting message : " + idMessage);


    let response = go(config.rootUrl + "/message/" + idMessage, 'POST', {})
        .then(response => console.log(response))
        .catch(e => console.log(e));


    location.reload();

    return go(config.rootUrl + "/message/user/" + idReceptor, 'POST', { message: "El gestor " + username + " ha aceptado tu solicitud" })
        .then(response => console.log(response))
        .catch(e => console.log(e));
}

function generateChat(response, idReceptor, username) {

    const msgs = response["msgs"];

    const chat = new Chat("Mensajes de usuario " + idReceptor, idReceptor, username, msgs);

    $("#chatcontainer").html(chat.html());
}
function viewChat(idUser, username) {

    return go(config.rootUrl + "/message/receivedfrom/" + idUser, 'GET', {})
        .then(response => generateChat(response, idUser, username))
        .catch(e => console.log(e));

}
