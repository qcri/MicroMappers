$.ajaxSetup({
    headers: { 
        'X-CSRF-TOKEN': '${_csrf.token}',
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
});

$('#stopTwitterButton').click(function() {
    var id = '${collectionInfo.id}';
    
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
    var id = '${collectionInfo.id}';
    
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
    var id = '${collectionInfo.id}';
    
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
    var id = '${collectionInfo.id}';
    
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
    var id = '${collectionInfo.id}';
    
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
     var id = '${collectionInfo.id}';
     
     $.get( "${rc.getContextPath()}/collection/status?id="+id )
         .done(function( statusData ) {
             if(toStartTwitterCountScheduler()){
                 $.get( "${rc.getContextPath()}/twitter/count?id="+id )
                     .done(function( data ) {
                         if('${collectionInfo.twitterStatus}' != statusData.data.twitterStatus){
                             location.reload();
                         }else{
                             $('#twitterCount').html(data.data);
                         }
                 });
             }
             
             if(toStartFacebookCountScheduler()){
                 $.get( "${rc.getContextPath()}/facebook/count?id="+id )
                     .done(function( data ) {
                         if('${collectionInfo.facebookStatus}' != statusData.data.facebookStatus){
                             location.reload();
                         }else{
                             $('#facebookCount').html(data.data);
                         }
                 });
             }
     });
}

if(toStartTwitterCountScheduler() || toStartFacebookCountScheduler()){
    setInterval('autoRefreshCollectionCount()', 5000);
}

function toStartTwitterCountScheduler(){
    status = '${collectionInfo.twitterStatus}';
    if(status =="RUNNING" || status == "RUNNING_WARNING" || status == "WARNING"){
        return true;
    }else{
        return false;
    }
}

function toStartFacebookCountScheduler(){
    status = '${collectionInfo.facebookStatus}';
    if(status =="RUNNING" || status == "RUNNING_WARNING" || status == "WARNING"){
        return true;
    }else{
        return false;
    }
}