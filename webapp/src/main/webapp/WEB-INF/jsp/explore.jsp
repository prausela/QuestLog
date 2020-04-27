<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/exploreListItem.css"/>">
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div>
        <div>
	        <%@ include file="platformsList.jsp"%>
    	</div>
    	<div>
	        <%@ include file="developersList.jsp"%>
    	</div>
    	<div>
	        <%@ include file="publishersList.jsp"%>
    	</div>
    	<div>
	        <%@ include file="genresList.jsp"%>
    	</div>
    </div>
</body>
</html>