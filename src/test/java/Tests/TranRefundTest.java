package Tests;

import BaseClass.BaseAPI;
import JSON.JSONCompare;
import JSON.ResponseHandling;
import Tests.TestFunctions.TranRefundFunctions;
import Utilities.LogFileHandling;
import org.testng.annotations.Test;
import utilities.MainFunctions;
import utilities.RetryAnalyzer;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class TranRefundTest extends BasePage {
    JSON.JSONCompare JSONCompare = new JSONCompare();
    ResponseHandling responseHandling = new ResponseHandling();
    TranRefundFunctions tranRefundFunctions = new TranRefundFunctions();



    @Test(testName = "TranRefundTest",retryAnalyzer = RetryAnalyzer.class)
    public void tranRefundTest () throws IOException {
        avgTimeTranRefund.clear();

        for(int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++){


            if(JSONGetData.GetIfToCancelFlag(TestJSONToSend,i).equals("1")){
                ExReTernRefundReport.info("~~~~~~~~~~~~~~~~~~~~~~~~ Transaction "+(i+1)+" ~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println(MainFunctions.BaseLogStringFunc()+"~~~~~~~~~~~~~~~~~~~~~~~~ Transaction "+(i+1)+" ~~~~~~~~~~~~~~~~~~~~~~~~");

                tranRefundFunctions.fillPreAllAccumsPoints(i);
                System.out.println(MainFunctions.BaseLogStringFunc()+"preAllAccumsPoints: "+preAllAccumsPoints);

                try {
                subTotalResponse = tranRefundFunctions.makeDealSubTotal(i);
                subTotalResponse_String = MainFunctions.convertOkHttpResponseToString(subTotalResponse);

                    if (!(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0"))) {
                        System.out.println(MainFunctions.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                        ExReTernRefundReport.fail("ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.jsonToSend.toString(), LOG_FILE_DIRECTORY, "subTotalCall",i+1);
                        LogFileHandling.createLogFile(subTotalResponse_String, LOG_FILE_DIRECTORY, "subTotalResponse",i+1);
                        subTotalResponse.body().close();
                        MainFunctions.onTestFailure("tranRefundTest");
                        continue;
                    }
                }catch (NullPointerException e){
                    System.out.println(MainFunctions.BaseLogStringFunc()+"ERROE (subTotalResponse) --- The server is currently busy, please try again later ");
                    continue;
                }catch (SocketTimeoutException e){
                    System.out.println(MainFunctions.BaseLogStringFunc()+"ERROE (subTotalResponse) --- timeout ");
                    continue;

                }


                try {
                trenEndResponse = tranRefundFunctions.makeDealTrenEnd(i,subTotalResponse_String);
                trenEndResponse_String = MainFunctions.convertOkHttpResponseToString(trenEndResponse);


                    if (!(trenEndResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse_String).equals("0"))) {
                        System.out.println("**ERROR" +
                                "  --- status code is not 200" + "(" + trenEndResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        ExReTernRefundReport.fail("ERROR --- status code is not 200" + "(" + trenEndResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.jsonToSend.toString(), LOG_FILE_DIRECTORY, "trenEndCall",i+1);
                        LogFileHandling.createLogFile(trenEndResponse_String, LOG_FILE_DIRECTORY, "trenEndResponse",i+1);
                        trenEndResponse.body().close();
                        MainFunctions.onTestFailure("tranRefundTest");
                        continue;
                    }
                }catch (NullPointerException e){
                    System.out.println(MainFunctions.BaseLogStringFunc()+"ERROR (trenEndResponse) --- The server is currently busy, please try again later ");
                    continue;
                }catch (SocketTimeoutException e){
                    System.out.println(MainFunctions.BaseLogStringFunc()+"ERROR (trenEndResponse) --- timeout ");
                    continue;

                }

                try {
                    trenRefundResponse = tranRefundFunctions.makeTranRefund(i,trenEndResponse_String);
                    trenRefundResponse_String = MainFunctions.convertOkHttpResponseToString(trenRefundResponse);
                    if (!(trenRefundResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenRefundResponse_String).equals("0"))) {
                        System.out.println("**ERROR" +
                                "  --- status code is not 200" + "(" + trenRefundResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenRefundResponse_String) + ")");
                        ExReTernRefundReport.fail("ERROR --- status code is not 200" + "(" + trenRefundResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.tranRefundToSend.toString(), LOG_FILE_DIRECTORY, "trenEndCall",i+1);
                        LogFileHandling.createLogFile(trenRefundResponse_String, LOG_FILE_DIRECTORY, "trenEndResponse",i+1);
                        MainFunctions.onTestFailure("tranRefundTest");
                        break;
                    }else{
                        avgTimeTranRefund.add(BaseAPI.getResponseTime_OkHttp(trenRefundResponse));

                    }
                }catch (NullPointerException e){
                    System.out.println(MainFunctions.BaseLogStringFunc()+"ERROR (trenRefundResponse) --- The server is currently busy, please try again later ");
                    continue;
                }catch (SocketTimeoutException e){
                    System.out.println(MainFunctions.BaseLogStringFunc()+"ERROR (trenRefundResponse) --- timeout ");
                    continue;

                }
                try {
                    System.out.println(BaseAPI.getResponseTime_OkHttp(trenRefundResponse));
                }catch (NullPointerException e){
                    System.out.println(MainFunctions.BaseLogStringFunc()+"ERROR (trenRefundResponse) --- The server is currently busy, please try again later ");
                    continue;

                }


                tranRefundFunctions.fillPostAllAccumsPoints(i);
                System.out.println(MainFunctions.BaseLogStringFunc()+"postAllAccumsPoints: "+postAllAccumsPoints);

                tranRefundFunctions.prePostCompere();

                ///close response

                subTotalResponse.body().close();
                trenEndResponse.body().close();
                //trenRefundResponse.body().close();


            }//main if for IfToCancelFlag check

        }//main for loop end
        ExReTernRefundReport.info("avgTimetrenRedund: "+ MainFunctions.getAvgTime(avgTimeTranRefund)+"ms");
        System.out.println(MainFunctions.BaseLogStringFunc()+"TimeTranRefund: "+avgTimeTranRefund);

    }//test end

}//class end
