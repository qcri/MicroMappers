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

        <table id="classifierData" class="table table-striped table-bordered" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>Name</th>
                <th>Theme</th>
                <th>Location</th>
                <th>Country</th>
                <th>Image Web Tag</th>
                <th>Image Tag</th>
            </tr>
            </thead>
            <tbody>
            <#list page as info>
            <tr>
                <td>
                    <a id="${info.name}" href="http://reliefweb.int/disaster/${info.id}" target="_blank">${info.name}</a>
                </td>
                <td title="${info.theme}">${info.theme}</td>
                <td title="${info.location}">${info.location}</td>
                <td title="${info.locationCC}">${info.locationCC}</td>
                <td title="${info.imageWebTag}">${info.imageWebTag}</td>
                <td title="${info.imageTag}">${info.imageTag}</td>
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