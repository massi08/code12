<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page session="true" %>
<html>
<head>
  <link type="text/css" rel="stylesheet" href="/static_website/css/materialize.min.css" media="screen,projection"/>
  <link href="/static_website/css/style.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/code.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/js_tree_style.min.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/js_tree_theme.css" type="text/css" rel="stylesheet"/>
  <link href="/static_website/css/custom_jstree.css" type="text/css" rel="stylesheet"/>
  <!--Let browser know website is optimized for mobile-->
  <link rel="icon" type="image/png" href="/static_website/img/favicon.png"/>
  <title>Code</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
</head>

<body>

<jsp:include page="header_2.jsp">
  <jsp:param name="projects" value=""></jsp:param>
  <jsp:param name="account" value=""></jsp:param>
  <jsp:param name="parameters" value=""></jsp:param>
</jsp:include>

<div class="container-full fullscreen">
  <input style="display:none" accept="text/plain" type="file" name="fileupload" id="code-12-fileupload"
         onchange="upload(event)">
  <div class="row row-full">
    <div class="col s1 project-management z-depth-1 full-height grey darken-4">
      <div class="col col-ide">
        <div class="row row-icon">
          <a href="#" data-activates="edit-slide" class="button-menu-collapse"><i
               class="material-icons center">mode_edit</i></a>
        </div>
        <div class="row row-icon">
          <a id="compile-project">
            <i class="material-icons">play_arrow</i>
          </a>
        </div>
        <div class="row row-icon">
          <a href="#" id="save" class="show-on-large"><i class="material-icons">save</i></a>
        </div>
        <div class="row row-icon">
          <a href="#" id="open-commit-modal">
            <svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 36 36">
              <title>Git-Icon</title>
              <path
                   d="M323.66,375.49l-15.72-15.72a2.32,2.32,0,0,0-3.28,0L301.4,363l4.14,4.14a2.75,2.75,0,0,1,3.49,3.51l4,
                                    4a2.73,2.73,0,1,1-1.68,1.56l-3.75-3.72v9.79a5.31,5.31,0,0,0,.76.52,2.75,2.75,0,1,1-3.9,0,9,9,0,0,0,.89-0.6v-9.88a9.06,9.06,0,0,
                                    0-.88-0.6,2.75,2.75,0,0,1-.58-3l-4.08-4.08L289,375.42a2.32,2.32,0,0,0,0,3.28l15.72,15.72a2.32,2.32,0,0,0,3.28,0l15.64-15.64A2.32,2.32,0,0,0,323.66,375.49Z"
                   transform="translate(-288.34 -359.09)"/>
            </svg>
          </a>
        </div>
        <div class="row row-icon">
          <a href="#" data-activates="chat-slide" class="button-menu-collapse show-on-large"><i
               class="material-icons">chat</i></a>
        </div>
      </div>

      <div class="col col-project">
        <div class="row row-icon">
          <a href="ManageTickets" class="">
            <svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 48 48">
              <title>Ticket-Icon</title>
              <path class="cls-1"
                    d="M325.51,392.45l-2-12.42a4.17,4.17,0,0,0-3.46-3.46l-12.42-2a4.17,4.17,0,0,0-3.6,1.17L278.78,401a4.17,4.17,0,0,0,0,5.89l14.39,
                                  14.39a4.17,4.17,0,0,0,5.89,0l25.28-25.28A4.17,4.17,0,0,0,325.51,392.45ZM298,418.1a1.63,1.63,0,0,1-2.3,0L282,404.43a1.63,1.63,0,0,
                                  1,2.3-2.3L298,415.8A1.63,1.63,0,0,1,298,418.1Zm18.39-29.93a3.14,3.14,0,1,1,0-4.44A3.14,3.14,0,0,1,316.37,388.17Z"
                    transform="translate(-277.56 -374.55)"/>
            </svg>
          </a>
        </div>

        <div class="row row-icon">
          <a href="createZIP" class="">
            <i class="material-icons">file_download</i>
          </a>
        </div>

        <div class="row row-icon">
          <a href="#" data-activates="news-slide" class="button-menu-collapse"><i
               class="material-icons">wb_sunny</i></a>
        </div>
        <c:if test="${userRole.getIdRole()==1}">
          <div class="row row-icon">
            <a href="modifyProject" data-activates="settings-slide" class=""><i
                 class="material-icons">settings</i></a>
          </div>
        </c:if>

      </div>
    </div>

    <div class="col s11 code-editor">
      <div class="row no-margin">
        <div class="col s12 no-padding tabs-wrapper">
          <ul class="tabs">
          </ul>
        </div>
      </div>

      <div id="compilation-modal" class="modal bottom-sheet">
        <div class="modal-content">
          <div id="compilation-modal-close"><i class="material-icons">close</i></div>
          <h5 class="center">Console</h5>
          <div id="compilation-message"></div>
        </div>
      </div>

      <div id="open-compilation-modal"><a class="btn-floating btn-large waves-effect waves-light"><i
           class="material-icons">keyboard_arrow_up</i></a>
      </div>
    </div>

    <div class="chat-windows">
      <div class="chat-row row">
      </div>
    </div>

    <div id="edit-slide" class="side-nav custom-side-nav">
      <h5 class="center-align">Fichiers</h5>
      <div id="project_tree"></div>
    </div>

    <div id="chat-slide" class="side-nav custom-side-nav">
      <h5 class="center-align"> Chat </h5>
      <div class="chat-slide-wrapper">
        <h6>Conversations</h6>
        <div class="conversation-list">
          <c:forEach var="discussion" items="${discussions}">
            <!--Permet de retrouver la conversation avec l'id d'un user -->
            <div id="discussions-${discussion.key}" class="conversation waves-effect"
                 <c:if test="${discussion.value.getName().size() == 1}">
                   data-member="${discussion.value.getName().get(0).getIdUser()}"
                 </c:if>
            >
              <i class="material-icons">message</i>
              <div class="conversation-label">
                <c:set var="count" value="0" scope="page"/>
                <c:forEach var="namediscussion" items="${discussion.value.getName()}">
                  <c:set var="count" value="${count + 1}" scope="page"/>
                  ${namediscussion.getPseudo()}
                  <c:if test="${count != discussion.value.getName().size()}">
                    <c:out value=","/>
                  </c:if>
                </c:forEach>
              </div>
              <a>
                <i class="close material-icons">close</i>
              </a>
            </div>
          </c:forEach>
        </div>
        <h6>Amis</h6>
        <div class="friend-list">
          <c:forEach var="member" items="${members}">
            <div class="friend waves-effect" data-id="${member.getIdUser().getIdUser()}"
                 data-name="${member.getIdUser().getPseudo()}">
              <i class="material-icons">person_outline</i>
              <div class="friend-name">
                  ${member.getIdUser().getPseudo()}
              </div>
              <i class="status material-icons">fiber_manual_record</i>
            </div>
          </c:forEach>
        </div>
      </div>
    </div>
    <div id="news-slide" class="side-nav custom-side-nav">
      <h5 class="center-align"> News </h5>
      <c:forEach var="News" items="${listNews}">
        <div class="news-card">
          <span>${News}</span>
        </div>
      </c:forEach>
    </div>
  </div>
