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
  
  