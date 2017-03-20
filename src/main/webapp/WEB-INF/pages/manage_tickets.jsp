<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page session="true" %>
<html>
<head>
  <link type="text/css" rel="stylesheet" href="/static_website/css/materialize.min.css" media="screen,projection"/>
  <link href="/static_website/css/style.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/manage_tickets.css" type="text/css" rel="stylesheet"/>
  <!--Let browser know website is optimized for mobile-->
  <link rel="icon" type="image/png" href="/static_website/img/favicon.png"/>
  <title>Gestion des tickets</title>
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
  <div class="ticket-titre row">
    <h4 class="left-align h4-margin">Gestion des tickets</h4>
    <a href="/Project/CreateTicket" class="right-align waves-effect waves-light btn"><i
         class="material-icons right">add</i>AJOUTER TICKET</a>
  </div>

  <div class="row">
    <h5 class="title-filter-ticket center-align">Choisissez vos critères</h5>
    <form class="ticket-filter col" action="/RechercheTickets" method="POST">
      <div class="col s1 filter-objet">
        <label>Type</label>
        <select name="choose_type" class="hidden_select">
          <option value="None">None</option>
          <option value="Bug">Bug</option>
          <option value="Feature">Feature</option>
          <option value="Tracker">Tracker</option>
          <option value="Build">Build</option>
        </select>

        <select name="choose_type_eg" class="hidden_select">
          <option value="Eg">Egal</option>
          <option value="Diff">Différent</option>
        </select>
      </div>

      <div class="col s1 filter-objet">
        <label>Priorité</label>
        <select name="choose_priority" class="hidden_select">
          <option value="None">None</option>
          <option value="Mineure">Mineure</option>
          <option value="Majeure"> Majeure</option>
          <option value="Bloquante">Bloquante</option>
        </select>

        <select name="choose_priority_eg" class="hidden_select">
          <option value="Eg">Egal</option>
          <option value="Diff">Différent</option>
        </select>
      </div>

      <div class="col s1 filter-objet">
        <label>Etat</label>
        <select name="choose_etat" class="hidden_select">
          <option value="None">None</option>
          <option value="NonTraite">Non Traité</option>
          <option value="EnCours">En cours</option>
          <option value="Ferme">Fermé</option>
        </select>

        <select name="choose_etat_eg" class="hidden_select">
          <option value="Eg">Egal</option>
          <option value="Diff">Différent</option>
        </select>
      </div>

      <div class="col s1 filter-objet">
        <label>Superviseur</label>
        <select name="choose_supervisor" class="hidden_select">
          <option value="None">None</option>
          <option value="Aucun">Aucun</option>
          <c:forEach items="${members}" var="member">
            <option value="${member.getIdUser().getIdUser()}">${member.getIdUser().getUsername()}</option>
          </c:forEach>
        </select>
      </div>

      <div class="col s1 filter-objet">
        <label>Auteur</label>
        <select name="choose_author" class="hidden_select">
          <option value="None">None</option>
          <c:forEach items="${members}" var="member">
            <option value="${member.getIdUser().getIdUser()}">${member.getIdUser().getUsername()}</option>
          </c:forEach>
        </select>
      </div>

      <input type="hidden" name="${_csrf.parameterName}"
             value="${_csrf.token}"/>

      <button class="waves-effect col s2 offset-l2 waves-light btn"><i name="submit" type="submit"
                                                                       class="material-icons right">done_all</i>Afficher
      </button>

    </form>
  </div>

  <div class="row">
    <div class="col s4 row-tickets">
      <h5 class="center-align"> Mes Tickets</h5>
      <table class="highlight bordered centered responsive-table">
        <thead>
        <tr>
          <th data-field="numero">Numéro</th>
          <th data-field="titre">Titre</th>
          <th data-field="type">Type</th>
          <th data-field="priority">Priorité</th>
          <th data-field="etat">Etat</th>
          <th data-field="author">Auteur</th>
          <th data-field="assigne">Assigné à</th>
          <th data-field=""></th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${myTickets}" var="ticket">
          <tr>
            <td>${ticket.getNumber()}</td>
            <td>${ticket.getTitle()}</td>
            <td>${ticket.getType()}</td>
            <td>${ticket.getPriority()}</td>
            <td>${ticket.getNameEtat()}</td>
            <td>${ticket.getAuthor().getPseudo()}</td>
            <td>${ticket.getPseudoSupervisor()}</td>
            <td>
              <a href="ManageTicket?ticket=${ticket.getNumber()}" class="logo-color"><i
                   class="material-icons center">settings</i></a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%@include file="footer.jsp" %>

<script type="text/javascript" src="/static_website//js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/static_website//js/materialize.min.js"></script>
<script type="text/javascript" src="/static_website//js/init.js"></script>
</body>
</html>
