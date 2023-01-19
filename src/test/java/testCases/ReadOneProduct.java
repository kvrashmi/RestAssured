package testCases;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct {
	String baseURI = "https://techfios.com/api-prod/api/product";
	
	@Test
	public void readOneProduct()
	{
		SoftAssert sa = new SoftAssert();
		Response response = 
				given()
					.baseUri(this.baseURI)
					.header("Content-type","application/json")
					.auth().preemptive().basic("demo@techfios.com","abc123")
					.queryParam("id", "6212")
				.when()
					.get("/read_one.php")
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
		
		sa.assertEquals(200, statusCode,"Mismatch in status code");
		sa.assertEquals("application/json",responseHeader,"Mismatch in response header.");
		sa.assertAll();
		
		//Validating the json response
		if(responseBody!=null)
		{
			System.out.println("Record Exists");
			JsonPath jp = new JsonPath(responseBody);
			
			/*
			 * Response:{
			 * "id":"6212",
			 * "name":"Super Magic pen XYZ Generation b",
			 * "description":"The best pen for amazing programmers.",
			 * "price":"299",
			 * "category_id":"2",
			 * "category_name":"Electronics"}

			 */
			sa.assertEquals("Super Magic pen XYZ Generation b",jp.get("name"),"Mismatch in name");
			sa.assertEquals("The best pen for amazing programmers.",jp.get("description"),"Mismatch in description");
			sa.assertEquals("299",jp.get("price"),"Mismatch in price");
			sa.assertEquals("2",jp.get("category_id"),"Mismatch in category id");
			sa.assertEquals("Electronics",jp.get("category_name"),"Mismatch in category name");
		}
		else
		{
			System.out.println("No record found for the corresponding id.");
		}
	}

}
