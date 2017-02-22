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

$('#glideData').DataTable();