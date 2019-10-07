#r "Newtonsoft.Json"

using System.Net;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Primitives;
using Newtonsoft.Json;
using System.Data.SqlClient; 
public static async Task<IActionResult> Run(HttpRequest req, ILogger log)
{
   log.LogInformation("C# HTTP trigger function processed a request.");


    string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
    Report report = JsonConvert.DeserializeObject<Report>(requestBody);
  log.LogInformation("Here 1");
  string cnnString = "Server=smap-db-server.database.windows.net,1433;"+
 "Initial Catalog=smapDB;Persist Security Info=False;"+
 "User ID=smap-db-server;Password=Samp1234;"+
 "MultipleActiveResultSets=False;Encrypt=True;TrustServerCertificate=False;Connection Timeout=30";
  
 


  SqlConnection conn = new SqlConnection(cnnString);
    log.LogInformation("Here 2");
 conn.Open();
  string  query = "INSERT INTO Reports (Altitude, Longitude,CO , O3 , NO2 , SO2,PM2_5 , PM10, Report_Time , Total_Calculation)"+
        "VALUES ('"+report.altitude+"', '"+report.longitude+"', '"+report.CO+"', '"+report.O3+"', '"+report.NO2+"', '"+report.SO2+"', '"+report.PM25+"', '"+report.PM10+"', '"+report.time+"','"+report.AQI+"')";

     log.LogInformation("Here 3");
    SqlCommand sqlcmd = new SqlCommand(query, conn);
        log.LogInformation("Here 4");
        sqlcmd.ExecuteNonQuery();
        log.LogInformation("Here 5");

return (ActionResult)new OkObjectResult("good ");
   
}

public class Report{
public string altitude{set;get;}
public string longitude{set;get;}    
public string CO{set;get;}
public string O3{set;get;}
public string NO2{set;get;}
public string SO2{set;get;}
public string PM25{set;get;}
public string PM10{set;get;}
public string time{set;get;}
public string AQI{set;get;}
}     
