$('input').attr('autocomplete','off');

$("a.confirmDeletion").click(function() {
        if (!confirm("정말 삭제하시겠습니까?")) return false;
    });