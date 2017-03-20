<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c2" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c3" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
  <link type="text/css" rel="stylesheet" href="/static_website/css/materialize.min.css" media="screen,projection"/>
  <link href="/static_website/css/style.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/create_project.css" type="text/css" rel="stylesheet"/>
  <!--Let browser know website is optimized for mobile-->
  <link rel="icon" type="image/png" href="./img/favicon.png"/>
  <title> Inscription </title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
</head>
<body>

<jsp:include page="header_2.jsp">
  <jsp:param name="projects" value="active-tab"></jsp:param>
  <jsp:param name="account" value=""></jsp:param>
  <jsp:param name="parameters" value=""></jsp:param>
</jsp:include>

<div class="container-full valign-wrapper">
  <div class="row row-full valign-wrapper">
    <form action="modifyProjectDone" method="POST">
      <input type="hidden" name="${_csrf.parameterName}"
             value="${_csrf.token}"/>
      <h4 class="title-inscription">Modifiez votre projet</h4>
      <div class="row">
        <div class="input-field col s6 input-center">
          <label>Nom du Projet</label>
          <input name="nomProject" type="text" value="${nameProject}">
          <c3:if test="${error!= null}">
            <div class="error"> ${error}</div>

          </c3:if>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <label>Langage</label>
          <input type="text" disabled value="${languageProject}" class="validate">
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center">
          <label>Dépot GIT</label>
          <input type="text" disabled value="${gitProject}" class="validate">
        </div>
      </div>

      <div class="row">
        <div class="connect-btn">


        </div>
      </div>
      <div class="row">
        <div class="connect-btn">
          <a href="/Home" class="btn-action-project col s3 offset-l3 waves-effect btn-flat btn">
            <i class="material-icons left">close</i>Annuler</a>
          <button class="waves-effect waves-light btn">
            <i name="submit" type="submit" class="material-icons right">chevron_right</i>Suivant</a>
          </button>
        </div>
      </div>

    </form>
  </div>
</div>

<footer class="page-footer grey darken-4">
  <div class="container">
    <div class="row">
      <div class="col l6 s12">
        <p class="grey-text text-lighten-4">We are a team of college students working on this project like it's
          our full
          time job. Any amount would help support and continue development on this project and is greatly
          appreciated.
        </p>
      </div>
    </div>
  </div>
  <div class="footer-copyright">
    <div class="container">
      © Code12, Tous droits résérvés
      <a class="grey-text text-lighten-4 right" href="http://www.univ-lyon1.fr/">Université Lyon I</a>
    </div>
  </div>
</footer>

<script type="text/javascript" src="/static_website/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/static_website/js/materialize.min.js"></script>
<script type="text/javascript" src="/static_website/js/init.js"></script>
</body>
</html>
