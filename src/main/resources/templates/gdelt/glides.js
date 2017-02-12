$.ajaxSetup({
	headers: {
		'X-CSRF-TOKEN': '${_csrf.token}',
		'Accept': 'application/json',
		'Content-Type': 'application/json'
	}
});

$(document).ready(function() {
	//console.log('Glides Loaded.');
	$('#reliefWebDiv').width($(document).width()*2/3+'px');

	$('#reliefWebDiv').height($(window).height()*2/3);
	$('#reliefWebIFrame').width('100%');
	$('#reliefWebIFrame').height('100%');
	$('.hoverMe').hover(function(event) {
		var left=event.pageX+30;
		var top=50;//event.pageY;
		console.log("  Left:"+left+ ' Top :'+top);
		$('#reliefWebDiv').css('left', left+'px');
		$('#reliefWebDiv').css('top', top+'px');
		$('#reliefWebIFrame').attr('src',$(this).attr('href'));
		$('#reliefWebDiv').show();
	}, function() {
		$('#reliefWebDiv').hide();
		$('#reliefWebDiv').offset({ top: 0, left: 0});
		$("#reliefWebIFrame").contents().find("body").html('');
	});
});

$('.image-classifier').on('click', function(e) {
	e.preventDefault();

	var id = $(this).data('id');
	window.location="${rc.getContextPath()}/service/request/cv?type=gdelt&id="+id+"&acid="+id;

});