$.ajaxSetup({
    headers: { 
        'X-CSRF-TOKEN': '${_csrf.token}',
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
});

$('#stopTwitterButton').click(function() {
    // handle deletion here
    var id = $('#stopTwitterButton').data('id');
    
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

$('#stopFacebookButton').click(function() {
    // handle deletion here
    var id = $('#stopFacebookButton').data('id');
    
    $.ajax({
        type: "GET",
        url: "${rc.getContextPath()}/facebook/stop?id="+id,
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

$('#startTwitterButton').click(function() {
    // handle deletion here
    var id = $('#startTwitterButton').data('id');
    
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

$('#startFacebookButton').click(function() {
    // handle deletion here
    var id = $('#startFacebookButton').data('id');
    
    $.ajax({
        type: "GET",
        url: "${rc.getContextPath()}/facebook/start?id="+id,
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
     var id = $('#twitterCount').data('id');
     if(!id){
         id = $('#facebookCount').data('id');
     }
     $.get( "${rc.getContextPath()}/collection/status?id="+id );
     
     if(initializeTwitterCountScheduler){
         $.get( "${rc.getContextPath()}/twitter/count?id="+id )
             .done(function( data ) {
                 $('#twitterCount').html(data.data);
         });
     }
     
     if(initializeFacebookCountScheduler){
         $.get( "${rc.getContextPath()}/facebook/count?id="+id )
             .done(function( data ) {
                 $('#facebookCount').html(data.data);
         });
     }
}

function countScheduler() {
    setInterval('autoRefreshCollectionCount()', 5000); // refresh collection count after every 5 secs
};

if(initializeTwitterCountScheduler || initializeFacebookCountScheduler){
    setInterval('autoRefreshCollectionCount()', 5000);
}