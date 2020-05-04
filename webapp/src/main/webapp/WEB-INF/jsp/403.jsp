<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
   <%@include file="commonHead.jsp"%>
</head>
<body class="background-color">
    <%@include file="navigation.jsp"%>
    <div class="error-box">
        <h2>Error 403 - Unauthorized</h2>
		<h5><spring:message code="error.unauthorized"/></h5>
    </div>
</body>
</html>