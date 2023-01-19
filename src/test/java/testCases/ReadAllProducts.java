package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

public class ReadAllProducts {
	
	@Test
	public void readAllProducts()
	{
		Response response= 
		given()
				.baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type","application/json;charset=UTF-8")
				.auth().preemptive().basic("demo@techfios.com","abc123")
				.log() //To see what request is being sent
				.all() //To see what request is being sent
		.when()
				//.log() //To see what request is being sent
				//.all() //To see what request is being sent
				.get("/read.php")
		.then()
				//.log() // To print the response
				//.all() // To print the response
				//.statusCode(200)
				//.header("Content-Type","application/json; charset=UTF-8");
				.extract()
				.response();
		
		System.out.println("Response Body 1:"+response.asString());
		System.out.println("Response Body 2:"+response.getBody());
		
		System.out.println("Response Status Code:"+response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(), 200);
		
		System.out.println("ResponseTime:"+response.getTimeIn(TimeUnit.MILLISECONDS));
		if(response.getTimeIn(TimeUnit.MILLISECONDS)<1200)
		{
			System.out.println("Within time limit...");
		}
		else
		{
			System.out.println("Out of time limit...");
		}
		
		System.out.println("Response Header:"+response.getHeader("Content-Type"));
		Assert.assertEquals(response.getHeader("Content-Type"),"application/json; charset=UTF-8");
		
		JsonPath jp = new JsonPath(response.getBody().asString());
		System.out.println("FirstProductId:"+jp.getString("records[0].id"));
		
		if(jp.getString("records[0].id")!=null)
		{
			System.out.println("Product list is not empty..");
		}
		else
		{
			System.out.println("Product list is empty..");
		}
	}

}
