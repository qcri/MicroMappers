<!DOCTYPE html>
<html lang="en">
<#include "_header.html">
<body>
<#include "_navbar.ftl">

<div class="container" style="min-height:400px;">

    <div class="row">
        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>Title</th>
                <th>Summary</th>
                <th>Tag</th>
            </tr>
            </thead>
            <tbody>
            <#list page.list as info>
            <tr>
                <td>
                    <a href="/detail/${info.id}" title="${info.title}">
                    ${info.title}
                    </a>
                </td>
                <td title="${info.description}">
                    ${info.description}
                </td>
                <td>${info.articleTag}</td>
            </tr>
            </#list>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="3" class="text-center">
                    <div class="pagination pagination-centered" style="margin:0px;">
                        <ul>
                            <!-- First Page -->
                        <#if page.isFirstPage()>
                            <li><span aria-hidden="true">First</span></li>
                        <#else>
                            <li>
                                <a href="/Micromappers/global/events/snopes?page=${page.pageNumber-1}" aria-label="Previous">
                                    <span aria-hidden="true">Previous</span>
                                </a>
                            </li>
                        </#if>
                        <#list page.navigatePageNumbers as index>
                            <li><a href="/Micromappers/global/events/snopes?page=${index?counter}">${index?counter}</a></li>
                        </#list>

                            <!-- Last Page -->
                        <#if page.isLastPage()>
                            <li><span aria-hidden="true">Last</span></li>
                        <#else>
                            <li>
                                <a href="/Micromappers/global/events/snopes?page=${page.pageNumber+1}" aria-label="Previous">
                                    <span aria-hidden="true">Next</span>
                                </a>
                            </li>
                        </#if>
                        </ul>
                    </div>
                </td>
            </tr>
            </tfoot>
        </table>
    </div>

</div> <!-- /container -->


<#include "_footer.html">

<!-- Le javascript
================================================== -->
<!-- Bootstrap core JavaScript -->
<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<!-- Placed at the end of the document so the pages load faster -->
</body>
</html>
