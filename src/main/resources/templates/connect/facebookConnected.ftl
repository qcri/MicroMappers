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
							<h4 style="color:red">You have successfully connected to facebook. If you want to connect again, please click the button below.</h4>
							<div class="row">
								<div class="col-md-3 col-md-offset-5">
									<form action="${rc.getContextPath()}/connect/facebook" method="POST">
										<input type="hidden" name="_csrf" value="${_csrf.token}" />
										<button type="submit" class="btn btn-social btn-facebook" style="margin-left:-30%">
											<i class="fa fa-facebook"></i> Connect with Facebook
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