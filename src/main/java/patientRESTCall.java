import com.sun.jdi.request.StepRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testng.asserts.Assertion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class patientRESTCall<T> {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://iezapt5vu6.execute-api.us-east-1.amazonaws.com/test/patient";
    }

    // sending the request with query params
    Response response = RestAssured.given().
            contentType(ContentType.JSON).
            param("api_key" ,"18128199013820220912").
            when().get().then().
            extract().response();

    /**
     * Number of patients with appointment in June 2022
     * @throws ParseException
     */
    @Test
    public void getRequest() throws ParseException {
//        String api_key = "18128199013820220912";
//        Response response = RestAssured.given().
//                contentType(ContentType.JSON).
//                param("api_key" ,"18128199013820220912").
//                when().get().then().
//                extract().response();

        //System.out.println(response.asString());
        //System.out.println("String format" + response.getBody().asString());




        JsonPath path = response.jsonPath();
        List<T> data = path.getList("");
        System.out.println("All data: " + data.get(1));


        String id = path.getString("id");
//        System.out.println(id);
        // Task 5

        List<String> date = path.getList("appointment_date");
        //System.out.println("Date: " + date);

        int count = 0;
        for (int i = 0; i < date.size(); i++) {
            String currentDate = date.get(i);
            if (currentDate.substring(5,7).equals("06")) {
                count++;
            }
        }
        System.out.println("Patients with appointment date in June: " + count);
        Assertions.assertTrue(count >= 1);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date1 = sdf.parse("2022-07-01");
//        Date date2 = sdf.parse("2022-07-31");
//
//        List<Date> currentDate = path.getList("appointment_date");
//
//        //System.out.println("current date " + currentDate);
//
//        for (int i = 0; i < currentDate.size(); i++) {
//                Date current = currentDate.get(i);
//
//                Assertions.assertTrue(date1.after(current) && date2.before(current));
//        }
//
//        String appointment_date = path.getString("appointment_date");
       // System.out.println("date " + appointment_date);
        //System.out.println("date type" + appointment_date.getClass());

//        for (int i = 0; i < id.length(); i++) {
//            System.out.println(i);
//        }


    }


    /**
     * Step 6: fail the test if the following patient does not exist in the response data
     */
    @Test
    public void testStep6() {
        String steveRogers = "\"id\": \"SR19760827202206208364\",\n" +
                "\n" +
                "\"birthdate\": \"1976-08-27\",\n" +
                "\"phone\": \"347-555-9876\",\n" +
                "\"appointment_date\": \"2022-06-20\",\n" +
                "\"name\": {\n" +
                "\"lastName\": \"Rogers\",\n" +
                "\"firstName\": \"Steve\"\n" +
                "},\n" +
                "\"address\": {\n" +
                "\"street\": \"45 W 45th St\",\n" +
                "\"city\": \"New York\",\n" +
                "\"state\": \"NY\",\n" +
                "\"zip\": \"10036\"\n" +
                "}\n" +
                "}";

        JsonPath path = response.jsonPath();
        List<T> data = path.getList("");

        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i));
            if (data.get(i).equals(steveRogers)) {
                System.out.println("This test passes");
            }
            Assertions.assertTrue(data.get(i).equals(steveRogers));
        }

    }


    @Test
    public void test7() {
        JsonPath path = response.jsonPath();
        List<T> data = path.getList("");
//        List<String> name = path.getList("name");
//
//        System.out.println("Name " + name);
        Object obj= path.getList("name").get(0);

        List<String> birthyear = path.get("birthdate");
        List<String> appt_date = path.get("appointment_date");

        for (int i = 0; i < data.size(); i++) {
            LinkedHashMap map = (LinkedHashMap) path.getList("name").get(i);
            String lastName = (String) map.get("lastName");
            String firstName = (String) map.get("firstName");
            String result = "" + lastName.charAt(0) +
                    firstName.charAt(0)  +
                    birthyear.get(i).substring(0,4) +
                    birthyear.get(i).substring(5,7) +
                    birthyear.get(i).substring(8) +
                    appt_date.get(i).substring(0,4) +
                    appt_date.get(i).substring(5,7) +
                    appt_date.get(i).substring(8);

            Assertions.assertEquals(result, path.getList("id").get(i));
        }


        }

    }
