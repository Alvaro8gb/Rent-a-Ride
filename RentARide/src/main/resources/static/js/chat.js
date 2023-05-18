function openChat(idUser, msgId) {
    go(config.rootUrl + "/messages/" + msgId, 'POST', {})
        .then(response => {
            if (response.result == "accepted") {
                $(".agentChatForm button").attr("onclick", `sendMsg(${idUser})`);
                console.log(response);
                $(".agentChatHeader h3").html(`Atendiendo a: <span class='username'>${response.clientName}</span>`);
                console.log(response.clientName);
                // $(".agentChatBody").empty();
                $('#chatModal').modal('show');
            } else
                alert(`Error! ${response["data"]}`);
        })
        .catch(e => console.log(e));
}

function openChatHistory(idUser) {
    go(config.rootUrl + "/messages/history/" + idUser, 'GET', {})
        .then(response => {
            console.log(response);
            if (response["result"] == "accepted") {
                $(".agentChatForm button").attr("onclick", `sendMsg(${idUser})`);
                $(".agentChatHeader h3").html(`Historial del usuario: <span class='username'>${response.clientName}</span>`);
                $("#username").text(`#${idUser}`);
                $(".agentChatBody").empty();
                $('#chatModal').modal('show');
                response.messages.forEach(msg => {
                    insertMessage(msg.text, msg.sender);
                });
            } else
                alert(`Error! ${response["data"]}`);
        })
        .catch(e => console.log(e));
}

function insertMessage(text, sender) {
    // Create a new message element
    var newMessage;
    if (sender)
        newMessage = $(`<div class="row justify-content-end"><div class="col-6 agentChatMessage text-end">${text}</div></div>`);
    else
        newMessage = $(`<div class="row justify-content-start"><div class="col-6 agentChatMessage text-start">${text}</div></div>`);

    // Get the last agent chat message
    var lastMessage = $('.agentChatBody .row:last');

    if (lastMessage.length <= 0) {
        lastMessage = $('.agentChatBody');
        lastMessage.append(newMessage);
    } else
        lastMessage.after(newMessage); // Insert the new message after the last message
}

function sendMsg(idReceptor) {
    let msg = $("#message").val();

    if (msg == "")
        alert(`Escribe un mensaje`);
    else{
        $("#message").val("");
        let params;

        if (typeof firstMessage != 'undefined' && firstMessage) {
            params = { message: msg, firstMessage: firstMessage };
            firstMessage = false;
        } else {
            firstMessage = false;
            params = { message: msg, firstMessage: firstMessage };
        }

        // Spring Security lo añade en formularios html, pero no en Ajax
        params[config.csrf.name] = config.csrf.value;

        insertMessage(msg, true);

    return go(config.rootUrl + "/messages/user/" + idReceptor, 'POST', params)
        .then(response => console.log(response))
        .catch(e => console.log(e));
    }
}


ws.receive = (m) => {
    if (m.text == "chatAccepted") {
        insertMessage(`El agente ${m["from"]} se ha conectado al chat.`, false);
        $(".agentChatForm button").attr("onclick", `sendMsg(${m["senderID"]})`);
    } else
        insertMessage(m.text, false);
}

$(document).ready(function() {
    const input = $('#message');
    const button = document.querySelector('#chatSend');
  
    input.on('keyup', function(event) {
      if (event.key === 'Enter') {
        console.log("Enviado mediante Enter");
        button.click();
      }
    });
});
  




  