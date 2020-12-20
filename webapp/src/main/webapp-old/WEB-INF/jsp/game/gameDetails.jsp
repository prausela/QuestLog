<%--
    Include this page:
        <%@ include file="gameDetails.jsp"%>
--%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="card m-3 d-flex bg-transparent" style="width: 18rem;">
    <c:if test="${loggedUser != null && loggedUser.adminStatus == true}">
        <div class="d-flex">
            <spring:message code="game.edit" var="edit"/>
            <spring:message code="game.delete" var="delete"/>
            <a href="<c:url value="/admin/game/${game.id}/edit"/>" class="btn btn-block btn-outline-warning not-rounded-bottom btn-lg"><c:out value="${edit}"/></a>
            <button type="button" class="btn btn-block btn-outline-danger not-rounded-bottom btn-lg mt-0" data-toggle="modal" data-target="#deleteGameModal-<c:out value="${game.id}"/>">
                <c:out value="${delete}"/>
            </button>
            <div class="modal fade" id="deleteGameModal-<c:out value="${game.id}"/>" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <spring:message code="game.delete" var="gameDelete"/>
                            <h5 class="modal-title" id="deleteGameModalLabel-<c:out value="${game.id}"/>"><c:out value="${gameDelete}"/><c:out value="${game.title}"/></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <spring:message code="game.delete.message" var="deleteMessage"/>
                            <c:out value="${deleteMessage}"/>
                        </div>
                        <div class="modal-footer">
                            <spring:message code="game.delete.dismiss" var="deleteDismiss"/>
                            <button type="button" class="btn btn-light" data-dismiss="modal"><c:out value="${deleteDismiss}"/></button>
							<c:url value="/admin/game/${game.id}/delete/fromdetails" var="post_url"/>
						    <form method="post" action="<c:out value="${post_url}"/>">
						    	<spring:message code="game.delete" var="deleteReview"/>
                                <spring:message code="game.delete.confirm" var="confirmDelete"/>
						    	<input class="btn btn-danger" type="submit" value="<c:out value="${confirmDelete}"/>"/>
						    </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
    <form method="post" onsubmit="var buttons = document.getElementsByClassName('button-<c:out value="${game.id}"/>'); for(var i=0; i < buttons.length; i++){buttons[i].disabled=true;}">
    <input type="hidden" name="gameId" value="<c:out value="${game.id}"/>">
    <spring:message code="game.addToBacklog" var="addToBacklog"/>
    <spring:message code="game.addingToBacklog" var="addingToBacklog"/>
    <spring:message code="game.removeFromBacklog" var="removeFromBacklog"/>
    <spring:message code="game.removingFromBacklog" var="removingFromBacklog"/>
        <c:choose>
            <c:when test="${game.inBacklog}">
                <input class="btn btn-block btn-outline-danger btn-lg not-rounded-bottom button-<c:out value="${game.id}"/> remove-button-<c:out value="${game.id}"/>" type="submit" onclick="var buttons = document.getElementsByClassName('remove-button-<c:out value="${game.id}"/>'); for(var i=0; i < buttons.length; i++){buttons[i].value = '<c:out value="${removingFromBacklog}"/>';}" value="<c:out value="${removeFromBacklog}"/>"/>
            </c:when>
            <c:otherwise>
                <input class="btn btn-block btn-outline-success btn-lg not-rounded-bottom button-<c:out value="${game.id}"/> add-button-<c:out value="${game.id}"/>" type="submit" onclick="var buttons = document.getElementsByClassName('add-button-<c:out value="${game.id}"/>'); for(var i=0; i < buttons.length; i++){buttons[i].value = '<c:out value="${addingToBacklog}"/>';}" value="<c:out value="${addToBacklog}"/>"/>
            </c:otherwise>
        </c:choose>
    </form>
    <div class="d-flex flex-column flex-grow-1 text-white">
        <c:choose>
            <c:when test="${ game.cover != null }">
                <img class="card-img-top" src="<c:url value="/images/${game.cover}"/>" alt="<c:out value="${game.title}"/>"/>
            </c:when>
            <c:otherwise>
                <img class="card-img-top" src="<c:url value="/images/static/games/default_game_cover.png"/>" alt="<c:out value="${game.title}"/>"/>
            </c:otherwise>
        </c:choose>
        <c:if test="${ game.trailer != null}">
            <iframe width="286" height="161" src="https://www.youtube.com/embed/<c:out value="${game.trailer}"/>" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
        </c:if>
        <div class="card-body bg-primary flex-grow-1">
            <dl>
                <div class="game-details-release-dates">
                    <spring:message code="game.releaseDates" var="releaseDates"/>
                    <c:set var="title" value="${releaseDates}"/>
                    <c:set var="items" value="${game.releaseDates}"/>
                    <%@include file="gameDetailsInfoItem.jsp"%>
                </div>
                <div class="game-details-genres">
                    <spring:message code="game.genres" var="genres"/>
                    <c:set var="title" value="${genres}"/>
                    <c:set var="items" value="${game.genres}"/>
                    <%@include file="gameGenreDetailsInfoItem.jsp"%>
                </div>
                <div class="game-details-platforms">
                    <spring:message code="game.platforms" var="platforms"/>
                    <c:set var="title" value="${platforms}"/>
                    <c:set var="items" value="${game.platforms}"/>
                    <%@include file="gameDetailsInfoItem.jsp"%>
                </div>
                <div class="game-details-developers">
                    <spring:message code="game.developers" var="developers"/>
                    <c:set var="title" value="${developers}"/>
                    <c:set var="items" value="${game.developers}"/>
                    <%@include file="gameDetailsInfoItem.jsp"%>
                </div>
                <div class="game-details-publishers">
                    <spring:message code="game.publishers" var="publishers"/>
                    <c:set var="title" value="${publishers}"/>
                    <c:set var="items" value="${game.publishers}"/>
                    <%@include file="gameDetailsInfoItem.jsp"%>
                </div>
            </dl>
        </div>
    </div>
</div>