package code;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.codehaus.groovy.transform.SourceURIASTTransformation;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Create_Retrieve_Delete {
    String baseURI = RestAssured.baseURI = "https://rahulshettyacademy.com";

    /*
    {
       "name":"{{book_name}}",
       "isbn":"{{isbn}}",
       "aisle":"4571147",
       "author":"{{author_name}}"
    }

    returns
    {
    "Msg":"book created successfully"
    "ID"="isbn+aisle"
    }
     */
    @Test(description = "create a book")
    void orderBook() {
        System.out.println("/////////////////// CREATE BOOK //////////////////////");

        Faker faker = new Faker();
        String fakeName = faker.book().title();
        String fakeIsbn = faker.number().digits(4);
        String fakeAisle = faker.number().digit();
        String fakeAuthor = faker.book().author();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", fakeName);
        jsonObject.put("isbn", fakeIsbn);
        jsonObject.put("aisle", fakeAisle);
        jsonObject.put("author", fakeAuthor);

        String requestPayload = jsonObject.toString();
        RequestSpecification createBookrequest = given().
                header("Content-Type", "application/json").
                body(requestPayload);

        Response createBookResponse = createBookrequest.when().post("/Library/Addbook.php");
        createBookResponse.then().assertThat().statusCode(200);

        System.out.println("The request body: ");
        System.out.println(requestPayload);
        System.out.println("The response body :");
        System.out.println(createBookResponse.getBody().asString());
        String ID = createBookResponse.jsonPath().getString("ID");

        System.out.println("/////////////////// GET BOOK //////////////////////");
        // make a get call to get the book
        RequestSpecification request= given().queryParam("ID", ID);

        Response response=request.when().get("/Library/GetBook.php");
        response.then().assertThat().statusCode(200);
        System.out.println("The response body: ");

        System.out.println(response.getBody().asString());

        System.out.println("/////////////////// DELETE BOOK //////////////////////");


        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("ID",ID);
        String IDtoDelete=jsonObject1.toString();
        System.out.println("The request body: ");
        System.out.println(IDtoDelete);

        RequestSpecification request1=given().
                header("Content-Type","application/json").
                body(IDtoDelete);
        Response response1=request1.when().delete("/Library/DeleteBook.php");
        System.out.println("The response body: ");
        System.out.println(response1.getBody().asString());



    }


    }

