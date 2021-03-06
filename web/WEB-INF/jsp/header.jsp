<!DOCTYPE html>
<html lang="en" class="no-js">
    <!--<![endif]-->
    <!-- BEGIN HEAD -->
    <head>
        <%@ page contentType="text/html; charset=UTF-8" %>
        <meta charset="utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta content="width=device-width, initial-scale=1" name="viewport"/>
        <meta content="" name="description"/>
        <meta content="Faraz Ahmad" name="author"/>
        <meta http-equiv="cache-control" content="no-cache"/>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
        <!-- BEGIN GLOBAL MANDATORY STYLES -->
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
        <link href="assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css"/>
        <!-- END GLOBAL MANDATORY STYLES -->
        <link rel="stylesheet" type="text/css" href="assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css"/>
        <!-- DOC: To use 'rounded corners' style just load 'components-rounded.css' stylesheet instead of 'components.css' in the below style tag -->
        <link href="assets/global/css/components-md.css" id="style_components" rel="stylesheet" type="text/css"/>
        <link href="assets/global/css/plugins-md.css" rel="stylesheet" type="text/css"/>
        <link href="assets/admin/layout4/css/layout.css" rel="stylesheet" type="text/css"/>
        <link href="assets/admin/layout4/css/themes/light.css" rel="stylesheet" type="text/css" id="style_color"/>
        <link rel="stylesheet" type="text/css" href="assets/global/plugins/icheck/skins/all.css">
        <link rel="stylesheet" type="text/css" href="assets/global/plugins/select2/select2.css"/>
        <!-- END THEME STYLES -->
        <title>Ezimedic</title>
        <link rel="shortcut icon" href="manifest/favicon.ico" type="image/x-icon">
        <link rel="icon" href="manifest/favicon.ico" type="image/x-icon">
        <link rel="manifest" href="manifest/manifest.json">
        <meta name="msapplication-TileColor" content="#ffffff">
        <meta name="msapplication-TileImage" content="manifest/ms-icon-144x144.png">
        <meta name="theme-color" content="#ffffff">
        <meta name="description" content="Treat Well Services">

        <script src="assets/global/plugins/jquery.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
        <!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
        <script src="assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/jquery.cokie.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/moment.min.js" type="text/javascript"></script>
        <script src="assets/global/scripts/metronic.js" type="text/javascript"></script>
        <script src="assets/admin/layout4/scripts/layout.js" type="text/javascript"></script>
        <script src="assets/admin/layout4/scripts/quick-sidebar.js" type="text/javascript"></script>
        <script src="assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
        <script src="assets/global/plugins/icheck/icheck.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
        <script type="text/javascript" src="assets/global/plugins/select2/select2.min.js"></script>
        <script type="text/javascript" src="js/notify.min.js"></script>
        <script type="text/javascript" src="js/script.js"></script>
        <script src="assets/global/plugins/bootstrap-growl/jquery.bootstrap-growl.min.js"></script>
        <script src="assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
        <script src="https://code.highcharts.com/highcharts.js"></script>
        <script src="https://code.highcharts.com/modules/exporting.js"></script>
        <script>
            jQuery(document).ready(function () {
                Metronic.init(); // init metronic core components
                Layout.init(); // init current layout

                var rightName = $.trim($('#right_nme').val());
                if (rightName !== '') {
                    var li = $('.page-sidebar-menu').find('li');
                    $.each(li, function (key, value) {
                        var ul = $(value).find('.sub-menu').find('li');
                        $.each(ul, function (i, val) {
                            var href = $.trim($(val).find('a').text());
                            if (rightName === href) {
                                $(val).addClass('active');
                                $(value).addClass('active');
                            }
                        });
                    });
                }
            });
        </script>
        <style>
            .table-danger {
                background-color: #F3565D !important;
            }
            .danger {
                color: #F3565D !important;
            }
            .table-danger > a {
                color: #FFFFFF !important;
            }
        </style>
    </head>
    <body class="page-md page-header-fixed page-sidebar-closed-hide-logo page-sidebar-fixed page-sidebar-closed-hide-logo">
        <div class="page-header md-shadow-none navbar  navbar-fixed-top">
            <input type="hidden" id="right_nme" value="${requestScope.refData.rightName}">
            <!-- BEGIN HEADER INNER -->
            <div class="page-header-inner">
                <!-- BEGIN LOGO -->
                <div class="page-logo">
                    <a href="#">
                        <img src="images/ezimedic_black.png"  alt="Ezimedic" style="height: 50px;width: 160px;margin-top: 15px;" class="logo-default"/>
                    </a>
                    <div class="menu-toggler sidebar-toggler">
                        <!-- DOC: Remove the above "hide" to enable the sidebar toggler button on header -->
                    </div>
                </div>
                <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
                </a>
                <!-- END LOGO -->
                <div class="page-actions">
                    <c:choose>
                        <c:when test="${sessionScope.userType == 'DOCTOR' || sessionScope.userType == 'ADMIN'}">
                            <div class="btn-group">
                                <form  action="setup.htm?action=addPatient&addNewPatient=Y" method="post">
                                    <button type="submit" class="btn red-haze btn-sm "  >
                                        <i class="fa fa-plus-square"></i> <span class="hidden-sm hidden-xs">Patient</span>
                                    </button>
                                </form>
                            </div>
                        </c:when>
                    </c:choose>
                    <!--div class="btn-group">
                        <form  action="setup.htm?action=addPatient" method="post">
                            <button type="submit" class="btn green btn-sm "  >
                                <i class="fa fa-list-ul"></i> <span class="hidden-sm hidden-xs"> Intake Form&nbsp;</span>
                            </button>
                        </form>
                    </div>
                    <div class="btn-group">
                        <form  action="setup.htm?action=saleHealthCards" method="post">
                            <button type="submit" class="btn blue btn-sm "  >
                                <i class="fa fa-address-card"></i> <span class="hidden-sm hidden-xs"> Health Card&nbsp;</span>
                            </button>
                        </form>
                    </div-->
                </div>
                <!-- BEGIN PAGE TOP -->
                <div class="page-top">
                    <form class="search-form" action="setup.htm?action=addPatient" method="post">
                        <div class="input-group">
                            <input type="text" class="form-control input-sm" placeholder="Search patient by name" name="searchTopPatient">
                            <span class="input-group-btn">
                                <a href="javascript:;" class="btn submit"><i class="icon-magnifier"></i></a>
                            </span>
                        </div>
                    </form>
                    <!-- BEGIN TOP NAVIGATION MENU -->
                    <div class="top-menu">

                        <ul class="nav navbar-nav pull-right">
                            <li class="separator hide">
                            </li>
                            <!--li class="dropdown dropdown-extended dropdown-notification dropdown-dark" id="header_notification_bar">
                                <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                                    <i class="icon-bell"></i>
                                    <span class="badge badge-success">
                                        7 </span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li class="external">
                                        <h3><span class="bold">12 pending</span> notifications</h3>
                                        <a href="extra_profile.html">view all</a>
                                    </li>
                                    <li>
                                        <ul class="dropdown-menu-list scroller" style="height: 250px;" data-handle-color="#637283">
                                            <li>
                                                <a href="javascript:;">
                                                    <span class="time">just now</span>
                                                    <span class="details">
                                                        <span class="label label-sm label-icon label-success">
                                                            <i class="fa fa-plus"></i>
                                                        </span>
                                                        New user registered. </span>
                                                </a>
                                            </li>
                                        </ul>
                                    </li>
                                </ul>
                            </li-->
                            <!--li class="separator hide">
                            </li>
                            <li class="dropdown dropdown-extended dropdown-inbox dropdown-dark" id="header_inbox_bar">
                                <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                                    <i class="icon-envelope-open"></i>
                                    <span class="badge badge-danger">
                                        4 </span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li class="external">
                                        <h3>You have <span class="bold">7 New</span> Messages</h3>
                                        <a href="inbox.html">view all</a>
                                    </li>
                                    <li>
                                        <ul class="dropdown-menu-list scroller" style="height: 275px;" data-handle-color="#637283">
                                            <li>
                                                <a href="inbox.html?a=view">
                                                    <span class="photo">
                                                        <img src="assets/admin/layout3/img/avatar2.jpg" class="img-circle" alt="">
                                                    </span>
                                                    <span class="subject">
                                                        <span class="from">
                                                            Lisa Wong </span>
                                                        <span class="time">Just Now </span>
                                                    </span>
                                                    <span class="message">
                                                        Vivamus sed auctor nibh congue nibh. auctor nibh auctor nibh... </span>
                                                </a>
                                            </li>
                                        </ul>
                                    </li>
                                </ul>
                            </li-->
                            <!--li class="separator hide">
                            </li>
                            <li class="dropdown dropdown-extended dropdown-tasks dropdown-dark" id="header_task_bar">
                                <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                                    <i class="icon-calendar"></i>
                                    <span class="badge badge-primary">
                                        3 </span>
                                </a>
                                <ul class="dropdown-menu extended tasks">
                                    <li class="external">
                                        <h3>You have <span class="bold">12 pending</span> tasks</h3>
                                        <a href="page_todo.html">view all</a>
                                    </li>
                                    <li>
                                        <ul class="dropdown-menu-list scroller" style="height: 275px;" data-handle-color="#637283">
                                            <li>
                                                <a href="javascript:;">
                                                    <span class="task">
                                                        <span class="desc">New release v1.2 </span>
                                                        <span class="percent">30%</span>
                                                    </span>
                                                    <span class="progress">
                                                        <span style="width: 40%;" class="progress-bar progress-bar-success" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100"><span class="sr-only">40% Complete</span></span>
                                                    </span>
                                                </a>
                                            </li>
                                        </ul>
                                    </li>
                                </ul>
                            </li-->
                            <li class="dropdown dropdown-user dropdown-dark">
                                <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                                    <span class="username username-hide-on-mobile">
                                        ${sessionScope.user.firstName} </span>
                                    <!-- DOC: Do not remove below empty space(&nbsp;) as its purposely used -->
                                    <img alt="" class="img-circle" src="assets/admin/layout4/img/avatar.png"/>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-default">
                                    <li>
                                        <a href="login.htm?action=changePassword">
                                            <i class="icon-lock"></i> Change Password </a>
                                    </li>
                                    <li>
                                        <a href="login.htm?action=processSignOut">
                                            <i class="icon-key"></i> Log Out </a>
                                    </li>
                                </ul>
                            </li>
                            <li class="dropdown dropdown-extended quick-sidebar-toggler">
                                <span class="sr-only">Toggle Quick Sidebar</span>
                                <i class="icon-logout"></i>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <!-- END HEADER -->
        <div class="clearfix"></div>
        <div class="page-container">
            <div class="page-sidebar-wrapper">
                <div class="page-sidebar md-shadow-z-2-i  navbar-collapse collapse">
                    <ul class="page-sidebar-menu " data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">
                        <c:if test="${sessionScope.selectedClinic !=null}">
                            <li class="start bg-danger">
                                <a href="login.htm?action=selectClinic">
                                    <i class="fa fa-hospital-o"></i>
                                    <span class="title">${sessionScope.selectedClinic.CLINIC_NME}</span>
                                </a>
                            </li>
                        </c:if>
                        <li >
                            <a href="login.htm?action=viewDashBoard">
                                <i class="icon-home"></i>
                                <span class="title">Dashboard</span>
                            </a>
                        </li>
                        <c:forEach items="${sessionScope.parentMenu}" var="parent">
                            <li >
                                <a href="javascript:;">
                                    <i class="${parent.iconeName}"></i>
                                    <span class="title">${parent.rightText}</span>
                                    <span class="arrow "></span>
                                </a>
                                <ul class="sub-menu">
                                    <c:forEach items="${sessionScope.childMenu}" var="child">
                                        <c:if test="${parent.rightId==child.parentId}">
                                            <li>
                                                <a href="${child.targetUrl}">
                                                    <i class="fa fa-angle-double-right"></i>
                                                    ${child.rightText}</a>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <!-- END SIDEBAR -->
            <!-- BEGIN CONTENT -->
            <div class="page-content-wrapper">
                <div class="page-content">
                    <!-- BEGIN PAGE HEAD -->


