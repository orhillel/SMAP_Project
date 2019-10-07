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

var labelsFromDB = [];
var coFromDB = [];
var o3FromDB = [];
var no2FromDB = [];
var so2FromDB = [];
var pm2_5FromDB = [];
var pm10FromDB = [];


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
        coFromDB[0]= element.CO;
        no2FromDB[0] = element.NO2;
        so2FromDB[0]= element.SO2;
        o3FromDB[0] = element.O3;
        pm10FromDB[0] = element.PM10;
        pm2_5FromDB[0] = element.PM2_5;
    }
    if (arePointsNear(currentPoint,RomemaPoint,maxKm)){
        coFromDB[1] = element.CO;
        no2FromDB[1] = element.NO2;
        so2FromDB[1] = element.SO2;
        o3FromDB[1] = element.O3;
        pm10FromDB[1] = element.PM10;
        pm2_5FromDB[1] = element.PM2_5;
    }
    if (arePointsNear(currentPoint,KarmeliyaPoint,maxKm)){
        coFromDB[2] = element.CO;
        no2FromDB[2] = element.NO2;
        so2FromDB[2] = element.SO2;
        o3FromDB[2] = element.O3;
        pm10FromDB[2] = element.PM10;
        pm2_5FromDB[2] = element.PM2_5;
    }
    if (arePointsNear(currentPoint,WasiNisNasPoint,maxKm)){
        coFromDB[3] = element.CO;
        no2FromDB[3] = element.NO2;
        so2FromDB[3] = element.SO2;
        o3FromDB[3] = element.O3;
        pm10FromDB[3] = element.PM10;
        pm2_5FromDB[3] = element.PM2_5;
    }
}

function addDataToChart(jsonRequest){
    var json = JSON.parse(jsonRequest);
    var stringvalue = json.m_StringValue;
    var jsonArray = JSON.parse(stringvalue);
    jsonArray.forEach(function(element){
        console.log(element);
        WhichNeighborhood(element);
    });
    console.log(coFromDB);
    initChartAfterDB();
}

function initChartAfterDB(){
    let massPopChart= new Chart(myChart, {
        type:'bar', //bar,hotizontalBar, pie, line, doughnut, radar, polarArea
        data:{
            labels:["Neve Sha'anan","Romema","Karmeliya","Wadi Nisnas"],
            datasets:[{
                label:'Co',
                data:coFromDB,
                backgroundColor:[
                    'rgba(46, 204, 113, 1)',
                    'rgba(46, 204, 113, 1)',
                    'rgba(46, 204, 113, 1)',
                    'rgba(46, 204, 113, 1)'
                ],
                borderWidth:1,
                borderColor:'#777',
                hoverBorderWidth:3,
                hoverBorderColor:'#000'
            },
            {label:'O3',
                data:o3FromDB,
                backgroundColor:[
                    'rgba(44, 130, 201, 1)',
                    'rgba(44, 130, 201, 1)',
                    'rgba(44, 130, 201, 1)',
                    'rgba(44, 130, 201, 1)'
                ],
                borderWidth:1,
                borderColor:'#777',
                hoverBorderWidth:3,
                hoverBorderColor:'#000'
            },
            {label:'NO2',
                data:no2FromDB,
                backgroundColor:[
                    'rgba(217, 30, 24, 1)',
                    'rgba(217, 30, 24, 1)',
                    'rgba(217, 30, 24, 1)',
                    'rgba(217, 30, 24, 1)'

                ],
                borderWidth:1,
                borderColor:'#777',
                hoverBorderWidth:3,
                hoverBorderColor:'#000'
            },
            {label:'SO2',
                data:so2FromDB,
                backgroundColor:[
                    'rgba(165, 55, 253, 1)',
                    'rgba(165, 55, 253, 1)',
                    'rgba(165, 55, 253, 1)',
                    'rgba(165, 55, 253, 1)'

                ],
                borderWidth:1,
                borderColor:'#777',
                hoverBorderWidth:3,
                hoverBorderColor:'#000'
            },
            {label:'PM2_5',
                data:pm2_5FromDB,
                backgroundColor:[
                    'rgba(248, 148, 6, 1)',
                    'rgba(248, 148, 6, 1)',
                    'rgba(248, 148, 6, 1)',
                    'rgba(248, 148, 6, 1)'

                ],
                borderWidth:1,
                borderColor:'#777',
                hoverBorderWidth:3,
                hoverBorderColor:'#000'
            },
            {label:'PM10',
                data:pm10FromDB,
                backgroundColor:[
                    'rgba(25, 181, 254, 1)',
                    'rgba(25, 181, 254, 1)',
                    'rgba(25, 181, 254, 1)',
                    'rgba(25, 181, 254, 1)'

                ],
                borderWidth:1,
                borderColor:'#777',
                hoverBorderWidth:3,
                hoverBorderColor:'#000'
            }

        ]
        },
        options:{
            //title:{
            // 	display:true,
            // 	text:'blablabla'
            // 	fontSize:25
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