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
	                	<#if isDuplicateError?? >
	                		<h4 style="color:red">This facebook user is already connected to other user. Please logout this user and connect with a different account.</h4>
	            		</#if>
	                	<form action="${rc.getContextPath()}/connect/facebook" method="POST">
	                		<input type="hidden" name="_csrf" value="${_csrf.token}" />
	                    	<button type="submit" class="btn btn-default btn-md social-btn" >
	                    		<img src="${rc.getContextPath()}/img/facebook.png" class="social-logo" /> Connect with Facebook
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
