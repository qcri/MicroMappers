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
				<table id="collectionGrid" class="table table-striped table-bordered">
					<thead>
						<tr>
							<th style="width: 50%;">Name</th>
							<th style="width: 20%;">Status</th>
                            <th style="width: 10%;">Count</th>
							<th style="width: 20%;">Action</th>
						</tr>
					</thead>
					<tbody>
						<#list page as info>
						<tr>
							<td style="width: 50%;">
								<#if info.globalEventDefinition?? >
									<a href="${rc.getContextPath()}/collection/view/details?id=${info.id}" title="${info.globalEventDefinition.title}">
								<#elseif info.glideMaster?? >
									<a href="${rc.getContextPath()}/collection/view/details?id=${info.id}" title="${info.glideMaster.glideCode}">
								</#if>
								${info.name}</a>
								<#if current_user != info.owner>
									- ${info.owner}
								</#if>
							</td>

							<td class="col-md-2" style="width: 20%;">
								<div>
									<#if info.provider == "ALL" || info.provider == "TWITTER">
										<i class="fa fa-twitter twitter-color"></i>&nbsp;${info.twitterStatus}
									</#if>
									<!-- <#if info.provider == "ALL">
										,&nbsp;
									</#if> -->
								</div>
								<div>
									<#if info.provider == "ALL" || info.provider == "FACEBOOK">
										<i class="fa fa-facebook facebook-color"></i>&nbsp;${info.facebookStatus}
									</#if>
								</div>
							</td>
                            <td style="width: 10%;">${info.count}</td>
							<!--<td width="20%">
								<#if info.globalEventDefinition?? >
									<a href="${info.globalEventDefinition.eventUrl}" target="_blank" title="${info.globalEventDefinition.title}" class="hoverMe">
										${info.globalEventDefinition.title}
									</a>
								<#elseif info.glideMaster?? >
									<a href="http://reliefweb.int/disaster/${info.glideMaster.glideCode}" title="${info.glideMaster.glideCode}" class="hoverMe" target="_blank" >${info.glideMaster.glideCode}</a>
								</#if>

							</td>-->
							<td class="col-md-1" style="width: 20%;">
                                <span data-placement="top" data-toggle="tooltip" title="Sentiment">
									<a href="${rc.getContextPath()}/dashboard/keywordSentiment?cid=${info.id}"><i class="btn btn-primary btn-xs" data-title="ton" data-id="${info.id}">
                                        <span class="glyphicon glyphicon-heart"></span>
                                    </i></a>
								</span>
								<span data-placement="top" data-toggle="tooltip" title="Purifier">
									<a href="https://public.tableau.com/profile/maupetit#!/vizhome/DEMO_V2/Dashboard2" target="_blank"><i class="btn btn-primary btn-xs" data-title="Rumour" data-id="${info.id}">
                                        <span class="glyphicon glyphicon-stats"></span>
                                    </i></a>
								</span>
								<span data-placement="top" data-toggle="tooltip" title="Crowdsourcing">
									<a href="http://clickers.micromappers.org/project/${info.code}/newtask" target="_blank"><i class="btn btn-primary btn-xs" data-title="Crowdsourcing" data-id="${info.id}">
                                        <span class="glyphicon glyphicon-user"></span>
                                    </i></a>
								</span>
								<span data-placement="top" data-toggle="tooltip" title="Download">
									<i class="confirm-download btn btn-primary btn-xs" data-title="Edit" data-id="${info.id}">
                                        <span class="glyphicon glyphicon-download"></span>
                                    </i>
								</span>
								<span data-placement="top" data-toggle="tooltip" title="Edit">
									<i class="confirm-edit btn btn-primary btn-xs" data-title="Edit" data-id="${info.id}">
                                        <span class="glyphicon glyphicon-edit"></span>
                                    </i>
								</span>
								<#if info.isTrashed()>
                                    <span data-placement="top" data-toggle="tooltip" title="Restore">
										<button class="confirm-restore btn btn-primary btn-xs" data-title="Delete" data-toggle="modal"  role="button" data-id="${info.id}">
                                            <span class="glyphicon glyphicon-repeat"></span>
                                        </button>
									</span>
								<#else>
                                    <span data-placement="top" data-toggle="tooltip" title="Delete">
										<button class="confirm-delete btn btn-danger btn-xs" data-title="Delete" data-toggle="modal"  role="button" data-id="${info.id}">
                                            <span class="glyphicon glyphicon-trash"></span>
                                        </button>
									</span>

								</#if>
							</td>
						</tr>
						</#list>
						<!--<div  id="urlModalDiv" style="display: none;overflow:hidden;width:auto;padding:0px;">
						    <iframe id="urlModalIFrame"  scrolling="no1" style="width:100%;height: 100%; border: 0px none; margin-bottom: 0px; margin-left: 0px;">
						    </iframe>
						</div>-->
					</tbody>
				</table>
			</div>
			<!-- <div class="modal fade" id="edit" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
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
					/.modal-content
				</div>
				/.modal-dialog
				</div> -->
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
			<#include "urlpopup.js">
		</script>
	</body>
</html>