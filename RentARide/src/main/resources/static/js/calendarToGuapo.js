function updateDateDifference() {
    var startDateValue = document.getElementById('inDate').value;
    var endDateValue = document.getElementById('outDate').value;
    if (startDateValue && endDateValue) {
        var startDate = new Date(document.getElementById('inDate').value);
        var endDate = new Date(document.getElementById('outDate').value);
        var timeDiff = endDate.getTime() - startDate.getTime();
        var daysDiff = Math.floor(timeDiff / (1000 * 3600 * 24)) + 1;
        var daysWithEuro = document.getElementById('precioDiario').textContent;
        document.getElementById('precioTotal').innerHTML = daysDiff*parseFloat(daysWithEuro.replace(' €', '')) + " €";
        
        if(endDate.getTime() - startDate.getTime() < 0){
            document.getElementById('outDate').value = document.getElementById('inDate').value;
            document.getElementById('precioTotal').innerHTML = parseFloat(daysWithEuro.replace(' €', '')) + " €";
        }
    }
}
document.getElementById('outDate').addEventListener('change', updateDateDifference);
document.getElementById('inDate').addEventListener('change', updateDateDifference);
document.getElementById('inDate').addEventListener('change', function(){
    outDate.setAttribute("min", document.getElementById('inDate').value);
    var startDate = new Date(document.getElementById('inDate').value);
    var endDate = new Date(document.getElementById('outDate').value);
});

let currentDate = new Date().toISOString().substr(0, 10);
let inDate = document.getElementById("inDate");
let outDate = document.getElementById("outDate");
inDate.setAttribute("min", currentDate);
outDate.setAttribute("min", currentDate);