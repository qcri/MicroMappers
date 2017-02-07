<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<div class="row">
                <div class="btn-group">
                    <a class="btn" href="${rc.getContextPath()}/home"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Home</a>
                    <a class="btn" href="${rc.getContextPath()}/connect"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Social Media Setting</a>
                </div>
				<div class="col-lg-12 text-center">
					<h1>MicroMappers</h1>
					<p class="lead">Social Message Purification</p>
					<ul class="list-unstyled">
						<li>
							Welcome
							<#if isDuplicateError?? >
							<h4 style="color:red">This twitter user is already connected to other user. Please logout this user and connect with a different account.</h4>
							</#if>
							<div class="row">
								<div class="col-md-3 col-md-offset-5">
									<form action="${rc.getContextPath()}/connect/twitter" method="POST">
										<input type="hidden" name="_csrf" value="${_csrf.token}" />
										<button type="submit" class="btn btn-social btn-twitter" style="margin-left:-40%">
											<i class="fa fa-twitter"></i> Connect with Twitter
										</button>
									</form>
								</div>
							</div>
						</li>
					</ul>
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