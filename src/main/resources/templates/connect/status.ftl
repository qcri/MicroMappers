<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<div class="row">
                <div class="btn-group">
                    <a class="btn" href="${rc.getContextPath()}/home"><i class="icon-tags"></i>&nbsp;Home</a>
                </div>
                <div class="row-fluid well well-small">
					<#if connectionMap["twitter"]?size gt 0 >
						<h4>Hi <span style="color:blue">${(connectionMap["twitter"])[0].getDisplayName()}</span>, you have already connected to <i class="icon-twitter"></i>&nbsp;twitter. If you want to connect again, please <i class="icon-twitter-sign"></i>&nbsp;<a href="${rc.getContextPath()}/connect/twitter">click here</a></h4>
					<#else>
						<h4>Please click the button below to connect <i class="icon-twitter"></i>twitter.</h4>
						<form action="${rc.getContextPath()}/connect/twitter" method="POST">
							<input type="hidden" name="_csrf" value="${_csrf.token}" />
							<button type="submit" class="btn btn-default btn-md social-btn" >
								<img src="${rc.getContextPath()}/img/twitter.png" class="social-logo" /> Connect with Twitter
							</button>
						</form>
					</#if>
                </div>
                <div class="row-fluid well well-small">
					<#if connectionMap["facebook"]?size gt 0 >
						<h4>Hi <span style="color:blue">${(connectionMap["facebook"])[0].getDisplayName()}</span>, you have already connected to <i class="icon-facebook"></i>&nbsp;facebook. If you want to connect again, please <i class="icon-facebook-sign"></i>&nbsp;<a href="${rc.getContextPath()}/connect/facebook">click here</a></h4>
					<#else>
						<h4>Please click the button below to connect <i class="icon-facebook"></i>facebook.</h4>
						<form action="${rc.getContextPath()}/connect/facebook" method="POST">
							<input type="hidden" name="_csrf" value="${_csrf.token}" />
							<button type="submit" class="btn btn-default btn-md social-btn" >
								<img src="${rc.getContextPath()}/img/facebook.png" class="social-logo" /> Connect with Facebook
							</button>
						</form>
					</#if>
                </div>
			</div>
		</div>
		<!-- /container -->
		<#include "_footer.html">
		<script>
			<#include "cookies.js">
		</script>
	</body>
</html>