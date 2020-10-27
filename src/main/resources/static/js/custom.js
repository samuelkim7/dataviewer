$('input').attr('autocomplete','off');

$("a.confirmCancel").click(function() {
        if (!confirm("정말 취소하시겠습니까?")) return false;
    });