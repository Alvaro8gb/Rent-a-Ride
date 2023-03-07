document.getElementById('startDate').addEventListener('change', function(){
    let outDate = document.getElementById("endDate");
    outDate.setAttribute("min", document.getElementById('startDate').value);
    
    var startDate = new Date(document.getElementById('startDate').value);
    var endDate = new Date(document.getElementById('endDate').value);

    if(endDate.getTime() - startDate.getTime() < 0){
        document.getElementById('endDate').value = document.getElementById('startDate').value;
    }
});

let currentDate = new Date().toISOString().substr(0, 10);
let inDate = document.getElementById("startDate");
let outDate = document.getElementById("endDate");
inDate.setAttribute("min", currentDate);
outDate.setAttribute("min", currentDate);