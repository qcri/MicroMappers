<form id="signOut" action="${rc.getContextPath()}/signout" method="post" class="secondary-nav pull-right">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container">
      <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </a>
       <a class="brand" href="http://clickers.micromappers.org"><img src="${rc.getContextPath()}/img/mm_logo.png"></a>
       <div class="nav-collapse collapse">
        <ul class="nav">
            <li><a href="http://clickers.micromappers.org">Home</a></li>
            <li><a href="http://aidr-web.qcri.org/MMAPI" target="_blank">Maps</a></li>
            <li><a href="http://micromappers.org/" target="_blank">About</a></li>
        </ul>
        <#if current_user?? >
            <ul class="nav secondary-nav pull-right">
                <li><a href="#" data-toggle="dropdown" class="dropdown-toggle"><i class="icon icon-user"></i>&nbsp;&nbsp;${current_user}<span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href=""><i class="icon icon-user"></i>&nbsp;&nbsp;My Profile</a></li>
                    <li><a href=""><i class="icon icon-th-large"></i>&nbsp;&nbsp;My Collections</a></li>
                    <li><a href=""><i class="icon icon-cog"></i>&nbsp;&nbsp;My Settings</a></li>
                    <li class="divider"></li>
                    <li><a href="#" onclick="document.getElementById('signOut').submit();"><i class="icon icon-off"></i>&nbsp;&nbsp;Sign out</a></li>
                </ul>
                </li>
            </ul>
        <#else>
            <ul class="nav secondary-nav pull-right">
              <li><a href="${rc.getContextPath()}/signin"><i class="icon icon-white icon-signin"></i>&nbsp;&nbsp;Sign in</a></li>
            </ul>
        </#if>
      </div><!--/.nav-collapse -->
    </div>
  </div>
</div>
</form>
