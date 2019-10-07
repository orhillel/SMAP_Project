let nyChart= document.getElementById('myChart').getContext('2d');
    
Chart.defaults.global.defaultFontFamily='Lato';
Chart.defaults.global.defaultFontSize=18;
Chart.defaults.global.defaultFontColor='#777';

var neveShananPoint = {
    lat:32.778119, 
    lng:35.023409
}

var RomemaPoint = {
    lat:32.790350, 
    lng:34.995341
}

var KarmeliyaPoint = {
    lat:32.797910, 
    lng:34.975153
}

var WasiNisNasPoint = {
    lat: 32.816543, 
    lng: 34.996242
}

var totalCalculationFromDB = [];

var colors = ['rgba(63, 191, 63, 1)','rgba(252, 249, 79, 1)','rgba(241, 53, 28, 1)','rgba(152, 28, 241, 1)']

function arePointsNear(checkPoint, centerPoint, km) {
    var ky = 40000 / 360;
    var kx = Math.cos(Math.PI * centerPoint.lat / 180.0) * ky;
    var dx = Math.abs(centerPoint.lng - checkPoint.lng) * kx;
    var dy = Math.abs(centerPoint.lat - checkPoint.lat) * ky;
    return Math.sqrt(dx * dx + dy * dy) <= km;
}

function httpGetAsync(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() { 
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET", theUrl, true); // true for asynchronous 
    xmlHttp.send(null);
}

function getDevicesLocation(){
    var url = "https://smapapp.azurewebsites.net/api/getReport?code=CnnjQ5ejJPYR7IHpzicg1wcLDpIj9tIe1A6stvyb6Je8aW3OqDD3aA==";
    httpGetAsync(url,addDataToChart);
}

function WhichNeighborhood(element){
    var maxKm = 2;
    var currentPoint = {
        lat:parseFloat(element.Altitude),
        lng:parseFloat(element.Longitude)
    }
    if (arePointsNear(currentPoint,neveShananPoint,maxKm)){
        totalCalculationFromDB[0] = element.Total_Calculation
    }
    if (arePointsNear(currentPoint,RomemaPoint,maxKm)){
        totalCalculationFromDB[1] = element.Total_Calculation
    }
    if (arePointsNear(currentPoint,KarmeliyaPoint,maxKm)){
        totalCalculationFromDB[2] = element.Total_Calculation
    }
    if (arePointsNear(currentPoint,WasiNisNasPoint,maxKm)){
        totalCalculationFromDB[3] = element.Total_Calculation
    }
}

function addDataToChart(jsonRequest){
    var json = JSON.parse(jsonRequest);
    var stringvalue = json.m_StringValue;
    var jsonArray = JSON.parse(stringvalue);
    jsonArray.forEach(function(element){
        WhichNeighborhood(element);
    });
    initChartAfterDB();
}

function getColor(value){
    if(value>50){
        return colors[0];
    }
    else if(value>0){
        return colors[1];
    }
    else if(value>-200){
        return colors[2];
    }
    else if(value>-400){
        return colors[3];
    }
}

function initChartAfterDB(){
    let massPopChart= new Chart(myChart, {
        type:'bar', //bar,hotizontalBar, pie, line, doughnut, radar, polarArea
        data:{
            labels:["Neve Sha'anan","Romema","Karmeliya","Wadi Nisnas"],
            datasets:[ {label:'Total Rate',
            data:totalCalculationFromDB,
            backgroundColor:[
                // 'rgb(244, 196, 26, 0.8)',
                // 'rgb(244, 196, 26, 0.8)',
                // 'rgb(244, 196, 26, 0.8)',
                // 'rgb(244, 196, 26, 0.8)'
                getColor(totalCalculationFromDB[0]),
                getColor(totalCalculationFromDB[0]),
                getColor(totalCalculationFromDB[0]),
                getColor(totalCalculationFromDB[0])

            ],
            borderWidth:0.5,
            borderColor:'#777',
            hoverBorderWidth:2.5,
            hoverBorderColor:'#000'
        }
        ]
        },
        options:{
            //title:{
            //  display:true,
            //  text:'blablabla'
            //  fontSize:25
            // },
            legend:{
                position:'right',
                labels:{
                    fontColor:'#000'
                }
            },
            layout:{
                padding:{
                    left:50,
                    right:0,
                    bottom:0,
                    top:0
                }
            }
        }
    });
}

getDevicesLocation();