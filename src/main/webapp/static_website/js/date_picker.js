(function ($) {
    $(function () {

        // Date Picker
        $('.datepicker').pickadate({
            selectMonths: true,//Creates a dropdown to control month
            selectYears: 15,//Creates a dropdown of 15 years to control year
            //The title label to use for the month nav buttons
            labelMonthNext: 'Mois Suivant',
            labelMonthPrev: 'Mois Précédent',
            //The title label to use for the dropdown selectors
            labelMonthSelect: 'Choisir un mois',
            labelYearSelect: 'Choisir une année',
            //Months and weekdays
            monthsFull: ['Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'],
            monthsShort: ['Jan', 'Fev', 'Mar', 'Avr', 'Mai', 'Juin', 'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Dec'],
            weekdaysFull: ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'],
            weekdaysShort: ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'],
            //Materialize modified
            weekdaysLetter: ['D', 'L', 'M', 'M', 'J', 'V', 'S'],
            //Today and clear
            today: 'Aujourd\'hui',
            clear: 'Effacer',
            close: 'Fermer',
            //The format to show on the `input` element
            format: 'dd/mm/yyyy'
        });


    });
})(jQuery);