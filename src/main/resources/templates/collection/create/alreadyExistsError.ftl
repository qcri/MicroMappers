<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<h3>There is a collection already existed for you. You can update your older collection.
			<a href="${rc.getContextPath()}/collection/view/details?id=${collectionId}">Click here</a> to view/edit.</h3>
		</div>
		
		<#include "_footer.html">
		
		<script>
			<#include "cookies.js">
		</script>
	</body>
</html>