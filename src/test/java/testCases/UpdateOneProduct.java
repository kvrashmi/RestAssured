package testCases;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateOneProduct {
	String baseURI;
	SoftAssert softAssert;
	String createPayloadPath;
	HashMap<String, String> createPayload;
	String firstProductId;
	HashMap<String, String> updatePayload;
	String updateProductId;
	
public UpdateOneProduct() {
		
		baseURI= "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		createPayloadPath = "src/main/java/data/CreatePayload.json";
		createPayload = new HashMap<String, String>();
		updatePayload = new HashMap<String, String>();
	}
	
	public Map<String, String> createPayloadMap(){
		
		createPayload.put("name", "Most Amazing Mic 2.0 By RKV ");
		createPayload.put("description", "The very best Mic for amazing programmers.");
		createPayload.put("price", "499");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
				}
	
	public Map<String, String> updatePayloadMap(){
		
		updatePayload.put("id", updateProductId);
		updatePayload.put("name", "Amazing Mic 6.0 By Rashmi");
		updatePayload.put("description", "The very best Headset for amazing programmers.");
		updatePayload.put("price", "699");
		updatePayload.put("category_id", "2");
		updatePayload.put("category_name", "Electronics");
		
		return updatePayload;
				}
	
	@Test (priority = 1)
	public void createOneProduct() {
		
		System.out.println("Create Payload Map:" + createPayloadMap());
		
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json; charset=UTF-8" )
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				.body(createPayloadMap()).
		when()
				.post("/create.php").
		then()
				.log().all()
				.extract().response();
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		  System.out.println("Response Time " + responseTime);
		  
		   if (responseTime<=2500) {
		    System.out.println("Response time is within range");
		   } else 
		    System.out.println("Response time is out of range");
		  
		  int responseStatusCode = response.getStatusCode();
		  softAssert.assertEquals(responseStatusCode, 201, "Status Codes are not matching!");
		  System.out.println("Response Status Code: " + responseStatusCode);
		  			  
		  String responseHeaderContentType = response.getHeader("Content-Type");
		  softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response Header Content Types are not matching!");
		  System.out.println("Response Header ContentType: " + responseHeaderContentType);
		  		  
		  String responseBody = response.getBody().asString();
		  System.out.println("Response Body: " + responseBody);
		  
		  JsonPath jp = new JsonPath(responseBody);
		  
		  String productMessage =jp.getString("message");
		  softAssert.assertEquals(productMessage, "Product was created.", "Product Message is not matching!");
		  System.out.println("Product Message:" + productMessage); 
		  		  
		  softAssert.assertAll();
				
	}

	@Test (priority = 2)
	public void readAllProducts() {
		
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json; charset=UTF-8" )
				.auth().preemptive().basic("demo@techfios.com", "abc123").
		when()
				.get("/read.php").
		then()
				.log().all()
				.extract().response();
						  
		  String responseBody = response.getBody().asString();
		  System.out.println("Response Body: " + responseBody);
		  
		  JsonPath jp = new JsonPath(responseBody);
		  firstProductId = jp.getString("records[0].id");
		  System.out.println("First Product ID " + firstProductId); 
		  updateProductId = firstProductId;
				
	}
	

	@Test (priority = 3)
	public void updateOneProduct() {
						
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json; charset=UTF-8" )
				.auth().preemptive().basic("demo@techfios.com", "abc123")		
				.body(updatePayloadMap()).
		when()
				.put("/update.php").
		then()
				.log().all()
				.extract().response();
		
		long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		  System.out.println("Response Time " + responseTime);
		  
		   if (responseTime<=2500) {
		    System.out.println("Response time is within range");
		   } else 
		    System.out.println("Response time is out of range");
		  
		  int responseStatusCode = response.getStatusCode();
		  softAssert.assertEquals(responseStatusCode, 200, "Status Codes are not matching!");
		  System.out.println("Response Status Code: " + responseStatusCode);
		  			  
		  String responseHeaderContentType = response.getHeader("Content-Type");
		  softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response Header Content Types are not matching!");
		  System.out.println("Response Header ContentType: " + responseHeaderContentType);
		  		  
		  String responseBody = response.getBody().asString();
		  System.out.println("Response Body: " + responseBody);
		  
		  JsonPath jp = new JsonPath(responseBody);
		  
		  String productMessage =jp.getString("message");
		  softAssert.assertEquals(productMessage, "Product was updated.", "Product Message is not matching!");
		  System.out.println("Product Message:" + productMessage); 
		  		  
		  softAssert.assertAll();				
	}
	
	@Test (priority=4)
	public void readOneUpdatedProduct() {
		
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json" )
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				.queryParam("id", updatePayloadMap().get("id")).
		when()
				.get("/read_one.php").
		then()
				.log().all()
				.extract().response();		    
		  
		  String actualResponseBody = response.getBody().asString();
		  System.out.println("Response Body: " + actualResponseBody);
		  
		  JsonPath jp = new JsonPath(actualResponseBody);
		  
		  String actualProductName = jp.getString("name");
		  String expectedProductName = updatePayloadMap().get("name");
		  softAssert.assertEquals(actualProductName, expectedProductName, "Product names are not matching!");
		  System.out.println("Actual Product Name: " + actualProductName);
		  		  
		  String actualProductDescription = jp.getString("description");
		  String expectedProductDescription = updatePayloadMap().get("description");
		  softAssert.assertEquals(actualProductDescription, expectedProductDescription, "Product descriptions are not matching!");
		  System.out.println("Actual Product Description: " + actualProductDescription);
		  		  
		  String actualProductPrice = jp.getString("price");
		  String expectedProductPrice = updatePayloadMap().get("price");
		  softAssert.assertEquals(actualProductPrice, expectedProductPrice, "Product prices are not matching!");
		  System.out.println("Actual Product Price: " + actualProductPrice);
		  
		  softAssert.assertAll();				
	}

}
