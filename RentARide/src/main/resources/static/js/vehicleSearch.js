$(document).ready(function(){
    $("#searchV").keyup(function(){
        var filtro = $(this).val();

        fetch('/vehicle/searchByName?filtro=' + filtro)
        .then(response => {
            if (!response.ok) {
            throw new Error('Error con la peticion por Ajax');
            }
            return response.json();
        })
        .then(data => {
            
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });
});