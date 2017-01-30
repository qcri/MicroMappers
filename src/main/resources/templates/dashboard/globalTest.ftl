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
var frequency_list = [{"text":"study","size":100},{"text":"motion","size":15},{"text":"forces","size":10},{"text":"distance","size":35}
    ,{"text":"wheels","size":5},{"text":"revelations","size":5},{"text":"minute","size":5},{"text":"acceleration","size":20},
    {"text":"add","size":5},{"text":"traveled","size":5},{"text":"weight","size":5},{"text":"electrical","size":5},
    {"text":"power","size":5}];

var words_list = ["Hello", "world", "normally", "you", "want", "more", "words", "than", "this"]
        .map(function(d) {
            return {text: d, size: 10 + Math.random() * 90};
        });


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
    //svg = d3.select("#vis").append("svg").attr("width", w).attr("height", h),
    //background = svg.append("g"),
    d3.select("#vis").append("svg")
            .attr("width", 600)
            .attr("height", 600)
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