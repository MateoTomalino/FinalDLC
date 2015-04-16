function load (pagina) {   
    var ajax = new XMLHttpRequest();
    ajax.onreadystatechange = function() {
        if (ajax.status == 200 && ajax.readyState == 4) {            
            $("#resultado").html(ajax.responseText);
        }
    }
    ajax.open("GET", "http://localhost:8080/oven/BuscadorController?pagina=" + pagina, true);
    ajax.send("");
}

$(document).ready(function () {
    $('.nodo').on('click', control);

     $.fn.tagcloud.defaults = {
    size: {start: 18, end: 30, unit: "px"}, 
    color: {start: '#cde', end: '#f52'}
  };

    $(function () {
        $('#tagCloud a').tagcloud();
    });
});

function control (evt) {
    var el = $(evt.currentTarget);
    
    if ($(".nodo").size() == 1) {
        $("#irUltimo").addClass('disabled');
    }
	
    if (el.hasClass('primero')) {
        $("#irPrimero").addClass('disabled');
        $("#irUltimo").removeClass('disabled');
    }

    if (el.hasClass('ultimo')) {
        $("#irPrimero").removeClass('disabled');
        $("#irUltimo").addClass('disabled');
    }

    if (el.hasClass('medio')) {
        $("#irPrimero").removeClass('disabled');
        $("#irUltimo").removeClass('disabled');
    }
    $('.nodo').removeClass('active');
    el.addClass('active');
}

