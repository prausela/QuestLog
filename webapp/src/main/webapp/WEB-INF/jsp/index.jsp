<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>QuestLog</title>
    <link rel="stylesheet" type="text/css" href="<c:out value="http://fonts.googleapis.com/css?family=Roboto"/>" >
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigation.css"/>"> 
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigationSearchBar.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/genericListItem.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/genericList.css"/>">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
    	<c:if test="${loggedUser != null && !empty cookieBacklog}">
    		<p><spring:message code="index.importPrompt"/></p>
    		<a class="button" href="<c:url value="/clear_backlog"/>"><spring:message code="index.noImport"/></a><br>
	    	<a class="button" href="<c:url value="/transfer_backlog"/>"><spring:message code="index.yesImport"/></a>
	    	<p><spring:message code="index.ignoreImport"/></p>
    	</c:if>
        <%@include file="mainGameLists.jsp"%>
    </div>
</body>
</html>