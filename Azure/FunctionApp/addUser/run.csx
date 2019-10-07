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
   User user = JsonConvert.DeserializeObject<User>(requestBody);

    string cnnString = "Server=smap-db-server.database.windows.net,1433;"+
 "Initial Catalog=smapDB;Persist Security Info=False;"+
 "User ID=smap-db-server;Password=Samp1234;"+
 "MultipleActiveResultSets=False;Encrypt=True;TrustServerCertificate=False;Connection Timeout=30";
  
 
  SqlConnection conn = new SqlConnection(cnnString);
    log.LogInformation("Here 2");
 conn.Open();
  string  query = "INSERT INTO Users (email, password)"+
        "VALUES ('" + user.email+ "','" +user.password+"')";
    SqlCommand sqlcmd = new SqlCommand(query, conn);
        sqlcmd.ExecuteNonQuery();
    

log.LogInformation("Here 3");

return (ActionResult)new OkObjectResult("good");
   
}

public class User{
public string email{set;get;}
public string password{set;get;}
}     
