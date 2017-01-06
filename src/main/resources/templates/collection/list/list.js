$.ajaxSetup({
    headers: { 
        'X-CSRF-TOKEN': '${_csrf.token}',
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
});


$('.confirm-delete').on('click', function(e) {
    e.preventDefault();

    var id = $(this).data('id');
    $('#delete').data('id', id).modal('show');
});

$('.confirm-restore').on('click', function(e) {
    e.preventDefault();

    var id = $(this).data('id');
    $('#restore').data('id', id).modal('show');
});


$('#deleteYes').click(function() {
    // handle deletion here
    var id = $('#delete').data('id');
    
    $.ajax({
        type: "POST",
        url: "${rc.getContextPath()}/collection/delete?id="+id,
        success: function(data)
        {
            if(data.success){
                alert(data.message);
                location.reload();
            }else{
                alert(data.message);
            }
        }
    });
    
    $('#delete').modal('hide');
});

$('#restoreYes').click(function() {
    // handle deletion here
    var id = $('#restore').data('id');
    
    $.ajax({
        type: "POST",
        url: "${rc.getContextPath()}/collection/untrash?id="+id,
        success: function(data)
        {
            if(data.success){
                alert(data.message);
                location.reload();
            }else{
                alert(data.message);
            }
        }
    });
    
    $('#restore').modal('hide');
});