$('#stopCollectionButton').click(function() {
    // handle deletion here
    var id = $('#stopCollectionButton').data('id');
    
    $.ajax({
        type: "GET",
        url: "${rc.getContextPath()}/twitter/stop?id="+id,
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
});

$('#startCollectionButton').click(function() {
    // handle deletion here
    var id = $('#startCollectionButton').data('id');
    
    $.ajax({
        type: "GET",
        url: "${rc.getContextPath()}/twitter/start?id="+id,
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
});

$('#restoreCollectionButton').click(function() {
    // handle deletion here
    var id = $('#restoreCollectionButton').data('id');
    
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
});