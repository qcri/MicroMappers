<!DOCTYPE html>
<html lang="en">
<#include "_header.html">
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


<#include "_footer.html">

<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script>
    <#include "cookies.js">
  </script>
</body>
</html>
