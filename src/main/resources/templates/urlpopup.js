/**
 * 
 */
$(document).ready(function() {
	//console.log('Glides Loaded.');
	$('.hoverMe').hover(function(event) {
		$('#urlModalDiv').dialog();
		$('#urlModalIFrame').attr('src',$(this).attr('href'));
		
	}, function() {
		$("#urlModalIFrame").contents().find("body").html('');
	});
});