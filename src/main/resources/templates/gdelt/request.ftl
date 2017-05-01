<!DOCTYPE html>
<html lang="en">
<#include "_header.html">
<body>
<#include "_navbar.ftl">
<div class="container" style="min-height:400px;">
    <div class="row">
        <div class="btn-group">
            <a class="btn" href="${rc.getContextPath()}/home"><i class="glyphicon glyphicon-bookmark"></i>&nbsp;Home</a>
        </div>

        <div class="container" style="min-height:400px;">

            <form id="imageClassifierCreate" class="form-horizontal">
                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#general">General</a></li>
                </ul>
                <div class="tab-content" style="margin-top:15px">
                    <div id="general" class="tab-pane fade in active">
                        <div class="form-group">
                            <label class="control-label col-sm-3" for="textName">Name :</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="textName" placeholder="Please title the classifier" required>
                            </div>
                        </div>
                       <!-- <div class="form-group">
                            <label class="control-label col-sm-3" for="textTheme">Image Theme :</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="textTheme" placeholder="Please add topic word. e.g. flood" required>
                            </div>
                        </div>-->
                        <div class="form-group">
                            <label class="control-label col-sm-3" for="textImageWebTag">Image Web Tag :</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="textImageWebTag" placeholder="Comma Seperated related tags e.g. flood, fire" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3" for="textImageTag">Image Tag :</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="textImageTag" placeholder="Comma Seperated related tags e.g. flood, fire" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3" for="textLocation">Location :</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="textLocation" placeholder="please location e.g. new york" required>
                            </div>
                        </div>
                        <!-- <div class="form-group">
                            <label class="control-label col-sm-3" for="textCountry">Country :</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="textCountry" placeholder="please country e.g canada" required>
                            </div>
                        </div>-->

                    </div>
                </div>


                <!-- Cancel & Submit-->
                <div class="form-group">
                    <div class="col-sm-offset-4">
                        <button type="reset"  class="btn btn-danger">Reset</button>
                        <button type="submit" class="btn btn-primary" id="createClassifierRequest">Create</button>
                    </div>
                </div>
            </form>
        </div> <!-- /container -->

    </div>
</div>
<!-- /container -->
<#include "_footer.html">
<script>
	<#include "cookies.js">
	<#include "/gdelt/glides.js">
</script>
</body>
</html>