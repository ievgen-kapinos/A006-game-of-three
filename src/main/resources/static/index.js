
function startGame(palyerId) {	
	var number = $("#player" + palyerId + "-number");
	
	if (!number[0].reportValidity()) { // Check min/max values and shows erro to user
		return;
	}
	
	var data = { 
	  gameId: Math.floor(Math.random() * 1000000),
	  playerId: palyerId,
	  number: number.val() 
	}
	
	$.ajax({
	  method: "POST",
	  url: "start-game",
	  data: data
	});
	
	appendOutput("User: Request for new Game " + JSON.stringify(data));
}

function appendOutput(text){
    var $output = $("#output");
    $output.val($output.val() + text + '\n');
}

$(function () {
    // TODO Inital statuses should be provided via Thymeleaf 
    
	// TODO need proper handling
    $("#player1-status,#player2-status")
    	.removeClass('badge-secondary')
    	.addClass('badge-success')
    	.text("Active");
    
    $("#player1-startGame,#player2-startGame").prop("disabled", false); 
    
    $("#player1-startGame").click(function() { startGame(1); });
    $("#player2-startGame").click(function() { startGame(2); });
    
    appendOutput("User: Loaded");
});