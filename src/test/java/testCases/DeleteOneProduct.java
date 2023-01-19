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

public class DeleteOneProduct {
	String baseURI;
	SoftAssert softAssert;
	HashMap<String, String> createPayload;
	String firstProductId;
	HashMap<String, String> deletePayload;
	String deleteProductId;
	
	public DeleteOneProduct() {
		
		baseURI= "https://techfios.com/api-prod/api/product";
		softAssert = new SoftAssert();
		createPayload = new HashMap<String, String>();
		deletePayload = new HashMap<String, String>();
	}
	
	public Map<String, String> createPayloadMap(){
		
		createPayload.put("name", "Most Amazing Headset 4.0 By SRao");
		createPayload.put("description", "The very best Headset for amazing programmers.");
		createPayload.put("price", "499");
		createPayload.put("category_id", "2");
		createPayload.put("category_name", "Electronics");
		
		return createPayload;
				}
	
	public Map<String, String> deletePayloadMap(){
		
		deletePayload.put("id", deleteProductId);
				
		return deletePayload;
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
		 deleteProductId = firstProductId;
			}
	
	@Test (priority = 3)
	public void deleteOneProduct() {
						
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json; charset=UTF-8" )
				.auth().preemptive().basic("demo@techfios.com", "abc123")		
				.body(deletePayloadMap()).
		when()
				.delete("/delete.php").
		then()
				.log().all()
				.extract().response();
						  
		  int responseStatusCode = response.getStatusCode();
		  softAssert.assertEquals(responseStatusCode, 200, "Status Codes are not matching!");
		  System.out.println("Response Status Code: " + responseStatusCode);
		  			  
		  String responseHeaderContentType = response.getHeader("Content-Type");
		  softAssert.assertEquals(responseHeaderContentType, "application/json; charset=UTF-8", "Response Header Content Types are not matching!");
		  System.out.println("Response Header ContentType: " + responseHeaderContentType);
		  		  
		  String responseBody = response.getBody().asString();
		  System.out.println("Response Body: " + responseBody);
		  
		  JsonPath jp = new JsonPath(responseBody);
		  
		  String deleteProductMessage =jp.getString("message");
		  softAssert.assertEquals(deleteProductMessage, "Product was deleted.", " Delete Product Message is not matching!");
		  System.out.println("Product Message:" + deleteProductMessage); 
		  		  
		  softAssert.assertAll();				
	}
	
	@Test (priority=4)
	public void readOneDeletedProduct() {
		
		Response response =
		given()
				.baseUri(baseURI)
				.header("Content-Type","application/json" )
				.auth().preemptive().basic("demo@techfios.com", "abc123")
				.queryParam("id", deletePayloadMap().get("id")).
		when()
				.get("/read_one.php").
		then()
				.log().all()
				.extract().response();		    
		
		  int responseStatusCode = response.getStatusCode();
		  softAssert.assertEquals(responseStatusCode, 404, "Status Codes are not matching!");
		  System.out.println("Response Status Code: " + responseStatusCode);
		  
		  String actualResponseBody = response.getBody().asString();
		  System.out.println("Response Body: " + actualResponseBody);
		  
		  JsonPath jp = new JsonPath(actualResponseBody);
		  
		  String actualDeleteProductMessage = jp.getString("message");
		  String expectedDeleteProductMessage = "Product does not exist.";
		  softAssert.assertEquals(actualDeleteProductMessage, expectedDeleteProductMessage, "Product names are not matching!");
		  System.out.println("Actual Delete Product Message: " + actualDeleteProductMessage);
		    		  		  
		  softAssert.assertAll();				
	}

}
