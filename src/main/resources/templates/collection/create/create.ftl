<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<#if eventType?? && eventTypeId?? >
			<input type="hidden" id="eventType" value="${eventType}">
			<input type="hidden" id="eventTypeId" value="${eventTypeId}">
		</#if>
		<div class="container" style="min-height:400px;">
		
			<form id="collectionCreate" class="form-horizontal">
   
			   <h3 style="margin-top:-1%">Create Collection</h3>
			   
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#general">General</a></li>
<!-- 					<li><a data-toggle="tab" href="#advanced">Advanced</a></li> -->
				</ul>
				<div class="tab-content" style="margin-top:15px">
				
					<!--General Tab -->
					<div id="general" class="tab-pane fade in active">
						<div class="form-group">
							<label class="control-label col-sm-3" for="name">Collection Name :</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="name" placeholder="Collection Name" required>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-3" for="provider">Collection Source :</label>
							<div class="col-sm-6">
								<select class="form-control" name="provider">
									<option value="TWITTER" selected>Twitter</option>
<!-- 									<option value="FACEBOOK">Facebook</option> -->
<!-- 									<option value="BOTH">Both</option> -->
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
								<textarea class="form-control" rows="6" name="track" placeholder="Enter comma seperated keywords">${keywords}</textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-offset-3 checkbox-inline" style="padding-left: 35px; padding-top: 0px">
								<input type="checkbox" value="true" id="runAfterCreate">Run after create.
							</label>
						</div>
						
					</div>
					
					
					<!-- Advanced Tab -->
					<!-- <div id="advanced" class="tab-pane fade">
						<div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="geo">Geo Boundaries :</label>
								<div class="col-sm-6">
									<textarea class="form-control" rows="6" name="geo" placeholder="Enter comma seperated geo location."></textarea>
									<span id="geoHelpBlock" class="form-text text-muted">
										<span style="color:red;">*</span> Click here to get coordinates: <a href="http://boundingbox.klokantech.com" target="_blank">boundingbox.klokantech.com</a>("Copy/paste CSV format of a boundingbox")
									</span>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="geoR">Geo Boundaries Strictness :</label>
								<div class="col-sm-6">
									<div class="radio">
										<label><input type="radio" name="geoR" value='null'>Does not apply (no geographical boundary)</label>
									</div>
									<div class="radio">
										<label><input type="radio" name="geoR" value='approximate'>Approximate: a tweet may be collected if it comes from a country that overlaps with the geographical boundaries.</label>
									</div>
									<div class="radio">
										<label><input type="radio" name="geoR" value='strict'>Strict: a tweet can only be collected if it has geographical coordinates strictly inside the geographical boundaries.</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="follow">Follow :</label>
								<div class="col-sm-6"> 
									<input type="text" class="form-control" name="follow" placeholder="Enter comma seperated twitter user id's to follow.">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-3" for="duration">Collect data for duration :</label>
								<div class="col-sm-6">
									<select class="form-control" name="duration">
										<option value="12">12 hours</option>
										<option value="24">1 day</option>
										<option value="36">1 day 12 hours</option>
										<option value="48">2 days</option>
										<option value="60">2 day 12 hours</option>
										<option value="72">3 days</option>
										<option value="96">4 days</option>
										<option value="120">5 days</option>
										<option value="144">6 days</option>
										<option value="168">7 days</option>
										<option value="336">14 days</option>
									</select>
									<span id="durationHelpBlock" class="form-text text-muted">
										<span style="color:red;">*</span> A normal setting for most crises is 2 to 5 days. If you need more than 7 days contact the AIDR team.
									</span>
								</div>
							</div>
						</div>
					</div> -->
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