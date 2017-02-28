<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
	<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
	<body>
		<#include "_navbar.ftl">
		<#if eventType?? && eventTypeId?? >
			<input type="hidden" id="eventType" value="${eventType}">
			<input type="hidden" id="eventTypeId" value="${eventTypeId}">
		</#if>
		<div class="container" style="min-height:400px;">
		
			<form id="collectionCreate" class="form-horizontal">

                <div class="btn-group">
                    <a class="btn" href="${rc.getContextPath()}/home"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Home</a>
					<#if eventType?? && eventType='snopes'>
                        <a class="btn" href="${rc.getContextPath()}/global/events/snopes"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Snopes Global Events</a>
					<#else>
                        <a class="btn" href="${rc.getContextPath()}/global/events/gdelt/glides"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Global Disaster Events</a>
					</#if>
                </div>
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#general">General</a></li>
					<li><a data-toggle="tab" href="#textDisambiguityConfig" id="textDisambiguityConfigTab">Text Disambiguity Config</a></li>
				</ul>
				<div class="tab-content" style="margin-top:15px">
				
					<!--General Tab -->
					<div id="general" class="tab-pane fade in active">
						<div class="form-group">
							<label class="control-label col-sm-3" for="eventTitle">Event :</label>
							<div class="col-sm-6">
								<#if eventInfo.title??>
									<label class="form-control-static" style="font-weight: normal;" title="Snopes: ${eventInfo.title}"><a href="${eventInfo.eventUrl}" target="_blank">News: ${eventInfo.title}</a></label>
								<#elseif eventInfo.glideCode??>
									<label class="form-control-static" style="font-weight: normal;" title="Gdelt: ${eventInfo.glideCode}"><a href="http://reliefweb.int/disaster/${eventInfo.glideCode}" target="_blank">Gdelt: ${eventInfo.glideCode}</a></label>
								</#if>
								
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="name">Collection Name :</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="name" placeholder="Collection Name" required>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="provider">Collection Source :</label>
							<div class="col-sm-6">
								<select class="form-control" name="provider" id="provider">
									<option value="TWITTER" selected>Twitter</option>
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
						<div id="twitterConfigDiv">
							<div class="form-group" id="keywordsDiv">
								<label class="control-label col-sm-3" for="track">Keywords :</label>
								<div class="col-sm-6"> 
									<textarea class="form-control" rows="6" name="track" placeholder="Enter comma seperated keywords">${keywords}</textarea>
								</div>
							</div>
							
							<div class="form-group">
								<label class="control-label col-sm-3" for="twitterDatePicker" title="Enter to collect Twitter historical data">Twitter From Date :</label>
								<div class="row">
									<div class="col-sm-6" style="padding-right: 30px;">
										<div class="input-daterange input-group" id="twitterDatePicker">
										    <input type="text" class="input-sm form-control" name="start" id="twitterStartDate"/>
										    <span class="input-group-addon">to</span>
										    <input type="text" class="input-sm form-control" name="end" id="twitterEndDate"/>
										</div>
                                        <div class="breadcrumb">Enter dates if you want to collect historical data also, otherwise leave empty & collect only Realtime data.</div>
									</div>
								</div>
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
							<div class="form-group">
								<label class="control-label col-sm-3" for="fetchInterval">Facebook Fetch Interval :</label>
								<div class="col-sm-6">
									<select class="form-control" name="fetchInterval">
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
									<select class="form-control" name="fetchFrom">
										<option value="168" selected>7 days</option>
										<option value="360">15 days</option>
										<option value="720">1 month</option>
										<option value="2160">3 months</option>
										<option value="4320">6 months</option>
									</select>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-sm-offset-3 checkbox-inline" style="padding-left: 35px; padding-top: 0px">
								<input type="checkbox" value="true" id="runAfterCreate">Run after create.
							</label>
						</div>
						
					</div>
					
<!-- 					Text Disambiguity tab -->
					<div id="textDisambiguityConfig" class="tab-pane fade">
						<div class="form-group">
							<label class="control-label col-sm-3" for="textDisambiguityTopic">Text Disambiguity Topic :</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="textDisambiguityTopic" placeholder="Text Disambiguity Topic" required>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="firstLabel">First Label :</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="firstLabel" placeholder="First Label" required>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="secondLabel">Second Label :</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="secondLabel" placeholder="Second Label" required>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="firstLabelTags">First Label Tags :</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="firstLabelTags" placeholder="Comma Seperated tags for first label" required>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="secondLabelTags">Second Label Tags :</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="secondLabelTags" placeholder="Comma Seperated tags for second label" required>
							</div>
						</div>
					</div>
				</div>
				
				
				<!-- Cancel & Submit-->
				<div class="form-group">
					<div class="col-sm-offset-4">
						<button type="reset"  class="btn btn-danger">Reset</button>
						<button type="submit" class="btn btn-primary" id="submitButton">Create and Start</button>
					</div>
				</div>
			</form>
		</div> <!-- /container -->
		
		<#include "_footer.html">
		
		<script>
			<#include "cookies.js">
			<#include "lang.js">
			<#include "/collection/create/create.js"> 
		</script>
	</body>
</html>