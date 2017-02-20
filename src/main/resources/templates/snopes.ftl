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
				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							<th style="width: 20%">Title</th>
							<th style="width: 40%">Summary</th>
							<th style="width: 25%">Tag</th>
							<th style="width: 15%">Action</th>
						</tr>
					</thead>
					<tbody>
						<#list page.list as info>
						<tr>
							<td>
								<a href="${info.eventUrl}" title="${info.title}" target="_blank" class="hoverMe">
								${info.title}
								</a>
							</td>
							<td title="${info.description}">
								${info.description}
							</td>
							<td>${info.articleTag}</td>
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
					<tfoot>
						<tr>
							<td colspan="4" class="text-center">
								<div style="margin:0px;">
									<ul class="pagination pull-right">
										<!-- First Page -->
										<#if page.isFirstPage()>
											<li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-left" ></span></li>
										<#else>
											<li><a href="${rc.getContextPath()}/global/events/snopes?page=${page.pageNumber-1}"><span class="glyphicon glyphicon-chevron-left"></span></a></li>
										</#if>
										
										<#list page.navigatePageNumbers as index>
											<#if page.getPageNumber() == index>
												<li class="active">
											<#else>
												<li>
											</#if>
												<a href="${rc.getContextPath()}/global/events/snopes?page=${index}">${index}</a></li>
										</#list>
										<!-- Last Page -->
										<#if page.isLastPage()>
											<li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-right" ></span></li>
										<#else>
											<li ><a href="${rc.getContextPath()}/global/events/snopes?page=${page.pageNumber+1}"><span class="glyphicon glyphicon-chevron-right"></span></a></li>
										</#if>
									</ul>
								</div>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
		<!-- /container -->
		<#include "_footer.html">
		<script>
			<#include "cookies.js">
			<#include "urlpopup.js">
		</script>
	</body>
</html>