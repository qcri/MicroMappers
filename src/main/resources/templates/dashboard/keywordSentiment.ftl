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
				<table class="table table-striped table-bordered">
                    <thead>
                    <tr>
                        <td colspan="4" class="text-center">
                            <table width="100%">
                                <tr>
                                    <td style="width: 50%">
                                        <!--<form id="filterWords" action="${rc.getContextPath()}/dashboard/keywordSentiment?page=${index}&cid=${cid}" class="form-main">
                                            <div class="col-md-10 col-sm-10 col-xs-12">
                                                <label class="sr-only" for="search">Search</label>
                                                <div class="input-group">
                                                    <input type="text" class="form-control input-search" name="q" id="q" placeholder="Search">
                    									<span class="input-group-addon group-icon"><span class="glyphicon glyphicon-search"></span>
                                                </div>
                                            </div>
                                            <div class="col-md-2 col-sm-2 col-xs-12">
                                                <button type="submit" class="btn btn-primary" onclick="searchBy()">
                                                    <span class="glyphicon glyphicon-search" aria-hidden="true"></span><span class="hidden-sm hidden-xs"> Search </span>
                                                </button>
                                            </div>-->
                                        <a href="${rc.getContextPath()}/dashboard/keywordSentiment?page=${index}&cid=${cid}&dw=y">
                                            <span data-placement="top" data-toggle="tooltip" title="download">
                                                <i class="confirm-download btn btn-primary btn-large" data-title="Download">
                                                    <span class="glyphicon glyphicon-download"></span>
                                                </i>
                                            </span><b>Download</b>
                                        </a>
                                    </td>
                                    <td style="width: 50%">
                                        <div style="margin:0px;">
                                            <ul class="pagination pull-right">
                                                <!-- First Page -->
                                            <#if page.isFirstPage()>
                                                <li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-left" ></span></li>
                                            <#else>
                                                <li><a href="${rc.getContextPath()}/dashboard/keywordSentiment?page=${page.pageNumber-1}&cid=${cid}"><span class="glyphicon glyphicon-chevron-left"></span></a></li>
                                            </#if>

                                            <#list page.navigatePageNumbers as index>
                                                <#if page.getPageNumber() == index>
                                                <li class="active">
                                                <#else>
                                                <li>
                                                </#if>
                                                <a href="${rc.getContextPath()}/dashboard/keywordSentiment?page=${index}&cid=${cid}">${index}</a></li>
                                            </#list>
                                                <!-- Last Page -->
                                            <#if page.isLastPage()>
                                                <li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-right" ></span></li>
                                            <#else>
                                                <li ><a href="${rc.getContextPath()}/dashboard/keywordSentiment?page=${page.pageNumber+1}&cid=${cid}"><span class="glyphicon glyphicon-chevron-right"></span></a></li>
                                            </#if>
                                            </ul>
                                        </div>
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
						<#list page.list as info>
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