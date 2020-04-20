<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>LIST OF GENRES</h2>
<br><br>
<c:forEach items="${genres}" var="genre">
        <h2>[${genre.id}] <spring:message code="genres.${genre.name}"/></h2>
        <br><img height="100" width="100" src=${genre.logo}></img>
        <h4>Examples:</h4>
        <c:forEach items="${genre.games}" var="game">
		    <li>      
		        [${game.id}] ${game.title}
		    </li>
		    <br>
		</c:forEach>
    <br><br>
</c:forEach>
</body>
</html>