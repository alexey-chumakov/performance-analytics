<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <script type="text/javascript">
        var _contextPath = "${pageContext.request.contextPath}";
    </script>


    <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.2/angular.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.2/angular-route.js"></script>

    <script src="js/angular/app.js"></script>
    <script src="js/angular/directives.js"></script>
    <script src="js/angular/filters.js"></script>
    <script src="js/angular/services.js"></script>

    <script src="js/angular/request/request.js"></script>
    <script src="js/angular/request/controllers.js"></script>
    <script src="js/angular/request/services.js"></script>

    <script src="js/angular/duration/duration.js"></script>
    <script src="js/angular/duration/controllers.js"></script>
    <script src="js/angular/duration/services.js"></script>

    <script src="js/angular/alerts/alerts.js"></script>
    <script src="js/angular/alerts/controllers.js"></script>
    <script src="js/angular/alerts/services.js"></script>

    <script src="js/angular/url-details/url-details.js"></script>
    <script src="js/angular/url-details/controllers.js"></script>
    <script src="js/angular/url-details/services.js"></script>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Performance analytics</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Bootstrap DateRangePicker CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.14.30/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" type="text/css" href="//cdn.jsdelivr.net/bootstrap.daterangepicker/1/daterangepicker-bs3.css" />

    <!-- Custom CSS -->
    <link href="css/sb-admin.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="css/plugins/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body ng-app="PerformanceAnalytics">

<div id="wrapper">

    <!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#/">Performance Analytics</a>
            <div style="padding-top: 8px" panel></div>
        </div>



        <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav side-nav">
                <li class="">
                    <a href="#/duration"><i class="fa fa-fw fa-bar-chart-o"></i> Applications</a>
                </li>
                <li class="">
                    <a href="#/request"><i class="fa fa-fw fa-star"></i> Application requests</a>
                </li>
                <li class="">
                    <a href="#/alerts"><i class="fa fa-fw fa-bell"></i> Alerts</a>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </nav>

    <div id="page-wrapper">

        <div class="container-fluid">

            <div id="main">
                <div ng-view></div>
            </div>

        </div>
        <!-- /.container-fluid -->

    </div>
    <!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->


<!-- jQuery -->
<script src="js/lib/jquery.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.3/moment.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="js/lib/bootstrap.min.js"></script>

<!-- Morris Charts JavaScript -->
<script src="js/lib/plugins/morris/raphael.min.js"></script>
<script src="js/lib/plugins/morris/morris.js"></script>
<script src="js/lib/plugins/morris/morris-data.js"></script>

<!-- DateRangePicker JavaScript -->
<script type="text/javascript" src="//cdn.jsdelivr.net/bootstrap.daterangepicker/1/daterangepicker.js"></script>
</body>
</html>
