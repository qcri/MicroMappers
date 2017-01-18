<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<div class="row">
                <div class="btn-group">
                    <a class="btn" href="${rc.getContextPath()}/home"><i class="icon-tags"></i>&nbsp;Home</a>
                </div>
				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>Title</th>
							<th>No. of Dataset</th>
							<th>Started</th>
                            <th>Source</th>
						</tr>
					</thead>
					<tbody>
						<#list page.list as info>
						<tr>
							<td>
								<#if info.glideMaster?? >
                                    <a href="http://reliefweb.int/disaster/${info.glideMaster.glideCode}" target="_blank">${info.glideMaster.glideCode}</a>
								<#else>
                                    <a href="eventUrl" target="_blank">${info.globalEventDefinition.title}</a>
								</#if>
							</td>
                            <td>
								<#if info.glideMaster?? >
                                    ${info.gdeltCollectionTotal + info.socialCollectionTotal}
								<#else>
									${info.socialCollectionTotal}
								</#if>
                            </td>
							<td title="${info.createdAt}">
								${info.createdAt}
							</td>
                            <td>
								${info.source}
                            </td>
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
											<li><a href="${rc.getContextPath()}/dashboard/global?page=${page.pageNumber-1}"><span class="glyphicon glyphicon-chevron-left"></span></a></li>
										</#if>
										
										<#list page.navigatePageNumbers as index>
											<#if page.getPageNumber() == index>
												<li class="active">
											<#else>
												<li>
											</#if>
												<a href="${rc.getContextPath()}/dashboard/global?page=${index}">${index}</a></li>
										</#list>
										<!-- Last Page -->
										<#if page.isLastPage()>
											<li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-right" ></span></li>
										<#else>
											<li ><a href="${rc.getContextPath()}/dashboard/global?page=${page.pageNumber+1}"><span class="glyphicon glyphicon-chevron-right"></span></a></li>
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
			<#include  "cookies.js">
		</script>
	</body>
</html>