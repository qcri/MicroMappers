<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
		
			<form id="collectionUpdate" class="form-horizontal">
   
 				<#if collectionInfo.langFilters??>
 					<input type="hidden" id="collectionId" value="${collectionInfo.id}">
					<input type="hidden" id="oldLangFilters" value="${collectionInfo.langFilters}">
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
									<#if collectionInfo.provider == "TWITTER">
										<option value="TWITTER" selected>Twitter</option>
									<#else>
										<option value="TWITTER">Twitter</option>
									</#if>
									<!-- <#if collectionInfo.provider == "FACEBOOK">
										<option value="FACEBOOK" selected>Facebook</option>
									<#else>
										<option value="FACEBOOK">Facebook</option>
									</#if>
									<#if collectionInfo.provider == "BOTH">
										<option value="BOTH" selected>Both</option>
									<#else>
										<option value="BOTH">Both</option>
									</#if> -->
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
						<div class="form-group">
							<label class="control-label col-sm-3" for="track">Keywords :</label>
							<div class="col-sm-6"> 
								<textarea class="form-control" rows="6" name="track" placeholder="Enter comma seperated keywords">${collectionInfo.track}</textarea>
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