var stompClient = null;

function appendOutput(text){
	let $output = $("#output");
    $output.val($output.val() + text + '\n');
}

function startGame(palyerId) {	
	let $number = $("#player" + palyerId + "-number");
	
	if (!$number[0].reportValidity()) { // Checks min/max values and shows erro to user
		return;
	}
	
	let gameId = Math.floor(Math.random() * 1000000);
	
	let destination = '/topic/' + gameId;
	appendOutput("Subscribing to " + destination);

	stompClient.subscribe(destination, function (frame) {
		
		appendOutput("websocket: frame " + frame.body);
		
		let event = JSON.parse(frame.body);
		if (event["@type"] == "end-game"){
			let destination = '/topic/' + event.gameId;
			appendOutput("Unsubscribing from " + destination);

			stompClient.unsubscribe(destination)
			appendOutput("\n");
		}
    });
	
	let data = { 
	  gameId: gameId,
	  playerId: palyerId,
	  number: $number.val() 
	}
			
	$.ajax({
	  method: "POST",
	  url: "start-game",
	  data: data
	});
}

$(function () {
	let socket = new SockJS('/player-events');
    stompClient = Stomp.over(socket);  
    stompClient.connect({}, function(frame) {
    	appendOutput("Connected");
    	$("#player1-startGame,#player2-startGame").prop("disabled", false); 
    }, function(frame) {
    	appendOutput("Connection error. Reload page to reconnect")
    	$("#player1-startGame,#player2-startGame").prop("disabled", true); 
    });
	
    $("#player1-startGame").click(function() { startGame(1); });
    $("#player2-startGame").click(function() { startGame(2); });
});