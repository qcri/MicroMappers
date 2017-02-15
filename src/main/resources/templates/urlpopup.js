/**
 * 
 */

$(document).ready(function() {
	//console.log('Glides Loaded.');
	$('#urlModalDiv').width($(document).width()*2/3+'px');

	$('#urlModalDiv').height($(window).height()*2/3);
	$('#urlModalIFrame').width('100%');
	$('#urlModalIFrame').height('100%');
	$('.hoverMe').hover(function(event) {
		var left=event.pageX+30;
		var top=50;//event.pageY;
		console.log("  Left:"+left+ ' Top :'+top);
		$('#urlModalDiv').css('left', left+'px');
		$('#urlModalDiv').css('top', top+'px');
		$('#urlModalIFrame').attr('src',$(this).attr('href'));
		$('#urlModalDiv').show();
	}, function() {
		$('#urlModalDiv').hide();
		$('#urlModalDiv').offset({ top: 0, left: 0});
		$("#urlModalIFrame").contents().find("body").html('');
	});
});