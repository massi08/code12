<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page session="true" %>
<html>
<head>
  <link type="text/css" rel="stylesheet" href="/static_website/css/materialize.min.css" media="screen,projection"/>
  <link href="/static_website/css/style.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/manage_project.css" type="text/css" rel="stylesheet"/>
  <!--Let browser know website is optimized for mobile-->
  <link rel="icon" type="image/png" href="/static_website/img/favicon.png"/>
  <title>Gestion d'un projet</title>
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
  <div class="projet-titre row">
    <h4 class="left-align h4-margin">Mes Projets</h4>
    <a href="createproject" class="right-align waves-effect waves-light btn"><i
         class="material-icons right">add</i>AJOUTER PROJET</a>
  </div>

  <div class="row row-cards">
    <div class="cards defaut-color">
      <c:forEach items="${projects}" var="item">
        <div class="col s4 card-projects">
          <div class="card-project card-panel white">
            <div class="center-align connect-btn">
              <c:if test="${roleOfProject[item].getIdRole()==1}"><a
                   href="/Project/manageProject?projectid=${item.getIdProject()}"
                   class="col s3 waves-effect btn-flat">Gérer</a>
                <a href="deleteproject-result?project=${item.getIdProject()}"
                   class="col offset-l7 s2 waves-effect waves-effect btn-flat"><i
                     class="material-icons center">close</i></a>
              </c:if>

            </div>
            <a href="/Project/code?projectid=${item.getIdProject()}" class="card-project-a">
              <h5 class="center-align">${item.getName()}</h5>
              <p class="center-align">${roleOfProject[item].getNom()}</p>
            </a>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
</div>

<%@include file="footer.jsp" %>

<script type="text/javascript" src="/static_website/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/static_website/js/materialize.min.js"></script>
<script type="text/javascript" src="/static_website/js/init.js"></script>
</body>
</html>
