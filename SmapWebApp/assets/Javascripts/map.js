    function initMap(){
        // Map options
        var options = {
          zoom:12,
          center:{lat:32.794044,lng:34.989571}
        }
  
        // New map
        var map = new google.maps.Map(document.getElementById('map'), options);
        

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
            httpGetAsync(url,addToMarkerArray);
        }

        function addToMarkerArray(jsonRequest){
            var json = JSON.parse(jsonRequest);
            var stringvalue = json.m_StringValue;
            var jsonArray = JSON.parse(stringvalue);
            jsonArray.forEach(function(element){
                console.log(element);
                var color = 'red';
                if (element.Total_Calculation > 50){
                    color = 'green';
                }
                else if (element.Total_Calculation > 0){
                    color = 'yellow'
                }
                var url = "http://maps.google.com/mapfiles/ms/icons/";
                url += color + "-dot.png";
                var markerToAdd = {
                    coords:{lat:parseFloat(element.Altitude),lng:parseFloat(element.Longitude)},
                    content:element.Total_Calculation.toString(),
                    iconImage:url
                };
                addMarker(markerToAdd)     
            });
            var markerToAddTest = {
                coords:{lat:32.807722,lng:35.003362},
                content:'-90',
                iconImage:'http://maps.google.com/mapfiles/ms/icons/purple-dot.png'
            };
            var markerToAddTest2 = {
                coords:{lat:32.807732,lng:35.004470},
                content:'-103',
                iconImage:'http://maps.google.com/mapfiles/ms/icons/purple-dot.png'
            };
            // 32.730867, 35.005269
            var markerToAddTest3 = {
                coords:{lat:32.730867,lng:35.005269},
                content:'65',
                iconImage:'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
            };
            //32.762103, 34.972504
            var markerToAddTest4 = {
                coords:{lat:32.762103,lng:34.972504},
                content:'20',
                iconImage:'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png'
            };
            addMarker(markerToAddTest);  
            addMarker(markerToAddTest2);    
            addMarker(markerToAddTest3); 
            addMarker(markerToAddTest4);     
        }

        // Add Marker Function
        function addMarker(props){
            var marker = new google.maps.Marker({
              position:props.coords,
              map:map,
              //icon:props.iconImage
            });
    
            // Check for customicon
            if(props.iconImage){
              marker.setIcon(props.iconImage);
            }

            // Check content
            if(props.content){
              var infoWindow = new google.maps.InfoWindow({
                content:props.content
              });
    
              marker.addListener('click', function(){
                infoWindow.open(map, marker);
              });
            }
          }

        getDevicesLocation()
      }

    