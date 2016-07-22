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
            <h1 class="pull-left">Simple Wrappers</h1>
            <ul class="pull-right breadcrumb">
                <li><a href="/service-console/index">HOME</a></li>
                <li class="active">Simple Wrappers</li>
            </ul>
        </div>
    </div>
    <!--/breadcrumbs-->

    <!--=== Profile ===-->
    <div class="container content profile">
        <div class="row">
            <div class="col-md-12">
                <form action="/service-console/policy/create" class="form-horizontal"
                      role="form"
                      id="policyForm" method="post">
                    <h4>Create New Simple Wrapper</h4>

                    <c:choose>
                        <c:when test="${duplicate}">
                            <h4 style="color: #ff0000">Wrapper Name already exist. </h4>
                        </c:when>
                        <c:when test="${failed}">
                            <h4 style="color: #ff0000">Simple Wrapper create failed. </h4>
                        </c:when>
                    </c:choose>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Name <span class="color-red">*</span></label>

                        <div class="col-md-6">
                            <input name="name" type="text" class="form-control" value="">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Check Authentication <span
                                class="color-red">*</span></label>

                        <div class="col-md-6">
                            <label class="checkbox"><input type="checkbox" name="authentication" value="Y"
                                                           checked>active</label>
                        </div>
                    </div>

                    <h4 name="customForm">Where is Token?</h4>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Location </label>

                        <div class="col-md-6">
                            <select name="tokenLocation" class="form-control">
                                <option value="header">Header</option>
                                <option value="parameter">GET Parameter</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Parameter Name </label>

                        <div class="col-md-6">
                            <input name="tokenName" type="text" class="form-control">
                        </div>
                    </div>

                    <h4 name="customForm">Target Option</h4>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Target URI <span
                                class="color-red">*</span></label>

                        <div class="col-md-6">
                            <input name="proxyUri" type="text" class="form-control" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Prefix URI <span
                                class="color-red">*</span></label>

                        <div class="col-md-2">
                            <input name="prefixUriFrom" type="text" class="form-control">
                        </div>
                        <div class="col-md-2" style="text-align: center">
                            Replace With
                        </div>
                        <div class="col-md-2">
                            <input name="prefixUriTo" type="text" class="form-control">
                        </div>
                        <input type="hidden" name="prefixUri">
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Before Use </label>

                        <div class="col-md-6">
                                    <textarea id="beforeUse" name="beforeUse" rows="8"
                                              class="form-control"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">After Use </label>

                        <div class="col-md-6">
                                    <textarea id="afterUse" name="afterUse" rows="8"
                                              class="form-control"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-md-offset-2 col-md-10">
                            <button id="submitBtn" type="submit" class="btn-u btn-u-primary">Create Simple Wrapper
                            </button>
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

<script type="text/javascript">
    $(function () {
        var form = $('#policyForm');

        //폼 발리데이션
        $.validator.addMethod('authCheck', function (value, element, param) {
            var authCheck = form.find('[name=authentication]').prop('checked');
            if (authCheck) {
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
                name: {
                    required: true
                },
                proxyUri: {
                    required: true
                },
                prefixUriFrom: {
                    required: true
                },
                prefixUriTo: {
                    required: true
                },
                tokenLocation: {
                    authCheck: true
                },
                tokenName: {
                    authCheck: true
                }
            },
            messages: {
                name: {
                    required: "<span style='color: red;'>Required filed</span>"
                },
                proxyUri: {
                    required: "<span style='color: red;'>Required filed</span>"
                },
                prefixUriFrom: {
                    required: "<span style='color: red;'>Required filed</span>"
                },
                prefixUriTo: {
                    required: "<span style='color: red;'>Required filed</span>"
                },
                tokenLocation: {
                    authCheck: "<span style='color: red;'>Required filed</span>"
                },
                tokenName: {
                    authCheck: "<span style='color: red;'>Required filed</span>"
                }
            },
            submitHandler: function (form, event) {
                event.preventDefault();
                var from = $('[name=prefixUriFrom]').val();
                var to = $('[name=prefixUriTo]').val();
                $(form).find('[name=prefixUri]').val(from + ',' + to);
                form.submit();
            },
            invalidHandler: function () {
                blockStop();
            }
        });
    })
</script>
</html>