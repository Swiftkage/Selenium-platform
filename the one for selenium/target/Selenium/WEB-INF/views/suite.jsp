<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">

<script src="/Selenium/static/js/ui.js"></script>
<script src="/Selenium/static/js/expandcollapse.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js" type="text/javascript"></script>
<script src="/Selenium/static/js/jquery.growl.js" type="text/javascript"></script>
<script src="/Selenium/static/js/doc.js"></script>


<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Selenium scheduler portal</title>

    <link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
    <link href="<c:url value='/static/css/layouts/table.css' />" rel="stylesheet">
    <link href="<c:url value='/static/css/layouts/marketing-old-ie.css' />" rel="stylesheet">
    <link href="<c:url value='/static/css/layouts/marketing.css' />" rel="stylesheet">
    <link href="<c:url value='/static/css/layouts/side-menu-old-ie.css' />" rel="stylesheet">
    <link href="<c:url value='/static/css/layouts/side-menu.css' />" rel="stylesheet">
    <link href="<c:url value='/static/css/growl.css' />" rel="stylesheet" type="text/css">

</head>


<body>

<div id="layout">
    <!-- Menu toggle -->
    <a href="#menu" id="menuLink" class="menu-link"  onclick="expandMenu()">
        <span></span>
    </a>
    <div id="menu">
        <div class="pure-menu">
            <ul class="pure-menu-list">
                <li class="pure-menu-item"><a href="/Selenium/index" class="pure-menu-link">Scripts</a></li>
                <a class="pure-menu-heading" href="/Selenium/suite">Suites</a>
                <li class="pure-menu-item"><a href="/Selenium/user" class="pure-menu-link">User</a></li>
                <li class="pure-menu-item"><a href="/Selenium/about" class="pure-menu-link">About</a></li>
            </ul>
        </div>
    </div>


    <div class="header">
        <h1>Suites</h1><br/>
        <h2>A suite is a group of scripts. The cake is a lie.</h2>
    </div>

    <div class="header">
        <a class="pure-button pure-button-green" href="/Selenium/suite/generate">Generate suite</a>
        <br/>
        <br/>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th width="250">File Name</th>
                <th width="750">Description</th>
                <th width="300">Schedule (cron)</th>
                <th width="100"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${documents}" var="doc" varStatus="counter">
                <c:if test="${doc.type eq 'application/octet-stream'}">
                    <tr>
                        <td>${doc.name}</td>
                        <td>${doc.description}</td>
                        <td>${doc.cron}</td>
                        <td>
                            <a class="pure-button pure-button-more" href="/Selenium/suite/${doc.id}">More</a>
                            <a class="pure-button pure-button-d" onclick="deleteSuite('${doc.id}','${doc.name}');">Delete</a>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>

    </div>
</div>

<script>
    successAdd('${MESSAGE}');
</script>

</body>
</html>
