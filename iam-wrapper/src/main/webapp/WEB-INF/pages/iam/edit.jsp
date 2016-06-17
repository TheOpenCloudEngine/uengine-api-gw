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

    <link rel="stylesheet" href="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/css/sky-forms.css">
    <link rel="stylesheet" href="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/custom/custom-sky-forms.css">
    <!--[if lt IE 9]>
    <link rel="stylesheet" href="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/css/sky-forms-ie8.css"><![endif]-->

    <!-- CSS Page Style -->
    <link rel="stylesheet" href="/service-console/resources/assets/css/pages/page_faq1.css">
    <link rel="stylesheet" href="/service-console/resources/assets/css/pages/page_search_inner.css">
</head>


<div class="wrapper">
    <%@include file="../template/header.jsp" %>

    <!--=== Breadcrumbs ===-->
    <div class="breadcrumbs">
        <div class="container">
            <h1 class="pull-left">Configuration</h1>
            <ul class="pull-right breadcrumb">
                <li><a href="/service-console/index">HOME</a></li>
                <li class="active">Configuration</li>
            </ul>
        </div>
    </div>
    <!--/breadcrumbs-->

    <!--=== Content Part ===-->
    <div class="container content profile">
        <div class="row">
            <div class="col-md-12">
                <form action="/service-console/iam/edit" class="form-horizontal"
                      role="form"
                      id="iamForm" method="post">
                    <h4>Edit Configuration </h4>

                    <c:choose>
                        <c:when test="${failed}">
                            <h4 style="color: #ff0000">Configuration edit failed. </h4>
                        </c:when>
                    </c:choose>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Host <span class="color-red">*</span></label>

                        <div class="col-md-6">
                            <input name="host" type="text" class="form-control" value="${iam.host}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Port <span class="color-red">*</span></label>

                        <div class="col-md-6">
                            <input name="port" type="number" class="form-control" value="${iam.port}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">managementKey <span class="color-red">*</span></label>

                        <div class="col-md-6">
                            <input name="managementKey" type="text" class="form-control" value="${iam.managementKey}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">managementSecret <span class="color-red">*</span></label>

                        <div class="col-md-6">
                            <input name="managementSecret" type="text" class="form-control" value="${iam.managementSecret}">
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-md-offset-2 col-md-10">
                            <button id="submitBtn" type="submit" class="btn-u btn-u-primary">Edit
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!--=== End Content Part ===-->

    <%@include file="../template/footer.jsp" %>
</div>
<!--/wrapper-->
<%@include file="../template/footer_js.jsp" %>

<script type="text/javascript">
    $(function () {
        var form = $('#iamForm');

        //폼 발리데이션
        form.validate({

            focusInvalid: false, // do not focus the last invalid input
            ignore: "",
            rules: {

            },
            invalidHandler: function () {
                blockStop();
            }
        });
    })
</script>
</html>
