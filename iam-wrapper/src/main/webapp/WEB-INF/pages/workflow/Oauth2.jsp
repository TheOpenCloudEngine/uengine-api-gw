<%@ page contentType="text/html; charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<div id="oauth2" name="propertyWindow" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="myLargeModalLabel"
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
                        <li class="active"><a href="#oauth2-1" data-toggle="tab">Label</a></li>
                        <li class=""><a href="#oauth2-2" data-toggle="tab">Oauth2</a></li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active" id="oauth2-1">
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
                        <div class="tab-pane fade in" id="oauth2-2">
                            <div class="row">
                                <div class="col-md-12">
                                    <form name="propertyForm" action="#" class="form-horizontal" role="form">
                                        <h4>Redirect User to Oauth2 Login Page</h4>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Token Type <span
                                                    class="color-red">*</span></label>

                                            <div class="col-md-9">
                                                <select name="tokenType" class="form-control" required>
                                                    <option value="JWT">JWT</option>
                                                    <option value="Bearer">Bearer</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Scope <span
                                                    class="color-red">*</span></label>

                                            <div class="col-md-9">
                                                <input name="scope" type="text" class="form-control" required>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Claim </label>

                                            <div class="col-md-9">
                                                <textarea name="claim" rows="8" class="form-control"></textarea>
                                                <p>For Bearer Token, stay empty value</p>
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


