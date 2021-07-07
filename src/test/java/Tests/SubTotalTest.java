package Tests;

import  BaseClass.*;

import JSON.JSONCompare;
import JSON.ResponseHandling;



import com.sun.org.glassfish.gmbal.Description;
import org.testng.annotations.Test;




public class SubTotalTest extends BasePage {
    JSONCompare JSONCompare = new JSONCompare();
    ResponseHandling responseHandling = new ResponseHandling();

    @Test(testName = "SubTotalTest")
    @Description("Sub Total Test")
    public void subTotalTest() {
        BasePage.ExReApiTestReport.info("basic API Test");

        // this test check sub total
        //Sending a transaction, checking that the correct deals have appeared and then closing the transaction without using points

        //this loop run on the the tests in the "test JSON to send"
        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {

            updateJSONFile.upDateBaseJSONFile(JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                    JSONGetData.getAccoundID(TestJSONToSend, i), JSONGetData.getTranItems(TestJSONToSend, i));

            subTotalResponse = APIPost.postSubTotal(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);
            if(subTotalResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse).equals("0")) {
                System.out.println(subTotalResponse.getBody().asString());
                JSONCompare.responVSTestJson(i, subTotalResponse);
                JSONCompare.TestJSONVSResponse(i, subTotalResponse);
            }else{
                System.out.println("ERROR --- status code is not 200 or ErrorCodeStatus is not 0 ");
                ExReApiTestReport.fail("ERROR --- status code is not 200"+"("+subTotalResponse.getStatusCode()+")" +" or ErrorCodeStatus is not 0 "+"("+
                        responseHandling.getErrorCodeStatusJson(subTotalResponse)+")");
              break;
            }


        }

    }





}





