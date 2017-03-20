<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="true" %>
<html>
<head>
  <link type="text/css" rel="stylesheet" href="/static_website/css/materialize.min.css" media="screen,projection"/>
  <link href="/static_website/css/style.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/account.css" type="text/css" rel="stylesheet"/>
  <!--Let browser know website is optimized for mobile-->
  <link rel="icon" type="image/png" href="/static_website/img/favicon.png"/>
  <title>Gestion du compte </title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
</head>

<body>

<jsp:include page="header_2.jsp">
  <jsp:param name="projects" value=""></jsp:param>
  <jsp:param name="account" value="active-tab"></jsp:param>
  <jsp:param name="parameters" value=""></jsp:param>
</jsp:include>

<div class="container-full valign-wrapper">
  <div class="row row-full valign-wrapper">
    <form action="ManageAccount" method="POST">
      <input type="hidden" name="${_csrf.parameterName}"
             value="${_csrf.token}"/>
      <h4 class="title-inscription">Mon compte</h4>
      <div class="row">
        <div class="input-field col s6 input-center">
          <input type="text" value="${user.getLastName()}" disabled name="lastname" class="validate">
          <label>Nom</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <input type="text" value="${user.getFirstName()}" disabled name="firstname" class="validate">
          <label>Prenom</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <input type="text" value="${user.getPseudo()}" disabled name="pseudo" class="validate">
          <label>Pseudo</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <input type="email" value="${user.getEmail()}" disabled name="email" class="validate">
          <label>Email</label>
        </div>
      </div>


      <div class="row">
        <div class="input-field col s6 input-center">
          <input type="password" name="old_password" id="old_password" required class="validate">
          <c3:if test="${error!= null}">
            <div class="error"> ${error}</div>
          </c3:if>
          <label>Mot de passe actuel</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <input type="password" name="password" id="password" required class="validate">
          <c3:if test="${error!= null}">
            <div class="error"> ${error}</div>
          </c3:if>
          <label>Nouveau mot de passe</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <input type="password" name="confirm_password" id="confirm_password" required class="validate">
          <c3:if test="${error!= null}">
            <div class="error"> ${error}</div>
          </c3:if>
          <label>Confirmation du nouveau mot de passe</label>
        </div>
      </div>

      <div class="row">
        <div class="connect-btn">
          <a href="/Home"
             class="btn-action-project col s3 offset-l3 waves-effect btn-flat btn"><i
               class="material-icons left">close</i>Annuler</a>
          <button class="waves-effect waves-light btn"><i name="submit" type="submit"
                                                          class="material-icons right">autorenew</i>Actualiser
          </button>
        </div>
      </div>
    </form>
  </div>
</div>

<%@include file="footer.jsp" %>
<script type="text/javascript" src="/static_website/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/static_website/js/materialize.min.js"></script>
<script type="text/javascript" src="/static_website/js/init.js"></script>
<script type="text/javascript" src="/static_website/js/password_verification.js"></script>
</body>
</html>