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
        <div class="span12" style="min-height:400px;">
            <div class="row-fluid">
                <div id="dashboard" class="span6 well">
                    <h2><i class="icon-dashboard"></i> My Social Media Tokens</h2>
                    <p>Internal Statistics</p>
                    <a href="${rc.getContextPath()}/settings" class="btn btn-primary">
                        Manage <i class="icon-chevron-right"></i>
                    </a>
                </div>
                <div id="dashboard" class="span6 well">
                    <h2><i class="icon-dashboard"></i> My Jobs</h2>
                    <p>Monitor the background jobs</p>
                    <a href="" class="btn btn-primary">
                        Manage <i class="icon-chevron-right"></i>
                    </a>
                </div>
            </div>
            <div class="row-fluid">
                <div id="featured-apps" class="span6 well">
                    <h2><i class="icon-star"></i> Snopes Global Events</h2>
                    <p>Show projects on the front page</p>
                    <a href="/Micromappers/global/events/snopes" class="btn btn-primary">
                        View <i class="icon-chevron-right"></i>
                    </a>
                </div>
                <div id="categories" class="span6 well">
                    <h2><i class="icon-check"></i> News Global Events</h2>
                    <p>Manage project categories</p>
                    <a href="" class="btn btn-primary">
                        View <i class="icon-chevron-right"></i>
                    </a>
                </div>
            </div>
            <div class="row-fluid">
                <div id="users" class="span6 well">
                    <h2><i class="icon-user"></i> Settings</h2>
                    <p>Manage administrators</p>
                    <a href="" class="btn btn-primary">
                        Manage <i class="icon-chevron-right"></i>
                    </a>
                </div>
                <div id="users-list" class="span6 well">
                    <h2><i class="icon-list"></i> My Collaborators</h2>
                    <p>Export a list of users}</p>
                    <a href="" class="btn btn-primary">
                        Manage <i class="icon-chevron-right"></i>
                    </a>
                </div>
            </div>
        </div>
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
