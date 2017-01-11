$('#stopCollectionButton').click(function() {
    // handle deletion here
    var id = $('#stopCollectionButton').data('id');
    
    $.ajax({
        type: "GET",
        url: "${rc.getContextPath()}/twitter/stop?id="+id,
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
                showInfoAlert(data.message);
                location.reload();
            }else{
                showErrorAlert(data.message);
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
                showInfoAlert(data.message);
                location.reload();
            }else{
                showErrorAlert(data.message);
            }
        }
    });
});

function autoRefreshCollectionCount()
{    
     var id = $('#collectionCount').data('id');
     $.get( "${rc.getContextPath()}/twitter/status?id="+id );
     
     $.get( "${rc.getContextPath()}/collection/count?id="+id )
         .done(function( data ) {
             $('#collectionCount').html(data.data);
     });
}

function countScheduler() {
    setInterval('autoRefreshCollectionCount()', 5000); // refresh collection count after every 5 secs
};

if(initializeCountScheduler){
    setInterval('autoRefreshCollectionCount()', 5000);
}