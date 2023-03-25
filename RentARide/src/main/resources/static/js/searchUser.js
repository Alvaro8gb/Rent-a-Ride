$(document).ready(function(){
    $("#search").keyup(function(){
      var valores = $('.busqueda');
      var buscando = $(this).val();
      var item='';
      for(let i = 0; i < valores.length; i++){
        item = $(valores[i]).html().toLowerCase();
        for(let j = 0; j < item.length; j++){
          if( buscando.length == 0 || item.indexOf( buscando ) > -1 ) $(valores[i]).parents('.fila').show();
          else $(valores[i]).parents('.fila').hide();
        }
      }
    })
  })