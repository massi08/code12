(function ($) {
    $(function () {

        //Permet d'enregistrer un fichier
        $("#save").click(function () {
            if (_selectedFile == undefined) {
                Materialize.toast("Il n'y a rien à sauvegarder.", 4000);
                return;
            }
            var div = $("#" + _selectedFile);
            var tab = $("#tab-" + _selectedFile);
            var text = ace.edit(_selectedFile).getValue();
            var path = div.attr('data-path');
            var name = div.attr('data-name');
            var request = $.ajax({
                method: "POST",
                url: "/Project/ajax/dev/enregistre",
                dataType: "json",
                data: {
                    texte: text,
                    project: _projectid,
                    arborescence: path,
                    filename: name,
                    _csrf: _csrf
                }
            });
            request.done(function (msg) {
                Materialize.toast("Sauvegarde de " + name + " effectuée", 4000);
                SaveCallback(tab, msg.idFile); // on met à jour l'id du node
            });
            request.fail(function (jqXHR, textStatus) {
                Materialize.toast(name + "- " + jqXHR.responseText, 4000);
            });
        });

        // Permet de compiler un fichier
        $("#compile-project").click(function () {
            var request = $.ajax({
                method: "GET",
                url: "/ajax/compiler",
                dataType: "json",
                data: {
                    idProject: _projectid,
                    idLanguage: 1
                }
            });
            request.done(function (output) {
                $("#compilation-message").html(output);
                $(document).ready(function () {
                    leanModalAction('#compilation-modal', "open", false);
                });
            });
            request.fail(function (jqXHR, textStatus) {
                $("#compilation-message").html(jqXHR.responseText);
                $(document).ready(function () {
                    leanModalAction('#compilation-modal', "open", false);
                });
            });
        });

        //Dévérouillage d'un fichier
        unlockfile = function (idoffile) {
            var request = $.ajax({
                method: "GET",
                url: "/Project/ajax/dev/closeFile",
                data: {
                    idfile: idoffile
                }
            });
            request.done(function (msg) {
                Materialize.toast("Fichier dévérouillé", 4000);
            });
            request.fail(function (jqXHR, textStatus) {
                Materialize.toast(jqXHR.responseText, 4000);
            });
        };

        //Permet de delete un fichier en distant, on veut les delete les uns à la suite pour gérer les erreurs, d'oùle synchrone
        deletefile = function (idoffile, filename) {
            var b;
            var request = $.ajax({
                method: "GET",
                url: "/Project/ajax/dev/deleteFile",
                async: false,
                data: {
                    idfile: idoffile
                },
                success: function (msg, textStatus, jqXHR) {
                    Materialize.toast("Suppression de " + filename, 4000);
                    b = true;
                },
                error: function (msg, textStatus, jqXHR) {
                    Materialize.toast(filename + "- " + msg.responseText, 4000);
                    b = false;
                }
            });
            return b;
        };

        // Permet d'obtenir le texte d'un fichier
        getfile = function (idoffile, node, writeOrRead) {
            var request = $.ajax({
                method: "GET",
                url: "/Project/ajax/getfile",
                dataType: "json",
                timeout: 2000,
                data: {
                    idfile: idoffile,
                    read: writeOrRead
                }
            });
            request.done(function (msg) {
                var type;
                if (msg.lock == "false") {
                    type = " en lecture."
                } else {
                    type = " en écriture."
                }
                Materialize.toast("Récupération de " + node.text + type, 4000);
                getFileCallback(msg.file, node, msg.text, msg.lock);
            });
            request.fail(function (jqXHR, textStatus) {
                console.log(textStatus);
                if (textStatus != "parsererror") {
                    Materialize.toast(node.text + "- " + jqXHR.responseText, 4000);
                } else {
                    console.log(jqXHR);
                    Materialize.toast("Erreur dans le parsing json, vous avez un mauvais caractère", 4000);
                }
            });
        };

        // Requête de sauvegarde des changements de noms et de path d'un fichier
        SaveMetas = function (idoffile, filename, path) {
            console.log("I send : " + idoffile + " " + filename + " " + path);
            var b;
            var request = $.ajax({
                method: "GET",
                url: "/Project/ajax/dev/EntregistreMetas",
                async: false,
                data: {
                    idfile: idoffile,
                    filename: filename,
                    path: path
                },
                success: function (data, textStatus, jqXHR) {
                    b = true;
                    Materialize.toast("Sauvegarde réussie de " + filename, 4000);
                },
                error: function (data, textStatus, jqXHR) {
                    Materialize.toast(filename + "- " + data.responseText, 4000);
                    b = false;
                }
            });
            return b;
        };

        getDiscussionMembers = function (idDiscussion) {
            var request = $.ajax({
                method: "GET",
                url: "/Project/ajax/DiscussionNotMembers",
                dataType: "json",
                data: {
                    discussion: idDiscussion
                }
            });
            request.done(function (msg) {
                console.log(msg);
                chatCallbackAddMember(msg.users, idDiscussion);
            });
            request.fail(function (jqXHR, textStatus) {
                console.log(jqXHR);
                Materialize.toast(jqXHR.responseText, 4000);
            });
        };

        //Permet d'enregistrer un fichier
        $("#commit-submit-modal").click(function () {
            leanModalAction("#commit-modal", "close", true);
            var request = $.ajax({
                method: "POST",
                url: "/ajax/gitcommit",
                data: {
                    message: $("#commit_message").val(),
                    project: _projectid,
                    _csrf: _csrf
                }
            });
            request.done(function (msg) {
                Materialize.toast("Commit réalisé avec succès", 4000);
            });
            request.fail(function (jqXHR, textStatus) {
                Materialize.toast("Echec du commit", 4000);
                console.log(jqXHR.responseText);
            });
        });

        //Permet d'enregistrer un fichier
        $("#checkout-submit-modal").click(function () {
            leanModalAction("#checkout-modal", "close", true);
            var id = $("#idFileHidden").val();
            var node = _Project_tree.jstree().get_node('file-' + id);
            var request = $.ajax({
                method: "POST",
                url: "/ajax/gitcheckout",
                dataType: "json",
                data: {
                    idFile: id,
                    idCommit: $('#listCommit').val(),
                    project: _projectid,
                    _csrf: _csrf
                }
            });
            request.done(function (msg) {
                if (msg.output != "") {
                    Materialize.toast("Le checkout n'a pas pu aboutir");
                } else {
                    getFileCallback(msg.file, node, msg.text, msg.lock, true);
                    Materialize.toast(node.text + "- Le checkout a été effectué.", 4000);
                }
            });
            request.fail(function (jqXHR, textStatus) {
                Materialize.toast("Echec du checkout");
                console.log(jqXHR.responseText);
            });
        });

        getLogs = function (id) {
            $.ajax({
                method: "POST",
                url: "/ajax/gitlog",
                data: {
                    project: _projectid,
                    idFile: id,
                    _csrf: _csrf
                }
            }).done(function (msg) {
                ModalLogDisplay(msg, id);
            }).fail(function (jqXHR, textStatus) {
                alert(jqXHR.responseText);
            });
        };
    });
})(jQuery);