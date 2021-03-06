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
							<form action="${rc.getContextPath()}/connect/twitter" method="POST">
								<input type="hidden" name="_csrf" value="${_csrf.token}" />
								<button type="submit" class="btn btn-social btn-twitter" style="margin-left:-40%">
									<i class="fa fa-twitter"></i> Connect with Twitter
								</button>
							</form>
						</li>
						</br>
						<li>
							<form action="${rc.getContextPath()}/connect/facebook" method="POST">
								<input type="hidden" name="_csrf" value="${_csrf.token}" />
								<button type="submit" class="btn btn-social btn-facebook" style="margin-left:-30%">
									<i class="fa fa-facebook"></i> Connect with Facebook
								</button>
							</form>
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