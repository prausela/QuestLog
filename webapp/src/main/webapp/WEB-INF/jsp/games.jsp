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
</head>
<body class="background-primary">
<%@include file="navigation.jsp"%>
<div class="content">
    <div class="game-search-results">
        <c:set var="listName">Showing Results for... '<c:out value="${searchTerm}"/>'</c:set>
        <%@ include file="gameList.jsp"%>
    </div>
</div>
</body>
</html>