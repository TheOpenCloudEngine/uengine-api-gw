<%@ page contentType="text/html; charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<div id="api" name="propertyWindow" class="modal fade" tabindex="-1" role="dialog"
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
                        <li class="active"><a href="#api-1" data-toggle="tab">Label</a></li>
                        <li class=""><a href="#api-2" data-toggle="tab">Embedded</a></li>
                        <li class=""><a href="#api-3" data-toggle="tab" name="scriptTab">Script</a></li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active" id="api-1">
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
                        <div class="tab-pane fade in" id="api-2">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="row content-boxes-v2 margin-bottom-50">
                                        <h4>These objects may be used in the script. </h4>

                                        <div class="col-md-6 md-margin-bottom-30">
                                            <h2 class="heading-sm">
                                                <i class="icon-custom rounded icon-color-dark fa fa-lightbulb-o"></i>
                                                <span>Request</span>
                                            </h2>

                                            <p>The original request</p>

                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Name</th>
                                                    <th>Type</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>uri</td>
                                                    <td>String</td>
                                                </tr>
                                                <tr>
                                                    <td>method</td>
                                                    <td>String</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane fade in" id="api-3">
                            <div class="row">
                                <div class="col-md-12">
                                    <form name="propertyForm" action="#" class="form-horizontal" role="form">
                                        <div class="row content-boxes-v2">
                                            <div class="col-md-12 md-margin-bottom-10">
                                                <h4>This Script must return HttpRequest </h4>

                                                <p>return HttpRequest(uri,method,headers,body)</p>

                                                <table class="table">
                                                    <thead>
                                                    <tr>
                                                        <th>Name</th>
                                                        <th>Type</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr>
                                                        <td>uri</td>
                                                        <td>String</td>
                                                    </tr>
                                                    <tr>
                                                        <td>method</td>
                                                        <td>String</td>
                                                    </tr>
                                                    <tr>
                                                        <td>headers</td>
                                                        <td>Map( key, value )</td>
                                                    </tr>
                                                    <tr>
                                                        <td>body</td>
                                                        <td>String</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                                <textarea name="script" rows="12"
                                                          class="form-control"></textarea>
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


