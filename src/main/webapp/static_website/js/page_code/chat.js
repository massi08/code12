(function ($) {
    $(function () {

        var conversation_list = $(".conversation-list");
        var chat_windows = $(".chat-windows");
        /**
         * Event de fermeture pour les conversation
         */
        conversation_list.on('click', 'a', function () {
            var id = $(this).closest(".conversation")[0].id.substr(12);
            modifyDiscussion(_userId, "remove", id);
        });

        /**
         * event d'ouverture d'une fenêtre de message
         */
        conversation_list.on('click', ".conversation-label", function () {
            var discussion = $(this).closest('.conversation');
            MessagesOpening(discussion[0]);
        });


        //Ouverture d'une discussion à partir d'un membre
        $(".friend-list").on('click', '.friend', function () {
            var conversation = $(this);
            var id = conversation.attr("data-id");
            var res = $('.conversation[data-member="' + id + '"]');
            if (!res.length) //On vérifie que la conversation n'existe pas déjà
                modifyDiscussion(id, "creation");
        });

        //Affichage des messages
        chat_windows.on('click', ".chat-window-label .user", function () {
            ClickLabel(this);
        });

        // Fermeture d'une fenêtre de message, deconnexion de la conversation
        chat_windows.on('click', ".chat-window-label .chat-close", function () {
            var chatWindow = $(this).closest('.chat-window');
            CloseWindow(chatWindow);
        });

        // affichage de la date, etc.... d'un message
        chat_windows.on('click', ".message-wrap .message", function () {
            var messageWrap = $(this).closest('.message-wrap');
            messageWrap.toggleClass("show-message-info");
        });

        //Envoi d'un message
        chat_windows.on('click', "a", function () {
            SendMessageDisplay(this);
        });

        //Envoi d'un message avec entrée
        chat_windows.on('keypress', "input", function (event) {
            if (event.keyCode == 13)
                SendMessageDisplay(this);
        });

        //Event de modification de message
        chat_windows.on('click', "#message-modify", function () {
            var message_wrap = $(this).closest(".message-wrap")[0];
            var input = $(message_wrap).find("p");
            input.attr("contenteditable", true);
            input.focus();
        });

        // Event ouverture fenêtre ajout d'un membre dans uen discussion
        chat_windows.on('click', ".chat-add-people", function () {
            var iddis = $(this).closest(".chat-window")[0].id.substr(19);
            getDiscussionMembers(iddis);
        });

        //Event, fin de modification de message
        chat_windows.on('keypress', "p[contenteditable=true]", function (event) {
            if (event.keyCode == 13) {
                var target = $(event.target);
                var iddis = target.closest(".chat-messages")[0].id.substring(8); // On récupère l'id de la discussion
                var id = target.closest(".message-wrap")[0].id.substr(16); // L'id du message
                sendMessage(iddis, target.text(), "modification", id);
                target.attr("contenteditable", false);
                event.preventDefault();
            }
        });

        //Event de suppression de message
        chat_windows.on('click', "#message-delete", function () {
            var target = $(this);
            var id = target.closest(".message-wrap")[0].id.substring(16);
            var iddis = target.closest(".chat-messages")[0].id.substring(8);
            sendMessage(iddis, "", "suppression", id);
        });


        $("#add-chat-members-modal").on('click', ".friend-to-add", function () {
            var target = $(this);
            console.log(target.attr("data-user"));
            modifyDiscussion(target.attr("data-user"), "ajout", target.attr("data-discussion"));
            leanModalAction("#add-chat-members-modal", "close");
        });

        /**
         * Ouverture d'une discussion
         * @param members
         * @param id
         * @constructor
         */
        OpenDiscussion = function (members, id) {
            var conversationList = $(".conversation-list");
            var memberData = "";
            var list = ConstructUserNameList(members);
            if (!list.mustPrint)
                return;
            if (members.length == 1) {
                memberData = " data-member=\"" + members[0].pseudo + "\"";
            }
            var toAppend = $("" +
                "<div id=\"discussions-" + id + "\" class=\"conversation waves-effect\"" + memberData + " >" +
                "<i class=\"material-icons\">message</i>" +
                "<div class=\"conversation-label\">" +
                list.data +
                "</div>" +
                "<a>" +
                "<i class=\"close material-icons\">close</i>" +
                "</a>" +
                "</div>");
            conversationList.append(toAppend);

        };

        /**
         * Modifie dans l'interface la liste de membres d'une conversation
         * @param members
         * @param id
         * @constructor
         */
        ChangeMembresDiscussion = function (members, id) {
            var list = ConstructUserNameList(members);
            var conversation = $("#discussions-" + id); // On récupère la conversation
            console.log(conversation);
            if (!conversation.length || !list.mustPrint) {// Si elle existe pas déjà ou si il en fait pas parti
                console.log(list.mustPrint);
                return;
            }
            $(conversation[0]).find(".conversation-label").text(list.data);
            if ($("#window-discussions-" + id).length)
                $("#window-discussions-" + id + " .chat-window-label .user").text(list.data);
        };

        /**
         * Constuit une liste de nom à afficher
         * @param members
         * @returns {{mustPrint: boolean, data: string}}
         * @constructor
         */
        var ConstructUserNameList = function (members) {
            userList = "";
            var b = false;
            for (var i = 0; i < members.length; i++) {
                if (members[i].pseudo != _userPseudo) { // On veut pas qu'il s'affiche lui meme
                    userList += members[i].pseudo;
                    if (!(i == members.length - 1 || (i == members.length - 2 && members[i + 1].pseudo == _userPseudo)))
                        userList += " , ";
                } else {
                    b = true; // On vérifie l'utilisateur appartient à la discussion
                }
            }
            return {"mustPrint": b, "data": userList}
        };

        /**
         * Construction de la fenêtre modale d'ajout de membre.
         * @param members
         * @param idDiscussion
         */
        chatCallbackAddMember = function (members, idDiscussion) {
            var friendList = $("#add-chat-members");
            $(".friend-to-add").remove();
            for (var i = 0; i < members.length; i++) {
                var toAppend = $("" +
                    "<div class=\"friend-to-add waves-effect waves-light\" data-discussion=\"" + idDiscussion + "\" data-user=\"" + members[i].id + "\">" +
                    "<i class=\"material-icons\">person_outline</i>" +
                    "<div class=\"friend-name\">" +
                    members[i].pseudo +
                    " </div>" +
                    "<i class=\"status is-connected material-icons\">fiber_manual_record</i>" +
                    "</div>");
                friendList.append(toAppend);
            }
            leanModalAction("#add-chat-members-modal", "open");

        };

        /**
         * Clique sur une de ses conversations ouvertes.
         * @param target Attribut un objet jquery interne
         * @constructor
         */
        ClickLabel = function (target) {
            var chatWindow = $(target).closest('.chat-window');
            chatWindow.toggleClass("selected");
            var chatMessages = chatWindow.find(".chat-messages");
            var users=chatWindow.find('.user');
            var newMessage=users.text().indexOf(" !");
            if(newMessage != -1 )
                users.text(users.text().substring(0, newMessage)); // On enlève la notification de nouveaux message
            if (chatWindow.hasClass("selected"))
                chatMessages.scrollTop(chatMessages[0].scrollHeight);
        };

        /**
         * Fermeture d'une discussion
         * @param idDiscussion
         * @constructor
         */
        CloseDiscussion = function (idDiscussion) {
            if (_Subscribes[idDiscussion] != undefined) // Si la conversation est ouverte on ferme
                CloseWindow($("#window-discussions-" + idDiscussion));
            $("#discussions-" + idDiscussion).remove(); // On ferme la conversation
        };

        /**
         * INterface de l'ouverture et souscription à une discussion
         * @param target
         * @constructor
         */
        MessagesOpening = function (target) {
            var iddiscussion = target.id.substring(12);
            var idwindowdiscussion = "window-" + target.id;
            if (_Subscribes[iddiscussion] == undefined) {
                var value = $(target).find(".conversation-label").text();
                var toappend = $("" +
                    "<div class=\"chat-window append\" id=" + idwindowdiscussion + ">" +
                    "<div class=\"chat-window-label\">" +
                    "<span class=\"user\">" + value + "</span>" +
                    "<div class=\"chat-close\">" +
                    " <i class=\"material-icons close\">close</i>" +
                    " </div>" +
                    "<div class='chat-add-people'>" +
                    "<i class=\"material-icons\">person_add</i>" +
                    "</div>" +
                    "</div>" +
                    "<div class=\"chat-window-messages\">" +
                    "<div class=\"chat-messages\" id=message-" + iddiscussion + ">" +
                    "</div>" +
                    "<div class=\"chat-input valign-wrapper\">" +
                    "<input type=\"text\"> </input>" +
                    "<a class=\"valign\"><i class=\"material-icons\">send</i></a>" +
                    "</div>" +
                    "</div>" +
                    "</div>");
                $('.chat-row').append(toappend);
                setTimeout(function () {
                    toappend.removeClass("append");
                }, 0);
                connexion(iddiscussion);
                ClickLabel(toappend.find(".chat-window-label"));
            }
        };

        /**
         * Fermeture et désinscription d'une discussion
         * @param chatWindow
         * @constructor
         */
        CloseWindow = function (chatWindow) {
            var iddiscussion = chatWindow[0].id.substr(19);
            Deconnexion(iddiscussion);
            chatWindow.addClass("append");
            setTimeout(function () {
                chatWindow.remove();
            }, 600);
            delete _Subscribes[iddiscussion];
        };

        /**
         *  Envoi d'un message aux participants
         * @param target
         * @constructor
         */
        SendMessageDisplay = function (target) {
            var messageWindow = $(target).closest('.chat-window-messages');
            var iddis = messageWindow.find(".chat-messages")[0].id.substr(8);
            var input = messageWindow.find("input");
            var message = input.val();
            message = message.replace(/</g, "&lt;");
            message = message.replace(/>/g, "&gt;");
            message = message.replace(/"/g, "&quot;");
            if (message.trim() != "") { // On n'envoie pas de message vide
                sendMessage(iddis, message, "creation");
                input.val("");
            }
        };

        /**
         * Insertion d'un message dans l'interface
         * @param texte
         * @param id
         * @param name
         * @param date
         * @param discussion
         * @constructor
         */
        //Inserstion d'un message dans l'interface
        InsertMessage = function (texte, id, name, date, discussion) {
            var classUser, enable, pseudo;
            var discussion = $("#message-" + discussion);
            if (name == _userPseudo) { // La classe change si c'est son propre message et il peut modifier les messages
                classUser = "";
                enable = "<span id=\"message-modify\"> Modifier </span> " +
                    "<span id=\"message-delete\"> Supprimer </span> ";
                pseudo = "";
            } else {
                classUser = "other";
                enable = "";
                pseudo = "<span class=\"user-pseudo\">" + name + "</span>";
            }
            var messageview = $("" +
                "<div class=\"message-wrap " + classUser + "\" " + " id=\"message-wrap-id-" + id + "\"data-user=\"" + name + " \">" +
                pseudo +
                "<div class=\"message z-depth-1\">" +
                "<p>" + texte + "</p> " +
                "</div>" +
                "<div class=\"message-info\">" +
                "<span id=\"message-date\">" + date + "</span> " +
                enable +
                "</div>" +
                "</div>");
            discussion.append(messageview);
            var chatWindow = $(discussion).closest('.chat-window');
            var chatMessages = chatWindow.find(".chat-messages");
            if (chatWindow.hasClass("selected")) {
                chatMessages.scrollTop(chatMessages[0].scrollHeight);
            }else{
                var users=chatWindow.find(".user") ;
                users.text(users.text()+" !");
            }
        };

        /**
         * Modification d'un message dans l'interface
         * @param texte
         * @param date
         * @param id identificateur du message
         * @constructor
         */
        ModifyMessage = function (texte, date, id) {
            var messageWrap = $("#message-wrap-id-" + id);
            if (messageWrap.length == 0)
                return;
            $(messageWrap[0]).find(".message-date").text(date);
            $(messageWrap[0]).find("p").text(texte);
        };

        /**
         * Suppression d'un message dans le dom
         * @param id
         * @constructor
         */
        RemoveMessage = function (id) {
            var messageWrap = $("#message-wrap-id-" + id);
            console.log(messageWrap);
            if (messageWrap.length == 0)
                return;
            messageWrap.addClass("remove-message");
            setTimeout(function () {
                messageWrap[0].remove();
            }, 600);
        };

        ConnectedStatusFriend=function(connected, id){
            var imageConnected=$(".friend[data-id="+id+"] .status");
            if(connected)
                imageConnected.addClass("is-connected");
            else
                imageConnected.removeClass("is-connected");

        }

    });

})(jQuery);