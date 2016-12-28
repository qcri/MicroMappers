<!DOCTYPE html>
<html lang="en">
<#include "_header.html">
<body>
	<#include "_navbar.ftl">

		<div class="container" style="min-height:400px;">
		    <div class="row">
		        <div class="col-lg-12 text-center">
		            <h1>MicroMappers</h1>
		            <p class="lead">Social Message Purification</p>
		            <ul class="list-unstyled">
		                <li>
		                <h4 style="color:red">You have successfully connected to twitter. If you want to connect again, please click the button below.</h4>
		                	<form action="${rc.getContextPath()}/connect/twitter" method="POST">
	                		<input type="hidden" name="_csrf" value="${_csrf.token}" />
	                    	<button type="submit" class="btn btn-default btn-md social-btn" >
	                    		<img src="${rc.getContextPath()}/img/twitter.png" class="social-logo" /> Connect with Twitter
	                    	</button>
	                	</form> 
		                </li>
		                
		            </ul>
		        </div>
		    </div>
	  </div> <!-- /container -->

  	<#include "_footer.html">
	
	<script>
    	<#include "cookies.js">
  	</script>
</body>
</html>