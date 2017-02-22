$.ajaxSetup({
    headers: { 
        'X-CSRF-TOKEN': '${_csrf.token}',
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
});

$('#collectionGrid').DataTable();

$('.confirm-edit').on('click', function(e) {
    e.preventDefault();
    var id = $(this).data('id');
    window.location="${rc.getContextPath()}/collection/view/update?id="+id;
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

$('.confirm-download').on('click', function(e) {
    e.preventDefault();

    var id = $(this).data('id');
    var page_no = $(this).data('page');
    window.location="${rc.getContextPath()}/collection/view/list?id="+id;
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
                showInfoAlert(data.message);
                location.reload();
            }else{
                showErrorAlert(data.message);
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
                showInfoAlert(data.message);
                location.reload();
            }else{
                showErrorAlert(data.message);
            }
        }
    });
    
    $('#restore').modal('hide');
});