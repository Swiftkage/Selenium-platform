<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js" type="text/javascript"></script>
<script src="/Selenium/static/js/ui.js"></script>
<script src="/Selenium/static/js/doc.js"></script>
<script src="/Selenium/static/js/expandcollapse.js"></script>
<script src="/Selenium/static/js/jquery.growl.js" type="text/javascript"></script>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Selenium scheduler portal</title>


    <link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
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
    <a href="#menu" id="menuLink" class="menu-link" onclick="expandMenu()">
        <!-- Hamburger icon -->
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
        <h1>${document.name}</h1><br>

    </div>

    <div class="content">

        <div class="pure-u-1 pure-u-md-2-5">
            <form:form class="pure-form pure-form-stacked" method="post">
            <div class="pure-u-1">
                <label for="result">Result: <i class="fa fa-info-circle tip" aria-hidden="true">
                    <div class="help">
                        The timestamp format is in year,month,day,hour,minute.
                    </div>
                </i></label>
                <select id="result" name ="result" required="required" class="hey">
                    <c:forEach items="${results}" var="zip">
                        <option>${zip}</option>
                    </c:forEach>
                </select>
            </div>
            <br/>
            <br/><a class="pure-button pure-button-grey" href='/Selenium/suite/${document.id}'>Back</a>
                <a id="divider" class="pure-button pure-button-grey" href='/Selenium/suite/${document.id}/deleteresults'>Clear results</a>
            <button type="submit" class="pure-button" style="display: inline">Submit</button>
        </div>
    </form:form>


    </div>
</div>
<script>
    errorResult('${MESSAGE}');
</script>

</body>
</html>


