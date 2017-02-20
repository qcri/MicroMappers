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
							<th>Title</th>
							<th>Updated</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody>
						<#list page.list as info>
						<tr>
							<td>
                                <a id="${info.glideCode}" href="http://reliefweb.int/disaster/${info.glideCode}" target="_blank" class="hoverMe">${info.glideCode}</a>
							</td>
							<td title="${info.updated}">
								${info.updated}
							</td>
							<td>
								<span class="btn btn-primary btn-xs" title="View details">
									<a href="http://reliefweb.int/disaster/${info.glideCode}" target="_blank" style="color:white;">
									<span class="glyphicon glyphicon-info-sign"></span>&nbsp;Info
									</a>
								</span>
								<#if info.computerVisionRequestState??>
									<#if info.computerVisionRequestState == "approved">
                                    	<span class="image-classifier btn btn-primary btn-xs disabled" data-placement="top" data-toggle="tooltip" title="Image Classifier is running" data-title="cv" data-id="${info.id}">
                                        <span class="glyphicon glyphicon-plus-sign" style="color:white;"></span>&nbsp;Image Classifier Running
										</span>
									<#else>
                                    	<span class="image-classifier btn btn-primary btn-xs disabled" data-placement="top" data-toggle="tooltip" title="Image Classifer on request already" data-title="cv" data-id="${info.id}">
                                        <span class="glyphicon glyphicon-plus-sign" style="color:white;"></span>&nbsp;Image Classifier Pending
										</span>
									</#if>
								<#else>
                                    <span class="image-classifier btn btn-primary btn-xs" data-placement="top" data-toggle="tooltip" title="Add Image Classifer" data-title="cv" data-id="${info.id}" data-page="${page.pageNumber}">
									<span class="glyphicon glyphicon-plus-sign" style="color:white;"></span>&nbsp;Image Classifier Request
									</span>
								</#if>


								<span class="btn btn-primary btn-xs" title="Create collection">
									<a href="${rc.getContextPath()}/collection/view/create?type=gdelt&typeId=${info.id}" target="_blank" style="color:white;">
									<span class="glyphicon glyphicon-cog"></span>&nbsp;Create
									</a>
								</span>
								<span class="btn btn-primary btn-xs">
									<a href="${rc.getContextPath()}/global/events/gdelt/data3w?glideCode=${info.glideCode}" style="color:white;">
									<span class="glyphicon glyphicon-book"></span>&nbsp;3w
									</a>
								</span>
								<span class="btn btn-primary btn-xs">
									<a href="${rc.getContextPath()}/global/events/gdelt/datammic?glideCode=${info.glideCode}" style="color:white;">
									<span class="glyphicon glyphicon-book"></span>&nbsp;MMIC
									</a>
								</span>
							</td>
						</tr>
						</#list>
						<div  id="urlModalDiv" style="display: none;overflow:hidden;width:auto;padding:0px">
						    <iframe id="urlModalIFrame"  scrolling="no1" style="width:100%;height: 100%; border: 0px none; margin-bottom: 0px; margin-left: 0px;">
						    </iframe>
						</div>
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
											<li><a href="${rc.getContextPath()}/global/events/gdelt/glides?page=${page.pageNumber-1}"><span class="glyphicon glyphicon-chevron-left"></span></a></li>
										</#if>
										
										<#list page.navigatePageNumbers as index>
											<#if page.getPageNumber() == index>
												<li class="active">
											<#else>
												<li>
											</#if>
												<a href="${rc.getContextPath()}/global/events/gdelt/glides?page=${index}">${index}</a></li>
										</#list>
										<!-- Last Page -->
										<#if page.isLastPage()>
											<li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-right" ></span></li>
										<#else>
											<li ><a href="${rc.getContextPath()}/global/events/gdelt/glides?page=${page.pageNumber+1}"><span class="glyphicon glyphicon-chevron-right"></span></a></li>
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