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
            <a class="btn" href="${rc.getContextPath()}/global/events/gdelt/data3w?glideCode=${glideCode}"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;<strong>${glideCode}</strong></a>
        </div>
        <table id="data3wGrid" class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>Article Info</th>
                <th>Image Info</th>
                <th>Who</th>
                <th>Where</th>
                <th>Lang</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
			<#list page as info>
            <tr>
                <td title="${info.articleURL}"><a href="${info.articleURL}" target="_blank" class="hoverMe">Read</a></td>
                <td title="${info.imgURL}"><a href="${info.imgURL}" target="_blank"><img src="${info.imgURL}"
                                                                                         alt="${rc.getContextPath()}/img/tb_blue_404.png" height="42" width="42"
                         onError="this.onerror=null;this.src='${rc.getContextPath()}/img/tb_blue_404.png';"></a></td>
				<td title="${info.who}">${info.who}</td>
                <td>
                    <#if info.wheres?has_content>
                        <#assign p = info.jsWheres>
                        <#list p?sort_by("name") as i>
                            <p>${i.name}</p>
                        </#list>
                    </#if>
                </td>
                <td title="${info.languageCode}">${info.languageCode}</td>
                <td></td>
            </tr>
			</#list>
            <div  id="urlModalDiv" style="display: none;overflow:hidden;width:auto;padding:0px">
                <iframe id="urlModalIFrame"  scrolling="no1" style="width:100%;height: 100%; border: 0px none; margin-bottom: 0px; margin-left: 0px;">
                </iframe>
            </div>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="7" class="text-center">
                    <table style="width: 100%">
                        <tr>
                            <td><a href="${rc.getContextPath()}/global/events/gdelt/data3w?glideCode=${glideCode}&dw=y">
                                <span data-placement="top" data-toggle="tooltip" title="download">
									<i class="confirm-download btn btn-primary btn-large" data-title="Download">
                                        <span class="glyphicon glyphicon-download"></span>
                                    </i>
								</span><b>Download</b></a>
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
        <#include "urlpopup.js">
        <#include "cookies.js">
        $('#data3wGrid').DataTable();
	</script>
</body>
</html>