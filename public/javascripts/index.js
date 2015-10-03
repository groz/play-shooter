console.log("connecting...");
var ws = new WebSocket("ws://localhost:9000/socket");

function startGame() {

  console.log("Game started");

  ws.onmessage = function(msg) {
    var json = JSON.parse(msg.data);
    console.log(json);
  };

  ws.sendjson = function(obj) {
    ws.send(JSON.stringify(obj));
  }

}

ws.onopen = startGame;
