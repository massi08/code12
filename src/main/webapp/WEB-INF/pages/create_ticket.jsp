<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page session="true" %>
<html>
<head>
  <link type="text/css" rel="stylesheet" href="/static_website/css/materialize.min.css" media="screen,projection"/>
  <link href="/static_website/css/style.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/create_ticket.css" type="text/css" rel="stylesheet"/>
  <!--Let browser know website is optimized for mobile-->
  <link rel="icon" type="image/png" href="/static_website/img/favicon.png"/>
  <title>Créer ticket</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
</head>

<body>

<jsp:include page="header_2.jsp">
  <jsp:param name="projects" value=""></jsp:param>
  <jsp:param name="account" value=""></jsp:param>
  <jsp:param name="parameters" value=""></jsp:param>
</jsp:include>

<div class="container-full valign-wrapper">
  <div class="row row-full valign-wrapper">
    <form class="col" action="/Project/CreateTicket-result" method="POST">
      <h4 class="title-ticket">Créez un ticket</h4>
      <div class="row">
        <div class="input-field col s6 input-center">
          <input name="title" type="text" required class="validate">
          <label>Titre</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <select name="choose_type" class="hidden_select">
            <option value="Bug">Bug</option>
            <option value="Feature">Feature</option>
            <option value="Tracker">Tracker</option>
            <option value="Build">Build</option>
          </select>
          <label>Type</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <select name="choose_priority" class="hidden_select">
            <option value="Mineure">Mineure</option>
            <option value="Majeure"> Majeure</option>
            <option value="Bloquante">Bloquante</option>
          </select>
          <label>Priorité</label>
        </div>
      </div>
      <div class="row">
        <div class="input-field col s6 input-center">
          <select name="choose_statut" class="hidden_select">
            <option value="NonTraite">Non Traité</option>
            <option value="EnCours">En cours</option>
            <option value="Ferme">Fermé</option>
          </select>
          <label>Statut</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center input-date">
          <label>Date de début</label>
        </div>
        <div class="input-field col s6 input-center input-right-prefix">
          <i class="material-icons prefix"> date_range </i>
          <input name="date" type="date" class="datepicker">
        </div>
      </div>


      <div class="row">
        <div class="input-field col s6 input-center">
          <select name="choose_supervisor" class="hidden_select">
            <option value="Nobody">Aucun</option>
            <c:forEach items="${members}" var="member">
              <option value="${member.getIdUser().getIdUser()}">${member.getIdUser().getUsername()}</option>
            </c:forEach>
          </select>
          <label>Supervisor</label>
        </div>
      </div>

      <div class="input-field col s6 input-center">
        <textarea name="description" id="textarea1" class="materialize-textarea"></textarea>
        <label for="textarea1">Description</label>
      </div>


      <div class="row">
        <div class="connect-btn">
          <a href="ManageTickets"
             class="btn-action-project col s3 offset-l3 waves-effect btn-flat btn"><i
               class="material-icons left">close</i>Annuler</a>
          <input type="hidden" name="${_csrf.parameterName}"
                 value="${_csrf.token}"/>
          <button class="btn-action-project col s3 offset-l1 waves-effect waves-light btn">
            <i name="submit" type="submit" class="material-icons right">done_all</i>Valider
          </button>
        </div>
      </div>
    </form>
  </div>

</div>

<%@include file="footer.jsp" %>

<script type="text/javascript" src="/static_website//js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/static_website//js/materialize.min.js"></script>
<script type="text/javascript" src="/static_website//js/init.js"></script>
<script type="text/javascript" src="/static_website//js/date_picker.js"></script>
</body>
</html>