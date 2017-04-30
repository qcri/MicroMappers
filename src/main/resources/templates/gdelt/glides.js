$.ajaxSetup({
	headers: {
		'X-CSRF-TOKEN': '${_csrf.token}',
		'Accept': 'application/json',
		'Content-Type': 'application/json'
	}
});

$('.image-classifier').on('click', function(e) {
	e.preventDefault();

	var id = $(this).data('id');
	var url = "${rc.getContextPath()}/service/request/cv?type=gdelt&id="+id+"&acid="+id;

	$.ajax({
		type: "GET",
		url: url,
		dataType:"json",
		success: function(data){
			if(data.success){
				showInfoAlert(data.message);
				location.href = "${rc.getContextPath()}/global/events/gdelt/glides";
			}else{
				showErrorAlert(data.message);
			}
		}
	});
});

$('#glideData').dataTable( {
    columnDefs: [ { "orderable": false, "targets": 2 } ]
} );

$('#createClassifierRequest').on('click', function(e) {
	e.preventDefault();

	var data = {
		name: document.getElementsByName('textName')[0].value.toLowerCase().trim(),
		theme: document.getElementsByName('textTheme')[0].value.toLowerCase().trim(),
		loc: document.getElementsByName('textLocation')[0].value.toLowerCase().trim(),
		cc: document.getElementsByName('textCountry')[0].value,
		webtag: document.getElementsByName('textImageWebTag')[0].value,
		tag: document.getElementsByName('textImageTag')[0].value
	};

	var url = "${rc.getContextPath()}/global/events/gdelt/request?data="+JSON.stringify(data);
	$.ajax({
		type: "POST",
		url: url,
		dataType:"json",
		completed: function(data){
			location.href = "${rc.getContextPath()}/global/events/gdelt/classifiers";
		}
	});

});
