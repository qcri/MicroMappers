<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<div class="row">
				<div id="dashboard" class="col-md-6">
					<div class="well">
						<h4><i class="icon-dashboard"></i> My Social Media Tokens</h4>
						<p>Manage your token to data collection</p>
						<a href="${rc.getContextPath()}/connect" class="btn btn-primary">
						Manage <i class="icon-chevron-right"></i>
						</a>
					</div>
				</div>
				<div id="dashboard" class="col-md-6">
					<div class="well">
						<h4><i class="icon-dashboard"></i> My Collections</h4>
						<p>Monitor collections</p>
						<a href="${rc.getContextPath()}/collection/view/list" class="btn btn-primary">
						Manage <i class="icon-chevron-right"></i>
						</a>
                        <a href="${rc.getContextPath()}/collection/view/dashboard" class="btn btn-primary">
                            Dashboard <i class="icon-chevron-right"></i>
                        </a>
					</div>
				</div>
			</div>
			<div class="row">
				<div id="featured-apps" class="col-md-6">
					<div class="well">
						<h4><i class="icon-star"></i> Snopes Global Events</h4>
						<p>Monitor Snopes news</p>
						<a href="${rc.getContextPath()}/global/events/snopes" class="btn btn-primary">
						View <i class="icon-chevron-right"></i>
						</a>
                        <a href="${rc.getContextPath()}/global/events/snopes/dashboard" class="btn btn-primary">
                            Dashboard <i class="icon-chevron-right"></i>
                        </a>
					</div>
				</div>
				<div id="categories" class="col-md-6">
					<div class="well">
						<h4><i class="icon-check"></i> Global Disaster Events</h4>
						<p>Monitor World Diaster Information</p>
						<a href="${rc.getContextPath()}/global/events/glides" class="btn btn-primary">
						View <i class="icon-chevron-right"></i>
						</a>
                        <a href="${rc.getContextPath()}/global/events/glides/dashboard" class="btn btn-primary">
                            Dashboard <i class="icon-chevron-right"></i>
                        </a>
					</div>
				</div>
			</div>
			<div class="row">
				<div id="users" class="col-md-6">
					<div class="well">
						<h4><i class="icon-user"></i> Report</h4>
						<p>Manage Report History</p>
						<a href="" class="btn btn-primary">
						Manage <i class="icon-chevron-right"></i>
						</a>
					</div>
				</div>
				<div id="users-list" class="col-md-6">
					<div class="well">
						<h4><i class="icon-list"></i> My Collaborators</h4>
						<p>Manage collaborators</p>
						<a href="" class="btn btn-primary">
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