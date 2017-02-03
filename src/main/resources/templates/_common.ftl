<!-- FreeMarker Common Functions -->


<!-- ellipsis function -->
<#function applyEllipsis word max>
	<#if word?length gt max-3 >
		<#return word[0..*max] + "...">
	</#if>
  	<#return word>
</#function>
		

<!-- JavaScript common Functions -->
<script>
	$('#alertCloseButton').on('click', function(e) {
	    e.preventDefault();
	    $('#showAlert').hide();
	});
	
	// Info Alert Box
	function showInfoAlert(msg){
	    $('#showAlert').removeClass('alert-danger');
	    $('#showAlert').addClass('alert-success');
	    $('#alertText').html(msg);
	    $('#showAlert').show();
	    $('#showAlert').fadeIn('slow');
	    
	    setTimeout(function(){$('#showAlert').hide();}, 5000);
	}

	// Error Alert Box
	function showErrorAlert(msg){
	    $('#showAlert').removeClass('alert-success');
	    $('#showAlert').addClass('alert-danger');
	    $('#alertText').html(msg);
	    $('#showAlert').show();
	    $('#showAlert').fadeIn('slow');
	    
	    setTimeout(function(){$('#showAlert').hide();}, 5000);
	}
</script>