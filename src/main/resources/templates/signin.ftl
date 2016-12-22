<!DOCTYPE html>
<html lang="en">
<#include "_header.html">
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

  		<form action="auth/google" method="GET">
			<input type="hidden" name="scope" value="email" />
        	<button type="submit" class="btn btn-default btn-md social-btn" >
        		<img src="${rc.getContextPath()}/img/google.png" class="social-logo" /> Login with Google
        	</button>
    	</form>
    <hr>

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

