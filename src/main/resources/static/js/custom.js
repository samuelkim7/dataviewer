$('input').attr('autocomplete','off');

$(document).ready(function() {
    $("a.confirmCancel").click(function() {
            if (!confirm("정말 취소하시겠습니까?")) return false;
        });

    ClassicEditor
        .create(document.querySelector("#textarea"))
        .catch(error => {
            console.log(error);
        });
});
