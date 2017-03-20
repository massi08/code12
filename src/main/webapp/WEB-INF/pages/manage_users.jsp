<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link type="text/css" rel="stylesheet" href="/static_website/css/materialize.min.css" media="screen, projection"/>
  <link href="/static_website/css/style.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/manage_users.css" type="text/css" rel="stylesheet"/>
  <!--Let browser know website is optimized for mobile-->
  <link rel="icon" type="image/png" href="/static_website/img/favicon.png"/>
  <title>Gestion des utilisateurs</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
</head>


<jsp:include page="header_2.jsp">
  <jsp:param name="projects" value="active-tab"></jsp:param>
  <jsp:param name="account" value=""></jsp:param>
  <jsp:param name="parameters" value=""></jsp:param>
</jsp:include>


<body>

<div class="container-full valign-wrapper">
  <div class="row row-content">
    <div class="col s4 user-col">
      <h4 class="left-align h4">Gestion des utilisateurs</h4>
      <div class="col row-manage-users">
        <table class="table-roles highlight bordered centered responsive-table">
          <thead>
          <tr>
            <th data-field="name">NOM</th>
            <th data-field="droits">DROITS</th>
            <th data-field=></th>
          </tr>
          </thead>
          <tbody>

          <c:if test="${error!= null}">
            ${error}
          </c:if>

          <c:forEach var="Member" items="${Members}">
            <tr>
              <form action="ModifyRole" method="POST">
                <td>
                  <label class="user-name"
                         for="${Member.getIdUser().getIdUser()}">${Member.getIdUser().getPseudo()}
                  </label>
                </td>

                <input type="hidden" name="idMember"
                       value="${Member.getIdUser().getIdUser()}"/>
                <td>
                  <div class="user-action-row">
                    <div class="radio-add-user left-align">
                      <c:choose>
                        <c:when test="${Member.getRole().getIdRole()==1}">
                          <input type="radio"
                                 id="chef_de_projet.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="1" checked>
                          <label for="chef_de_projet.${Member.getIdUser().getIdUser()}">Chef
                            de Projet</label>

                          <input type="radio"
                                 id="developpeur.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="2">
                          <label for="developpeur.${Member.getIdUser().getIdUser()}">Developpeur</label>

                          <input type="radio" id="reporter.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="3">
                          <label for="reporter.${Member.getIdUser().getIdUser()}">Reporter</label>
                        </c:when>
                        <c:when test="${Member.getRole().getIdRole()==2}">
                          <input type="radio"
                                 id="chef_de_projet.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="1">
                          <label for="chef_de_projet.${Member.getIdUser().getIdUser()}">Chef
                            de Projet</label>

                          <input type="radio"
                                 id="developpeur.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="2" checked>
                          <label for="developpeur.${Member.getIdUser().getIdUser()}">Developpeur</label>

                          <input type="radio" id="reporter.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="3">
                          <label for="reporter.${Member.getIdUser().getIdUser()}">Reporter</label>
                        </c:when>
                        <c:when test="${Member.getRole().getIdRole()==3}">
                          <input type="radio"
                                 id="chef_de_projet.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="1">
                          <label for="chef_de_projet.${Member.getIdUser().getIdUser()}">Chef
                            de Projet</label>

                          <input type="radio"
                                 id="developpeur.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="2">
                          <label for="developpeur.${Member.getIdUser().getIdUser()}">Developpeur</label>

                          <input type="radio" id="reporter.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="3" checked>
                          <label for="reporter.${Member.getIdUser().getIdUser()}">Reporter</label>
                        </c:when>
                        <c:otherwise>
                          <input type="radio"
                                 id="chef_de_projet.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="1">
                          <label for="chef_de_projet.${Member.getIdUser().getIdUser()}">Chef
                            de Projet</label>

                          <input type="radio"
                                 id="developpeur.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="2" checked>
                          <label for="developpeur.${Member.getIdUser().getIdUser()}">Developpeur</label>

                          <input type="radio" id="reporter.${Member.getIdUser().getIdUser()}"
                                 name="radio_modify" value="3">
                          <label for="reporter.${Member.getIdUser().getIdUser()}">Reporter</label>
                        </c:otherwise>
                      </c:choose>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}"/>
                    <div class="connect-btn">
                      <button class="waves-effect waves-light btn">
                        <i name="submit" type="submit" class="material-icons">done_all</i>
                      </button>
                    </div>
                  </div>

                </td>
              </form>
              <form action="GetOldMember" method="POST">
                <input type="hidden" name="${_csrf.parameterName}"
                       value="${_csrf.token}"/>
                <input type="hidden" name="idMember"
                       value="${Member.getIdUser().getIdUser()}"/>
                <td>
                  <button class="btn-flat warn-color">
                    <i name="submitclose" , type="submit" class="material-icons center">close</i>
                  </button>
                </td>
              </form>
            </tr>
          </c:forEach>
          </tbody>

        </table>
      </div>
    </div>

    <div class="col user-col add-user-col">
      <h4 class="h4">Ajouter un utilisateur</h4>

      <div class="col row-add">
        <form action="AddMember" method="POST">
          <div class="all-users-add">
            <c:forEach var="User" items="${Users}">
              <div class="row">
                <input type="checkbox" id="select_utilisateur.${User.getIdUser()}"
                       name="${User.getIdUser()}" value="${User.getIdUser()}">
                <label class="label-project"
                       for="select_utilisateur.${User.getIdUser()}"> ${User.getPseudo()}</label>
              </div>
            </c:forEach>
          </div>
          <div class="row">
            <h5 class="center h5-add-user">Fonction</h5>
            <div class="radio-add-user left-align">
              <input type="radio" id="chef_de_projet" name="radio_function" value="1">
              <label for="chef_de_projet">Chef de Projet</label>

              <input type="radio" id="developpeur" name="radio_function" value="2" checked>
              <label for="developpeur">Developpeur</label>

              <input type="radio" id="reporter" name="radio_function" value="3">
              <label for="reporter">Reporter</label>
            </div>
          </div>
          <input type="hidden" name="${_csrf.parameterName}"
                 value="${_csrf.token}"/>
          <div class="row center-align connect-btn">
            <button class="waves-effect waves-light btn">
              <i name="submit" type="submit" class="material-icons">done_all</i></button>
          </div>
        </form>
      </div>
    </div>

    <div class="col row-button">
      <a href="/Home" class="btn-action-project offset-l3 waves-effect btn-flat btn"><i
           class="material-icons left">chevron_left</i>Précédent</a>
      <a href="/Home"
         class="btn-action-project offset-l1 waves-effect waves-light btn"><i
           class="material-icons right">done</i>Valider</a>
    </div>
  </div>
</div>

<footer class="page-footer grey darken-4">
  <div class="container">
    <div class="row">
      <div class="col l6 s12">
        <p class="grey-text text-lighten-4">We are a team of college students working on this project like
          it's
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
<script>
    function formSubmit() {
        document.getElementById("logoutForm").submit();
    }
</script>
</body>
</html>
