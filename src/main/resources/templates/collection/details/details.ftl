<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
		<table style="width:100%">
			<tr>
				<td class="col-md-9" style="word-wrap: break-word;max-width: 160px;white-space:normal;">
					<h2>${collectionInfo.name}</h2>
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
							<td>Status:</td>
							<script>initializeCountScheduler=false</script>
							<#if collectionInfo.status =="RUNNING">
								<td class="text-right text-success">
								<script>initializeCountScheduler=true</script>
							
							<#elseif collectionInfo.status =="NOT_RUNNING" > 
								<td class="text-right text-muted">
							
							<#elseif collectionInfo.status =="RUNNING_WARNING" || collectionInfo.status =="WARNING"> 
								<td class="text-right text-warning">
								<script>initializeCountScheduler=true</script>
								
							<#elseif collectionInfo.status =="TRASHED" > 
								<td class="text-right text-info">
							<#else>
								<td class="text-right text-danger">
							</#if>
							<b>${collectionInfo.status}</b></td>
						</tr>
						<tr>
							<td>Total Collection:</td>
							<td class="text-right" id="collectionCount" data-id="${collectionInfo.id}">${collectionCount}</td>
						</tr>
						<tr>
							<td>Created:</td>
							<td class="text-right">${collectionCreatedAt}</td>
						</tr>
						<tr>
							<td>Language:</td>
							<#if collectionInfo.langFilters == "">
								<td class="text-right">Any language</td>
							<#else>
								<td class="text-right" style="word-wrap: break-word;max-width: 160px;white-space:normal;">${collectionInfo.langFilters}</td>
							</#if>
						</tr>
						<tr>
							<td>Keywords:</td>
							<td class="text-right" style="word-wrap: break-word;max-width: 160px;white-space:normal;">${collectionInfo.track}</td>
						</tr>
						<tr>
							<td>CreatedBy:</td>
							<td class="text-right">${collectionInfo.owner}</td>
						</tr>
						<tr>
							<td>Collaborators:</td>
							<td class="text-right" style="word-wrap: break-word;max-width: 160px;white-space:normal;">${collectionCollaborators}</td>
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