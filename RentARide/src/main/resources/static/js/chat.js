
class Chat {
    constructor(title, idReceptor) {
        this.title = title;
        this.idReceptor = idReceptor;

    }

    header() {
        return `
        <div class="row chatHeader py-3">
            <div class="col-12">
                ${this.title}
            </div>
        </div>
        `
    }

    html(msgs) {
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

        container += Chat.msgRecd("Hola"); // Recorrer mensajes

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

    static msgRecd(text) {
        return `
        <div class="col-12 chatMessage my-2 py-2 chatReceiver">
            ${text}
        </div>`;
    }

}


ws.receive = (m) => {
    console.log("Mensaje Recibido: \n" + m);
    $(".chatBody").append(Chat.msgRecd(m["text"]));
}

function sendMsg(idReceptor) {
    let msg = $("#message").val();
    $("#message").val("");
    console.log(msg);
    let params = { message: msg };

    // Spring Security lo añade en formularios html, pero no en Ajax
    params[config.csrf.name] = config.csrf.value;

    $(".chatBody").append(Chat.msgSend(msg));

    return go(config.rootUrl + "/user/" + idReceptor + "/msg", 'POST', params)
        .then(response => console.log(response))
        .catch(e => console.log(e));

}

function viewChat(idReceptor) {

    const chat = new Chat("Mensajes de usuario " + idReceptor, idReceptor);

    $("#chatcontainer").html(chat.html());

}
