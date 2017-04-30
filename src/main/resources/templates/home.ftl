<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<div class="row">
				<div id="dashboard" class="col-md-6">
					<div class="well">
						<h4><i class="icon-gears"></i> My Social Media Tokens</h4>
						<p>Manage Your Social Media tokens</p>
						<a href="${rc.getContextPath()}/connect" class="btn btn-primary">
						Manage <i class="icon-chevron-right"></i>
						</a>
					</div>
				</div>
				<div id="dashboard" class="col-md-6">
					<div class="well">
						<h4><i class="icon-list"></i> My Repository</h4>
						<p>Monitor you own customerized data collection</p>
						<a href="${rc.getContextPath()}/collection/view/list" class="btn btn-primary">
						Manage <i class="icon-chevron-right"></i>
						</a>
					</div>
				</div>
			</div>
			<div class="row">
				<div id="featured-apps" class="col-md-6">
					<div class="well">
						<h4><i class="icon-star"></i> Global Events</h4>
						<p>Monitor world news and rumors</p>
						<a href="${rc.getContextPath()}/global/events/snopes" class="btn btn-primary">
                            Manage <i class="icon-chevron-right"></i>
						</a>
					</div>
				</div>
				<div id="categories" class="col-md-6">
					<div class="well">
						<h4><i class="icon-check"></i> Disaster Global Events</h4>
						<p>Monitor World Diaster Information. GDELT Data Repo</p>
						<a href="${rc.getContextPath()}/global/events/gdelt/glides" class="btn btn-primary">
                            News <i class="icon-chevron-right"></i>
						</a>
                        <a href="${rc.getContextPath()}/global/events/gdelt/classifiers" class="btn btn-primary">
                            Image Classifiers <i class="icon-chevron-right"></i>
                        </a>
					</div>
				</div>
			</div>
			<div class="row">
				<div id="users" class="col-md-6">
					<div class="well">
						<h4><i class="icon-dashboard"></i> MicroMappers Data Tracker</h4>
                        <p>Monitor Data Trends</p>
						<a href="${rc.getContextPath()}/dashboard/global" class="btn btn-primary">
                            Dashboard <i class="icon-chevron-right"></i>
						</a>
					</div>
				</div>
				<div id="users-list" class="col-md-6">
					<div class="well">
						<h4><i class="icon-user"></i> My Community</h4>
						<p>Coming Soon</p>
						<a href="" class="btn btn-primary disabled">
						Manage <i class="icon-chevron-right"></i>
						</a>
					</div>
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