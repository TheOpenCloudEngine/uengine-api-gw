<%@ page contentType="text/html; charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<div id="authentication" name="propertyWindow" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel"
     aria-hidden="true">
    <%--modal-lg--%>
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                <h4 class="modal-title" name="title"></h4>
            </div>
            <div class="modal-body">
                <!-- Tab v2 -->
                <div class="tab-v2">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#authentication-1" data-toggle="tab">Label</a></li>
                        <li class=""><a href="#authentication-2" data-toggle="tab">Token</a></li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active" id="authentication-1">
                            <div class="row">
                                <div class="col-md-12">
                                    <form name="propertyForm" action="#" class="form-horizontal" role="form">
                                        <h4>Change Label</h4>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Label <span
                                                    class="color-red">*</span></label>

                                            <div class="col-md-9">
                                                <input name="label" type="text" class="form-control" required>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane fade in" id="authentication-2">
                            <div class="row">
                                <div class="col-md-12">
                                    <form name="propertyForm" action="#" class="form-horizontal" role="form">
                                        <h4>Where is Token?</h4>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Location <span
                                                    class="color-red">*</span></label>

                                            <div class="col-md-9">
                                                <select name="location" class="form-control" required>
                                                    <option value="header">Header</option>
                                                    <option value="parameter">GET Parameter</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Parameter Name <span
                                                    class="color-red">*</span></label>

                                            <div class="col-md-9">
                                                <input name="name" type="text" class="form-control" required>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <a href="#" name="save" class="btn-u btn-u-primary pull-right">Save</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


