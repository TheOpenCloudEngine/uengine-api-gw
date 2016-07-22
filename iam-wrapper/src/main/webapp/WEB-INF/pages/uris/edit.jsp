<%@ page contentType="text/html; charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="uengine" uri="http://www.uengine.io/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if !IE]><!-->
<html xmlns="http://www.w3.org/1999/xhtml"
      lang="en">
<!--<![endif]-->
<head>
    <%@include file="../template/header_js.jsp" %>

    <!-- CSS Page Style -->
    <link rel="stylesheet" href="/service-console/resources/assets/css/pages/profile.css">

    <link rel="stylesheet" href="/service-console/resources/plugins/select2/select2.css">
    <script type="text/javascript" src="/service-console/resources/plugins/select2/select2.js"/>
</head>


<div class="wrapper">
    <%@include file="../template/header.jsp" %>

    <!--=== Breadcrumbs ===-->
    <div class="breadcrumbs">
        <div class="container">
            <h1 class="pull-left">URI Mapping</h1>
            <ul class="pull-right breadcrumb">
                <li><a href="/service-console/index">HOME</a></li>
                <li class="active">URI Mapping</li>
            </ul>
        </div>
    </div>
    <!--/breadcrumbs-->

    <!--=== Profile ===-->
    <div class="container content profile">
        <div class="row">
            <div class="col-md-12">
                <form action="/service-console/uris/update" class="form-horizontal"
                      role="form"
                      id="resourceUriForm" method="post">
                    <h4>Edit URI Mapping </h4>

                    <c:choose>
                        <c:when test="${duplicate}">
                            <h4 style="color: #ff0000">URI Mapping Order already exist. </h4>
                        </c:when>
                        <c:when test="${failed}">
                            <h4 style="color: #ff0000">URI Mapping create failed. </h4>
                        </c:when>
                    </c:choose>

                    <input type="hidden" id="_id" name="_id" value="${resourceUri._id}">

                    <div class="form-group">
                        <label class="col-md-2 control-label">Order <span class="color-red">*</span></label>

                        <div class="col-md-6">
                            <input name="order" type="text" class="form-control" value="${resourceUri.order}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Uri <span class="color-red">*</span></label>

                        <div class="col-md-6">
                            <input name="uri" type="text" class="form-control" value="${resourceUri.uri}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Method <span
                                class="color-red">*</span></label>

                        <div class="col-md-6">
                            <select name="method" class="form-control" multiple="multiple">
                                <option value="GET">GET</option>
                                <option value="POST">POST</option>
                                <option value="PUT">PUT</option>
                                <option value="DELETE">DELETE</option>
                                <option value="HEAD">HEAD</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Run with <span
                                class="color-red">*</span></label>

                        <div class="col-md-6">
                            <select name="runWith" class="form-control">
                                <option value="class"
                                        <c:if test="${resourceUri.runWith == 'class'}">selected</c:if>>Class Wrapper
                                </option>
                                <option value="policy"
                                        <c:if test="${resourceUri.runWith == 'policy'}">selected</c:if>>Simple Wrapper
                                </option>
                                <option value="workflow"
                                        <c:if test="${resourceUri.runWith == 'workflow'}">selected</c:if>>Composite Wrapper
                                </option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group" id="classNameForm">
                        <label class="col-md-2 control-label">Class Wrapper <span class="color-red">*</span></label>

                        <div class="col-md-6">
                            <input name="className" type="text" class="form-control" value="${resourceUri.className}">
                        </div>
                    </div>

                    <div class="form-group" id="policyForm">
                        <label class="col-md-2 control-label">Simple Wrapper <span
                                class="color-red">*</span></label>

                        <div class="col-md-6">
                            <select name="policyId" class="form-control">
                                <c:forEach items="${policies}" var="policy" varStatus="status">
                                    <option value="${policy._id}"
                                            <c:if test="${resourceUri.policyId == policy._id}">selected</c:if>
                                            >${policy.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group" id="widForm">
                        <label class="col-md-2 control-label">Composite Wrapper <span
                                class="color-red">*</span></label>

                        <div class="col-md-6">
                            <select name="wid" class="form-control">
                                <c:forEach items="${workflows}" var="workflow" varStatus="status">
                                    <option value="${workflow._id}"
                                            <c:if test="${resourceUri.wid == workflow._id}">selected</c:if>
                                            >${workflow.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-md-offset-2 col-md-10">
                            <button id="submitBtn" type="submit" class="btn-u btn-u-primary">Edit
                            </button>

                            <a class="btn-u btn-u-primary" id="deleteBtn">Delete </a>
                        </div>
                    </div>
                </form>
            </div>
            <!-- End Profile Content -->

        </div>
        <!--/end row-->
    </div>
    <!--=== End Profile ===-->

    <%@include file="../template/footer.jsp" %>
</div>
<!--/wrapper-->

<%@include file="../template/footer_js.jsp" %>

<div class="modal fade" id="deleteConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                <h4 class="modal-title">Caution</h4>
            </div>
            <div class="modal-body">
                <p style="text-align: center" name="content">Are you sure delete uri mapping?</p>
            </div>
            <div class="modal-footer">
                <button class="btn-u" type="button" name="delete">Delete</button>
                <button class="btn-u" type="button" name="close">Cancle</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function () {
        $('#deleteBtn').click(function () {
            $('#deleteConfirm').modal({
                show: true
            });
        });

        $('#deleteConfirm').find('[name=close]').click(function () {
            $('#deleteConfirm').find('.close').click();
        });

        $('#deleteConfirm').find('[name=delete]').click(function () {
            window.location.href = '/service-console/uris/delete?_id=' + $('#_id').val();
        });

        var form = $('#resourceUriForm');

        var methodOptions = form.find('[name=method]').find('option');
        var method = '${resourceUri.method}'
        var methods = method.split(',');
        methodOptions.each(function () {
            for (var i = 0; i < methods.length; i++) {
                if ($(this).val() === methods[i]) {
                    $(this).prop('selected', true);
                }
            }
        });

        form.find('[name=method]').select2();

        form.find('[name=runWith]').change(function () {
            switchRunWith();
        });

        var switchRunWith = function () {
            var runWith = form.find('[name=runWith]').val();
            var classNameForm = $('#classNameForm');
            var widForm = $('#widForm');
            var policyForm = $('#policyForm');
            if (runWith === 'class') {
                classNameForm.show();
                widForm.hide();
                policyForm.hide();
            }
            else if (runWith === 'policy') {
                classNameForm.hide();
                policyForm.show();
                widForm.hide();
            }
            else {
                classNameForm.hide();
                policyForm.hide();
                widForm.show();
            }
        };
        switchRunWith();

        //폼 발리데이션
        $.validator.addMethod('classCheck', function (value, element, param) {
            var runWith = form.find('[name=runWith]').val();
            if (runWith === 'class') {
                if (!value) {
                    return false;
                }
                if (value.length < 1) {
                    return false;
                }
            }
            return true;
        });
        form.validate({

            focusInvalid: false, // do not focus the last invalid input
            ignore: "",
            rules: {
                order: {
                    required: true
                },
                uri: {
                    required: true
                },
                method: {
                    required: true
                },
                runWith: {
                    required: true
                },
                className: {
                    classCheck: true
                }
            },
            messages: {
                order: {
                    required: "<span style='color: red;'>Required filed</span>"
                },
                uri: {
                    required: "<span style='color: red;'>Required filed</span>"
                },
                method: {
                    required: "<span style='color: red;'>Required filed</span>"
                },
                runWith: {
                    required: "<span style='color: red;'>Required filed</span>"
                },
                className: {
                    classCheck: "<span style='color: red;'>Required filed</span>"
                }
            },
            invalidHandler: function () {
                blockStop();
            }
        });
    })
</script>
</html>