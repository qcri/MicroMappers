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
    
});


$("#collectionUpdate").submit(function(e) {
    if($('#collectionUpdate').valid()){
        
        //Getting th selected languages
        var langFilters = [];
        $("#lang :selected").each(function( index ) {
            if($( this ).val().trim() != ""){
                langFilters.push($( this ).val().trim());
            }
            
        });

        var collectionId = $('#collectionId').val();
        var data = {
                track: document.getElementsByName('track')[0].value.toLowerCase().trim(),
                /*follow: document.getElementsByName('follow')[0].value.toLowerCase().trim(),
                geo: document.getElementsByName('geo')[0].value.toLowerCase().trim(),
                durationHours: document.getElementsByName('duration')[0].value,
                geoR: document.getElementsByName('geoR')[0].value.toLowerCase().trim(),*/
                langFilters: langFilters.join(","),
                provider: document.getElementsByName('provider')[0].value,
        };
        
        
        var url = "${rc.getContextPath()}/"+data.provider.toLocaleLowerCase()+"/update?id="+collectionId;
        
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
        
        e.preventDefault(); // avoid to execute the actual submit of the form.
    }
});