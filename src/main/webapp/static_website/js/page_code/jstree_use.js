(function ($) {
    $(function () {

        // Permet d'obtenir le path d'un noeud, en regardant ses parents
        var getPath = function (node) {
            var path = "/";
            if (node.parents.length > 1) {
                path += node.text + "/";// On ne veut pas le nom du projet si le fichier est à la racine
            }
            for (i = 0; i < node.parents.length - 2; i++) {
                path = "/" + _Project_tree.jstree().get_node(node.parents[i]).text + path
            }
            console.log(path);
            return path;
        };


        // Description d'un menu ( clique droit )
        var contextitems = function (node) {
            option = null;
            var items = {
                'Ouvrir': {
                    'label': 'Ouvrir',
                    'submenu': {
                        'Lecture': {
                            'label': 'Lecture',
                            'action': function () {
                                var ideditor = node.data.ideditor;
                                var idfile = node.data.idfile;
                                if (ideditor != undefined && node.data.haslock == "false") { // Cas ou le fichier a déjà été chargé
                                    var tab = $('#tab-' + ideditor);
                                    Clickontab(tab, ideditor);
                                    node.data.opened = "opened";
                                } else if (idfile != undefined && idfile != -1) { // cas où le fichier vient de la bdd
                                    getfile(idfile, node, "read");
                                }
                            }
                        },
                        'Ecriture': {
                            'label': 'Ecriture',
                            'action': function () {
                                var ideditor = node.data.ideditor;
                                var idfile = node.data.idfile;
                                if (ideditor != undefined && node.data.haslock == "true") { // Cas ou le fichier a déjà été chargé
                                    var tab = $('#tab-' + ideditor);
                                    Clickontab(tab, ideditor);
                                    node.data.opened = "opened";
                                } else if (idfile != undefined && idfile != -1) { // cas où le fichier vient de la bdd
                                    getfile(idfile, node, "write");
                                }
                            }
                        }

                    }
                },
                'Nouveau': {
                    'label': 'Nouveau',
                    'submenu': {
                        'fichier': {
                            'label': 'Fichier',
                            'action': function () {
                                OpenModalAndGetValue("Entre le nom du fichier", "Nouveau fichier");
                                option = "new-file";
                            }
                        },
                        'package': {
                            'label': 'Package',
                            'action': function () {
                                var filename = OpenModalAndGetValue("Entrez le nom du package", "Nouveau package");
                                option = "new-folder";
                            }
                        }
                    }
                },
                'Fermer': {
                    'label': 'Fermer',
                    'action': function () {
                        var ideditor = node.data.ideditor;
                        var found = $("#tab-" + ideditor);
                        found.find(".close").click();
                    }
                },
                'Supprimer': {
                    'label': 'Supprimer',
                    'action': function () {
                        if (node.type == "file") {
                            if (node.data.idfile != undefined) { // On détruit que s'il existe en distant
                                if (deletefile(node.data.idfile, node.text)) // destruction ficheir distant
                                    deleteCallback(node);
                            } else {
                                deleteCallback(node)
                            }
                        } else if (node.type == "package") {
                            Recursive_Package_Delete(node);
                        }
                    }
                },
                'Renommer': {
                    'label': 'Renommer',
                    'action': function () {
                        OpenModalAndGetValue("Entrez le nouveau nom", "Nouveau fichier");
                        option = "rename";
                    }
                },
                'Importer': {
                    'label': 'Importer',
                    'action': function () {
                        _ActualPathImport = node;
                        $("#code-12-fileupload").click();
                    }
                },
                'Checkout': {
                    'label': 'Checkout',
                    'action': function () {
                        if (node.data.idfile != undefined) {
                            var id = node.data.idfile;
                            getLogs(id);
                        }
                    }
                }

            };
            if (node.type == "package") {
                delete items.Renommer;
                delete items.Ouvrir;
            }
            if (node.type == "file") {
                delete items.Nouveau;
                delete items.Importer;
            }
            if (node.data.opened != "opened") { // si le fichier n'est pas ouvert
                delete items.Fermer; // On ne peut pas le fermer.
            } else {
                if (node.data.haslock == "false") {
                    delete items.Ouvrir.submenu.Lecture;
                } else {
                    delete items.Ouvrir.submenu.Ecriture;
                }
            }
            if (node.data.haslock == "false") {
                delete items.Supprimer;
            }
            if (node.data.idfile == undefined) {
                delete items.Ouvrir;
            }
            if (_git == "false" || node.data.idfile == undefined || node.data.idfile == -1) {
                delete items.Checkout;
            }
            return items;
        };

        $("#add-node-close").click(function () {
            leanModalAction("#add-node-modal", "close", true);
        })

        $(".add-node-modal-submit").click(function () {
            var value;
            var nodeCopy = $('#project_tree').jstree(true).get_selected(true)[0];
            if ($("#add-node-input").val().trim() !== "") {
                value = $("#add-node-input").val().trim();
            }
            $("#add-node-input").val("");
            leanModalAction("#add-node-modal", "close", true);
            if (value == null)
                return;

            switch (option) {
                case "new-file":
                    Create_File(nodeCopy, value);
                    break;

                case "new-folder":
                    var b = false;
                    for (i = 0; i < nodeCopy.children.length; i++) {
                        var name = _Project_tree.jstree().get_node(nodeCopy.children[i]).text;
                        if (name == value) {
                            b = true;
                            break;
                        }
                    }
                    if (!b) {
                        var id = _Project_tree.jstree('create_node', nodeCopy, {
                            "text": value,
                            "type": "package",
                            "data": {
                                "haslock": "true"
                            }
                        });
                    }
                    break;

                case "rename":
                    if (nodeCopy.type == "file" && nodeCopy.data.idfile != undefined) {
                        var node2 = _Project_tree.jstree().get_node(nodeCopy.parent); // On veut la position du noeud parent
                        var newPath = getPath(node2);
                        if (SaveMetas(nodeCopy.data.idfile, value, newPath)) { // On sauvegarde son nom
                            MetasCallback(nodeCopy, value, newPath);
                        }
                    } else {
                        MetasCallback(nodeCopy, value);
                    }
                    break;

                default:
                    return;
            }

        });

        // Initialisation globale de jstree
        $('#project_tree').jstree({
            "core": {
                'themes': {
                    'name': 'proton',
                    'responsive': true
                },
                'check_callback': function (operation, node, node_parent, node_position, more) {
                    if (operation === "move_node") { // On vérifie l'emplacement avant de terminer le dragndrop
                        if (node.data.idfile != undefined) {
                            var filename = node.text;
                            var newpath = getPath(node_parent);
                            var oldParentNode = _Project_tree.jstree().get_node(node.parent);
                            if (newpath != getPath(oldParentNode)) {
                                if (!SaveMetas(node.data.idfile, filename, newpath)) {
                                    return false;// On veut pas de changement si la sauvegarde a échoué
                                }
                            } else {
                                return false;
                            }
                            MetasCallback(node, filename, newpath);
                        }
                    }
                    return true;
                },
                "data": _jsondata
            },
            "plugins": ["themes", "contextmenu", "types", "unique", "wholerow", "dnd", "sort"],
            "contextmenu": {
                'items': contextitems
            },
            "dnd": {
                open_timeout: 10,
                check_while_dragging: false // Important, on vérifie la possibilité de déplacer le fichier que lorsqu'on le lache
            },
            "types": {//Différencie les packages des files
                "#": {
                    "valid_children": ["package", "file"]
                },
                "package": {
                    "valid_children": ["file", "package"]
                },
                "file": {
                    "valid_children": [""]
                }
            }
        });


        // Gère les différents évents de jstree
        _Project_tree
            .bind('ready.jstree', function (e, data) {
                LoadStoreFiles(); // On attend que l'arbre aie fini de se loader pour charger les fichiers locaux sinon ca fait buguer
            })
            .on('select_node.jstree', function (e, data) {
            })
            .on('create_node.jstree', function (e, data) {
                _Project_tree.jstree('deselect_all');
                _Project_tree.jstree('select_node', data.node);
            })
            .on('dblclick', '.jstree-anchor', function (e) {
                var instance = $.jstree.reference(this);
                var node = instance.get_node(this); // récupération du node
                if (node.type == "package") {
                    return;
                }

                var ideditor = node.data.ideditor;
                var idfile = node.data.idfile;
                if (ideditor != undefined) { // Cas ou le fichier a déjà été chargé
                    var tab = $('#tab-' + ideditor);
                    Clickontab(tab, ideditor);
                    node.data.opened = "opened";
                } else if (idfile != undefined && idfile != -1) { // cas où le fichier vient de la bdd
                    getfile(idfile, node, "write");
                }
            });

        Recursive_Package_Delete = function (node) {
            var success = true; // Permet de savoir si on doit supprimer le noeud package
            for (var i = node.children_d.length - 1; i > -1; i--) {// Permet de détruire les fichiers d'un package avec ce même package
                var node2 = _Project_tree.jstree().get_node(node.children_d[i]);
                if (node2.type == "file") {
                    if (node2.data.idfile != undefined) { // On détruit que s'il existe en distant
                        success = deletefile(node2.data.idfile, node2.text); // destruction ficheir distant
                        if (success)
                            deleteCallback(node2);
                    } else {
                        deleteCallback(node2);
                    }
                } else { // C'est un package on appelle la fonction récursive
                    success = Recursive_Package_Delete(node2);
                }
            }
            if (success && node.parents.length > 1)
                _Project_tree.jstree('delete_node', node); // destruction du noeud jstree
            return success;
        };

        Create_File = function (node, filename) {
            var b = false;
            // On vérifie d'abord qu'aucun fichier de même nom existe. le plugin unique ne prévoit pas d'event
            for (i = 0; i < node.children.length; i++) {
                var name = _Project_tree.jstree().get_node(node.children[i]).text;
                if (name == filename) {
                    b = true;
                    Materialize.toast(filename + "- Le fichier existe déjà.", 4000);
                }
            }
            if (!b) {
                //Placement de la tab avec ses données
                var tabandeditor = CreateTabAndEditor(filename, getPath(node));
                var ideditor = tabandeditor.ideditor;
                //On pense à créer le node, evite de reactualiser la page
                var id = _Project_tree.jstree('create_node', node, {
                    "text": filename,
                    "type": "file",
                    "id": "jstree-local-" + ideditor, //On est sur qu'il est unique, plus sûr à manipuler, j'avais des bugs
                    "data": {
                        "ideditor": ideditor,
                        "haslock": "true",
                        "opened": "opened"
                    }
                });
                tabandeditor.tab.attr("data-idjstree", id);
                var editor = InitialiseEditor(ideditor);
                Clickontab(tabandeditor.tab, ideditor);
                return editor;
            }
            return undefined;
        };

        OpenModalAndGetValue = function (messageLabel, inputLabel, idOfSubmitButton) {
            $("#add-node-modal .add-node-name-modal").html(messageLabel);
            $("#add-node-modal #add-node-input-label").html(inputLabel);
            leanModalAction("#add-node-modal", "open", true);
        };

        getFileCallback = function (res, node, text, lock, loadForced) {
            if (node.data.opened == "opened") { // traitement particulier s'il est déjà ouvert
                if (lock == "false") {
                    ace.edit(node.data.ideditor).setReadOnly(true);
                    node.data.haslock = "false";
                } else {
                    ace.edit(node.data.ideditor).setReadOnly(false);
                    node.data.haslock = "true";
                }
                ChangeLockIcone(res.idFile, lock);
                if (loadForced)
                    ace.edit(node.data.ideditor).setValue(text);
                return;
            }
            var tabAndEditor = CreateTabAndEditor(res.name, res.path, undefined, lock);//on récupère la tab etl'éditor créés
            tabAndEditor.tab.attr("data-idjstree", node.id);// important pour référencer un node depuis les tabs
            if (lock == "false") {
                InitialiseEditor(tabAndEditor.ideditor, true).setValue(text); // readonly
                node.data.haslock = "false";
            } else {
                InitialiseEditor(tabAndEditor.ideditor, false).setValue(text);
                node.data.haslock = "true";
            }
            Clickontab(tabAndEditor.tab, tabAndEditor.ideditor); // on clique dessus
            node.data.ideditor = tabAndEditor.ideditor; // On met à jour la data du noeud
            node.data.idfile = res.idFile;
            node.data.opened = "opened";
        };

        //Appelé après l'enregistrement des metas d'un fichier
        MetasCallback = function (node, filename, newpath) {
            _Project_tree.jstree('rename_node', node, filename); // Dans le cas d'un package, on renomme juste
            if (node.data.opened == "opened") {
                var tab = $("#tab-" + node.data.ideditor).find(".file-name").text(filename);// Change le titre de la tab
                var editorconcerned = $("#" + node.data.ideditor);
                editorconcerned.attr("data-name", filename); // On change les datas de l'éditor pour pouvoir sauvegarder correctement
                editorconcerned.attr("data-path", newpath);
                Clickontab(tab, node.data.ideditor); // on clique dessus
            }
            DeleteSave(node.data.ideditor); // ON enlève le stockage du précédent
        };

        //Suppression de noeud et fermeture de la tab
        deleteCallback = function (node) {
            CloseTab($("#tab-" + node.data.ideditor)); // On ferme d'abord
            DeleteSave(node.data.ideditor);
            Close_node(node.id); // Après deletesave sinon ideditor est undefined
            _Project_tree.jstree('delete_node', node);
        };

        Close_node = function (idnode, node) {
            if (node == undefined)
                node = _Project_tree.jstree().get_node(idnode);
            if (node.data.opened == "closed")
                return;
            if (node.data.idfile == undefined) { // Pas sauvegardé en BDD
                console.log("File will be destroyed, are you sure ?");
            }
            node.data.opened = "closed"; // Récupération du node
            node.data.ideditor = undefined; // Il devra recharger depuis la BDD
            return node;
        };

        SaveCallback = function (tab, idFile) { // On change l'id lorsqu'un fichier passe de statut local à enregistrer BDD
            var node = _Project_tree.jstree().get_node(tab.attr("data-idjstree"));
            var newIdFile = "file-" + idFile;
            if (node.id != newIdFile) {
                _Project_tree.jstree(true).set_id(node, newIdFile);
                node.data.idfile = idFile;
                tab.attr('data-idjstree', node.id);               //On remet à jour le pointeur sur le node
            }
        };
    });
})(jQuery);
