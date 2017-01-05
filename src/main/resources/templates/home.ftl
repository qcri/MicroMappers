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
						<p>Internal Statistics</p>
						<a href="${rc.getContextPath()}/connect" class="btn btn-primary">
						Manage <i class="icon-chevron-right"></i>
						</a>
						<a href="${rc.getContextPath()}/collection/create" class="btn btn-primary">
						Create Collection <i class="icon-chevron-right"></i>
						</a>
					</div>
				</div>
				<div id="dashboard" class="col-md-6">
					<div class="well">
						<h4><i class="icon-dashboard"></i> My Jobs</h4>
						<p>Monitor the background jobs</p>
						<a href="" class="btn btn-primary">
						Manage <i class="icon-chevron-right"></i>
						</a>
					</div>
				</div>
			</div>
			<div class="row">
				<div id="featured-apps" class="col-md-6">
					<div class="well">
						<h4><i class="icon-star"></i> Snopes Global Events</h4>
						<p>Show projects on the front page</p>
						<a href="${rc.getContextPath()}/global/events/snopes" class="btn btn-primary">
						View <i class="icon-chevron-right"></i>
						</a>
					</div>
				</div>
				<div id="categories" class="col-md-6">
					<div class="well">
						<h4><i class="icon-check"></i> News Global Events</h4>
						<p>Manage project categories</p>
						<a href="" class="btn btn-primary">
						View <i class="icon-chevron-right"></i>
						</a>
					</div>
				</div>
			</div>
			<div class="row">
				<div id="users" class="col-md-6">
					<div class="well">
						<h4><i class="icon-user"></i> Settings</h4>
						<p>Manage administrators</p>
						<a href="" class="btn btn-primary">
						Manage <i class="icon-chevron-right"></i>
						</a>
					</div>
				</div>
				<div id="users-list" class="col-md-6">
					<div class="well">
						<h4><i class="icon-list"></i> My Collaborators</h4>
						<p>Export a list of users}</p>
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