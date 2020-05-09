<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="card m-5 bg-very-light">
	<div class="card-header bg-light">
		<h2><spring:message code="explore.genres"/></h2>
	</div>
	<div class="card-body d-flex flex-wrap">
		<c:forEach items="${genres}" var="genre">
			<a href="<c:url value="genres/${genre.id}"/>">
				<div class="card m-3 d-flex bg-transparent" style="width: 18rem;">
					<a class="d-flex flex-column flex-grow-1 text-white" href="<c:url value="/games/${game.id}"/>">
						<img class="card-img-top" src="<c:url value="${genre.logo}"/>" alt="<c:out value="${genre.name}"/>"/>
						<div class="card-body bg-primary flex-grow-1">
							<h5><spring:message code="genres.${genre.name}"/></h5>
						</div>
					</a>
				</div>
			</a>
		</c:forEach>
	</div>
</div>