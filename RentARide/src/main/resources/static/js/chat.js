function openChat(idUser, msgId) {
    console.log(`Should post to ${msgId}`);
    go(config.rootUrl + "/messages/" + msgId, 'POST', {})
        .then(response => {
            console.log(response);
            $('#chatModal').modal('show');
            $(".agentChatForm button").attr("onclick", `sendMsg(${idUser})`);
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

ws.receive = (m) => {
    if (m["text"] == "chatAccepted") {
        insertMessage(`El agente ${m["from"]} se ha conectado al chat.`, false);
        $(".agentChatForm button").attr("onclick", `sendMsg(${m["senderID"]})`);
    } else
        insertMessage(m["text"], false);
}

function sendMsg(idReceptor) {
    let msg = $("#message").val();
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