<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c2" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="true" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<nav class="white defaut-color" role="navigation">
  <div class="nav-wrapper container">
    <a id="logo-container" href="/Home" class="brand-logo">
      <img src="/static_website/img/Logo_IDE.png">
    </a>
    <ul class="right hide-on-med-and-down">
      <li class="${param.inscription}"><a href="Inscription">S'inscrire</a></li>
      <li class="${param.connexion}"><a href="/">Se connecter</a></li>
    </ul>

    <ul id="nav-mobile" class="side-nav">
      <li class="${param.inscription}"><a href="Inscription">S'inscrire</a></li>
      <li class="${param.connexion}"><a href="/">Se connecter</a></li>
    </ul>
    <a href="#" data-activates="nav-mobile" class="button-collapse"><i class="material-icons">menu</i></a>
  </div>
</nav>