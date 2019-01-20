<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js" type="text/javascript"></script>
<script src="/Selenium/static/js/ui.js"></script>
<script src="/Selenium/static/js/jquery.growl.js" type="text/javascript"></script>
<script src="/Selenium/static/js/doc.js"></script>
<script src="/Selenium/static/js/suiteinput.js"></script>

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
    <a href="#menu" id="menuLink" class="menu-link" onclick="expandMenu()">
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
        <h2>Select the scripts, and group them into a suite!</h2>
    </div>


    <div class="content">
        <form:form method="POST" modelAttribute="suite"
                   class="pure-form pure-form-stacked">

            <div class="pure-u-1">
                <label for="name">Name</label>
                <form:input id="name" type="text" path="name" maxlength="25" required="required"/>
                <div class="has-error">
                    <form:errors path="name"/>
                </div>
            </div>

            <div class="pure-u-1">
                <label for="description">Description</label>
                <form:textarea id="description" path="description" maxlength="255" rows="4" cols="105"/>
            </div>

            <div class="pure-u-1">
                <label for="script">Scripts to be added</label>
                <div id="scriptDiv">
                    <div>
                    <form:select path="script" id="script" class='hey' required="required">
                        <c:forEach items="${documents}" var="doc">
                            <c:if test="${doc.type eq 'text/html'}">
                                <option>${doc.name}</option>
                            </c:if>
                        </c:forEach>
                    </form:select>
                        <input type='button' id='remove' value='Remove'>
                    </div>
                </div>
            </div>

            <input type="button" value="Add another script" onClick="addSuiteInput('scriptDiv','${list}');"/>
            <br>
            <button type="submit" class="pure-button">Upload</button>

        </form:form>
    </div>

</div>


<script>
    successAdd('${MESSAGE}');
</script>

</body>
</html>
