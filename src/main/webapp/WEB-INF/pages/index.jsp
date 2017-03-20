<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c2" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
  <link type="text/css" rel="stylesheet" href="/static_website/css/materialize.min.css" media="screen,projection"/>
  <link href="/static_website/css/style.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/connexion.css" type="text/css" rel="stylesheet"/>
  <!--Let browser know website is optimized for mobile-->
  <link rel="icon" type="image/png" href="/static_website/img/favicon.png"/>
  <title>Connexion</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
</head>

<body>

<jsp:include page="header_1.jsp">
  <jsp:param name="connexion" value="active-tab"></jsp:param>
  <jsp:param name="inscription" value=""></jsp:param>
</jsp:include>

<div class="container-full valign-wrapper">

  <div class="row row-full valign-wrapper">
    <div class="description col s6 valign">
      <p>
        We are a team of college students working on this project like it's our full
        time job. Any amount would help support and continue development on this project and is greatly
        appreciated.
      </p>
    </div>

    <form action="<c:url value='j_spring_security_check' />" method="POST" class="col s6">
      <div class="row">
        <div class="input-field col s6 input-center">
          <i class="material-icons prefix">account_circle</i>
          <input id="icon_prefix" id='pseudoconnect' name='pseudoconnect' type="text" class="validate">
          <label for="icon_prefix">Pseudo</label>
        </div>
      </div>
      <input type="hidden" name="${_csrf.parameterName}"
             value="${_csrf.token}"/>
      <div class="row">
        <div class="input-field col s6 input-center">
          <i class="material-icons prefix">lock</i>
          <input id="icon_password" id='passwordconnect' name='passwordconnect' type="password"
                 class="validate">
          <label for="icon_password">Mot de passe</label>
        </div>
      </div>

      <div class="row left-align connect-btn">
        <input type="checkbox" id="remember-me" name="remember-me"/>
        <label for="remember-me">Remember Me:</label>
      </div>
      <div class="row center-align connect-btn">
        <button href="./manage_project.html" class="waves-effect waves-light btn" name="submit2" type="submit">
          <i class="material-icons right">send</i>CONNEXION
        </button>
      </div>
    </form>
  </div>

</div>

<%@include file="footer.jsp" %>

<script type="text/javascript" src="/static_website/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/static_website/js/materialize.min.js"></script>
<script type="text/javascript" src="/static_website/js/init.js"></script>
</body>
</html>
