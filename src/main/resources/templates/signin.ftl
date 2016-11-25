<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>MicroMappers</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">

  <!-- Le styles -->
  <link href="./vendor/bootstrap.min.css" rel="stylesheet" type="text/css">
  <link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
  <link href="./css/fonts/stylesheet.css" rel="stylesheet">
  <link href="./css/styles.css" rel="stylesheet">

  <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
  <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->

  <!-- Le fav and touch icons -->
  <!-- Le fav and touch icons -->
  <link rel="shortcut icon" href="./img/favicon.ico">

  <!-- Le javascript -->
  <script type="text/javascript" src="./vendor/jquery.js"></script>
  <script type="text/javascript" src="./vendor/bootstrap/2.2.1/js/bootstrap.js"></script>
  <script type="text/javascript" src="./vendor/modernizr.min.js"></script>
</head>

<body>
<#include "_navbar.ftl">

  <div class="container" style="min-height:400px;">



    <p style="padding-top:5px;">
    <p style="padding-top:5px;">
    <span class="label label-warning"><i class="icon-bullhorn"></i>Note</span>
        By click the <strong>Sign in button</strong> below you are agreeing to
        the <a href="">terms of use</a> and
        <a href="">data</a>.
    </p>


      <a href="" class="btn btn-warning"><i class="icon-google-plus"></i> Sign in with Google</a>

    <hr>

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

