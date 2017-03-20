<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c2" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="true" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<nav class="white" role="navigation">
  <div class="nav-wrapper container">
    <a id="logo-container" href="/Home" class="brand-logo">
      <img src="/static_website/img/Logo_IDE.png">
    </a>
    <ul class="right hide-on-med-and-down">
      <li class="${param.projects}"><a href="/Home">Mes Projets</a></li>
      <li class="${param.account}"><a href="/ManageAccount">Mon Compte</a></li>
      <li>
        <c2:if test="${not empty msg}">
          <div class="msg">${msg}</div>
        </c2:if>
      </li>
      <li>
        <form action="<c2:url value='/j_spring_security_logout'/>" method="POST" id="logoutForm">
          <input type="hidden" name="${_csrf.parameterName}"
                 value="${_csrf.token}"/>
        </form>
        <c2:if test="${pageContext.request.userPrincipal.name != null}">
          <a href="javascript:formSubmit()">Déconnexion</a>
        </c2:if>
      </li>
    </ul>

    <ul id="nav-mobile" class="side-nav">
      <li class="${param.projects}"><a href="/Home">Mes Projets</a></li>
      <li class="${param.account}"><a href="/ManageAccount">Mon Compte</a></li>
      <li>
        <c2:if test="${not empty msg}">
          <div class="msg">${msg}</div>
        </c2:if>
      </li>
      <li>
        <form action="<c2:url value='/j_spring_security_logout'/>" method="POST" id="logoutForm">
          <input type="hidden" name="${_csrf.parameterName}"
                 value="${_csrf.token}"/>
        </form>
        <c2:if test="${pageContext.request.userPrincipal.name != null}">
          <a href="javascript:formSubmit()">Déconnexion</a>
        </c2:if>
      </li>
    </ul>
    <a href="#" data-activates="nav-mobile" class="button-collapse"><i class="material-icons">menu</i></a>
  </div>
</nav>