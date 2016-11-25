<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container">
      <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </a>
       <a class="brand" href="http://clickers.micromappers.org"><img src="./img/mm_logo.png"></a>
       <div class="nav-collapse collapse">
        <ul class="nav">
            <li><a href="http://clickers.micromappers.org">Home</a></li>
            <li><a href="http://aidr-web.qcri.org/MMAPI" target="_blank">Maps</a></li>
            <li><a href="http://micromappers.org/" target="_blank">About</a></li>
        </ul>


        <#if current_user.user_name == "Anonymous" >
            <ul class="nav secondary-nav pull-right">
              <li><a href="signin"><i class="icon icon-white icon-signin"></i>Sign in</a></li>
            </ul>
        </#if>
      </div><!--/.nav-collapse -->
    </div>
  </div>
</div>
