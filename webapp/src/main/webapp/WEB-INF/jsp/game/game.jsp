<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
	<%@include file="../common/commonHead.jsp"%>
	<title>QuestLog - ${game.title}</title>
</head>
<body>
    <%@ include file="../common/navigation.jsp"%>
	<div class="card m-5 bg-very-light right-wave left-wave">
		<div class="card-header bg-very-dark text-white">
			<h2 class="share-tech-mono">${game.title}</h2>
		</div>
		<div class="card-body d-flex padding-left-wave padding-right-wave">
			<div>
				<%@include file="gameDetails.jsp"%>
			</div>
			<div class="p-3 flex-grow-1">
				
			<div class="container">	
			<div class="row">
				<div class="col">
					<div class="row"><strong class="score-title"><spring:message code="game.averageUserScore"/></strong></div>
					<div class="row">
						<c:choose>
							<c:when test="${empty averageScore}">
								<p class="score-display badge badge-dark score-display-avg">N/A</p>
							</c:when>
							<c:otherwise>
								<p class="score-display badge badge-dark">${averageScore}</p>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="col-sm-7 my-auto">
					<form:form name="scores" method="POST" action="scores/${game.id}">
						<div class="row m-auto">
							<strong class="score-title"><spring:message code="game.yourScore"/></strong>
						</div>
						<spring:message code="game.yourScore" var="score"/>
						<c:choose>
							<c:when test="${empty user_score}">
								<div class="score-slider">
									<input class="slider mt-3 mb-3" id="range-slider" type="range" name="score" min="0" max="100" oninput="scoreText.innerHTML = document.getElementById('range-slider').value" value="50">
								</div>
							</c:when>
							<c:otherwise>
								<div class="score-slider">
									<input class="slider mt-3 mb-3" id="range-slider" type="range" name="score" min="0" max="100" oninput="scoreText.innerHTML = document.getElementById('range-slider').value" value="${user_score.score}">
								</div>
							</c:otherwise>
						</c:choose>
						<input type="hidden" value="${game.id}" name="game"/>
				</div>
				<div class="col">
						<c:choose>
							<c:when test="${empty user_score}">
								<div class="score-number">
									<p class="display-4 score-display badge badge-success" id="scoreText">-</p>
								</div>
								<div class="score-submit">
									<input type="submit" class="btn btn-primary btn-block score-submit-button button" value="<spring:message code="game.rate"/>"/>
								</div>
							</c:when>
							<c:otherwise>
								<div class="score-number text-center">
									<p class="score-display badge badge-success" id="scoreText">${user_score.score}</p>
								</div>
								<div class="score-submit">
									<input type="submit" class="btn btn-primary btn-block score-submit-button button" value="<spring:message code="game.rate"/>"/>
								</div>
							</c:otherwise>
						</c:choose>
				</div>
				</form:form>
			</div></div>


				
				<div class="card m-5 bg-very-light right-wave left-wave">
				    <div class="card-header bg-very-dark text-white d-flex">
				    	<div>
				            <h2 class="share-tech-mono"><spring:message code="game.averageTime"/></h2>
				        </div>
					</div>
					<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
						<div class="container">
							<div class="row">
								<div class="col text-right bg-primary text-white"><strong><spring:message code="game.playstyle"/></strong></div>
								<div class="col bg-primary text-white"><strong><spring:message code="game.averageTime"/></strong></div>
							</div>
							<c:forEach var="element" items="${playAverage}">
								<div class="row">
									<div class="col text-right"><spring:message code="playstyle.${element.key}"/></div>
									<div class="col">${element.value}</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
				
				<c:if test="${loggedUser != null && !empty user_runs}">
				<div class="card m-5 bg-very-light right-wave left-wave">
				    <div class="card-header bg-very-dark text-white d-flex">
				    	<div>
				            <h2 class="share-tech-mono"><spring:message code="game.yourRuns"/></h2>
				        </div>
					</div>
					<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
						<div class="container">
							<div class="row">
								<div class="col text-center bg-primary text-white"><strong><spring:message code="game.platform"/></strong></div>
								<div class="col text-center bg-primary text-white"><strong><spring:message code="game.playstyle"/></strong></div>
								<div class="col text-center bg-primary text-white"><strong><spring:message code="game.yourTime"/></strong></div>
							</div>
							<c:forEach var="element" items="${user_runs}">
								<div class="row">
									<div class="col text-center"><c:out value="${element.platform.shortName}"/></div>
									<div class="col text-center"><spring:message code="playstyle.${element.playstyle.name}"/></div>
									<div class="col text-center"><c:out value="${element}"/></div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
				</c:if>
				<a class="btn btn-primary btn-block create-run-button button" href="<c:url value="/createRun/${game.id}"/>"><spring:message code="game.addRun"/></a>
			</div>
		</div>
	</div>
</body>
</html>