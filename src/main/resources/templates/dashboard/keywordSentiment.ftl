<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<div class="row">
                <div class="btn-group">
                    <a class="btn" href="${rc.getContextPath()}/home"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Home</a>
                    <a class="btn" href="${rc.getContextPath()}/collection/view/list"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;My collections</a>
                </div>
                <div id="vis" class="col-md-offset-2">
                    <svg id="fillgauge1" width="30%" height="200"></svg>
                    <svg id="fillgauge2" width="30%" height="200"></svg>
                </div>
				<table id="sentimentDataGrid" class="table table-striped table-bordered">
                    <thead>
                    <tr>
                        <td colspan="4" class="text-center">
                            <table width="100%">
                                <tr>
                                    <td style="width: 50%">
                                        <a href="${rc.getContextPath()}/dashboard/keywordSentiment?page=${index}&cid=${cid}&dw=y">
                                            <span data-placement="top" data-toggle="tooltip" title="download">
                                                <i class="confirm-download btn btn-primary btn-large" data-title="Download">
                                                    <span class="glyphicon glyphicon-download"></span>
                                                </i>
                                            </span><b>Download</b>
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <th>Text</th>
                        <th>Positive</th>
                        <th>Negative</th>
                        <th>Created At</th>
                    </tr>
					</thead>
					<tbody>
						<#list page as info>
                            <tr>
                                <td title="${info.feedText}" width="60%">
                                    ${info.feedText}
                                </td>
                                <td width="10%" title="${info.positive}">
                                    <#if info.positive gt 0.5>
                                        <i class="glyphicon glyphicon-ok"></i>
                                    <#elseif info.positive == 0.5>
                                        <i class="glyphicon glyphicon-question-sign"></i>
                                     <#else>
                                         <i class="glyphicon glyphicon-remove"></i>
                                    </#if>
                                </td>
                                <td width="10%" title="${info.negative}">
                                    <#if info.negative gt 0.5>
                                        <i class="glyphicon glyphicon-ok"></i>
                                    <#elseif info.negative == 0.5>
                                        <i class="glyphicon glyphicon-question-sign"></i>
                                    <#else>
                                        <i class="glyphicon glyphicon-remove"></i>
                                    </#if>
                                </td>
                                <td title="${info.createdAt}" width="20%">
                                    ${info.createdAt}
                                </td>
                            </tr>
						</#list>
					</tbody>
				</table>
			</div>
		</div>
		<!-- /container -->
		<#include "_footer.html">
		<script>
			<#include  "cookies.js">
			<#include "urlpopup.js">
		</script>
        <script language="JavaScript">
            $('#sentimentDataGrid').DataTable();

            var config0 = liquidFillGaugeDefaultSettings();
            config0.circleThickness = 0.2;
            config0.textVertPosition = 0.2;
            config0.waveAnimateTime = 1000;
            var gauge1 = loadLiquidFillGauge("fillgauge1", ${pos_percent}, config0);

            var config1 = liquidFillGaugeDefaultSettings();
            config1.circleColor = "#FF7777";
            config1.textColor = "#FF4444";
            config1.waveTextColor = "#FFAAAA";
            config1.waveColor = "#FFDDDD";
            config1.circleThickness = 0.2;
            config1.textVertPosition = 0.2;
            config1.waveAnimateTime = 1000;
            var gauge2= loadLiquidFillGauge("fillgauge2", ${neg_percent}, config1);


        </script>
	</body>
</html>