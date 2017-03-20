(function ($) {
    $(function () {

        /**
         * On incrémente un possible id jusqu'à tomber sur un id non déjà stocké en local
         * @returns {number|*}
         * @constructor
         */
        GenereIdEditor = function () {
            if (_NumberEditor == undefined) {
                _NumberEditor = 0;
            }
            _NumberEditor++;
            while (localStorage[_projectid+"-editor-"+_NumberEditor+"_"+_userId] != undefined) {
                _NumberEditor++;
            }
            return _NumberEditor;
        };

        /**
         * Evènement de local storage
         */
        $(window).bind('keydown', function (event) {
            if (event.ctrlKey) {
                var b =false ;
                switch (String.fromCharCode(event.which).toLowerCase()) {
                    case 's': //Sauvegarde locale du fichier courant
                        b=true ;
                        if (_selectedFile == undefined) {
                            Materialize.toast("Il n'y a aucun fichier à sauvegarder localement.", 4000);
                            break;
                        }
                        console.log(_selectedFile);
                        LocalSave(_selectedFile);
                        Materialize.toast("Sauvegarde locale effectuée", 4000);
                        break;
                    case '3': // Vide entièrement localStorage
                        b=true ;
                        localStorage.clear();
                        Materialize.toast("Suppression des fichiers locaux", 4000);
                        break ;
                    case '2': //Vide un projet
                        b=true ;
                        for (i = 0; i < localStorage.length; i++) {
                            var key = localStorage.key(i);
                            var idlocal = key.substr(0, key.indexOf("-"));
                            if (idlocal == _projectid)
                                localStorage.removeItem(key);
                        }
                        Materialize.toast("Suppression des fichiers locaux du projet.", 4000);
                        break ;
                    case '1': // Recharge le fichier courant
                        b=true ;
                        if (_selectedFile == undefined) {
                            Materialize.toast("Sélectionnez un ficher à charger.", 4000);
                            break;
                        }
                        if (localStorage[_projectid + "-" + _selectedFile+"_"+_userId] == undefined) {
                            Materialize.toast("Ce fichier n'a pas de sauvegarde.", 4000);
                            break;
                        }
                        var val=JSON.parse(localStorage[_projectid + "-" + _selectedFile+"_"+_userId]).text ;
                        ace.edit(_selectedFile).setValue(val);
                        Materialize.toast("Rechargement du fichier.", 4000);
                        break ;
                }
                if(b) {
                    event.preventDefault();
                }

            }
        });

        /**
         * On sauvegarde tous les fichiers avant de quitter la page
         */
        $(window).on('beforeunload ', function () {
            //localStorage.clear();
            var allEditors = $('div[id^="editor-"]');
            $.each(allEditors, function () {
                LocalSave(this.id);
            });
        });


        /*
         Sauvegarde d'un fichier, identifier l'id de l'editor
         */
        LocalSave = function (editor) {
            editor = $("#" + editor); // récupération de l'editor
            var ideditor = editor.attr('id');
            var tab = $("#tab-" + ideditor); // récupération de la tab
            var idnode = tab.attr("data-idjstree");
            var node = _Project_tree.jstree().get_node(idnode); // récupération du noeud
            if(node.data == undefined)
                return ;
            if (node.data.haslock == "false ")
                return;
            var idfile = node.data.idfile;
            var Localitem = {
                "node": {
                    'idnode': idnode,
                    'path': editor.attr("data-path"),
                    'name': node.text,
                    "haslock": node.data.haslock,
                    "root": node.parents[node.parents.length - 2] // Le noeud du projet
                },
                "tab": tab.attr('id'),
                "file": idfile,
                "editor": ideditor,
                "text": ace.edit(ideditor).getValue(),
            };
            var idstore = _projectid + "-" + ideditor+"_"+_userId; // On veut un id unique, il dépend donc de l'editor, du projet et de l'user
            localStorage[idstore] = JSON.stringify(Localitem);
        };

        DeleteSave = function (ideditor) {
            localStorage.removeItem(_projectid + "-" + ideditor +"_"+_userId);
        };

        /**
         * La fonction est appelée lorsque l'arbre jstree a fini de se loader. Sinon la récupération de noeud bug.
         * Elle permet de récupérer les fichiers locaux.
         * @constructor
         */
        LoadStoreFiles = function () {
            for (i = 0; i < localStorage.length; i++) {
                var key = localStorage.key(i);
                var idlocal = key.substr(0, key.indexOf("-"));
                var userSave=key.substr(key.indexOf("_")+1);
                if (idlocal != _projectid || userSave != _userId)
                    continue ;
                var filestore = JSON.parse(localStorage[key]);
                var tabandeditor = CreateTabAndEditor(filestore.node.name, filestore.node.path, filestore.editor, filestore.node.haslock);
                tabandeditor.tab.attr("data-idjstree", filestore.node.idnode);
                if (filestore.file == undefined) { // Le fichier est pas en BDD donc on doit générer le noeud nous même
                    if (pack == undefined) { // Création d'un package virtuel, une fois sauvegardé, ce sera replacé dans la hiérarchie
                        var root = _Project_tree.jstree().get_node(filestore.node.root);
                        var idpackage = _Project_tree.jstree('create_node', root, {
                            "text": "unsaved files",
                            "type": "package",
                            "id": "unsaved-files-jstree",
                            "data": {
                                "haslock": "true"
                            }
                        });
                        var pack = _Project_tree.jstree().get_node(idpackage); // besoin du noeud parent
                    }
                    var idnode = _Project_tree.jstree('create_node', pack, { //On créé le noeud qui n'était pas enregistré avec les données
                        "text": filestore.node.name,
                        "type": "file",
                        "id": filestore.node.idnode,
                        "data": {
                            "ideditor": filestore.editor,
                            "haslock": filestore.node.haslock,
                            "opened": "opened"
                        }
                    });
                } else { // Cas où le fichier existe en base de donnée
                    var node = _Project_tree.jstree().get_node(filestore.node.idnode);
                    if(node===false){//Le fichier a visiblement été détruit entre temps
                        DeleteSave(filestore);
                        continue ;
                    }
                    node.data.ideditor = filestore.editor;
                    node.data.opened = "opened";
                    node.data.haslock = filestore.node.haslock;
                }
                if (filestore.node.haslock == "false") {
                    InitialiseEditor(tabandeditor.ideditor, true).setValue(filestore.text); // readonly
                } else {
                    InitialiseEditor(tabandeditor.ideditor, false).setValue(filestore.text);
                }
            }
            if (tabandeditor != undefined)
                Clickontab(tabandeditor.tab, tabandeditor.ideditor);
        };
    });

})(jQuery);