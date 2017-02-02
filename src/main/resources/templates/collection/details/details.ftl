<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<div class="row col-md-12" >
                <div class="btn-group">
                    <a class="btn" href="${rc.getContextPath()}/home"><i class="icon-tags"></i>&nbsp;Home</a>
                </div>
				<div class="table-responsive">
                    <table class="table" style="margin-bottom: 0px;">
                        <tr>
                            <td class="col-md-8" style="word-wrap: break-word; max-width: 160px; white-space:normal; border-top: 0px;">
                                <h3>${collectionInfo.name}
                                    <small style="font-size:60%">
                                    
										<#if current_user != collectionInfo.owner>
	                                        by ${collectionInfo.owner}
										</#if>
										
                                        <a href="${rc.getContextPath()}/collection/view/update?id=${collectionInfo.id}"><span class="glyphicon glyphicon-edit" title="Edit"></span></a>
                                        <br/>
                                        
									<!-- For twitter -->
										<#if collectionInfo.provider == "ALL" || collectionInfo.provider == "TWITTER">
											<#if collectionInfo.twitterStatus =="RUNNING">
		                                    	<span class="text-success">
											<#elseif collectionInfo.twitterStatus =="NOT_RUNNING" >
		                                    	<span class="text-muted">
											<#elseif collectionInfo.twitterStatus =="RUNNING_WARNING" || collectionInfo.twitterStatus =="WARNING">
			                                    <span class="text-warning">
											<#elseif collectionInfo.twitterStatus =="TRASHED" >
		                                    	<span class="text-info">
											<#else>
		                                    	<span class="text-danger">
											</#if>
											
	                                        <i class="fa fa-twitter twitter-color"></i>&nbsp;<b>${collectionInfo.twitterStatus}</b></span>
										</#if>
	                                    
                                   <!-- For facebook -->
										<#if collectionInfo.provider == "ALL">
											,&nbsp;
										</#if>
										<#if collectionInfo.provider == "ALL" || collectionInfo.provider == "FACEBOOK">
											<#if collectionInfo.facebookStatus =="RUNNING">
		                                    	<span class="text-success">
											<#elseif collectionInfo.facebookStatus =="NOT_RUNNING" >
		                                    	<span class="text-muted">
											<#elseif collectionInfo.facebookStatus =="RUNNING_WARNING" || collectionInfo.facebookStatus =="WARNING">
			                                    <span class="text-warning">
											<#elseif collectionInfo.facebookStatus =="TRASHED" >
		                                    	<span class="text-info">
											<#else>
		                                    	<span class="text-danger">
											</#if>
											
	                                        <i class="fa fa-facebook facebook-color"></i>&nbsp;<b>${collectionInfo.facebookStatus}</b></span>
										</#if>
                                    </small>
								</h3>
                            </td>

                            <td class="col-md-3" style="vertical-align: middle;border-top: 0px;">
                            	<#if collectionInfo.isTrashed()>
									<button class="btn btn-danger btn-lg pull-right" id="restoreCollectionButton" data-title="Restore Collection" role="button" data-id="${collectionInfo.id}" style="margin-left: 5%;">Restore</button>
								<#else>
									<!-- For facebook -->
									<#if collectionInfo.provider == "ALL" || collectionInfo.provider == "FACEBOOK">							
										<#if collectionInfo.facebookStatus =="RUNNING" || collectionInfo.facebookStatus =="RUNNING_WARNING" || collectionInfo.facebookStatus =="WARNING">
											<button class="btn btn-danger btn-lg pull-right btn-social" id="stopFacebookButton" data-title="Stop Collection" role="button" data-id="${collectionInfo.id}" style="margin-left: 5%;"><i class="fa fa-facebook"></i>Stop</button>
										<#elseif collectionInfo.facebookStatus !="TRASHED" >
											<button class="btn btn-success btn-lg pull-right btn-social" id="startFacebookButton" data-title="Start Collection" role="button" data-id="${collectionInfo.id}" style="margin-left: 5%;"><i class="fa fa-facebook"></i>Start</button>
										</#if>
									</#if>
									
									<!-- For twitter -->
									<#if collectionInfo.provider == "ALL" || collectionInfo.provider == "TWITTER">
										<#if collectionInfo.twitterStatus =="RUNNING" || collectionInfo.twitterStatus =="RUNNING_WARNING" || collectionInfo.twitterStatus =="WARNING">
											<button class="btn btn-danger btn-lg pull-right btn-social" id="stopTwitterButton" data-title="Stop Collection" role="button" data-id="${collectionInfo.id}"><i class="fa fa-twitter"></i>Stop</button>
										<#elseif collectionInfo.twitterStatus !="TRASHED" >
											<button class="btn btn-success btn-lg pull-right btn-social" id="startTwitterButton" data-title="Start Collection" role="button" data-id="${collectionInfo.id}"><i class="fa fa-twitter"></i>Start</button>
										</#if>
									</#if>
								</#if>	
                            </td>
                        </tr>
                    </table>
					<table class="table table-striped">
						<tbody>
							<tr>
								<td class="col-md-2">Event:</td>
								<#if collectionInfo.globalEventDefinition??>
									<td class="text-right col-md-10" style="word-wrap: break-word;max-width: 160px;white-space:normal;" title="Snopes: ${collectionInfo.globalEventDefinition.title}"><a href="${collectionInfo.globalEventDefinition.eventUrl}" target="_blank">Snopes: ${collectionInfo.globalEventDefinition.title}</a></td>
								<#elseif collectionInfo.glideMaster??>
									<td class="text-right col-md-10" style="word-wrap: break-word;max-width: 160px;white-space:normal;" title="Gdelt: ${collectionInfo.glideMaster.glideCode}"><a href="http://reliefweb.int/disaster/${collectionInfo.glideMaster.glideCode}" target="_blank">Gdelt: ${collectionInfo.glideMaster.glideCode}</a></td>
								</#if>

							</tr>
							
						<!-- For twitter -->
							<#if collectionInfo.provider == "ALL" || collectionInfo.provider == "TWITTER">
								<tr>
									<td>Twitter Collection:</td>
									<td class="text-right" id="twitterCount" data-id="${collectionInfo.id}">${twitterCount}</td>
								</tr>
							</#if>
							
						<!-- For facebook -->
							<#if collectionInfo.provider == "ALL" || collectionInfo.provider == "FACEBOOK">
								<tr>
									<td>Facebook Collection:</td>
									<td class="text-right" id="facebookCount" data-id="${collectionInfo.id}">${facebookCount}</td>
								</tr>
							</#if>
							
							<tr>
								<td>Created On:</td>
								<td class="text-right" title="${collectionCreatedAt}">${collectionCreatedAt}</td>
							</tr>
							<tr>
								<td>Language:</td>
								<#if collectionInfo.langFilters == "">
									<td class="text-right" title="Any language">Any language</td>
								<#else>
									<td class="text-right" style="word-wrap: break-word;max-width: 160px;white-space:normal;" title="${collectionInfo.langFilters}">${collectionInfo.langFilters}</td>
								</#if>
							</tr>
							
							<#if collectionInfo.provider == "ALL" || collectionInfo.provider == "TWITTER">
								<tr>
									<td>Keywords:</td>
									<td class="text-right" style="word-wrap: break-word;max-width: 160px;white-space:normal;" title="${collectionInfo.track}">${collectionInfo.track}</td>
								</tr>
							</#if>
							<#if collectionInfo.provider == "ALL" || collectionInfo.provider == "FACEBOOK">
								<tr>
									<td>Subscribed Profiles:</td>
									<td>
										<div class="row">
											<#list collectionInfo.subscribedProfiles?eval as profile>
												<div class="profile-div" title="${profile.name}">
													<div style="float: left">
														<img src=${profile.imageUrl}>
	    											</div>
	    											<div style="float: left; padding: 8px; line-height: 1.1;">
	    												<b>${applyEllipsis(profile.name,12)}</b><br>
	    												<#if profile.type == "PAGE">
	    													${profile.fans} Likes
	    												<#else>
	    													${profile.type}
	    												</#if>
	    											</div>
	    											<div style="float: right;padding: -5px;margin: -5px;color: #3c7ac6"><a href=${profile.link} target="_blank" title="${profile.link}"><i class="fa fa-external-link link"></i></a></div>
												</div>
											</#list>
										</div>
									</td>
								</tr>
								<tr>
									<td>Fetch Interval:</td>
									<td class="text-right" style="word-wrap: break-word;max-width: 160px;white-space:normal;" title="${collectionInfo.fetchInterval}">${collectionInfo.fetchInterval}</td>
								</tr>
							</#if>
							<tr>
								<td>CreatedBy:</td>
								<td class="text-right" title="${collectionInfo.owner}">${collectionInfo.owner}</td>
							</tr>
							<tr>
								<td>Collaborators:</td>
								<td class="text-right" style="word-wrap: break-word;max-width: 160px;white-space:normal;" title="${collectionCollaborators}">${collectionCollaborators}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>	<!-- /container -->
		<#include "_footer.html">
		<script>
			<#include "cookies.js">
			<#include "collection/details/details.js">
		</script>
	</body>
</html>