console.log("connecting...");

var ws = new WebSocket("ws://localhost:9000/socket");

function createScene() {
  var scene = new THREE.Scene();
  var camera = new THREE.PerspectiveCamera( 75, window.innerWidth / window.innerHeight, 0.1, 1000 );

  var renderer = new THREE.WebGLRenderer();
  renderer.setSize( window.innerWidth, window.innerHeight );
  document.body.appendChild( renderer.domElement );

  camera.position.z = 5;

  function render() {
    //cube.rotation.x += 0.01;
    //cube.rotation.y += 0.01;

    requestAnimationFrame( render );
    renderer.render( scene, camera );
  }

  render();

  return scene;
}

var gameObjectMap = {};

function addSceneObject(scene, gameObject, color) {

  var geometry = new THREE.BoxGeometry( 0.3, 0.3, 0.3 );
  var material = new THREE.MeshBasicMaterial( { color: color } );
  var cube = new THREE.Mesh( geometry, material );

  cube.position.x = gameObject.position.x;
  cube.position.y = gameObject.position.y;

  gameObjectMap[gameObject.id.id] = cube;

  scene.add( cube );
}

function removeSceneObject(scene, gameObject) {
  var sceneObject = gameObjectMap[gameObject.id.id];
  scene.remove(sceneObject);
}

function moveSceneObject(scene, gameObject) {
  console.log("SceneObject moving: ");
  console.log(gameObject);
  var sceneObject = gameObjectMap[gameObject.id.id];
  sceneObject.position.x = gameObject.position.x
  sceneObject.position.y = gameObject.position.y
}

function startGame() {

  console.log("Game started");

  ws.onmessage = function(msg) {
    var json = JSON.parse(msg.data);
    console.log("Received raw message: ", json);

    switch (json.name) {
      
      case "InitPlayer":
        console.log(json.data);

        //first reposition
        var newGameObject =  json.data.state
        var xNew = newGameObject.position.x + 4.0*(Math.random()-0.5)
        var yNew = newGameObject.position.y + 4.0*(Math.random()-0.5)
        var reposMe = {name: "reposition", data: {x: xNew, y: yNew}}
        ws.send(JSON.stringify(reposMe))

        newGameObject.position.x = xNew
        newGameObject.position.y = yNew

        addSceneObject(scene, newGameObject, "green");

        //addSceneObject(scene, json.data.state, "green");
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

}

ws.onopen = startGame;
