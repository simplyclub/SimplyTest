package Tests;

import BaseClass.BaseAPI;
import BaseClass.BaseXML;
import Tests.TestFunctions.TranEndFunctions;
import Utilities.LogFileHandling;
import org.testng.annotations.Test;
import utilities.MainFunctions;
import utilities.RetryAnalyzer;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.SocketTimeoutException;

import static BaseClass.BaseAPI.TEST_API_SYSTEM_URI;


public class TranEndTest extends BasePage {

//, retryAnalyzer = RetryAnalyzer.class
    @Test(testName = "TrenEnd Test ", retryAnalyzer = RetryAnalyzer.class)
    public void tranEndTest() throws TransformerException, IOException {

        TranEndFunctions tranEndFunctions = new TranEndFunctions();
        int SFFlag=0;


// i <= JSONGetData.getArraySize(TestJSONToSend) - 1
            for ( int i = 0;i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {
                try{

                if (JSONGetData.getDealTypeFlag(TestJSONToSend, i).equals("0")) {
                    ExReAccumReport.info("~~~~~~~~~~~~~~~~~~~~~~ Transaction: " + (i + 1) + " ~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println(MainFunctions.BaseLogStringFunc()+"~~~~~~~~~~~~~~~~~~~~~~Deal: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");
                    MainFunctions.RestGlobals();

                    // resat the base json  that i send in the infrastructure
                    baseJSON.BaseJSONCopy();

                    //check for points before the deal
                    userDataResponse = tranEndFunctions.getUserData(i);
                    userDataResponse_String = MainFunctions.convertOkHttpResponseToString(userDataResponse);

                    userDataResponse.body().close();

                    //check that the AccID from the response equal to the AccID from my  test JSON
                    tranEndFunctions.getPreDealPointsAccums(i, userDataResponse_String);

                    //make a deal subTotal
                    subTotalResponse = tranEndFunctions.makeDealSubTotal(i);
                    subTotalResponse_String = MainFunctions.convertOkHttpResponseToString(subTotalResponse);
                    //System.out.println("subTotalResponse: " + subTotalResponse.body().string());
                    if (!(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0"))) {
                        System.out.println("*ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                        ExReAccumReport.fail("*ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "subTotalCall",i+1);
                        LogFileHandling.createLogFile(subTotalResponse_String, LOG_FILE_DIRECTORY, "subTotalResponse",i+1);
                        subTotalResponse.body().close();
                        MainFunctions.onTestFailure("tranEndTest");
                        break;
                    } else {

                        avgTimeSubTotal.add(BaseAPI.getResponseTime_OkHttp(subTotalResponse));
                        ExReAccumReport.info("TransactionTimeSubTotal: "+avgTimeSubTotal.get(i)+"ms");
                        System.out.println(avgTimeSubTotal);
                    }


                    //this if check if we are using  points or vouchers
                    if (JSONGetData.getDealsToUse(TestJSONToSend, i).size() == 0) {
                        //****************************************************************Transaction without Using Points****************************************************************
                        ExReAccumReport.info("~~~~~ Transaction without Realization Points ~~~~~");
                        trenEndResponse = tranEndFunctions.makeDealTrenEnd(i, subTotalResponse_String);
                        // System.out.println(BaseAPI.getResponseTime_OkHttp(trenEndResponse));
                        trenEndResponse_String = MainFunctions.convertOkHttpResponseToString(trenEndResponse);
                       // System.out.println(MainFunctions.BaseLogStringFunc() + trenEndResponse.body().string());

                        if (!(trenEndResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse_String).equals("0"))) {
                            System.out.println(MainFunctions.BaseLogStringFunc() +"**ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                            ExReAccumReport.fail("**ERROR --- status code is not 200" + "(" + trenEndResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                    responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                            LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "trenEndCall",i+1);
                            LogFileHandling.createLogFile(trenEndResponse_String, LOG_FILE_DIRECTORY, "trenEndResponse",i+1);
                            trenEndResponse.body().close();
                            MainFunctions.onTestFailure("tranEndTest");
                            break;
                        } else {
                            avgTimeTrenEnd.add( BaseAPI.getResponseTime_OkHttp(trenEndResponse));
                            ExReAccumReport.info("TransactionTimeTrenEnd: "+avgTimeTrenEnd.get(i)+"ms");
                            System.out.println(MainFunctions.BaseLogStringFunc() +avgTimeTrenEnd);
                        }

                        //check for points after the deal
                        userDataResponse = tranEndFunctions.getUserData(i);
                        //System.out.println(BaseAPI.getResponseTime_OkHttp(userDataResponse));
                        userDataResponse_String = MainFunctions.convertOkHttpResponseToString(userDataResponse);

                        if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
                            System.out.println(MainFunctions.BaseLogStringFunc() +"***ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                    "                            responseHandling.getErrorCodeStatusJson(userDataResponse)" + ")");
                            ExReAccumReport.fail("***ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                    responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
                            LogFileHandling.createLogFile(baseJSON.getString(baseJSON.MEMBER_JSON_TO_SEND), LOG_FILE_DIRECTORY, "userDatacall",i+1);
                            LogFileHandling.createLogFile(userDataResponse_String, LOG_FILE_DIRECTORY, "userDataResponse",i+1);
                            userDataResponse.body().close();
                            MainFunctions.onTestFailure("tranEndTest");
                            break;
                        }
                        tranEndFunctions.getPostDealVouchers( userDataResponse_String);
                        userDataResponse.body().close();


                        //get Transaction View, with the data of all Discount Data in the end of the deal
                        updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
                        updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "tranKey", responseHandling.getServiceTranNumber(subTotalResponse_String));

                        transactionViewResponse = APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
                        if (!(transactionViewResponse.getStatusCode() == 200)) {
                            System.out.println(MainFunctions.BaseLogStringFunc() +"****ERROR xml--- status code is not 200 ");
                            ExReAccumReport.fail("****ERROR xml--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
                            LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                                    LOG_FILE_DIRECTORY, "XmlTransactionViewcall",i+1);
                            LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse",i+1);
                            MainFunctions.onTestFailure("tranEndTest");
                            break;

                        }
                        //System.out.println(transactionViewResponse.getBody().asString());
                        // "nodeList" is for using in discountLoop
                        nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());


                        //this for loop run on all the Discounts  and sum theme(sumDealPoints)
                        tranEndFunctions.discountLoop(i, nodeList);
                        ExReAccumReport.info("Accumulation Test -  without Realization with accumulation");
                        ExReAccumReport.info("sumDealPoints --> " + sumDealPoints.toString());

                        // Checks if the amount of points earned, for each of the Accums , is correct and equal to what I expected to receive
                        if(!tranEndFunctions.earnedChecks(i)){
                            SFFlag=1;
                        }



                        if (!tranEndFunctions.totalDealPaidCalculation(responseHandling.getXMLFilePaidTotal(transactionViewResponse.getBody().asString()), i)){
                            MainFunctions.onTestFailure("tranEndTest");
                        }

                        trenEndResponse.body().close();


                    } else {
                        //****************************************************************deal Using Points****************************************************************
                        ExReAccumReport.info("~~~~~ Transaction Realization Points ~~~~~");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        trenEndResponse = tranEndFunctions.makeDealWithUsingPointsTrenEnd(i, subTotalResponse_String);
                        trenEndResponse_String = MainFunctions.convertOkHttpResponseToString(trenEndResponse);
                        //System.out.println(BaseAPI.getResponseTime_OkHttp(trenEndResponse));
                        //System.out.println(MainFunctions.BaseLogStringFunc() +trenEndResponse.body().string());
                        if (!(trenEndResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse_String).equals("0"))) {
                            System.out.println(MainFunctions.BaseLogStringFunc() +"*****ERROR --- status code is not 200" + "(" + trenEndResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                    responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                            ExReAccumReport.fail("*****ERROR --- status code is not 200" + "(" + trenEndResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                    responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                            LogFileHandling.createLogFile(baseJSON.JSON_TO_SEND, LOG_FILE_DIRECTORY, "trenEndCall",i+1);
                            LogFileHandling.createLogFile(trenEndResponse_String, LOG_FILE_DIRECTORY, "trenEndResponse",i+1);
                            trenEndResponse.body().close();
                            MainFunctions.onTestFailure("tranEndTest");
                            break;
                        } else {
                            avgTimeTrenEnd.add(BaseAPI.getResponseTime_OkHttp(trenEndResponse));
                            ExReAccumReport.info("DealTimeTrenEnd: "+avgTimeTrenEnd.get(i)+"ms");
                            System.out.println(MainFunctions.BaseLogStringFunc() +avgTimeTrenEnd);

                        }
                        //System.out.println(trenEndResponse.getBody().asString());

                        //check for points after the deal

                        userDataResponse = tranEndFunctions.getUserData(i);

                        userDataResponse_String = MainFunctions.convertOkHttpResponseToString(userDataResponse);
                        //System.out.println(BaseAPI.getResponseTime_OkHttp(userDataResponse));

                        if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
                            System.out.println(MainFunctions.BaseLogStringFunc() +"******ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                    responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
                            ExReAccumReport.fail("******ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                    responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
                            LogFileHandling.createLogFile(baseJSON.getString(baseJSON.MEMBER_JSON_TO_SEND), LOG_FILE_DIRECTORY, "userDatacall",i+1);
                            LogFileHandling.createLogFile(userDataResponse_String, LOG_FILE_DIRECTORY, "userDataResponse",i+1);
                            userDataResponse.body().close();
                            MainFunctions.onTestFailure("tranEndTest");
                            break;
                        }
                        userDataResponse.body().close();

                        tranEndFunctions.getPostDealVouchers(userDataResponse_String);

                        //get Transaction View, with the data of all Discount Data in the end of the deal
                        updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
                        updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "tranKey", responseHandling.getServiceTranNumber(subTotalResponse_String));

                        transactionViewResponse = APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
                        if (!(transactionViewResponse.getStatusCode() == 200)) {
                            System.out.println(MainFunctions.BaseLogStringFunc() +"ERROR xml--- status code is not 200 ");
                            ExReAccumReport.fail("ERROR xml--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
                            LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                                    LOG_FILE_DIRECTORY, "XmlTransactionViewcall",i+1);
                            LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse",i+1);
                            MainFunctions.onTestFailure("tranEndTest");
                            break;

                        }
                        //System.out.println(transactionViewResponse.getBody().asString());

                        // "nodeList" is for using in discountLoop
                        nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());


                        //this for loop run on all the Discounts  and sum theme
                        tranEndFunctions.discountLoop(i, nodeList);


                        tranEndFunctions.sumBurnd(i);
                        tranEndFunctions.pointUseCalculation(i);
                        tranEndFunctions.sumDealToUsePointsCheck(i);

                        // Checks if the amount of points earned, for each of the Accums , is correct and equal to what I expected to receive
                        ExReAccumReport.info("~~~~~~~ .earnedChecks ~~~~~~~");
                        tranEndFunctions.earnedChecks(i);

                        //this function will check and compere the discounts in the "DealToUse" that are in thr TestJSONToSend
                        //  to the one in the TransactionView(XML).
                        ExReAccumReport.info("~~~~~~~ .DiscountConfirmTest - post transaction tests~~~~~~~");

                        if(!tranEndFunctions.DiscountConfirmTest(nodeList, i)){
                            SFFlag=1;
                        }
                        ExReAccumReport.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


                        tranEndFunctions.totalDealPaidCalculation(responseHandling.getXMLFilePaidTotal(transactionViewResponse.getBody().asString()), i);

                        trenEndResponse.body().close();


                    }

                    if(!TranEndFunctions.AccumsNotInUse(userDataResponse_String)){
                        SFFlag=1;
                    }


                }//end of DealTypeFlag "if"
                    }catch (SocketTimeoutException e){
                         ExReAccumReport.warning("ERROR---- Socket Timeout Exception  ");
                         System.out.println(MainFunctions.BaseLogStringFunc()+"ERROR---- Socket Timeout Exception  ");
                         MainFunctions.onTestFailure("tranEndTest");
                         SFFlag = 0;


                    }

            }//end main for loop




        System.out.println(MainFunctions.BaseLogStringFunc() +avgTimeSubTotal);
        System.out.println(MainFunctions.BaseLogStringFunc() +avgTimeTrenEnd);
        ExReAccumReport.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                .info("avgTimeSubTotal: "+ (MainFunctions.getAvgTime(avgTimeSubTotal)+"ms"))
                .info("avgTimeTrenEnd: "+ MainFunctions.getAvgTime(avgTimeTrenEnd) +"ms");
        if(SFFlag == 1){
            MainFunctions.onTestFailure("tranEndTest");
        }
    }//end ot test

}//class end

