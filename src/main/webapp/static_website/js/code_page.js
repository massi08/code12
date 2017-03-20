(function ($) {
    $(function () {
        $('.button-menu-collapse').click(function () {
            var selectedNav = $(this).attr("data-activates");
            $(".custom-side-nav:not(#" + selectedNav + ")").removeClass("is-active-side");
            $(".button-menu-collapse").not(this).removeClass("btn-active");
            $(this).toggleClass("btn-active");
            $("#" + selectedNav).toggleClass("is-active-side");
            if ($(".is-active-side").length) {
                $(".code-editor").addClass("adapt-width");
            }
            else {
                $(".code-editor").removeClass("adapt-width");
            }
        });

        leanModalAction = function (idOrClassModal, action, addOverlay) {
            if (action === "close") {
                $(idOrClassModal).closeModal();
                if (addOverlay)
                    $("#popup-overlay").removeClass("opened-popup");
            }

            else if (action === "open") {
                $(idOrClassModal).openModal();
                if (addOverlay)
                    $("#popup-overlay").addClass("opened-popup");
            }
        };

        $('ul.tabs').tabs();

        $("#open-commit-modal").click(function () {
            leanModalAction("#commit-modal", "open", true);
        });

        $("#commit-close-modal").click(function () {
            leanModalAction("#commit-modal", "close", true);
        });

        $("#checkout-close-modal").click(function () {
            leanModalAction("#checkout-modal", "close", true);
        });

        $(document).keyup(function (e) {
            if (e.keyCode == 27) { //Si on appuie sur escape
                var allModals = $(".modal");
                allModals.each(function () {
                    var id = $(this).attr('id');
                    leanModalAction(id, "close", true);
                })
            }
        });

        $('#compilation-modal-close').click(function () {
            $(document).ready(function () {
                leanModalAction('#compilation-modal', "close", false);
            });
        });

        $('#add-chat-members-modal-close').click(function () {
            $(document).ready(function () {
                leanModalAction('#add-chat-members-modal', "close", true);
            });
        });

        $('#open-compilation-modal').click(function () {
            $(document).ready(function () {
                leanModalAction('#compilation-modal', "open", false);
            });
        });

        $('ul.tabs').tabs();


        $(".tabs").on('click', ".tab-link", function () { // event changement de fenêtres
            ChangeSelectedFile($(this).attr('href'));
        });

        $(".tabs").on('click', ".close", function (event) {
            var idnode = CloseTab(this);
            var node = Close_node(idnode);
            if (node.data.idfile != undefined && node.data.haslock == "true") { // Viens du local
                console.log("Do you want to save your file before unlocking it ?");
                unlockfile(node.data.idfile); // On le delock dans la BDD
                node.data.haslock = "false";
            }
            event.preventDefault();
            event.stopImmediatePropagation(); // On veut pas que ce soit compter comme un click pour la tab donc il faut stop l'event
        });

        CloseTab = function (tab) {
            var allTabs = $(tab).closest('ul').children('li');
            var selectedEditorTab = $(tab).closest('li');
            var indexOfCurrentTab = allTabs.index(selectedEditorTab);
            var selectedEditorId = $(tab).closest('a').attr('href'); // Editor id deleted
            var selectedEditorIdClosestLi; // Nouvelle tab à selectionner

            var idjstree = selectedEditorTab.attr("data-idjstree");
            if (selectedEditorId != undefined)
                DeleteSave(selectedEditorId.substr(1)); // Delete du local
            $(selectedEditorId).remove(); //Enlève l'éditor
            selectedEditorTab.remove(); //Enlève la tab


            if (allTabs.length > indexOfCurrentTab + 1) {
                selectedEditorIdClosestLi = $(allTabs[indexOfCurrentTab + 1]);
            } else if (allTabs.length > 1) {
                selectedEditorIdClosestLi = $(allTabs[indexOfCurrentTab + -1]);
            } else {
                _selectedFile = undefined;
                return idjstree;
            }
            var selectedEditorIdClosestA = selectedEditorIdClosestLi.find("a");
            $(selectedEditorIdClosestA).click();

            return idjstree;
        };

        InitialiseEditor = function (id, readonly) {
            var editor = ace.edit(id);
            var modelist = ace.require("ace/ext/modelist");
            editor.setTheme("ace/theme/monokai");
            editor.setShowPrintMargin(false);
            if (_languageId == 1)
                editor.getSession().setMode("ace/mode/java");
            else if (_languageId == 2 || _languageId == 3)
                editor.getSession().setMode("ace/mode/c_cpp");
            editor.$blockScrolling = Infinity;
            editor.setReadOnly(readonly);
            editor.setOptions({
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true,
                fontSize: "16px"
            });
            return editor;
        };


        ChangeSelectedFile = function (id) {
            _selectedFile = id.substring(1);
        };

        Clickontab = function (tab, ideditor) { // click manuel
            tab.find(".tab-link").click();
        };

        /* Création d'une tab et d'un editor, on a besoin du path et du name, car le nom a besoin d'être affiché
         le path est quant à lui nécessaire lorsqu'on sauvegarde un fichier : on ne passe alors par les données de jstree
         */
        CreateTabAndEditor = function (filename, path, ideditorStore, hasLock) {
            if (ideditorStore == undefined)       //Cas où on ne créé pas depuis le local storage
                var ideditorStore = "editor-" + GenereIdEditor();
            var ideditor = ideditorStore;
            var fileStatusIcon = GetFileIconeStatus(hasLock);
            var editor = $(".code-editor").append('<div id=' + ideditor + ' data-path=' + path + ' data-name=' + filename + ' class="editor-wrapper"></div>');
            var newtab = $('<li id="tab-' + ideditor + '" class="tab col">' +
                '<a class="tab-link waves-effect waves-light" href="#' + ideditor + '">' +
                '<div class="file-name">' + filename + '</div>' +
                '<i class="material-icons file-status">' + fileStatusIcon + '</i>' +
                '<i class="material-icons close">close</i>' +
                '</a>' +
                '</li>');
            $(".code-editor ul").prepend(newtab); // On place l'éditor
            return {
                "tab": newtab,
                "editor": editor,
                "ideditor": ideditor,
                "hasLock": hasLock,
            };
        };

        ChangeLockIcone = function (idFile, haslock) {
            $("li[data-idjstree=file-" + idFile + "] .file-status").text(GetFileIconeStatus(haslock));
        };

        var GetFileIconeStatus = function (haslock) {
            if (haslock === "false")
                var fileStatusIcon = "lock_outline";  //"lock_outline" sinon
            else
                var fileStatusIcon = "mode_edit";
            return fileStatusIcon;
        };

        ModalLogDisplay=function(msg, id){
            var lines = msg.split('\n');
            var options='<option value="" disabled selected>Choisissez un commit</option>';
            for(var i = 0;i < lines.length;i++){
                //code here using lines[i] which will give you each line
                var idC=lines[i].substr(0,lines[i].indexOf(' ')); // id commit
                var com=lines[i].substr(lines[i].indexOf(' ')+1);
                var op=('<option value="'+idC+'">'+idC+' :'+com+'</option>');
                if(lines[i].length!=0){
                    options=options.concat(op);
                }

            }
            $('#idFileHidden').val(id);
            $('#listCommit').html(options);
            leanModalAction("#checkout-modal", "open", true);
        }


    });

})(jQuery);



