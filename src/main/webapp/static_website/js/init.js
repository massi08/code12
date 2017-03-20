(function ($) {
    $(function () {

        // Responsive sidebar
        $('.button-collapse').sideNav();

        // Selects
        $('select').material_select();

        formSubmit = function () {
            document.getElementById("logoutForm").submit();
        };

        $(document).ready(function () {
            $('.modal').leanModal();
            $('.datepicker.picker__input').prop('required',true).removeAttr("readonly");
        });
    });
})(jQuery);