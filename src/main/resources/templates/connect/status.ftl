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
	                	Welcome
	                	<#if connectionMap["twitter"]?size gt 0 >
							<h4>Hi <span style="color:blue">${(connectionMap["twitter"])[0].getDisplayName()}</span>, you have already connected to twitter. If you want to connect again, please <a href="${rc.getContextPath()}/connect/twitter">click here</a></h4>
						<#else>
							<h4>Please click the button below to connect twitter.</h4>
		                	<form action="${rc.getContextPath()}/connect/twitter" method="POST">
		                		<input type="hidden" name="_csrf" value="${_csrf.token}" />
		                    	<button type="submit" class="btn btn-default btn-md social-btn" >
		                    		<img src="${rc.getContextPath()}/img/twitter.png" class="social-logo" /> Connect with Twitter
		                    	</button>
		                	</form> 
						</#if>
	                </li></br>
	                <li>
	                	<#if connectionMap["facebook"]?size gt 0 >
							<h4>Hi <span style="color:blue">${(connectionMap["facebook"])[0].getDisplayName()}</span>, you have already connected to facebook. If you want to connect again, please <a href="${rc.getContextPath()}/connect/facebook">click here</a></h4>
						<#else>
							<h4>Please click the button below to connect facebook.</h4>
			                	<form action="${rc.getContextPath()}/connect/facebook" method="POST">
		                		<input type="hidden" name="_csrf" value="${_csrf.token}" />
		                    	<button type="submit" class="btn btn-default btn-md social-btn" >
		                    		<img src="${rc.getContextPath()}/img/facebook.png" class="social-logo" /> Connect with Facebook
		                    	</button>
	                		</form> 
						</#if>
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