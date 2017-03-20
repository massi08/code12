(function ($) {
    $(function () {
        var reader = new FileReader();
        reader.onload = function(event) {
            var editor=Create_File(_ActualPathImport, _FileImport.name);
            if(editor != undefined ){
                editor.setValue(event.target.result);
            }
            _FileImport=undefined ;
            _ActualPathImport=undefined ;
        };
        reader.onerror=function(event){
            _ActualPathImport=undefined ;
            _FileImport=undefined ;
        };
        reader.onabort=function(event){
            _ActualPathImport=undefined ;
            _FileImport=undefined ;
        };

        // récupération de du file de l'input
        upload=function(event){
            _FileImport = $("#code-12-fileupload")[0].files[0] ;
            console.log(_FileImport);
            if(_FileImport != undefined) {
                if(_FileImport.type.match("text/*")){
                    reader.readAsText(_FileImport);
                }else{
                    Materialize.toast("Le fichier n'est pas un fichier texte", 4000);
                }
            }
        }
    });
})(jQuery);