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
</head>


<div class="wrapper">
    <%@include file="../template/header.jsp" %>

    <!--=== Breadcrumbs ===-->
    <div class="breadcrumbs">
        <div class="container">
            <h1 class="pull-left">Transactions Logs</h1>
            <ul class="pull-right breadcrumb">
                <li><a href="/service-console/index">HOME</a></li>
                <li class="active">Transactions Logs</li>
            </ul>
        </div>
    </div>
    <!--/breadcrumbs-->

    <!--=== Profile ===-->
    <div class="container content profile">
        <div class="row">
            <div class="col-md-12">
                <div class="headline margin-bottom-10"><h4>Transactions tasks </h4></div>

                <div class="margin-bottom-10">
                    <div class="table-responsive">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>Task Name</th>
                                <th>Status</th>
                                <th>Started</th>
                                <th>Duration</th>
                                <th>Detail</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${taskHistories}" var="taskHistory">
                                <tr>
                                    <td>${taskHistory.taskName}</td>
                                    <c:choose>
                                        <c:when test="${taskHistory.status == 'FAILED'}">
                                            <td>
                                                <button class="btn btn-xs rounded btn-danger">${taskHistory.status}</button>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>
                                                <button class="btn btn-xs rounded btn-primary">${taskHistory.status}</button>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td>${taskHistory.humanStartDate}</td>
                                    <td>${taskHistory.humanDuration}</td>
                                    <td>
                                        <button class="btn btn-xs rounded btn-default"
                                                onclick="showDetail('${taskHistory.taskName}')">Detail
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
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
</html>

<%@include file="./task.jsp" %>
<script>
    var taskHistories = [];
    var getTaskHistoryByName = function(taskName){
        var taskHistory;
        $.each(taskHistories, function(i, task){
           if(task['taskName'] == taskName){
               taskHistory = task;
           }
        });
        return taskHistory;
    };
    var showDetail = function (taskName) {
        var taskHistory = getTaskHistoryByName(taskName);
        if(!taskHistory){
            return;
        }
        var modal = $('#taskHistory');
        modal.find('[name=title]').html('Task - ' + taskName);
        modal.find('[name=taskName]').html(taskHistory.taskName);
        modal.find('[name=status]').html(taskHistory.status);
        modal.find('[name=humanStartDate]').html(taskHistory.humanStartDate);
        modal.find('[name=humanDuration]').html(taskHistory.humanDuration);

        if(taskHistory.input){
            modal.find('[name=input]').html(JSON.stringify(JSON.parse(taskHistory.input), null, 2));
        }
        if(taskHistory.output){
            modal.find('[name=output]').html(JSON.stringify(JSON.parse(taskHistory.output), null, 2));
        }
        modal.find('[name=stdout]').html(taskHistory.stdout);
        modal.find('[name=stderr]').html(taskHistory.stderr);
        modal.modal('show');
    };
    $(function () {
        var url = '/service-console/analysis/task/list?identifier=' + '${transactionHistory.identifier}';
        $.ajax({
            type: "get",
            url: url,
            dataType: "json",
            async: false,
            success: function (result) {
                taskHistories = result;
            },
            error: function (e) {
                console.log(e.responseText);
            }
        });
    })
</script>