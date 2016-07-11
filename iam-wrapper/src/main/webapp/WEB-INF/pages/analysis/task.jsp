<%@ page contentType="text/html; charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<div id="taskHistory" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="myLargeModalLabel"
     aria-hidden="true">
    <%--modal-lg--%>
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                <h4 class="modal-title" name="title">Task - </h4>
            </div>
            <div class="modal-body">
                <!-- Tab v2 -->
                <div class="tab-v2">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#task-1" data-toggle="tab">Info</a></li>
                        <li class=""><a href="#task-2" data-toggle="tab">InputData</a></li>
                        <li class=""><a href="#task-3" data-toggle="tab">OutputData</a></li>
                        <li class=""><a href="#task-4" data-toggle="tab">Stdout</a></li>
                        <li class=""><a href="#task-5" data-toggle="tab">Stderr</a></li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active" id="task-1">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-horizontal">
                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Task Name</label>

                                            <div class="col-md-9">
                                                <div class="form-control" name="taskName"></div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Status</label>

                                            <div class="col-md-9">
                                                <div class="form-control" name="status"></div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Started</label>

                                            <div class="col-md-9">
                                                <div class="form-control" name="humanStartDate"></div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-md-3 control-label">Duration</label>

                                            <div class="col-md-9">
                                                <div class="form-control" name="humanDuration"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane fade in" id="task-2">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-horizontal">
                                        <div class="col-md-12">
                                                <textarea name="input" rows="12"
                                                          class="form-control" readonly></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane fade in" id="task-3">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-horizontal">
                                        <div class="col-md-12">
                                                <textarea name="output" rows="12"
                                                          class="form-control" readonly></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane fade in" id="task-4">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-horizontal">
                                        <div class="col-md-12">
                                                <textarea name="stdout" rows="12"
                                                          class="form-control" readonly></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane fade in" id="task-5">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-horizontal">
                                        <div class="col-md-12">
                                                <textarea name="stderr" rows="12"
                                                          class="form-control" readonly></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <a href="#" name="close" class="btn-u btn-u-primary pull-right">Close</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


