package Tests;

import BaseClass.BaseAPI;
import JSON.JSONCompare;
import JSON.ResponseHandling;
import Tests.TestFunctions.TranRefundFunctions;
import Utilities.LogFileHandling;
import org.testng.annotations.Test;
import utilities.MainFunction;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class TranRefundTest extends BasePage {
    JSON.JSONCompare JSONCompare = new JSONCompare();
    ResponseHandling responseHandling = new ResponseHandling();
    TranRefundFunctions tranRefundFunctions = new TranRefundFunctions();



    @Test(testName = "TranRefundTest")
    public void tranRefundTest () throws IOException {
        for(int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++){
            ExReTernRefundReport.info("~~~~~~~~~~~~~~~~~~~~~~~~ Deal "+(i+1)+" ~~~~~~~~~~~~~~~~~~~~~~~~");

            if(JSONGetData.GetIfToCancelFlag(TestJSONToSend,i).equals("1")){

                tranRefundFunctions.fillPreAllAccumsPoints(i);
                System.out.println(preAllAccumsPoints);

                try {
                subTotalResponse = tranRefundFunctions.makeDealSubTotal(i);
                subTotalResponse_String = MainFunction.convertOkHttpResponseToString(subTotalResponse);

                    if (!(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0"))) {
                        System.out.println("*ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                        ExReTernRefundReport.fail("ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "subTotalCall");
                        LogFileHandling.createLogFile(subTotalResponse_String, LOG_FILE_DIRECTORY, "subTotalResponse");
                        subTotalResponse.body().close();
                        break;
                    }
                }catch (NullPointerException e){
                    System.out.println("ERROE (subTotalResponse) --- The server is currently busy, please try again later ");
                }catch (SocketTimeoutException e){
                    System.out.println("ERROE (subTotalResponse) --- timeout ");

                }


                try {
                trenEndResponse = tranRefundFunctions.makeDealTrenEnd(i,subTotalResponse_String);
                trenEndResponse_String = MainFunction.convertOkHttpResponseToString(trenEndResponse);


                    if (!(trenEndResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse_String).equals("0"))) {
                        System.out.println("**ERROR" +
                                "  --- status code is not 200" + "(" + trenEndResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        ExReTernRefundReport.fail("ERROR --- status code is not 200" + "(" + trenEndResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "trenEndCall");
                        LogFileHandling.createLogFile(trenEndResponse_String, LOG_FILE_DIRECTORY, "trenEndResponse");
                        trenEndResponse.body().close();
                        break;
                    }
                }catch (NullPointerException e){
                    System.out.println("ERROE (trenEndResponse) --- The server is currently busy, please try again later ");
                }catch (SocketTimeoutException e){
                    System.out.println("ERROE (trenEndResponse) --- timeout ");

                }

                try {
                    trenRefundResponse = tranRefundFunctions.makeTranRefund(i,trenEndResponse_String);
                    trenRefundResponse_String = MainFunction.convertOkHttpResponseToString(trenRefundResponse);
                    if (!(trenRefundResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenRefundResponse_String).equals("0"))) {
                        System.out.println("**ERROR" +
                                "  --- status code is not 200" + "(" + trenRefundResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenRefundResponse_String) + ")");
                        ExReTernRefundReport.fail("ERROR --- status code is not 200" + "(" + trenRefundResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "trenEndCall");
                        LogFileHandling.createLogFile(trenRefundResponse_String, LOG_FILE_DIRECTORY, "trenEndResponse");
                        break;
                    }else{
                        avgTimetrenRedund.add(BaseAPI.getResponseTime_OkHttp(trenRefundResponse));
                    }
                }catch (NullPointerException e){
                    System.out.println("ERROE (trenRefundResponse) --- The server is currently busy, please try again later ");
                }catch (SocketTimeoutException e){
                    System.out.println("ERROE (trenRefundResponse) --- timeout ");

                }
                try {
                    System.out.println(BaseAPI.getResponseTime_OkHttp(trenRefundResponse));
                }catch (NullPointerException e){
                    System.out.println("ERROE (trenRefundResponse) --- The server is currently busy, please try again later ");

                }


                tranRefundFunctions.fillPostAllAccumsPoints(i);
                System.out.println(postAllAccumsPoints);

                tranRefundFunctions.prePostCompere();

                ///close response

                subTotalResponse.body().close();
                trenEndResponse.body().close();
                //trenRefundResponse.body().close();


            }//main if for IfToCancelFlag check

        }//main for loop end
        ExReTernRefundReport.info("avgTimetrenRedund: "+ MainFunction.getAvgTime(avgTimetrenRedund)+"ms");

    }//test end
}//class end
