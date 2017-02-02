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

    
    //Searching the subscribed profiles
    var selectedProfiles = [];
    var oldSubscribedProfiles;
    if('${collectionInfo.subscribedProfiles}' != ''){
        oldSubscribedProfiles = JSON.parse('${collectionInfo.subscribedProfiles}');
        oldSubscribedProfiles.forEach(function(profile){
            selectedProfiles.push(profile.id);
        })
    }
    
    
 $('.subscribedProfileSelect').select2({
        width:"100%",
        placeholder: "Search facebook Pages/Groups/Events to subscribe.",
        closeOnSelect: false,
        data: oldSubscribedProfiles,
        
        ajax: {
            url: "${rc.getContextPath()}/facebook/searchProfiles",
            dataType: 'json',
            delay: 250,
            data: function (params) {
              return {
                keyword: params.term, // search term
                limit: 30,
                offset: 0
              };
            },
            processResults: function (data, params) {
              return {
                results: data
              };
            },
            cache: true
          },
          
          escapeMarkup: function (markup) { return markup; },
          minimumInputLength: 1,
          templateResult: formatFacebookProfile,
          templateSelection: formatFacebookProfileSelection
     });
 
     $('.subscribedProfileSelect').val(selectedProfiles).trigger('change');
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
            subscribedProfiles: getSubscribedProfiles(),
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


function getSubscribedProfiles(){
    var selectedProfilesObject = $(".subscribedProfileSelect").select2('data');
    var newSelectedProfilesObject = [];
    
    selectedProfilesObject.forEach(function(selectedProfile) {
        var profile = {};
        profile.id = selectedProfile.id;
        profile.link = selectedProfile.link;
        profile.fans = selectedProfile.fans;
        profile.name = selectedProfile.name;
        profile.imageUrl = selectedProfile.imageUrl;
        profile.type = selectedProfile.type;
        
        newSelectedProfilesObject.push(profile);
    })
        
    return JSON.stringify(newSelectedProfilesObject);
}


function formatFacebookProfile (profile) {
    if (profile.loading) return profile.text;
    
    var markup = "<div class='select2-result-repository clearfix'>" +
      "<div class='select2-result-repository__avatar'><img src='" + profile.imageUrl + "' /></div>" +
      "<div class='select2-result-repository__meta'>" +
        "<div class='select2-result-repository__title'>" + profile.name + "</div>"; 
    
    if(profile.type == "PAGE"){
        markup += "<span class='select2-result-repository__likes'>"+ profile.fans + " Likes</span>";
    }else{
        markup += "<span class='select2-result-repository__likes'>"+ profile.type + "</span>";
    }
    
    return markup;
}

function formatFacebookProfileSelection (profile) {
    return profile.name || profile.text;
}