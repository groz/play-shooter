console.log("connecting...");

var ws = new WebSocket("ws://localhost:9000/socket");

function draw() {
  var scene = new THREE.Scene();
  var camera = new THREE.PerspectiveCamera( 75, window.innerWidth / window.innerHeight, 0.1, 1000 );

  var renderer = new THREE.WebGLRenderer();
  renderer.setSize( window.innerWidth, window.innerHeight );
  document.body.appendChild( renderer.domElement );

  var geometry = new THREE.BoxGeometry( 1, 1, 1 );
  var material = new THREE.MeshBasicMaterial( { color: 0x00ff00 } );
  var cube = new THREE.Mesh( geometry, material );
  scene.add( cube );

  camera.position.z = 5;

  function render() {
    requestAnimationFrame( render );

    cube.rotation.x += 0.01;
    cube.rotation.y += 0.01;

    renderer.render( scene, camera );
  }

  render();
}

function startGame() {

  console.log("Game started");

  ws.onmessage = function(msg) {
    var json = JSON.parse(msg.data);
    console.log(json);

    switch (json.name) {
      case "InitPlayer":
        console.log(json.data);
        break;
    }
  };

  ws.send(JSON.stringify(obj));

  draw();
}

ws.onopen = startGame;
