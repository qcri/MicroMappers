<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<div class="row">
                <div class="btn-group">
                    <a class="btn" href="${rc.getContextPath()}/home"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Home</a>
                </div>
				<table id="newsData" class="table table-striped table-bordered">
					<thead>
						<tr>
							<th WIDTH="35%">Title</th>
							<th WIDTH="35%">Summary</th>
							<th WIDTH="15%">Source</th>
							<th WIDTH="15%">Action</th>
						</tr>
					</thead>
					<tbody>
						<#list page as info>
						<tr>
							<td>
								<a href="${info.eventUrl}" title="${info.title}" target="_blank" class="hoverMe">
								${info.title}
								</a>
							</td>
							<#if info.articleTag??>
                            	<td title="${info.articleTag}">
							<#else>
                            	<td>
							</#if>
								${info.description}
							</td>
							<td>${info.author}</td>
							<td>
								<span class="btn btn-primary btn-xs" title="View details">
									<a href="${info.eventUrl}" target="_blank" style="color:white;">
									<span class="glyphicon glyphicon-info-sign"></span>&nbsp;Info
									</a>
								</span>
								<span class="btn btn-primary btn-xs" title="Create collection">
									<a href="${rc.getContextPath()}/collection/view/create?type=snopes&typeId=${info.id}" style="color:white;">
									<span class="glyphicon glyphicon-cog"></span>&nbsp;Create
									</a>
								</span>
						</tr>
						</#list>
						<div  id="urlModalDiv" style="display: none;overflow:hidden;width:auto;padding:0px">
						    <iframe id="urlModalIFrame"  scrolling="no1" style="width:100%;height: 100%; border: 0px none; margin-bottom: 0px; margin-left: 0px;">
						    </iframe>
						</div>
					</tbody>
				</table>
			</div>
		</div>
		<!-- /container -->
		<#include "_footer.html">
		<script>
			<#include "cookies.js">
			<#include "urlpopup.js">
            $('#newsData').dataTable( {
			    columnDefs: [ { "orderable": false, "targets": 3 } ]
			} );
		</script>
	</body>
</html>