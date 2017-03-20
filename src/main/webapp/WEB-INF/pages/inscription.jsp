<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page session="true" %>
<html>
<head>
  <link type="text/css" rel="stylesheet" href="/static_website/css/materialize.min.css" media="screen,projection"/>
  <link href="/static_website/css/style.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/inscription.css" type="text/css" rel="stylesheet"/>
  <!--Let browser know website is optimized for mobile-->
  <link rel="icon" type="/static_website/image/png" href="/static_website/img/favicon.png"/>
  <title>Inscription</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
</head>

<body>

<jsp:include page="header_1.jsp">
  <jsp:param name="connexion" value=""></jsp:param>
  <jsp:param name="inscription" value="active-tab"></jsp:param>
</jsp:include>

<div class="container-full valign-wrapper">
  <div class="row row-full valign-wrapper">
    <!-- <form class="col" action="Inscription" method="POST">-->
    <form:form action="Inscription" commandName="inscriptionForm" class="col">
      <input type="hidden" name="${_csrf.parameterName}"
             value="${_csrf.token}"/>
      <h4 class="title-inscription">Inscrivez-vous</h4>
      <div class="row">
        <div class="input-field col s6 input-center">
          <!--   <input type="text" name="lastname" required class="validate">  -->

          <form:input path="lastName" size="30"/>
          <form:errors path="lastName" cssClass="error"/>
          <label>Nom</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <!--   <input type="text" name="firstname" required class="validate"> -->

          <form:input path="firstName" size="30"/>
          <form:errors path="firstName" cssClass="error"/>

          <label>Prenom</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <!--  <input type="text" name="pseudo" required class="validate">-->
          <form:input path="pseudo" size="30"/>
          <form:errors path="pseudo" cssClass="error"/>
          <div id="pseudo" class="error"><c:if test="${pseudoOk !=null}">
            ${pseudoOk} existe déjà
          </c:if></div>
          <label>Pseudo</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <!--   <input type="email" name="email" required class="validate">-->
          <form:input path="email" size="30"/>
          <form:errors path="email" cssClass="error"/>
          <label>Email</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <!--  <input type="password" name="password" id="password" required class="validate"> -->
          <form:input type="password" path="password" size="30" id="password"/>
          <form:errors path="password" cssClass="error"/>
          <label>Mot de passe</label>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <!-- <input type="password" id="confirm_password" required class="validate">-->

          <form:input type="password" path="confirmPassword" size="30" id="confirm_password"/>
          <form:errors path="password" cssClass="error"/>
          <label>Confirmation du mot de passe</label>
        </div>
      </div>

      <div class="row center-align connect-btn">
        <button class="waves-effect waves-light btn"><i name="submit" type="submit"
                                                        class="material-icons right">done_all</i>INSCRIPTION
        </button>
      </div>

    </form:form>
  </div>

</div>

<%@include file="footer.jsp" %>

<script type="text/javascript" src="/static_website/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/static_website/js/materialize.min.js"></script>
<script type="text/javascript" src="/static_website/js/init.js"></script>
<script type="text/javascript" src="/static_website/js/password_verification.js"></script>
</body>
</html>

