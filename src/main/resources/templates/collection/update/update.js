$.ajaxSetup({
    headers: { 
        'X-CSRF-TOKEN': '${_csrf.token}',
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
});

$('#cancelButton').click(function() {
    e.preventDefault();
    window.location='${rc.getContextPath()}/home';
});


$('#provider').change(function() {
    if(document.getElementsByName('provider')[0].value == 'TWITTER'){
        $('#facebookConfigDiv').hide();
        $('#keywordsDiv').show();
    }else if(document.getElementsByName('provider')[0].value == 'FACEBOOK'){
        $('#facebookConfigDiv').show();
        $('#keywordsDiv').hide();
    }else{
        $('#facebookConfigDiv').show();
        $('#keywordsDiv').show();
    }
});

$(document).ready(function() {
    //Populating language dropdown
    var oldLangFiltersMap = {};
    $('#oldLangFilters').val().split(",").forEach(function(val){
        oldLangFiltersMap[val.trim()] = true;
    })
    
    var langSelect = $('#lang')[0];
    for(i = 0; i < LANGS.length; i++) { 
        if(oldLangFiltersMap[LANGS[i][1]] == true){
            langSelect.prepend(new Option(LANGS[i][0], LANGS[i][1], true, true));
        }else{
            langSelect.add(new Option(LANGS[i][0], LANGS[i][1]));
        }
    }
    
    
    var oldProvider = $('#oldProvider').val();
    $("#provider option[value="+oldProvider+"]").attr('selected','selected');
    
    if(oldProvider == "ALL" || oldProvider == "TWITTER"){
        $('#keywordsDiv').show();
    }
    
    if(oldProvider == "ALL" || oldProvider == "FACEBOOK"){
        $('#facebookConfigDiv').show();
    }
    
    var oldFetchInterval = $('#oldFetchInterval').val();
    $("#fetchInterval option[value="+oldFetchInterval+"]").attr('selected','selected');
    
    var oldfetchFrom = $('#oldfetchFrom').val();
    $("#fetchFrom option[value="+oldfetchFrom+"]").attr('selected','selected');
    
    $('#collectionUpdate').bootstrapValidator({
        // To use feedback icons, ensure that you use Bootstrap v3.1.0 or later
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        submitButtons: '#collectionUpdate button[type="submit"]',
        fields: {
            provider: {
                validators: {
                    remote: {
                        url: '${rc.getContextPath()}/user/isProviderConnected',
                        data: function(validator, $field, value) {
                            return {
                                provider: document.getElementsByName('provider')[0].value
                            };
                        },
                        message: 'Please connect your social accounts before creating collection. <a href="${rc.getContextPath()}/connect">Click here</a> to connect.',
                        type: 'GET'
                    }
                }
            }
        }        
    }).on('success.form.bv', function (e) {
        updateCollection();
    });
});


function updateCollection() {
        
    //Getting the selected languages
    var langFilters = [];
    $("#lang :selected").each(function( index ) {
        if($( this ).val().trim() != ""){
            langFilters.push($( this ).val().trim());
        }
        
    });

    var collectionId = $('#collectionId').val();
    var data = {
            track: document.getElementsByName('track')[0].value.toLowerCase().trim(),
            subscribedProfiles: document.getElementsByName('subscribedProfiles')[0].value,
            fetchInterval: document.getElementsByName('fetchInterval')[0].value,
            fetchFrom: document.getElementsByName('fetchFrom')[0].value,
            langFilters: langFilters.join(","),
            provider: document.getElementsByName('provider')[0].value,
    };
    
    var url = "${rc.getContextPath()}/collection/update?id="+collectionId;
    
    $.ajax({
        type: "POST",
        url: url,
        dataType:"json",
        data: JSON.stringify(data),
        
        success: function(data){
            if(data.success){
                showInfoAlert(data.message);
                location.href = "${rc.getContextPath()}/collection/view/details?id="+data.data.id;
            }else{
                showErrorAlert(data.message);
            }
        }
    });
}