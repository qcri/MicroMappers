<!DOCTYPE html>
<html lang="en">
	<#include "_header.html">
	<body>
		<#include "_navbar.ftl">
		<div class="container" style="min-height:400px;">
		<div class="col-md-12">
			<p style="padding-top:5px;">
				<span class="label label-warning"><i class="icon-bullhorn"></i>Note</span>
				By click the <strong>Sign in button</strong> below you are agreeing to
				the <a href="http://aidr.qcri.org/QSS-Usr-Agr-Print-Version.docx.pdf" target="_blank">terms of use</a> and
				<a href="http://aidr.qcri.org/QSS-Usr-Agr-Print-Version.docx.pdf" target="_blank">data</a>.
			</p>
		</div>
			
			<div class="col-md-3">
				<form action="auth/google" method="GET">
					<input type="hidden" name="scope" value="email" />
					<button type="submit" class="btn btn-social btn-google">
						<i class="fa fa-google"></i> Sign in with Google
					</button>
				</form>
			</div>
		</div>
		<!-- /container -->
		<#include "_footer.html">
		<script>
			<#include "cookies.js">
		</script>
	</body>
</html>