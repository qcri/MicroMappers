<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	
	<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
	<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
	
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<form id="collectionUpdate" class="form-horizontal">
   
 				<#if collectionInfo.langFilters??>
 					<input type="hidden" id="collectionId" value="${collectionInfo.id}">
					<input type="hidden" id="oldLangFilters" value="${collectionInfo.langFilters}">
					<input type="hidden" id="oldProvider" value="${collectionInfo.provider}">
					<input type="hidden" id="oldFetchInterval" value="${collectionInfo.fetchInterval}">
					<input type="hidden" id="oldfetchFrom" value="${collectionInfo.fetchFrom}">
				</#if>
				<div class="btn-group">
					<a class="btn" href="${rc.getContextPath()}/home"><i class="icon-tags"></i>&nbsp;Home</a>
				<#if collectionInfo.globalEventDefinition??>
					<a class="btn" href="${rc.getContextPath()}/global/events/snopes"><i class="icon-tags"></i>&nbsp;Snopes Global Events</a>
				<#else>
					<a class="btn" href="${rc.getContextPath()}/global/events/glides"><i class="icon-tags"></i>&nbsp;Global Disaster Events</a>
				</#if>
				</div>
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#general">General</a></li>
<!-- 					<li><a data-toggle="tab" href="#advanced">Advanced</a></li> -->
				</ul>
				<div class="tab-content" style="margin-top:15px">
				
					<!--General Tab -->
					<div id="general" class="tab-pane fade in active">
						<div class="form-group">
							<label class="control-label col-sm-3" for="eventTitle">Event :</label>
							<div class="col-sm-6">
								<#if collectionInfo.globalEventDefinition??>
									<label class="form-control-static" style="font-weight: normal;" title="${collectionInfo.globalEventDefinition.title}"><a href="${collectionInfo.globalEventDefinition.eventUrl}" target="_blank">Snopes: ${collectionInfo.globalEventDefinition.title}</a></label>
								<#elseif collectionInfo.glideMaster??>
									<label class="form-control-static" style="font-weight: normal;" title="${collectionInfo.glideMaster.glideCode}"><a href="http://reliefweb.int/disaster/${collectionInfo.glideMaster.glideCode}" target="_blank">Gdelt: ${collectionInfo.glideMaster.glideCode}</a></label>
								</#if>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="name">Collection Name :</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="name" placeholder="Collection Name" disabled required value="${collectionInfo.name}">
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="provider">Collection Source :</label>
							<div class="col-sm-6">
								<select class="form-control" name="provider" id="provider">
										<option value="TWITTER">Twitter</option>
										<option value="FACEBOOK">Facebook</option>
										<option value="ALL">All</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="lang">Language :</label>
							<div class="col-sm-6">
								<select  multiple class="form-control" name="lang" id="lang">
								
<!-- 								Here language list is populated through js script -->

								</select>
							</div>
						</div>
						<div class="form-group" id="keywordsDiv" hidden=true>
							<label class="control-label col-sm-3" for="track">Keywords :</label>
							<div class="col-sm-6"> 
								<textarea class="form-control" rows="6" name="track" placeholder="Enter comma seperated keywords">${collectionInfo.track}</textarea>
							</div>
						</div>
						
						<div id="facebookConfigDiv" hidden=true>
							<div class="form-group">
								<label class="control-label col-sm-3" for="subscribedProfiles">Subscribed Profiles :</label>
								<div class="col-sm-6"> 
									<!-- multiple dropdown -->
							  		<select class="subscribedProfileSelect js-states form-control" id="id_label_multiple" multiple="multiple">
								
									<!-- Options added by remote search text -->
							  		
							  		</select>
								</div>
							</div>
							<!-- <div class="form-group">
								<label class="control-label col-sm-3" for="subscribedProfiles">Subscribed Profiles :</label>
								<div class="col-sm-6"> 
									<textarea class="form-control" rows="6" name="subscribedProfiles" placeholder="Search profiles and select">${collectionInfo.subscribedProfiles}</textarea>
								</div>
							</div> -->
							<div class="form-group">
								<label class="control-label col-sm-3" for="fetchInterval">Facebook Fetch Interval :</label>
								<div class="col-sm-6">
									<select class="form-control" id="fetchInterval" name="fetchInterval">
										<option value="2" selected>2 hours</option>
										<option value="4">4 hours</option>
										<option value="6">6 hours</option>
										<option value="12">12 hours</option>
										<option value="24">1 day</option>
										<option value="72">3 days</option>
										<option value="168">7 days</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="fetchFrom">Facebook Fetch From Last :</label>
								<div class="col-sm-6">
									<select class="form-control" id="fetchFrom" name="fetchFrom">
										<option value="168" selected>7 days</option>
										<option value="360">15 days</option>
										<option value="720">1 month</option>
										<option value="2160">3 months</option>
										<option value="4320">6 months</option>
									</select>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				
				<!-- Cancel & Submit-->
				<div class="form-group">
					<div class="col-sm-offset-4">
						<button type=button class="btn btn-danger" id="cancelButton">Cancel</button>
						<button type="submit" class="btn btn-primary" id="updateButton">Update</button>
					</div>
				</div>
			</form>
		</div> <!-- /container -->
		
		<#include "_footer.html">
		
		<script>
			<#include "cookies.js">
			<#include "lang.js">
		 	<#include "collection/update/update.js">
		</script>
	</body>
</html>