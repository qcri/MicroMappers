<!DOCTYPE html>
<html lang="en">
<#include "_header.html">
<body>
<#include "_navbar.ftl">
<div class="container" style="min-height:400px;">
    <div class="row">
        <div class="btn-group">
            <a class="btn" href="${rc.getContextPath()}/home"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Home</a>
            <a class="btn" href="${rc.getContextPath()}/global/events/gdelt/request"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Request New Image Classifier</a>
        </div>
        <br><br>
        <table id="classifierData" class="table table-striped table-bordered" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>Name</th>
                <th>Location</th>
                <th>Image Web Tag</th>
                <th>Image Tag</th>
                <th>Map</th>
                <th>GeoJson</th>
            </tr>
            </thead>
            <tbody>
            <#list page as info>
            <tr>
                <td>
                    <a id="${info.name}" href="http://reliefweb.int/disaster/${info.id}" target="_blank">${info.name}</a>
                </td>
                <td title="${info.location}">${info.location}</td>
                <td title="${info.imageWebTag}">${info.imageWebTag}</td>
                <td title="${info.imageTag}">${info.imageTag}</td>
                <td title="${info.mapURL}"><a href="${info.mapURL}" target="_blank">View Map</a></td>
                <td title="${info.geoJsonURL}"><a href="${info.geoJsonURL}" target="_blank">Download GeoJson</a></td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div>
<!-- /container -->
<#include "_footer.html">
<script>
		<#include "cookies.js">
        $('#classifierData').DataTable();
</script>
</body>
</html>