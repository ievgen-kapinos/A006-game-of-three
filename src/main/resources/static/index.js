
function startGame(palyerId) {	
	var numberControl = $("#player" + palyerId + "-number");
	
	if (!numberControl[0].reportValidity()) {
		return;
	}
	
	var gameId = Math.floor(Math.random() * 1000000);
	
	$.ajax({
	  method: "POST",
	  url: "start-game",
	  data: { 
		  gameId: gameId,
		  playerId: palyerId,
		  number: numberControl.val() 
	  }
	});
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
});