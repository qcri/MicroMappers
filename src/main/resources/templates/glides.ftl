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
							<th>Title</th>
							<th>Updated</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody>
						<#list page.list as info>
						<tr>
							<td>
                                <a href="http://reliefweb.int/disaster/${info.glideCode}" target="_blank">${info.glideCode}</a>
							</td>
							<td title="${info.updated}">
								${info.updated}
							</td>
							<td><span class="label label-info">
								<i class="icon-info-sign"></i><a href="http://reliefweb.int/disaster/${info.glideCode}" target="_blank"><strong>&nbsp;Info</strong></a>
								</span>&nbsp;&nbsp;
								<span class="label label-info">
								<i class="icon-cogs"></i><a href="${rc.getContextPath()}/collection/view/create?type=gdelt&typeid=${info.id}"><strong>&nbsp;Create</strong></a></span>
                                </span>&nbsp;&nbsp;
								<span class="label label-info">
								<i class="icon-book"></i><a href="${rc.getContextPath()}/global/events/gdelt/data3w?glideCode=${info.glideCode}"><strong>&nbsp;3w</strong></a></span>
                                </span>&nbsp;&nbsp;
								<span class="label label-info">
								<i class="icon-book"></i><a href="${rc.getContextPath()}/global/events/gdelt/datammic?glideCode=${info.glideCode}"><strong>&nbsp;MMIC</strong></a></span>
                                </span>
							</td>
						</tr>
						</#list>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="3" class="text-center">
								<div style="margin:0px;">
									<ul class="pagination pull-right">
										<!-- First Page -->
										<#if page.isFirstPage()>
											<li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-left" ></span></li>
										<#else>
											<li><a href="${rc.getContextPath()}/global/events/glides?page=${page.pageNumber-1}"><span class="glyphicon glyphicon-chevron-left"></span></a></li>
										</#if>
										
										<#list page.navigatePageNumbers as index>
											<#if page.getPageNumber() == index>
												<li class="active">
											<#else>
												<li>
											</#if>
												<a href="${rc.getContextPath()}/global/events/glides?page=${index?counter}">${index?counter}</a></li>
										</#list>
										<!-- Last Page -->
										<#if page.isLastPage()>
											<li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-right" ></span></li>
										<#else>
											<li ><a href="${rc.getContextPath()}/global/events/glides?page=${page.pageNumber+1}"><span class="glyphicon glyphicon-chevron-right"></span></a></li>
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