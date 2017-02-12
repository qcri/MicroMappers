<!DOCTYPE html>
<html lang="en">
<#setting url_escaping_charset='ISO-8859-1'>
<#include "_header.html">
<body>
<#include "_navbar.ftl">
<div class="container" style="min-height:400px;">
    <div class="row">
        <div class="btn-group">
            <a class="btn" href="${rc.getContextPath()}/home"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Home</a>
            <a class="btn" href="${rc.getContextPath()}/global/events/gdelt/glides"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Global Disaster Events</a>
        </div>
        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>Code</th>
                <th>Article Info</th>
                <th>Image Info</th>
                <th>Where</th>
                <th>Lang</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
			<#list page.list as info>
            <tr>
                <td>${info.glideCode}</td>
                <td title="${info.articleURL}"><a href="${info.articleURL}" target="_blank">Read</a></td>
                <td title="${info.imgURL}"><a href="${info.imgURL}" target="_blank">
                    <img src="${info.imgURL}" alt="${rc.getContextPath()}/img/tb_blue_404.png" height="42" width="42"
                         onError="this.onerror=null;this.src='${rc.getContextPath()}/img/tb_blue_404.png';"></a></td>
                <td>
                    <#if info.location?has_content>
                        ${info.location}
                    </#if>
                </td>
                <td title="${info.languageCode}">${info.languageCode}</td>
                <td></td>
            </tr>
			</#list>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="6" class="text-center">
                <table style="width: 100%">
                    <tr>
                        <td><a href="${rc.getContextPath()}/global/events/gdelt/datammic?glideCode=${glideCode}&page=${page.pageNumber}&dw=y">
                            <span data-placement="top" data-toggle="tooltip" title="download">
									<i class="confirm-download btn btn-primary btn-large" data-title="Download">
                                        <span class="glyphicon glyphicon-download"></span>
                                    </i>
                            </span><b>Download</b></a></td>
                        <td>
                            <div style="margin:0px;">
                                <ul class="pagination pull-right">
                                    <!-- First Page -->
                                <#if page.isFirstPage()>
                                    <li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-left" ></span></li>
                                <#else>
                                    <li><a href="${rc.getContextPath()}/global/events/gdelt/datammic?glideCode=${glideCode}&page=${page.pageNumber-1}"><span class="glyphicon glyphicon-chevron-left"></span></a></li>
                                </#if>

                                <#list page.navigatePageNumbers as index>
                                    <#if page.getPageNumber() == index>
                                    <li class="active">
                                    <#else>
                                    <li>
                                    </#if>
                                    <a href="${rc.getContextPath()}/global/events/gdelt/datammic?glideCode=${glideCode}&page=${index}">${index}</a></li>
                                </#list>
                                    <!-- Last Page -->
                                <#if page.isLastPage()>
                                    <li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-right" ></span></li>
                                <#else>
                                    <li ><a href="${rc.getContextPath()}/global/events/gdelt/datammic?glideCode=${glideCode}&page=${page.pageNumber+1}"><span class="glyphicon glyphicon-chevron-right"></span></a></li>
                                </#if>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </table>
                </td>
            </tr>
            </tfoot>
        </table>
    </div>
</div>
<!-- /container -->
<#include "_footer.html">
<script>
			<#include "cookies.js">
		</script>
</body>
</html>