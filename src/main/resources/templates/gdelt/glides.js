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

	var n1 = document.getElementsByName('textName')[0].value.toLowerCase().trim();
	var n2 = document.getElementsByName('textImageWebTag')[0].value;
	var n3 = document.getElementsByName('textImageTag')[0].value;
	if(n1.length > 0 && ( n2.length > 0 || n3.length > 0)){
		var data = {
			name: document.getElementsByName('textName')[0].value.toLowerCase().trim(),
			loc: document.getElementsByName('textLocation')[0].value.toLowerCase().trim(),
			webtag: document.getElementsByName('textImageWebTag')[0].value,
			tag: document.getElementsByName('textImageTag')[0].value
		};

		location.href= "${rc.getContextPath()}/global/events/gdelt/request?data="+JSON.stringify(data);
	}
	else{
		showErrorAlert("Please fill out the form");
	}

});
