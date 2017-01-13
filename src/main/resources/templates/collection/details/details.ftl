<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
		<table style="width:100%">
			<tr>
				<td class="col-md-9" style="word-wrap: break-word;max-width: 160px;white-space:normal;">
					<h3>${collectionInfo.name}
					<small style="font-size:60%">
					<#if current_user != collectionInfo.owner>
						by ${collectionInfo.owner}
					</#if>
					<script>initializeCountScheduler=false</script>
							<#if collectionInfo.status =="RUNNING">
								<p class="text-success">
								<script>initializeCountScheduler=true</script>
							
							<#elseif collectionInfo.status =="NOT_RUNNING" > 
								<p class="text-muted">
							
							<#elseif collectionInfo.status =="RUNNING_WARNING" || collectionInfo.status =="WARNING"> 
								<p class="text-warning">
								<script>initializeCountScheduler=true</script>
								
							<#elseif collectionInfo.status =="TRASHED" > 
								<p class="text-info">
							<#else>
								<p class="text-danger">
							</#if>
							<b>${collectionInfo.status}</b>
						</p></small></h3>
					
				</td>
			
				<td class="col-md-3" style="width:100%">
					<#if collectionInfo.status =="RUNNING" || collectionInfo.status =="RUNNING_WARNING" || collectionInfo.status =="WARNING">
						<button class="btn btn-danger btn-lg btn-block pull-right" id="stopCollectionButton" style="margin-right:15px;" data-title="Stop Collection" role="button" data-id="${collectionInfo.id}"> Stop</button>

					<#elseif collectionInfo.status =="TRASHED" > 
						<button class="btn btn-danger btn-lg btn-block pull-right" id="restoreCollectionButton" style="margin-right:15px;" data-title="Restore Collection" role="button" data-id="${collectionInfo.id}"> Restore</button>
					<#else>
						<button class="btn btn-success btn-lg btn-block pull-right" id="startCollectionButton" style="margin-right:15px;" data-title="Start Collection" role="button" data-id="${collectionInfo.id}"> Start</button>
					</#if>
					
				</td>
			</tr>
		</table>
		
		
			<div class="row col-md-12" >
			<div class="table-responsive">
				<table class="table table-striped">
					<tbody>
						<tr>
							<td>Event:</td>
							<#if collectionInfo.globalEventDefinition??>
								<td class="text-right" style="word-wrap: break-word;max-width: 160px;white-space:normal;" title="Snopes: ${collectionInfo.globalEventDefinition.title}"><a href="${collectionInfo.globalEventDefinition.eventUrl}" target="_blank">Snopes: ${collectionInfo.globalEventDefinition.title}</a></td>
							<#elseif collectionInfo.glideMaster??>
								<td class="text-right" style="word-wrap: break-word;max-width: 160px;white-space:normal;" title="Gdelt: ${collectionInfo.glideMaster.glideCode}"><a href="http://reliefweb.int/disaster/${collectionInfo.glideMaster.glideCode}" target="_blank">Gdelt: ${collectionInfo.glideMaster.glideCode}</a></td>
							</#if>
							
						</tr>
						<tr>
							<td>Total Collection:</td>
							<td class="text-right" id="collectionCount" data-id="${collectionInfo.id}">${collectionCount}</td>
						</tr>
						<tr>
							<td>Created:</td>
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
						<tr>
							<td>Keywords:</td>
							<td class="text-right" style="word-wrap: break-word;max-width: 160px;white-space:normal;" title="${collectionInfo.track}">${collectionInfo.track}</td>
						</tr>
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