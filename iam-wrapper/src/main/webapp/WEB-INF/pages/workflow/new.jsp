<%@ page contentType="text/html; charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="uengine" uri="http://www.uengine.io/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if !IE]><!-->
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org"
      lang="en">
<!--<![endif]-->
<head>
    <%@include file="../template/header_js.jsp" %>

    <link rel="stylesheet" href="/service-console/resources/plugins/codemirror/codemirror.css">
    <script type="text/javascript" src="/service-console/resources/plugins/codemirror/codemirror.js"></script>
    <script type="text/javascript" src="/service-console/resources/plugins/codemirror/javascript.js"></script>

    <link rel="stylesheet" href="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/css/sky-forms.css">
    <link rel="stylesheet" href="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/custom/custom-sky-forms.css">
    <!--[if lt IE 9]>
    <link rel="stylesheet" href="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/css/sky-forms-ie8.css"><![endif]-->


    <link rel="stylesheet" type="text/css" href="/service-console/resources/opengraph/contextmenu/jquery.contextMenu.css"/>
    <script type="text/javascript" src="/service-console/resources/opengraph/contextmenu/jquery.contextMenu-min.js"></script>
    <script type="text/javascript" src="/service-console/resources/opengraph/OpenGraph-0.1.1-SNAPSHOT.js"/>

</head>

<div class="wrapper">

    <%@include file="../template/header.jsp" %>

    <!--=== Breadcrumbs ===-->
    <div class="breadcrumbs">
        <div class="container">
            <h1 class="pull-left">Workflow</h1>
            <ul class="pull-right breadcrumb">
                <li><a href="/service-console/index">HOME</a></li>
                <li class="active">Workflow</li>
            </ul>
        </div>
    </div>
    <!--/breadcrumbs-->

    <!--=== Content Part ===-->
    <div class="container content profile">
        <div class="row">
            <div class="col-md-12">
                <form action="/service-console/workflow/create" class="form-horizontal"
                      role="form"
                      id="workflowForm" method="post">
                    <h4>Create New Workflow </h4>

                    <c:choose>
                        <c:when test="${failed}">
                            <h4 style="color: #ff0000">Workflow create failed. </h4>
                        </c:when>
                    </c:choose>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Name <span class="color-red">*</span></label>

                        <div class="col-md-6">
                            <input name="name" type="text" class="form-control" value="">
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-md-12">
                            <button id="submitBtn" type="submit" class="btn-u btn-u-primary">Create</button>
                            <a href="#" class="btn-u btn-u-primary" onclick="console.log($.parseXML(canvas.toXML()));">printXML</a>
                            <a href="#" class="btn-u btn-u-primary" onclick="console.log(canvas.toJSON());">printJSON</a>
                            <a href="#" class="btn-u btn-u-primary" onclick="canvas.clear();">clear</a>
                            <a href="#" class="btn-u btn-u-primary" onclick="canvasCtl.save();">saveJSON</a>
                            <a href="#" class="btn-u btn-u-primary" onclick="canvas.loadJSON(opengraphJSON);">loadJSON</a>
                            <a href="#" class="btn-u btn-u-primary" onclick="canvas.undo();">undo</a>
                            <a href="#" class="btn-u btn-u-primary" onclick="canvas.redo();">redo</a>
                        </div>
                        <div class="col-md-12 row">
                            <div class="col-md-3">
                                <div class="alert alert-info fade in margin-bottom-40">
                                    <strong>Edit Workflow</strong> Drag icons to the canvas.</a>
                                </div>
                                <div class="md-margin-bottom-30">
                                    <h4 class="heading-sm">
                                        <img src="/service-console/resources/opengraph/router/Request.png" class="icon_shape"
                                             _shape_type="GEOM" _shape_id="OG.shape.router.Request"
                                             _label="Request"
                                             _textLine="Request"
                                             _width="50" _height="50"/>
                                        <span>Request</span>
                                    </h4>
                                    <h4 class="heading-sm">
                                        <img src="/service-console/resources/opengraph/router/Authentication.png" class="icon_shape"
                                             _shape_type="GEOM" _shape_id="OG.shape.router.Authentication"
                                             _label="Authentication"
                                             _textLine="YES,NO"
                                             _width="50" _height="50"/>
                                        <span>Authentication</span>
                                    </h4>
                                    <h4 class="heading-sm">
                                        <img src="/service-console/resources/opengraph/router/Oauth.png" class="icon_shape"
                                             _shape_type="GEOM" _shape_id="OG.shape.router.Oauth"
                                             _label="Oauth2"
                                             _textLine="Succeeded,Failed"
                                             _width="50" _height="50"/>
                                        <span>Oauth2</span>
                                    </h4>
                                    <h4 class="heading-sm">
                                        <img src="/service-console/resources/opengraph/router/Response.png" class="icon_shape"
                                             _shape_type="GEOM" _shape_id="OG.shape.router.Response"
                                             _label="Response"
                                             _textLine="Response"
                                             _width="50" _height="50"/>
                                        <span>Response</span>
                                    </h4>
                                    <h4 class="heading-sm">
                                        <img src="/service-console/resources/opengraph/router/Function.svg" class="icon_shape"
                                             _shape_type="GEOM" _shape_id="OG.shape.router.Function"
                                             _label="Function"
                                             _width="50" _height="50"/>
                                        <span>Function</span>
                                    </h4>
                                    <h4 class="heading-sm">
                                        <img src="/service-console/resources/opengraph/router/Api.png" class="icon_shape"
                                             _shape_type="GEOM" _shape_id="OG.shape.router.Api"
                                             _label="Api"
                                             _textLine="Response"
                                             _width="80" _height="50"/>
                                        <span>Api</span>
                                    </h4>
                                    <h4 class="heading-sm">
                                        <img src="/service-console/resources/opengraph/router/Proxy.png" class="icon_shape"
                                             _shape_type="GEOM" _shape_id="OG.shape.router.Proxy"
                                             _label="Proxy"
                                             _textLine="Succeeded,Failed"
                                             _width="50" _height="50"/>
                                        <span>Proxy</span>
                                    </h4>
                                </div>
                            </div>
                            <div class="col-md-8">
                                <div id="canvas"></div>
                            </div>
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


<script type="text/javascript" src="/service-console/resources/opengraph/opengraph.js"></script>
<script type="text/javascript" src="/service-console/resources/opengraph/router/Request.js"></script>
<script type="text/javascript" src="/service-console/resources/opengraph/router/Authentication.js"></script>
<script type="text/javascript" src="/service-console/resources/opengraph/router/Oauth.js"></script>
<script type="text/javascript" src="/service-console/resources/opengraph/router/Response.js"></script>
<script type="text/javascript" src="/service-console/resources/opengraph/router/Function.js"></script>
<script type="text/javascript" src="/service-console/resources/opengraph/router/Api.js"></script>
<script type="text/javascript" src="/service-console/resources/opengraph/router/Proxy.js"></script>

<%@include file="./Authentication.jsp" %>
<%@include file="./Oauth2.jsp" %>
<%@include file="./Function.jsp" %>

<script type="text/javascript">
    $(function () {
        var form = $('#workflowForm');

        //폼 발리데이션
        form.validate({

            focusInvalid: false, // do not focus the last invalid input
            ignore: "",
            rules: {
                name: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "<span style='color: red;'>Required filed</span>"
                }
            },
            invalidHandler: function () {
                blockStop();
            }
        });
    });
</script>
</html>
