<form id="signOut" action="${rc.getContextPath()}/signout" method="post" class="secondary-nav pull-right">
	<input type="hidden" name="_csrf" value="${_csrf.token}" />
</form>
<nav class="navbar navbar-default">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#myNavbar" aria-expanded="false">
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>                        
			</button>
			<a class="navbar-brand" href="${rc.getContextPath()}/home"><img height="35px" style = "margin-top:-10px" src="${rc.getContextPath()}/img/mm_logo.png"></a>
		</div>
		<div class="collapse navbar-collapse" id="myNavbar">
			<ul class="nav navbar-nav">
				<li><a href="${rc.getContextPath()}/home">Home</a></li>
				<li><a href="http://aidr-web.qcri.org/MMAPI" target="_blank">Maps</a></li>
				<li><a href="http://micromappers.org/" target="_blank">About</a></li>
			</ul>
			<#if current_user?? >
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false"><span class="icon icon-user"></span>&nbsp;&nbsp;${current_user}<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href=""><span class="icon icon-user"></span>&nbsp;&nbsp;My Profile</a></li>
						<li><a href=""><span class="icon icon-th-large"></span>&nbsp;&nbsp;My Collections</a></li>
						<li><a href=""><span class="icon icon-cog"></span>&nbsp;&nbsp;My Settings</a></li>
						<li class="divider"></li>
						<li><a href="#" onclick="document.getElementById('signOut').submit();"><i class="icon icon-off"></i>&nbsp;&nbsp;Sign out</a></li>
					</ul>
				</li>
			</ul>
			<#else>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="${rc.getContextPath()}/signin"><span class="icon icon-white icon-signin"></span>&nbsp;&nbsp;Sign in</a></li>
			</ul>
			</#if>
		</div>
	</div>
</nav>

<!-- Alert Box -->
<div id="showAlert" class="alert alert-dismissible col-md-4 col-md-offset-4" role="alert" style="display:none; position:fixed; top:0px;">
	<span id="alertText" style="word-wrap: break-word;white-space:normal;"></span>
	<button type="button" class="close" id="alertCloseButton" aria-label="Close">
	  <span aria-hidden="true">&times;</span>
	</button>
</div>
<script>
	$('#alertCloseButton').on('click', function(e) {
	    e.preventDefault();
	    $('#showAlert').hide();
	});
	
	function showInfoAlert(msg){
	    $('#showAlert').removeClass('alert-danger');
	    $('#showAlert').addClass('alert-success');
	    $('#alertText').html(msg);
	    $('#showAlert').show();
	    $('#showAlert').fadeIn('slow');
	    
	    setTimeout(function(){$('#showAlert').hide();}, 5000);
	}

	function showErrorAlert(msg){
	    $('#showAlert').removeClass('alert-success');
	    $('#showAlert').addClass('alert-danger');
	    $('#alertText').html(msg);
	    $('#showAlert').show();
	    $('#showAlert').fadeIn('slow');
	    
	    setTimeout(function(){$('#showAlert').hide();}, 5000);
	}
</script>