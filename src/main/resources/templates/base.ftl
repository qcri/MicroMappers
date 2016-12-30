<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>MicroMappers</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<!-- Le styles -->
		<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" type="text/css">
		<link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
		<link href="{{url_for('static', filename='${rc.getContextPath()}/css/styles.css')}}" rel="stylesheet">
		<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
		<!--[if lt IE 9]>
		<script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<![endif]-->
		<!-- Le fav and touch icons -->
		<link rel="shortcut icon" href="${rc.getContextPath()}/static/img/favicon.ico">
		<!-- Le javascript -->
		<script src="https://code.jquery.com/jquery-1.11.3.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/modernizr/2.7.1/modernizr.min.js"></script>
	</head>
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
			Let's what will show up
		</div>
		<!-- /container -->
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