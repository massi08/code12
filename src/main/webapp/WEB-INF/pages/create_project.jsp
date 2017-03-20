<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c2" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c3" uri="http://java.sun.com/jsp/jstl/core" %>
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
  <style>
    .error {
      color: red;
      font-weight: bold;
    }
  </style>
</head>

<body>
<jsp:include page="header_2.jsp">
  <jsp:param name="projects" value=""></jsp:param>
  <jsp:param name="account" value=""></jsp:param>
  <jsp:param name="parameters" value=""></jsp:param>
</jsp:include>

<div class="container-full valign-wrapper">
  <div class="row row-full valign-wrapper">
    <form:form action="createproject-result?${_csrf.parameterName}=${_csrf.token}" method="POST"
               commandName="projectForm" enctype="multipart/form-data">
      <input type="hidden" name="${_csrf.parameterName}"
             value="${_csrf.token}"/>
      <h4 class="title-inscription">Créez un projet</h4>
      <div class="row">
        <div class="input-field col s6 input-center">
          <form:input path="name" size="30"/>
          <form:errors path="name" cssClass="error"/>
          <label>Nom du Projet</label>
        </div>

      </div>
      <div class="row">
        <div class="input-field col s6 input-center">
          <label>Langage</label>
        </div>

      </div>
      <div class="row">
        <div class="input-field col s6 input-center git-checkbox">
          <form:select path="language"> <c3:forEach items="${monlangage}" var="langages"> <form:option
               value="${langages.getName()}">${langages.getName()}</form:option> </c3:forEach>
          </form:select> <form:errors path="language" cssClass="error"/></div>
      </div>

      <div class="row">
        <div class="col s6 input-center file-input-custom">
          <label>Choisissez un fichier à télécharger</label>
          <div class="file-field input-field col s12">
            <div class="btn">
              <span>File</span>
              <input type="file" name="file">
            </div>
            <div class="file-path-wrapper">
              <input class="file-path validate" type="text">
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="input-field col s6 input-center"><label>Dépot GIT</label></div>
      </div>
      <div class="row">
        <div class="input-field col s6 input-center git-checkbox">
          <div><form:radiobutton id="oui" path="git" value="true"/> <label class="label-project"
                                                                           for="oui">Oui</label>
            <form:radiobutton path="git" id="non" value="false"/> <label class="label-project"
                                                                         for="non">Non</label> <form:errors
                 path="git" cssClass="error"/></div>
        </div>
      </div>
      <div class="row">
        <div class="connect-btn"><a href="/Home"
                                    class="btn-action-project col s3 offset-l3 waves-effect btn-flat btn"> <i
             class="material-icons left">close</i>Annuler</a>
          <button class="waves-effect waves-light btn"><i name="submit" type="submit"
                                                          class="material-icons right">chevron_right</i>Suivant</a>
          </button>
        </div>
      </div>
    </form:form></div>
</div>

<%@include file="footer.jsp" %>

<script type="text/javascript" src="/static_website/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/static_website/js/materialize.min.js"></script>
<script type="text/javascript" src="/static_website/js/init.js"></script>
</body>
</html>
