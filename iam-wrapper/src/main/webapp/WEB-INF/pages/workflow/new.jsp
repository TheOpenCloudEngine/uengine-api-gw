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
            <h1 class="pull-left">Composite Wrappers</h1>
            <ul class="pull-right breadcrumb">
                <li><a href="/service-console/index">HOME</a></li>
                <li class="active">Composite Wrappers</li>
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
                    <h4>Create New Composite Wrapper </h4>

                    <c:choose>
                        <c:when test="${duplicate}">
                            <h4 style="color: #ff0000">Wrapper Name already exist. </h4>
                        </c:when>
                        <c:when test="${failed}">
                            <h4 style="color: #ff0000">Composite Wrapper create failed. </h4>
                        </c:when>
                    </c:choose>

                    <div class="form-group">
                        <label class="col-md-2 control-label">Name <span class="color-red">*</span></label>

                        <div class="col-md-6">
                            <input name="name" type="text" class="form-control" value="">
                        </div>
                    </div>

                    <input type="hidden" name="designer_xml">

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
                                    <c:forEach items="${shapesProvidedList}" var="shape" varStatus="i">
                                        <h4 class="heading-sm">
                                            <img src="${shape.img}" class="icon_shape"
                                                 _shape_type="${shape.shapeType}"
                                                 _shape_id="${shape.shapeId}"
                                                 _label="${shape.label}"
                                                 _textLine="${shape.textLine}"
                                                 _width="${shape.width}"
                                                 _height="${shape.height}"/>
                                            <span>${shape.label}</span>
                                        </h4>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="col-md-9">
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
<%@include file="./Response.jsp" %>
<%@include file="./Api.jsp" %>
<%@include file="./Proxy.jsp" %>

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
            },
            submitHandler: function (form, event) {
                event.preventDefault();
                canvasCtl.onSaveClick(form);
            }
        });

        canvasCtl.init();
    });
</script>
</html>
