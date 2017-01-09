<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<div class="row">
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
								<a href="/detail/${info.id}" title="${info.title}">
								${info.title}
								</a>
							</td>
							<td title="${info.description}">
								${info.description}
							</td>
							<td>${info.articleTag}</td>
							<td>
								<span class="label label-info">
								<i class="icon-info-sign"></i><a href="${info.eventUrl}" title="view details" TARGET="_blank"><strong>&nbsp;Info</strong></a>
								</span>&nbsp;&nbsp;
								<span class="label label-info">
								<i class="icon-cogs"></i><a href="${rc.getContextPath()}/collection/view/create?snopes=${info.id}"><strong>&nbsp;Create</strong></a></span>
                                </span>

						</tr>
						</#list>
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
												<a href="${rc.getContextPath()}/global/events/snopes?page=${index?counter}">${index?counter}</a></li>
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
		</script>
	</body>
</html>