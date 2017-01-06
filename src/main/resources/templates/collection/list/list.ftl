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
							<th>Name</th>
							<th>Status</th>
							<th>Delete/Restore</th>
						</tr>
					</thead>
					<tbody>
						<#list page.list as info>
						<tr>
							<td>
								<a href="/detail/${info.id}" title="${info.name}">
								${info.name}
								</a>
							</td>
							<td>${info.status}</td>
							<#if info.status == "TRASHED">
								<td><p data-placement="top" data-toggle="tooltip" title="Restore">
									<button class="confirm-restore btn btn-primary btn-xs" data-title="Delete" data-toggle="modal"  role="button" data-id="${info.id}">
										<span class="glyphicon glyphicon-repeat"></span>
									</button>
									</p>
								</td>
							<#else>
								<td><p data-placement="top" data-toggle="tooltip" title="Delete">
									<button class="confirm-delete btn btn-danger btn-xs" data-title="Delete" data-toggle="modal"  role="button" data-id="${info.id}">
										<span class="glyphicon glyphicon-trash"></span>
									</button>
									</p>
								</td>
							</#if>
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
											<li><a href="${rc.getContextPath()}/collection/view/list?page=${page.pageNumber-1}"><span class="glyphicon glyphicon-chevron-left"></span></a></li>
										</#if>
										
										<#list page.navigatePageNumbers as index>
											<#if page.getPageNumber() == index>
												<li class="active">
											<#else>
												<li>
											</#if>
												<a href="${rc.getContextPath()}/collection/view/list?page=${index?counter}">${index?counter}</a></li>
										</#list>
										<!-- Last Page -->
										<#if page.isLastPage()>
											<li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-right" ></span></li>
										<#else>
											<li ><a href="${rc.getContextPath()}/collection/view/list?page=${page.pageNumber+1}"><span class="glyphicon glyphicon-chevron-right"></span></a></li>
										</#if>
									</ul>
								</div>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
			
			
			<div class="modal fade" id="edit" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>
							<h4 class="modal-title custom_align" id="Heading">Edit Your Detail</h4>
						</div>
						<div class="modal-body">
							<div class="form-group">
								<input class="form-control " type="text" placeholder="Mohsin">
							</div>
							<div class="form-group">
								<input class="form-control " type="text" placeholder="Irshad">
							</div>
							<div class="form-group">
								<textarea rows="2" class="form-control" placeholder="CB 106/107 Street # 11 Wah Cantt Islamabad Pakistan"></textarea>
							</div>
						</div>
						<div class="modal-footer ">
							<button type="button" class="btn btn-warning btn-lg" style="width: 100%;"><span class="glyphicon glyphicon-ok-sign"></span> Update</button>
						</div>
					</div>
					<!-- /.modal-content --> 
				</div>
				<!-- /.modal-dialog --> 
			</div>
			
			
			<div class="modal fade" id="delete" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>
							<h4 class="modal-title custom_align" id="Heading">Delete this collection</h4>
						</div>
						<div class="modal-body">
							<div class="alert alert-danger"><span class="glyphicon glyphicon-warning-sign"></span> Are you sure you want to delete this Collection?</div>
						</div>
						<div class="modal-footer ">
							<button type="button" class="btn btn-success" data-dismiss="modal" id="deleteYes"><span class="glyphicon glyphicon-ok-sign"></span> Yes</button>
							<button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span> No</button>
						</div>
					</div>
					<!-- /.modal-content --> 
				</div>
				<!-- /.modal-dialog --> 
			</div>
			
			<div class="modal fade" id="restore" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>
							<h4 class="modal-title custom_align" id="Heading">Restore this collection</h4>
						</div>
						<div class="modal-body">
							<div class="alert alert-danger"><span class="glyphicon glyphicon-warning-sign"></span> Are you sure you want to restore this Collection?</div>
						</div>
						<div class="modal-footer ">
							<button type="button" class="btn btn-success" data-dismiss="modal" id="restoreYes"><span class="glyphicon glyphicon-ok-sign"></span> Yes</button>
							<button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span> No</button>
						</div>
					</div>
					<!-- /.modal-content --> 
				</div>
				<!-- /.modal-dialog --> 
			</div>
			
		</div>
		<!-- /container -->
		<#include "_footer.html">
		<script>
			<#include "cookies.js">
			<#include "collection/list/list.js">
		</script>
	</body>
</html>