</div>

<div id="add-chat-members-modal" class="modal">
  <div class="modal-content">
    <div id="add-chat-members-modal-close"><i class="material-icons">close</i></div>
    <h5 class="center">Ajout des Membres</h5>
    <div id="add-chat-members">
      <div class="friend-to-add waves-effect waves-light" data-id="2" data-name="test">
        <i class="material-icons">person_outline</i>
        <div class="friend-name">
          test
        </div>
        <i class="status is-connected material-icons">fiber_manual_record</i>
      </div>
    </div>
  </div>
</div>

<div id="add-node-modal" class="modal">
  <div class="modal-content">
    <h4 class="add-node-name-modal"></h4>
    <div class="row">
      <div class="row modal-form-row">
        <div class="input-field col s12">
          <input name="nodeName" id="add-node-input" type="text" class="validate">
          <label id="add-node-input-label" for="add-node-input"></label>
        </div>
      </div>
    </div>
  </div>
  <div class="modal-footer">
    <a id="add-node-close" class="waves-effect waves-blue btn-flat">Annuler</a>
    <a class="add-node-modal-submit modal-action waves-effect waves-blue btn-flat">Soummetre</a>
  </div>
</div>


<div id="commit-modal" class="modal">
  <div class="modal-content">
    <h4>Git Commit</h4>
    <div class="row">
      <form class="col s12">
        <div class="row modal-form-row">
          <div class="input-field col s12">
            <input id="commit_message" type="text" class="validate">
            <label for="commit_message">Message de commit</label>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="modal-footer">
    <a id="commit-close-modal" class="modal-close waves-effect waves-green btn-flat">Annuler</a>
    <a id="commit-submit-modal" class="modal-action modal-close waves-effect waves-green btn-flat">Envoyer</a>
  </div>
</div>

<div id="checkout-modal" class="modal">
  <div class="modal-content">
    <label>Liste des commits</label>
    <select id="listCommit" class="browser-default">
    </select>
  </div>
  <div class="modal-footer">
    <input id="idFileHidden" type="hidden" value="">
    <a id="checkout-close-modal" class="waves-effect waves-green btn-flat">Annuler</a>
    <a id="checkout-submit-modal" class="modal-action waves-effect waves-green btn-flat">Confirmer</a>
  </div>
</div>
<div id="popup-overlay"></div>


<script type="text/javascript" src="/static_website/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/static_website/js/materialize.min.js"></script>
<script type="text/javascript" src="/static_website/js/init.js"></script>
<script type="text/javascript" src="/static_website/js/sockjs-0.3.min.js"></script>
<script type="text/javascript" src="/static_website/js/stomp.js"></script>
<script>
    _csrf = "${_csrf.token}"; // le champs csrf pour les requetes post ;
    _jsondata = JSON.parse('${jstree}'); //Notre json pour jstree
    _projectid = "${id}"; // L'id du projet
    _git = "${git}";
    _selectedFile = undefined; // Sert à savoir quel fichier est sélectionné
    _NumberEditor = undefined;  // Sert à identifier les editors
    _Project_tree = $("#project_tree"); // L'id de notre tree qu'on utilise souvent : on veut éviter de trop parcourir le dom
    _ActualPathImport = undefined;
    _FileImport = undefined;
    _userPseudo = "${userPseudo}";
    _userId = "${userId}";
    _Subscribes = {};
    _languageId = "${languageId}";
    _ConnectedPeople = {};
    _delayDeconnectPeople = 2;
</script>
<script type="text/javascript" src="/static_website/js/page_code/websocket.js"></script>
<script type="text/javascript" src="/static_website/js/page_code/chat.js"></script>
<script type="text/javascript" src="/static_website/js/code_page.js"></script>
<script type="text/javascript" src="/static_website/js/ace/ace.js"></script>
<script src="/static_website/js/ace/ext-language_tools.js"></script>
<script type="text/javascript" src="/static_website/js/jstree.min.js"></script>
<script type="text/javascript" src="/static_website/js/page_code/file_upload.js"></script>
<script type="text/javascript" src="/static_website/js/page_code/ajax_requests.js"></script>
<script type="text/javascript" src="/static_website/js/page_code/jstree_use.js"></script>
<script type="text/javascript" src="/static_website/js/page_code/localstorage.js"></script>
</body>
</html>
