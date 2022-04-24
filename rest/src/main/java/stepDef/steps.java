package stepDef;

import static io.restassured.RestAssured.*;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


import java.util.ArrayList;
import java.util.HashMap;


public class steps {
	static String group_guid;
	static String token="ee5bc095f2c67d11c579b9750dd6fcef64bfe554";
	static String url="https://api-ssl.bitly.com/v4";
	static String username="shravanbitly";

	@Before
	public static void setGroupGuid(){
	 
		Response res =
				given().
				header("Authorization","Bearer "+token).
				
				when().
				get(url+"/groups").
				
				then().
				
				assertThat().
				statusCode(200).
				
				and().
				header("Content-Type", "application/json").extract().response();

		
		
		ArrayList<String> groups=res.then().extract().path("groups.guid");
		group_guid=groups.get(0).toString();
		System.out.println("The get groups response is : "+res.asString());
		System.out.println("The group id is : "+group_guid);
	}
	 	
	@Given("^user has generated token for Authorization$") 
	public static void authorization(){
		System.out.println("The authorization for "+username+" is successful and token= "+token);		
	} 	
	
	@Given("^we validate GET\\/groups\\/\\{group_guid}$") 
	public static void getResponse_GroupGuid(){
		System.out.println("To perform Get response of : "+url+"/groups/"+group_guid);
				
	}
	
	@Then("^we retrieve all details for a group$") 
	public static void validateGetResponse_GroupGuid(){
		 
		Response res =
				given().
				header("Authorization","Bearer "+token).
				
				when().
				get(url+"/groups/"+group_guid).
				
				then().
				assertThat().
				statusCode(200).
				body(
						"name",Matchers.equalTo(username),
						"guid",Matchers.equalTo(group_guid)
					).

				and().
				header("Content-Type", "application/json").extract().response();
		
		 System.out.println("The get group_guid response is : "+res.asString());

		 /*JsonPath js = new JsonPath(res.asString());
		String name = js.getString("name");
		System.out.println(name);
		
		String name=res.then().extract().path("name").toString();
		 System.out.println(name);*/
	}
	
	@Given("^we validate GET\\/groups\\/\\{group_guid}\\/bitlinks\\/\\{sort}$") 
	public static void getResponse_Sort(){
		System.out.println("To perform GET response of : "+url+"/groups/"+group_guid+"/bitlinks/clicks?unit=month");		
	}
	
	@Then("^we get list of bitlinks sorted by group$") 
	public static void validateGetResponse_Sort(){
		 
		Response res =
				given().
				header("Authorization","Bearer "+token).
				
				when().
				get(url+"/groups/"+group_guid+"/bitlinks/clicks?unit=month").
				
				then().
				assertThat().
				statusCode(200).
				
				and().
				header("Content-Type", "application/json").extract().response();
		
		System.out.println("The get sort response is : "+res.asString());

		
		ArrayList<String> sorts=res.then().extract().path("sorted_links");
		 System.out.println("The sorted list is : "+sorts);
	}
	
	@Given("^we validate POST\\/bitlinks$") 
	public static void postResponse_bitlinks(){
		System.out.println("To perform POST response of : "+url+"/bitlinks");		
	}
	
	
	@Then("^we create a bitlink with parameters like longurl,domain,groupid$") 

	public static void validatePostResponse_bitlinks(){
		 HashMap<String,Object> data=new HashMap<String,Object>(); 
		 
		data.put("long_url", "https://www.elsevier.com/en-in");
		data.put("domain", "bit.ly");
		data.put("group_guid", group_guid);
		data.put("title", "Elsevier");
		
		Response res =
				given().
				header("Authorization","Bearer "+token).
				header("Content-Type","application/json").
				
				contentType(ContentType.JSON).
	            body(data).
	            
				when().
				post(url+"/bitlinks").
				
				then().
				assertThat().
				statusCode(201).
				body(
						"long_url",Matchers.equalTo(data.get("long_url")),
						"title",Matchers.equalTo(data.get("title"))
					).
				
				and().
				header("Content-Type", "application/json").extract().response();
		
		 System.out.println("The post bitlink creation response is : "+res.asString());

		JsonPath js = new JsonPath(res.asString());
		
		String title = js.getString("title");
		System.out.println("The created bitlink title is :"+title);		
		
		String long_url = js.getString("long_url");
		System.out.println("The created bitlink long_url is :"+long_url);
		
		String id = js.getString("id");
		System.out.println("The created bitlink id is :"+id);
		
		String link = js.getString("link");
		System.out.println("The created bitlink link is :"+link);
		
		/*Assert.assertEquals(title, data.get(title));
		Assert.assertEquals(long_url, data.get(long_url));*/ 

	}
	
	
}
