package Tests;

import  BaseClass.*;

import JSON.JSONCompare;
import JSON.ResponseHandling;



import org.testng.annotations.Test;
import utilities.MainFunction;
import utilities.RetryAnalyzer;

import java.io.IOException;


public class SubTotalTest extends BasePage {
    JSONCompare JSONCompare = new JSONCompare();
    ResponseHandling responseHandling = new ResponseHandling();
    BasePage basePage = new BasePage();

    @Test(testName = "SubTotalTest",retryAnalyzer = RetryAnalyzer.class)
    public void subTotalTest() throws IOException {
        basePage.ExReApiTestReport.info("basic API Test");

        int SFFlag = 0;

        // this test check sub total
        //Sending a transaction, checking that the correct deals have appeared and then closing the transaction without using points

        //this loop run on the the tests in the "test JSON to send"
        //i <= JSONGetData.getArraySize(TestJSONToSend) - 1
        for (int i =0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {
            ExReApiTestReport.info("~~~~~~~~~~~~~~~~~~~~~~~~ Transaction "+(i+1)+" ~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println(MainFunction.BaseLogStringFunc()+"~~~~~~~~~~~~~~~~~~~~~~~~ Deal "+(i+1)+" ~~~~~~~~~~~~~~~~~~~~~~~~");

            updateJSONFile.upDateBaseJSONFile(
                    JSONGetData.getUser(TestJSONToSend, i),
                    JSONGetData.getPassword(TestJSONToSend, i),
                    JSONGetData.getAccoundID(TestJSONToSend, i),
                    JSONGetData.getTranItems(TestJSONToSend, i),
                    JSONGetData.getCardNumber(TestJSONToSend, i));

            subTotalResponse = APIPost.postSubTotal_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);
            subTotalResponse_String = MainFunction.convertOkHttpResponseToString(subTotalResponse);
            System.out.println(MainFunction.BaseLogStringFunc()+subTotalResponse_String);


            if(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0")) {

                ExReApiTestReport.info("subTotalResponse Time : "+BaseAPI.getResponseTime_OkHttp(subTotalResponse) + "ms");
                basePage.avgTimeSubTotal.add(BaseAPI.getResponseTime_OkHttp(subTotalResponse));

                if(!JSONCompare.responVSTestJson(i, subTotalResponse_String)){
                    SFFlag = 1 ;
                }
                JSONCompare.TestJSONVSResponse(i, subTotalResponse_String);

                //cancel of a deal
                Object x= updateJSONFile.upDateTrenCancelJSONFile(JSONGetData.getAccoundID(TestJSONToSend, i),JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                        responseHandling.getServiceTranNumber(subTotalResponse_String));
                //System.out.println(x.toString());
                trenCancelResponse = APIPost.postTrenCancel_OkHttp(BaseAPI.TEST_REST_API_URI,BaseJSON.TREN_CONCEL_JSON);
                String trenCancelResponse_string = MainFunction.convertOkHttpResponseToString(trenCancelResponse);
                //System.out.println(trenCancelResponse.getBody().asString());

                if (trenCancelResponse.code()!= 200 && !(responseHandling.getErrorCodeStatusJson(trenCancelResponse_string).equals("0"))){
                    System.out.println(MainFunction.BaseLogStringFunc()+"ERROR --- the Transaction "+responseHandling.getServiceTranNumber(trenCancelResponse_string)+ " did not cancel");
                    ExReApiTestReport.warning("ERROR --- the Transaction "+responseHandling.getServiceTranNumber(trenCancelResponse_string)+ " did not cancel").assignCategory("warning");
                     SFFlag=1;
                }else{
                    ExReApiTestReport.info("trenCancelResponse Time : "+ BaseAPI.getResponseTime_OkHttp(trenCancelResponse)+"ms");
                    basePage.avgTimeTranRefund.add(BaseAPI.getResponseTime_OkHttp(trenCancelResponse));



                }



            }else{
                System.out.println(MainFunction.BaseLogStringFunc()+"ERROR --- status code is not 200 or ErrorCodeStatus is not 0 ");
                ExReApiTestReport.fail("ERROR --- status code is not 200"+"("+subTotalResponse.code()+")" +" or ErrorCodeStatus is not 0 "+"("+
                        responseHandling.getErrorCodeStatusJson(subTotalResponse_String)+")");
                 SFFlag=1;
              break;
            }

            subTotalResponse.body().close();
            trenCancelResponse.body().close();
        }//end main for loop
        System.out.println(MainFunction.BaseLogStringFunc()+avgTimeSubTotal);
        System.out.println(MainFunction.BaseLogStringFunc()+ avgTimeTranRefund);
        System.out.println(MainFunction.BaseLogStringFunc()+MainFunction.getAvgTime(avgTimeSubTotal));
        System.out.println(MainFunction.BaseLogStringFunc()+MainFunction.getAvgTime(avgTimeTranRefund));
        if(SFFlag == 1) {
            MainFunction.onTestFailure("subTotalTest");
        }
        ExReApiTestReport.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                .info("avgTimeSubTotal: "+ (MainFunction.getAvgTime(avgTimeSubTotal)+"ms"))
                .info("avgTimetrenCancel: "+MainFunction.getAvgTime(avgTimeTranRefund) +"ms");


    }





}





