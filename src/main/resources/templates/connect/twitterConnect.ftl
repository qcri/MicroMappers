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
<#include "/_navbar.ftl">

  <div class="container" style="min-height:400px;">

    <div class="row">
        <div class="col-lg-12 text-center">
            <h1>MicroMappers</h1>
            <p class="lead">Social Message Purification</p>
            <ul class="list-unstyled">
                <li>
                	Welcome
                	<form action="${rc.getContextPath()}/connect/twitter" method="POST">
                		<input type="hidden" name="_csrf" value="${_csrf.token}" />
                    	<button type="submit" class="btn btn-default btn-md social-btn" >
                    		<img src="${rc.getContextPath()}/img/twitter.png" class="social-logo" /> Connect with Twitter
                    	</button>
                	</form> 
                </li>
                
            </ul>
        </div>
    </div>

  </div> <!-- /container -->

  <footer>
    <#include "/_footer.html">
  </footer>
  <!-- Le javascript
  ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <script>
    <#include "/cookies.js">
  </script>
</body>
</html>

