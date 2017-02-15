$.ajaxSetup({
    headers: { 
        'X-CSRF-TOKEN': '${_csrf.token}',
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
});

$(document).ready(function() {
    $('#collectionCreate').bootstrapValidator({
        // To use feedback icons, ensure that you use Bootstrap v3.1.0 or later
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        submitButtons: '#collectionCreate button[type="submit"]',
        fields: {
            name: {
                trigger: "focus blur",
                validators: {
                    remote: {
                        url: '${rc.getContextPath()}/collection/existName',
                        data: function(validator, $field, value) {
                            return {
                                name: document.getElementsByName('name')[0].value
                            };
                        },
                        message: 'This collectionName is not available',
                        type: 'GET'
                    },
                    stringLength: {
                        min: 2,
                    }
                }
            },
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
        createCollection();
    });
    
    //Populating language dropdown
    var langSelect = $('#lang')[0];
    langSelect.add(new Option(LANGS[0][0], LANGS[0][1], true, true));
    for(i = 1; i < LANGS.length; i++) { 
        langSelect.add(new Option(LANGS[i][0], LANGS[i][1]));
    }
    
    //Searching the subscribed profiles
    $('.subscribedProfileSelect').select2({
        width:"100%",
        placeholder: "Search facebook Pages/Groups/Events to subscribe.",
        closeOnSelect: false,
        
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
          templateResult: formatRepo,
          templateSelection: formatRepoSelection
          
          
     });
    
    $('#twitterDatePicker').datepicker({
        format: "dd/mm/yyyy",
        endDate: "today",
        maxViewMode: 3,
        clearBtn: true,
        todayHighlight: true,
        autoclose: true,
        startDate: "21-03-2006"
    });
});

$('#provider').change(function() {
    if(document.getElementsByName('provider')[0].value == 'TWITTER'){
        $('#facebookConfigDiv').hide();
        $('#twitterConfigDiv').show();
    }else if(document.getElementsByName('provider')[0].value == 'FACEBOOK'){
        $('#facebookConfigDiv').show();
        $('#twitterConfigDiv').hide();
    }else{
        $('#facebookConfigDiv').show();
        $('#twitterConfigDiv').show();
    }
});

function createCollection() {
        
    //Getting the selected languages
    var langFilters = [];
    $("#lang :selected").each(function( index ) {
        if($( this ).val().trim() != ""){
            langFilters.push($( this ).val().trim());
        }
        
    });

    var eventTypeId = $("#eventTypeId").val();
    var eventType = $("#eventType").val();
    
    var data = {
            name: document.getElementsByName('name')[0].value.toLowerCase().trim(),
            code: document.getElementsByName('name')[0].value.toLowerCase().trim().replace(" ", "_") + "_" + (new Date()).getTime(),
            track: document.getElementsByName('track')[0].value.toLowerCase().trim(),
            fetchInterval: document.getElementsByName('fetchInterval')[0].value,
            fetchFrom: document.getElementsByName('fetchFrom')[0].value,
            langFilters: langFilters.join(","),
            subscribedProfiles: getSubscribedProfiles(),
            provider: document.getElementsByName('provider')[0].value,
    };
    
    if($('#twitterStartDate').val() != ""){
        data.twitterSinceDate = $('#twitterStartDate').data('datepicker').viewDate.getTime();
    }
    
    if($('#twitterEndDate').val() != ""){
        data.twitterUntilDate = $('#twitterEndDate').data('datepicker').viewDate.getTime();
    }
        
    if(eventType != null && eventType.toLowerCase() === "snopes" && eventTypeId != null){
        data.globalEventDefinition = {};
        data.globalEventDefinition.id = eventTypeId;
    }else if(eventType != null && eventType.toLowerCase() === "gdelt" && eventTypeId != null){
        data.glideMaster = {};
        data.glideMaster.id = eventTypeId;
    }
    
    var runAfterCreate = false;
    if($('#runAfterCreate').is(':checked')){
        runAfterCreate = true;
    }
    
    var url = "${rc.getContextPath()}/collection/create?runAfterCreate="+runAfterCreate;
    
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

function formatRepo (repo) {
    if (repo.loading) return repo.text;
    
    var markup = "<div class='select2-result-repository clearfix'>" +
      "<div class='select2-result-repository__avatar'><img src='" + repo.imageUrl + "' /></div>" +
      "<div class='select2-result-repository__meta'>" +
        "<div class='select2-result-repository__title'>" + repo.name + "</div>"; 
    
    if(repo.type == "PAGE"){
        markup += "<span class='select2-result-repository__likes'>"+ repo.fans + " Likes</span>";
    }else{
        markup += "<span class='select2-result-repository__likes'>"+ repo.type + "</span>";
    }
    
    return markup;
}

function formatRepoSelection (repo) {
    return repo.name || repo.text;
}