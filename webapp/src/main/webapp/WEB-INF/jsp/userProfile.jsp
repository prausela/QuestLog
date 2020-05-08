<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="commonHead.jsp"%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/game.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetails.css"/>">
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
</head>
<body>
    <%@include file="navigation.jsp"%>
    <div class="content">
		<h2><spring:message code="user.introduction" arguments="${user.username}"/></h2>
		
		<div class="main-game-lists-backlog">
			<spring:message code="user.backlog" arguments="${user.username}" var="backlog"/>
			<c:set var="listName" value="${backlog}"/>
			<c:set var="games" value="${user.backlog}"/>
			<%@ include file="gameList.jsp"%>
		</div>
		
		<div>
			<h2><spring:message code="user.scores" arguments="${user.username}"/></h2>
			<c:if test="${empty user.scores}">
            	<p><spring:message code="user.noScores"/></p>
        	</c:if>
			<c:forEach var="element" items="${user.scores}">
				<div class="user-avg-score">
					<strong class="score-title">
						${element.game}:
							<p class="score-display score-display-avg">${element.score}</p>
					</strong>
				</div>
			</c:forEach>
		</div>
	
		<div class="game-table-run">
			<h2><spring:message code="user.runs" arguments="${user.username}"/></h2>
			<c:if test="${empty user.runs}">
            	<p><spring:message code="user.noRuns"/></p>
        	</c:if>
			<table class="runs-table">
				<tr>
				<th><spring:message code="game.game"/></th>
				<th><spring:message code="game.platform"/></th>
				<th><spring:message code="game.playstyle"/></th>
				<th width="130px"><spring:message code="game.time"/></th>
				</tr>
				<c:forEach var="element" items="${user.runs}">
					<tr>
						<td><c:out value="${element.game.title}"/></td>
						<td><c:out value="${element.platform.shortName}"/></td>
						<td><spring:message code="playstyle.${element.playstyle.name}"/></td>
						<td width="130px"><c:out value="${element}"/></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</body>
</html>
