#r "Newtonsoft.Json"

using System.Data;
using System.Data.SqlClient;
using System.Text;
using Newtonsoft.Json;
using System.Net;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Primitives;

public static async Task<IActionResult> Run(HttpRequest req, ILogger log)
{
 string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
   User user = JsonConvert.DeserializeObject<User>(requestBody);
    log.LogInformation("C# HTTP trigger function processed a request.");
   string cnnString = "Server=smap-db-server.database.windows.net,1433;"+
 "Initial Catalog=smapDB;Persist Security Info=False;"+
 "User ID=smap-db-server;Password=Samp1234;"+
 "MultipleActiveResultSets=False;Encrypt=True;TrustServerCertificate=False;Connection Timeout=30";
  
    var jsonResult = new StringBuilder();
    using (SqlConnection conn = new SqlConnection(cnnString) )
    {
        conn.Open();
        var getallpending = "SELECT * FROM Users where users.email =" + "'" +user.email + "' " +" AND password =" + "'" +user.password + "' " + "FOR JSON PATH";
        log.LogInformation(getallpending.ToString());
         log.LogInformation("1923448394");
        using (SqlCommand cmd = new SqlCommand(getallpending, conn))
        {
            using (SqlDataReader reader = cmd.ExecuteReader()) {
                if (reader.HasRows){
                    log.LogInformation("here 1 ");
                    while (reader.Read()){
                         log.LogInformation("dkjfjnksdjh");
                         log.LogInformation(reader.GetValue(0).ToString());
                         jsonResult.Append(reader.GetValue(0).ToString());
                    }
                }
            }
        }
    }

    log.LogInformation(jsonResult.ToString());
  return  (ActionResult)new OkObjectResult(jsonResult);
}
public class User{
public string email{set;get;}
public string password{set;get;}
}     
