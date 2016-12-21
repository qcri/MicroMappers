<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>MicroMappers</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="${rc.getContextPath()}/vendor/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
    <link href="${rc.getContextPath()}/css/fonts/stylesheet.css" rel="stylesheet">
    <link href="${rc.getContextPath()}/css/styles.css" rel="stylesheet">

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- Le fav and touch icons -->
    <link rel="shortcut icon" href="${rc.getContextPath()}/img/favicon.ico">

    <!-- Le javascript -->
    <script type="text/javascript" src="${rc.getContextPath()}/vendor/jquery.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/vendor/bootstrap/2.2.1/js/bootstrap.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/vendor/modernizr.min.js"></script>
</head>

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
                    <ul class="pagination" style="margin:0px;">
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

                    <#list ['1', '2', '3'] as i>
                        ${i?counter}: ${i}
                    </#list>
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
                </td>
            </tr>
            </tfoot>
        </table>
    </div>

</div> <!-- /container -->

<footer>
<#include "_footer.html">
</footer>
<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script>
      <#include "cookies.js">
    </script>
</body>
</html>
