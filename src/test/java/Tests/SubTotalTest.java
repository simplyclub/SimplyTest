package Tests;

import  BaseClass.*;

import JSON.JSONCompare;
import JSON.ResponseHandling;



import com.sun.org.glassfish.gmbal.Description;
import org.testng.annotations.Test;
import utilities.MainFunction;

import java.io.IOException;


public class SubTotalTest extends BasePage {
    JSONCompare JSONCompare = new JSONCompare();
    ResponseHandling responseHandling = new ResponseHandling();

    @Test(testName = "SubTotalTest")
    @Description("Sub Total Test")
    public void subTotalTest() throws IOException {
        BasePage.ExReApiTestReport.info("basic API Test");

        // this test check sub total
        //Sending a transaction, checking that the correct deals have appeared and then closing the transaction without using points

        //this loop run on the the tests in the "test JSON to send"
        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {
            ExReApiTestReport.info("~~~~~~~~~~~~~~~~~~~~~~~~ Deal "+(i+1)+" ~~~~~~~~~~~~~~~~~~~~~~~~");

            updateJSONFile.upDateBaseJSONFile(JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                    JSONGetData.getAccoundID(TestJSONToSend, i), JSONGetData.getTranItems(TestJSONToSend, i),JSONGetData.getCardNumber(TestJSONToSend, i));

            subTotalResponse = APIPost.postSubTotal_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);
            subTotalResponse_String = MainFunction.convertOkHttpResponseToString(subTotalResponse);
            if(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0")) {

                ExReApiTestReport.info("subTotalResponse Time : "+BaseAPI.getResponseTime_OkHttp(subTotalResponse) + "ms");
                BasePage.avgTimeSubTotal.add(BaseAPI.getResponseTime_OkHttp(subTotalResponse));

                JSONCompare.responVSTestJson(i, subTotalResponse_String);
                JSONCompare.TestJSONVSResponse(i, subTotalResponse_String);

                //cancel of a deal
                Object x= updateJSONFile.upDateTrenCancelJSONFile(JSONGetData.getAccoundID(TestJSONToSend, i),JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                        responseHandling.getServiceTranNumber(subTotalResponse_String));
                //System.out.println(x.toString());
                trenCancelResponse = APIPost.postTrenCancel_OkHttp(BaseAPI.TEST_REST_API_URI,BaseJSON.TREN_CONCEL_JSON);
                String trenCancelResponse_string = MainFunction.convertOkHttpResponseToString(trenCancelResponse);
                //System.out.println(trenCancelResponse.getBody().asString());

                if (trenCancelResponse.code()!= 200 && !(responseHandling.getErrorCodeStatusJson(trenCancelResponse_string).equals("0"))){
                    System.out.println("ERROR --- the deal "+responseHandling.getServiceTranNumber(trenCancelResponse_string)+ " did not cancel");
                    ExReApiTestReport.warning("ERROR --- the deal "+responseHandling.getServiceTranNumber(trenCancelResponse_string)+ " did not cancel").assignCategory("warning");
                }else{
                    ExReApiTestReport.info("trenCancelResponse Time : "+ BaseAPI.getResponseTime_OkHttp(trenCancelResponse)+"ms");
                    BasePage.avgTimetrenRedund.add(BaseAPI.getResponseTime_OkHttp(trenCancelResponse));



                }



            }else{
                System.out.println("ERROR --- status code is not 200 or ErrorCodeStatus is not 0 ");
                ExReApiTestReport.fail("ERROR --- status code is not 200"+"("+subTotalResponse.code()+")" +" or ErrorCodeStatus is not 0 "+"("+
                        responseHandling.getErrorCodeStatusJson(subTotalResponse_String)+")");
              break;
            }

            subTotalResponse.body().close();
            trenCancelResponse.body().close();
        }//end main for loop
        System.out.println(avgTimeSubTotal);
        System.out.println(avgTimetrenRedund);
        System.out.println(MainFunction.getAvgTime(avgTimeSubTotal));
        System.out.println(MainFunction.getAvgTime(avgTimetrenRedund));
        ExReApiTestReport.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                .info("avgTimeSubTotal: "+ (MainFunction.getAvgTime(avgTimeSubTotal)+"ms"))
                .info("avgTimetrenCancel: "+MainFunction.getAvgTime(avgTimetrenRedund) +"ms");



    }





}





