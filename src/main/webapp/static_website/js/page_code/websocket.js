(function ($) {
    $(function () {

        Deconnexion = function (id) {
            _Subscribes[id].unsubscribe();
        };

        /**
         * On se connecte à un salon
         * @param id
         */
        connexion = function (id) {
            console.log("connexion");
            _Subscribes[id]=_stompClient.subscribe('/topic/' + id, receive);
        };


        //Callback de connection à la socket, on va établir une liaison pour discuter à propos du projet
        connectCallback = function () {
            _Subscribes[_projectid+"-project"]=_stompClient.subscribe('/topic/project/' + _projectid, projectReceive);
            $('#buttonConnected').remove();
            Materialize.toast("Vous êtes connecté.", 4000);
            _Subscribes["connectCallBack"]=_stompClient.subscribe('/topic/project/connect/'+_projectid, ConnectedPeople);

            _IntervalConnectMessages=setInterval(RegularMessages, 10000);

        };
        /* Appelé chaque interval de temps pour signaler qu'on est connecté
         */
        RegularMessages=function(){
            _stompClient.send("/topic/project/connect/"+_projectid,
                {},
                _userId);
            _delayDeconnectPeople-- ;
            if(_delayDeconnectPeople == 0){ // On attend la non réception de 2 messages pour afficher la déconnexion
                for( var id in _ConnectedPeople) {
                    _ConnectedPeople[id]--;
                    if (_ConnectedPeople[id] == 0) { //
                        ConnectedStatusFriend(false, id);
                    }

                }
                _delayDeconnectPeople=2 ;
            }
        };

        // Réception d'un message de connexion
        ConnectedPeople=function(msg) {
            var user=msg.body ;
            if(user == _userId)
                return ;
            if(_ConnectedPeople[user] == undefined || _ConnectedPeople[user] < 3) {
                _ConnectedPeople[user] = 3;
                ConnectedStatusFriend(true, user);
            }
        };


        //Il n'y a plus de connexion au serveur
        errorCallback = function (error) {
            // display the error's message header:
            clearInterval(_IntervalConnectMessages); // On arrete d'envoyer les messages de connexion
            if (error.headers != undefined)
                console.log(error.headers.message);
            Materialize.toast("Vous n'êtes plus connecté.", 4000);
            $(".chat-window").remove() ;
            _Subscribes={};
            if(!$('#buttonConnected').length)
                    $('.conversation-list').prepend("<button class=\"waves-effect waves-light btn-floating btn-reconnect\"id='buttonConnected' onclick='CreateSocket()'><i class=\"material-icons\">autorenew</i></button>");
        };


        /**
         * Souscription à un salon, réception des messages stockés en base de données
         * @param message
         */
        receive=function(message){
            var object=JSON.parse(message.body) ;
            var listmess=object.Messages ;
            if(listmess != undefined) { // On a une liste de messages à insérer
                for (var i = 0; i < listmess.length; i++)    {
                    var mess = listmess[i];
                    InsertMessage(mess.Texte, mess.id, mess.Utilisateur, mess.Date ,  object.discussion);
                }
            }else{ // Uniquement un message
                if(object.type == "modification"){
                    ModifyMessage(object.text, object.date, object.id ) ;
                }else if(object.type=="suppression"){
                    RemoveMessage(object.id);
                }else if(object.type=="creation"){ // C'est une création de message
                    InsertMessage(object.text, object.id, object.name, object.date,object.discussion);
                } // pas de else pour gérer le cas où on fait rien
            }
        };

        projectReceive=function(message){
            var msg=JSON.parse(message.body);
            console.log(msg);
            var content=JSON.parse(msg.content); // Double parsing, pas propre mais fonctionne
            console.log(content);
            if(msg.type == "creation") {
                OpenDiscussion(content.users, msg.discussion);
            }else if(msg.type=="ajout"){
                if(_userId == msg.idUser){ // Si c'est le nouveau membre, on ouvre la discussion
                    OpenDiscussion(content.users, msg.discussion);
                }else{
                    ChangeMembresDiscussion(content.users, msg.discussion);
                }
            }else if(msg.type=="remove"){
                if(_userId == msg.idUser){ // Si c'est le nouveau membre, on ferme la discussion
                    CloseDiscussion(msg.discussion);
                }else{
                    ChangeMembresDiscussion(content.users, msg.discussion);
                }
            }
        };

        /**
         * Envoi d'un message par le chat
         * @param discussion
         * @param message
         * @param type
         * @param id
         */
        sendMessage=function(discussion, message, type, id){
            var idSend= id || -1 ;
            _stompClient.send("/message/"+discussion,
                {},
                JSON.stringify({
                    'text' : message,
                    'type' : type,
                    'id' : id,
                    "discussion": discussion,
                    'name': _userPseudo
                }));
        };


        modifyDiscussion = function(idUser, type, idDiscussion){
            var discussion=idDiscussion || -1;
            _stompClient.send('/message/project/' + _projectid,
                {},
                JSON.stringify({
                    'idUser' : idUser,
                    'type' : type ,
                    'discussion' : discussion,
                    'name': _userPseudo
                }));
        };



        CreateSocket=function(){
            var socket = new SockJS("/message");
            _stompClient = Stomp.over(socket);
            _stompClient.connect("guest", "guest", connectCallback, errorCallback);
        };
        CreateSocket() ;


    });

})(jQuery);