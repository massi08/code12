<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page session="true" %>
<html>
<head>
  <link type="text/css" rel="stylesheet" href="/static_website/css/materialize.min.css" media="screen,projection"/>
  <link href="/static_website/css/style.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/manage_ticket.css" type="text/css" rel="stylesheet"/>
  <!--Let browser know website is optimized for mobile-->
  <link rel="icon" type="image/png" href="/static_website/img/favicon.png"/>
  <title>Gestion d'un ticket</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
</head>

<body>

<jsp:include page="header_2.jsp">
  <jsp:param name="projects" value="active-tab"></jsp:param>
  <jsp:param name="account" value=""></jsp:param>
  <jsp:param name="parameters" value=""></jsp:param>
</jsp:include>

<div class="container-full">
  <div class="title-ticket row">
    <h4 class="left-align h4-margin">Modifier un ticket</h4>
  </div>

  <div class="row row-full">
    <form class="col s6" action="/Project/ManageTicket-result?ticket=${ticket.getNumber()}"
          method="POST">
      <div class="row">
        <div class="input-field col s6 input-center">
          <input type="text" disabled value="${ticket.getTitle()}" class="validate">
          <label>Titre</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <input type="text" disabled value="${ticket.getAuthor().getUsername()}" class="validate">
          <label>Auteur</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <select name="choose_type" class="hidden_select">
            <option value="Bug" <c:if test="${ticket.getType() =='Bug' }"> selected </c:if>
            >Bug
            </option>
            <option value="Feature" <c:if test="${ticket.getType() =='Feature' }"> selected </c:if>
            >Feature
            </option>
            <option value="Tracker" <c:if test="${ticket.getType() =='Tracker' }"> selected </c:if>
            >Tracker
            </option>
            <option value="Build" <c:if test="${ticket.getType() =='Build' }"> selected </c:if>
            >Build
            </option>
          </select>
          <label>Type</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <select name="choose_priority" class="hidden_select">
            <option value="Mineure"
                 <c:if test="${ticket.getPriority() =='Mineure' }"> selected </c:if>
            >Mineure
            </option>
            <option value="Majeure"<c:if test="${ticket.getPriority() =='Majeure' }"> selected </c:if>
            > Majeure
            </option>
            <option value="Bloquante" <c:if test="${ticket.getPriority() =='Bloquante' }"> selected </c:if>
            >Bloquante
            </option>
          </select>
          <label>Priorité</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <select name="choose_statut" class="hidden_select">
            <option value="NonTraite" <c:if test="${ticket.getNameEtat() =='NonTraite' }"> selected </c:if>
            >Non Traité
            </option>
            <option value="EnCours"<c:if test="${ticket.getNameEtat() =='EnCours' }"> selected </c:if>
            >En cours
            </option>
            <option value="Ferme"<c:if test="${ticket.getNameEtat() =='Ferme' }"> selected </c:if>
            >Fermé
            </option>
          </select>
          <label>Statut</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center input-date">
          <label>Date de début</label>
        </div>
        <div class="input-field col s6 input-center input-right-prefix">
          <input type="date" disabled value="${ticket.getStartDate()}" class="datepicker">
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <select name="choose_supervisor" class="hidden_select">
            <option value="Nobody">Aucun</option>
            <c:forEach items="${members}" var="member">
              <option value="${member.getIdUser().getIdUser()}" <c:if
                   test="${ticket.getSupervisor() == member.getIdUser() }"> selected </c:if>
              >${member.getIdUser().getUsername()}</option>
            </c:forEach>
          </select>
          <label>Supervisor</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <input type="text" disabled value="${ticket.getContent()}" class="validate">
          <label>Description</label>
        </div>
      </div>


      <input type="hidden" name="${_csrf.parameterName}"
             value="${_csrf.token}"/>
      <div class="connect-btn">
        <a href="/Project/ManageTicket?ticket=${ticket.getNumber()}"
           class="btn-action-project col s3 offset-l3 waves-effect btn-flat btn"><i
             class="material-icons left">close</i>Annuler</a>
        <button class="btn-action-project col s3 offset-l1 waves-effect waves-light btn">
          <i name="submit" type="submit" class="material-icons right">done_all</i>Modifier
        </button>
      </div>

    </form>

    <form class="col s6" action="/Project/ManageTicket-commentaire?ticket=${ticket.getNumber()}"
          method="POST">
      <c:forEach items="${commentaries}" var="commentary">
        <div class="col  s8">
          <label for="textarea1">Commentaire de ${commentary.getIdAuthor().getUsername()}
            le ${commentary.getDate()}</label>
          <input type="text" disabled value="${commentary.getContent()}" class="validate">
        </div>
      </c:forEach>

      <div class="post-commentary s12 col">
        <div class="input-field s8 col">
          <textarea name="commentaire" id="textarea1" class="materialize-textarea"></textarea>
          <label for="textarea1">Poster un Commentaire</label>
        </div>

        <input type="hidden" name="${_csrf.parameterName}"
               value="${_csrf.token}"/>
        <button class="waves-effect waves-light btn"><i name="submit" type="submit"
                                                        class="material-icons right">close</i>Poster
        </button>
      </div>

    </form>
  </div>
</div>

<%@include file="footer.jsp" %>

<script type="text/javascript" src="/static_website//js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/static_website//js/materialize.min.js"></script>
<script type="text/javascript" src="/static_website//js/init.js"></script>
</body>
</html>
