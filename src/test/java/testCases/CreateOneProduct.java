package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateOneProduct {
	
		String baseURI="https://techfios.com/api-prod/api/product";
		String jsonFilePath="//Users//rashmikanduluvavikraman//selenium-workspace//RestAssured//src//main//java//data//cPayLoad.json";
		SoftAssert sa =null;
		Map<String,String> cPayLoadMap;
		String firstProdId;
		
		public CreateOneProduct()
		{
			sa = new SoftAssert();
			cPayLoadMap = new HashMap<String,String>();
		}
		
		public Map<String,String> createPayLoadMap()
		{
			cPayLoadMap.put("name", "Most Amazing Headset 4.0 By RKV");
			cPayLoadMap.put("description", "The very best Headset for amazing programmers.");
			cPayLoadMap.put("price", "499");
			cPayLoadMap.put("category_id", "2");
			cPayLoadMap.put("category_name", "Electronics");
			return cPayLoadMap;
			
		}
		
		@Test(priority=1)
		public void createOneProduct()
		{
			
			Response response = 
					given()
						.baseUri(this.baseURI)
						.header("Content-type","application/json")
						.auth().preemptive().basic("demo@techfios.com","abc123")
						//.body(new File(this.jsonFilePath))
						.body(createPayLoadMap())
					.when()
						.post("/create.php")
					.then()
						.extract()
						.response();
		
			String responseBody = response.getBody().asString();
			System.out.println("Response:"+responseBody);
			long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);	
			int statusCode = response.getStatusCode();
			String responseHeader = response.getHeader("content-type");
			
			//Validating response time
			if(responseTime>1200)
			{
				System.out.println("Response time within the limit");
			}
			else
			{
				System.out.println("Response time off the limit");
			}
			
			sa.assertEquals(201, statusCode,"Mismatch in status code");
			sa.assertEquals("application/json; charset=UTF-8",responseHeader,"Mismatch in response header.");
			JsonPath jp = new JsonPath(responseBody);
			sa.assertEquals("Product was created.",jp.get("message"),"Mismatch in message");
			sa.assertAll();
			
		}
		
		@Test(priority=2)
		public void readAllProducts()
		{
			Response response= 
					given()
							.baseUri(this.baseURI)
							.header("Content-Type","application/json;charset=UTF-8")
							.auth().preemptive().basic("demo@techfios.com","abc123")
					.when()
							.get("/read.php")
					.then()
							.extract()
							.response();
			
			JsonPath jp = new JsonPath(response.getBody().asString());
			this.firstProdId=jp.getString("records[0].id");
			System.out.println("FirstProductId:"+this.firstProdId);
		}
		
		@Test(priority=3)
		public void readOneProduct()
		{
			Response response = 
					given()
						.baseUri(this.baseURI)
						.header("Content-type","application/json")
						.auth().preemptive().basic("demo@techfios.com","abc123")
						.queryParam("id", this.firstProdId)
					.when()
						.get("/read_one.php")
					.then()
						.extract()
						.response();
			
			String actualResponseBody = response.getBody().asString();
			  System.out.println("Response Body: " + actualResponseBody);
			  
			  JsonPath jp = new JsonPath(actualResponseBody);
			  
			  String actualProductName = jp.getString("name");
			  String expectedProductName = createPayLoadMap().get("name");
			  sa.assertEquals(actualProductName, expectedProductName, "Product names are not matching!");
			  System.out.println("Actual Product Name: " + actualProductName);
			  		  
			  String actualProductDescription = jp.getString("description");
			  String expectedProductDescription = createPayLoadMap().get("description");
			  sa.assertEquals(actualProductDescription, expectedProductDescription, "Product descriptions are not matching!");
			  System.out.println("Actual Product Description: " + actualProductDescription);
			  		  
			  String actualProductPrice = jp.getString("price");
			  String expectedProductPrice = createPayLoadMap().get("price");
			  sa.assertEquals(actualProductPrice, expectedProductPrice, "Product prices are not matching!");
			  System.out.println("Actual Product Price: " + actualProductPrice);
			  
			  sa.assertAll();				
		}
}
