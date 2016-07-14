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

    <script src="https://cdn.datatables.net/1.10.11/js/jquery.dataTables.min.js"></script>
    <link href="https://cdn.datatables.net/1.10.11/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css"/>

    <!-- CSS Page Style -->
    <link rel="stylesheet" href="/service-console/resources/assets/css/pages/profile.css">

    <script type="text/javascript">
        function search(table, searchValue) {
            if (event.keyCode == 13) {
                reload(table, searchValue);
            }
        }

        function reload(table, searchValue) {
            // limit and skip setting
            var tableAPI = table.api();
            var limit = tableAPI.settings()[0]._iDisplayLength
            var skip = tableAPI.settings()[0]._iDisplayStart;

            tableAPI.ajax.url('/service-console/analysis/list?limit=' + limit + '&skip=' + skip + '&uri=' + searchValue);
            tableAPI.ajax.reload();
        }

        $(document).ready(function () {
            var table = $('#analysis').DataTable({
                serverSide: true,
                searching: false,
                ajax: {
                    url: '/service-console/analysis/list',
                    dataSrc: function (transactionHistories) {
                        // change init page setting (_iDisplayStart )
                        table.settings()[0]._iDisplayStart = transactionHistories.displayStart;
                        // make id edit href
                        for (var i = 0; i < transactionHistories.data.length; i++) {
                            transactionHistories.data[i]._id = '<a href=/service-console/analysis/info?_id=' + transactionHistories.data[i]._id + '>Detail</a>';
                            if (transactionHistories.data[i].runWith == 'workflow') {
                                transactionHistories.data[i].runWith = '' +
                                        '<button class="btn btn-xs rounded btn-primary">Composite Wrapper</button>  ' +
                                        '<a href=/service-console/workflow-edit?_id=' + transactionHistories.data[i].wid + '>' +
                                        transactionHistories.data[i].workflowName + '</a>';
                            }
                            if (transactionHistories.data[i].runWith == 'class') {
                                transactionHistories.data[i].runWith = '' +
                                        '<button class="btn btn-xs rounded btn-primary">Class Wrapper</button>  ' +
                                        resourceUris.data[i].className
                            }
                            if (transactionHistories.data[i].runWith == 'policy') {
                                transactionHistories.data[i].runWith = '' +
                                        '<button class="btn btn-xs rounded btn-primary">Simple Wrapper</button>  ' +
                                        '<a href=/service-console/policy/edit?_id=' + transactionHistories.data[i].policyId + '>' +
                                        transactionHistories.data[i].policyName + '</a>';
                            }

                            if (transactionHistories.data[i].status == 'FAILED') {
                                transactionHistories.data[i].status = '' +
                                        '<button class="btn btn-xs rounded btn-danger">'+transactionHistories.data[i].status+'</button>'
                            }else{
                                transactionHistories.data[i].status = '' +
                                        '<button class="btn btn-xs rounded btn-primary">'+transactionHistories.data[i].status+'</button>'
                            }
                        }
                        return transactionHistories.data;
                    }
                },
                columns: [
                    {data: 'uri'},
                    {data: 'method'},
                    {data: 'status'},
                    {data: 'humanStartDate'},
                    {data: 'humanDuration'},
                    {data: 'runWith'},
                    {data: '_id'}
                ]
            });

            // page event
            $('#analysis').on('page.dt', function () {
                reload($('#analysis').dataTable(), $('#customSearch').val().trim());

                // page length event
            }).on('length.dt', function () {
                reload($('#analysis').dataTable(), $('#customSearch').val().trim());

            });
        });
    </script>

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
                <div class="headline margin-bottom-10"><h4>Transactions Logs </h4></div>

                <div class="margin-bottom-10">
                    <div class="table-responsive">
                        <div style="float: right"> Search : <input type="text" id="customSearch"
                                                                   onKeyDown="javascript: search($('#analysis').dataTable(), this.value)"/>
                        </div>
                        <table id="analysis" class="display table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th>Uri</th>
                                <th>Method</th>
                                <th>Status</th>
                                <th>Started</th>
                                <th>Duration</th>
                                <th>RunWith</th>
                                <th>Detail</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
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