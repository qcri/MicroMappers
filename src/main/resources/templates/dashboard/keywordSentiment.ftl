<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<div class="row">
                <div class="btn-group">
                    <a class="btn" href="${rc.getContextPath()}/home"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Home</a>
                </div>
                <div id="vis"></div>
				<table class="table table-striped table-bordered">
                    <thead>
                    <tr>
                        <td colspan="4" class="text-center">
                            <table width="100%">
                                <tr>
                                    <td style="width: 50%">
                                        <form id="filterWords" action="${rc.getContextPath()}/dashboard/keywordSentiment?page=${index}&cid=${cid}" class="form-main">
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
                                            </div>
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
							<th>Positive Score</th>
							<th>Negative Score</th>
                            <th>Created At</th>
						</tr>
					</thead>
					<tbody>
						<#list page.list as info>
                            <tr>
                                <td title="${info.feedText}" width="60%">
                                    ${info.feedText}
                                </td>
                                <td width="15%">
                                    ${info.positive}
                                </td>
                                <td width="15%">
                                    ${info.negative}
                                </td>
                                <td title="${info.createdAt}" width="10%">
                                    ${info.createdAt?date}
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
	</body>
</html>