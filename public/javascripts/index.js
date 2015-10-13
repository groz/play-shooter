console.log("connecting...");

var ws = new WebSocket("ws://localhost:9000/socket");

var i = 0;

function sendCommand(name, data) {
  var msg = {
    name: name,
    data: data
  };
  if (++i < 10) console.log(msg);
  ws.send(JSON.stringify(msg));
}

var keyControls = {
  up: 87,
  left: 65,
  down: 83,
  right: 68
};

function createScene() {
  var scene = new THREE.Scene();
  var camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);

  var renderer = new THREE.WebGLRenderer();
  renderer.setSize(window.innerWidth, window.innerHeight);
  document.body.appendChild(renderer.domElement);

  camera.position.z = 5;

  var previousFrameTime = new Date().getTime();

  function render() {
    var currentTime = new Date().getTime();
    var timeDelta = currentTime - previousFrameTime; // in millis

    for (var k in gameObjectMap) {
      var gameObject = gameObjectMap[k];

      gameObject.speed.x *= 0.95;
      gameObject.speed.y *= 0.95;

      gameObject.sceneObject.position.x += gameObject.speed.x * (timeDelta / 1000);
      gameObject.sceneObject.position.y += gameObject.speed.y * (timeDelta / 1000);
    }

    if (currentPlayer.sceneObject) {
      sendCommand("Reposition", {
        newPosition: {
          x: currentPlayer.sceneObject.position.x,
          y: currentPlayer.sceneObject.position.y
        }
      });
    }

    requestAnimationFrame(render);
    renderer.render(scene, camera);
    previousFrameTime = currentTime;
  }

  render();

  return scene;
}

var gameObjectMap = {};
var currentPlayer = {};

function addSceneObject(scene, gameObject, color) {

  var geometry = new THREE.BoxGeometry(0.3, 0.3, 0.3);
  var material = new THREE.MeshBasicMaterial({color: color});
  var cube = new THREE.Mesh(geometry, material);

  cube.position.x = gameObject.position.x;
  cube.position.y = gameObject.position.y;

  gameObject.sceneObject = cube;

  gameObjectMap[gameObject.id.id] = gameObject;

  scene.add(cube);
}

function removeSceneObject(scene, gameObject) {
  var sceneObject = gameObjectMap[gameObject.id.id].sceneObject;
  scene.remove(sceneObject);
}

function moveSceneObject(scene, gameObject) {
  console.log("SceneObject moving: ");
  console.log(gameObject);
  var sceneObject = gameObjectMap[gameObject.id.id].sceneObject;
  sceneObject.position.x = gameObject.position.x;
  sceneObject.position.y = gameObject.position.y;
}

function startGame() {

  console.log("Game started");

  ws.onmessage = function (msg) {
    var json = JSON.parse(msg.data);
    console.log("Received raw message: ", json);

    switch (json.name) {

      case "InitPlayer":
        console.log(json.data);

        //first reposition
        currentPlayer = json.data.state;
        var xNew = currentPlayer.position.x + 4.0 * (Math.random() - 0.5);
        var yNew = currentPlayer.position.y + 4.0 * (Math.random() - 0.5);
        sendCommand("Reposition", {newPosition: {x: xNew, y: yNew}});

        currentPlayer.position.x = xNew;
        currentPlayer.position.y = yNew;

        addSceneObject(scene, currentPlayer, "green");

        for (var p = 0, end = json.data.players.length; p < end; ++p) {
          addSceneObject(scene, json.data.players[p], "red");
        }
        break;

      case "PlayerReposition":
        console.log("Repositioning: ");
        console.log(json.data);
        moveSceneObject(scene, json.data.state);
        break;


      case "PlayerJoined":
        console.log(json.data);
        addSceneObject(scene, json.data.state, "red");
        break;

      case "PlayerLeft":
        console.log(json.data);
        removeSceneObject(scene, json.data.state);
        break;
    }
  };

  var scene = createScene();

  function movePlayer(x, y) {
    currentPlayer.speed = {x: x, y: y};
  }

  window.onkeydown = function (e) {

    var keycode = (window.event && window.event.keyCode) || e.which;

    switch (keycode) {
      case keyControls.up:
        movePlayer(0, 2);
        break;
      case keyControls.down:
        movePlayer(0, -2);
        break;
      case keyControls.left:
        movePlayer(-2, 0);
        break;
      case keyControls.right:
        movePlayer(2, 0);
        break;
    }

  }

}

ws.onopen = startGame;
