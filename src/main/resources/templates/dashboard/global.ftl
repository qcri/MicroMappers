<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			<div class="row">
                <div class="btn-group">
                    <a class="btn" href="${rc.getContextPath()}/home"><i class="icon-tags"></i>&nbsp;Home</a>
                </div>
                <div id="vis"></div>
				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>Title</th>
							<th>Dataset No.</th>
							<th>Started</th>
                            <th>Source</th>
						</tr>
					</thead>
					<tbody>
						<#list page.list as info>
						<tr>
							<td>
								<#if info.glideMaster?? >
                                    <a href="http://reliefweb.int/disaster/${info.glideMaster.glideCode}" target="_blank">${info.glideMaster.glideCode}</a>
								<#else>
                                    <a href="eventUrl" target="_blank">${info.globalEventDefinition.title}</a>
								</#if>
							</td>
                            <td>
								<#if info.glideMaster?? >
                                    ${info.gdeltCollectionTotal + info.socialCollectionTotal}
								<#else>
									${info.socialCollectionTotal}
								</#if>
                            </td>
							<td title="${info.createdAt}">
								${info.createdAt?date}
							</td>
                            <td>
								${info.source}
                            </td>
						</#list>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="4" class="text-center">
								<div style="margin:0px;">
									<ul class="pagination pull-right">
										<!-- First Page -->
										<#if page.isFirstPage()>
											<li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-left" ></span></li>
										<#else>
											<li><a href="${rc.getContextPath()}/dashboard/global?page=${page.pageNumber-1}"><span class="glyphicon glyphicon-chevron-left"></span></a></li>
										</#if>
										
										<#list page.navigatePageNumbers as index>
											<#if page.getPageNumber() == index>
												<li class="active">
											<#else>
												<li>
											</#if>
												<a href="${rc.getContextPath()}/dashboard/global?page=${index}">${index}</a></li>
										</#list>
										<!-- Last Page -->
										<#if page.isLastPage()>
											<li class="disabled"><span style="margin-top:-1px;" class="glyphicon glyphicon-chevron-right" ></span></li>
										<#else>
											<li ><a href="${rc.getContextPath()}/dashboard/global?page=${page.pageNumber+1}"><span class="glyphicon glyphicon-chevron-right"></span></a></li>
										</#if>
									</ul>
								</div>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
		<!-- /container -->
		<#include "_footer.html">
		<script>
			<#include  "cookies.js">
		</script>
	</body>
    <script>
        var b_keywords= ${wordClouds};
        var color = d3.scale.linear()
                .domain([0,1,2,3,4,5,6,10,15,20,100])
                .range(["#ddd", "#ccc", "#bbb", "#aaa", "#999", "#888", "#777", "#666", "#555", "#444", "#333", "#222"]);

        d3.layout.cloud().size([400, 300])
                .words(b_keywords)
                .rotate(0)
                .fontSize(function(d) { return d.size; })
                .on("end", draw)
                .start();

        function draw(words) {
            d3.select("#vis").append("svg")
                    .attr("width", 600)
                    .attr("height", 315)
                    .attr("class", "wordcloud")
                    .append("g")
                // without the transform, words words would get cutoff to the left and top, they would
                // appear outside of the SVG area
                //.attr("transform", "translate(320,200)")
                    .attr("transform", "translate(" + 400 / 2 + "," + 300 / 2 + ")")
                    .selectAll("text")
                    .data(words)
                    .enter().append("text")
                    .style("font-size", function(d) { return d.size + "px"; })
                    .style("fill", function(d, i) { return color(i); })
                    .attr("transform", function(d) {
                        return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                    })
                    .text(function(d) { return d.text; });
        }
    </script>
</html>