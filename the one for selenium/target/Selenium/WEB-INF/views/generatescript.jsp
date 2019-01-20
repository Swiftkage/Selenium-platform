<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js" type="text/javascript"></script>
<script src="/Selenium/static/js/ui.js"></script>
<script src="/Selenium/static/js/jquery.growl.js" type="text/javascript"></script>
<script src="/Selenium/static/js/doc.js"></script>
<script src="/Selenium/static/js/sequenceinput.js"></script>

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
                <a class="pure-menu-heading" href="/Selenium/index">Scripts</a>
                <li class="pure-menu-item"><a href="/Selenium/suite" class="pure-menu-link">Suites</a></li>
                <li class="pure-menu-item"><a href="/Selenium/user" class="pure-menu-link">User</a></li>
                <li class="pure-menu-item"><a href="/Selenium/about" class="pure-menu-link">About</a></li>
            </ul>
        </div>
    </div>

    <div class="header">
        <h1>Generate a script</h1><br/>
        <h2>Its easy. Just follow the steps!</h2>
    </div>


    <div class="content">
        <form:form method="POST" modelAttribute="generateScript"
                   class="pure-form pure-form-stacked">

            <h2 class="content-subhead">Step 1 : Provide name and description for script</h2>
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

            <h2 class="content-subhead">Step 2 : IWS IP address</h2>
            <div class="pure-u-1">
                <label for="ip">IWS IP address</label>
                <form:select path="ip" id="ip" class="pure-u-1" required="required">
                    <option>192.168.0.247</option>
                    <option>192.168.0.245</option>
                </form:select>
            </div>


            <h2 class="content-subhead">Step 3 : Login information</h2>
            <div class="pure-u-1">
                <label for="username">Username</label>
                <form:input id="username" type="text" path="username" maxlength="100" required="required"/>
                <div class="has-error">
                    <form:errors path="username"/>
                </div>
            </div>
            <div class="pure-u-1">
                <label for="password">Password</label>
                <form:input id="password" type="password" path="password" maxlength="100" required="required"/>
                <div class="has-error">
                    <form:errors path="password"/>
                </div>
            </div>

            <h2 class="content-subhead">Step 4 : Action sequence</h2>
            <div class="pure-u-1">
                <label for="sequence">Action</label>
                <div id="sequenceDiv">
                    <div>
                    <form:select path="sequence" id="sequence" class="hey" required="required">
                        <option>View incidents</option>
                        <option>View rules</option>
                        <option>View hash</option>
                    </form:select>
                        <input type='button' id='remove' value='Remove'>
                    </div>
                </div>
            </div>

            <input type="button" value="Add another action" onClick="addSequenceInput('sequenceDiv');"/>


            <br>
            <button type="submit" class="pure-button">Upload</button>

        </form:form>


    </div>

</div>

</body>
</html>
