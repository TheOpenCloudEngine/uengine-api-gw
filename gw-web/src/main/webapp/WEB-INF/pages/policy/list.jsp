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

            tableAPI.ajax.url('/service-console/policy/list?limit=' + limit + '&skip=' + skip + '&name=' + searchValue);
            tableAPI.ajax.reload();
        }

        $(document).ready(function () {
            var table = $('#policy').DataTable({
                serverSide: true,
                searching: false,
                ajax: {
                    url: '/service-console/policy/list',
                    dataSrc: function (policies) {
                        // change init page setting (_iDisplayStart )
                        table.settings()[0]._iDisplayStart = policies.displayStart;

                        // make id edit href
                        for (var i = 0; i < policies.data.length; i++) {
                            policies.data[i].authentication = policies.data[i].authentication ? 'Y' : 'N';
                            policies.data[i]._id = '<a href=/service-console/policy/edit?_id=' + policies.data[i]._id + '>Edit</a>';
                            var prefixUri = policies.data[i].prefixUri;
                            var prefixUris = prefixUri.split(',');
                            var from = '', to = '';
                            if (prefixUris[0]) {
                                from = prefixUris[0];
                            }
                            if (prefixUris[1]) {
                                to = prefixUris[1];
                            }
                            policies.data[i].prefixUri = '"' + from + '"  to  "' + to + '"';
                        }
                        return policies.data;
                    }
                },
                columns: [
                    {data: 'name'},
                    {data: 'authentication'},
                    {data: 'proxyUri'},
                    {data: 'prefixUri'},
                    {data: '_id'}
                ]
            });

            // page event
            $('#policy').on('page.dt', function () {
                reload($('#policy').dataTable(), $('#customSearch').val().trim());

                // page length event
            }).on('length.dt', function () {
                reload($('#policy').dataTable(), $('#customSearch').val().trim());

            });
        });
    </script>

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
                <div class="headline margin-bottom-10"><h4>Simple Wrapper </h4></div>

                <a class="btn-u btn-u-primary" href="/service-console/policy/new">Create Simple Wrapper</a>

                <br>
                <br>

                <div class="margin-bottom-10">
                    <div class="table-responsive">
                        <div style="float: right"> Search : <input type="text" id="customSearch"
                                                                   onKeyDown="javascript: search($('#policy').dataTable(), this.value)"/>
                        </div>
                        <table id="policy" class="display table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Authentication</th>
                                <th>ProxyUri</th>
                                <th>PrefixUri</th>
                                <th></th>
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