package Tests;

import BaseClass.BaseAPI;
import BaseClass.BaseXML;
import JSON.JSONGetData;
import Tests.TestFunctions.TrenEndOnePhaseFunctions;
import Utilities.LogFileHandling;
import org.testng.annotations.Test;
import utilities.MainFunction;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static BaseClass.BaseAPI.TEST_API_SYSTEM_URI;

public class TrenEndOnePhaseTest extends BasePage {
    JSONGetData jsonGetData = new JSONGetData();
    TrenEndOnePhaseFunctions trenEndOnePhaseFunctions = new TrenEndOnePhaseFunctions();


//
    @Test
    public void trenEndOnePhaseTest() throws IOException {
       for (int i = 0; i <=jsonGetData.getArraySize(TestJSONToSend) - 1; i++) {
           ExReTrenEndOnePhaseReport.info("~~~~~~~~~~~~~~~~~~~~~~Transaction: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");
           MainFunction.RestGlobals();

           System.out.println("i: " + i);


//get the pre deal points
                try {
                    //System.out.println("pre userDataResponse");
                    userDataResponse = trenEndOnePhaseFunctions.getUserData(i);
                    userDataResponse_String = MainFunction.convertOkHttpResponseToString(userDataResponse);
                    userDataResponse.body().close();
                    trenEndOnePhaseFunctions.getPreDealPointsAccums(i, userDataResponse_String);
                    //System.out.println(preDeal);

                }catch (SocketTimeoutException e){

                    ExReTrenEndOnePhaseReport.fail("ERROR(pre Transaction userDataResponse) --- Socket Timeout Exception ");
                    ExReTrenEndOnePhaseReport.info(
                            LogFileHandling.createLogFile(userDataResponse_String,LOG_FILE_DIRECTORY,"userDataResponse",i+1));
                    ExReTrenEndOnePhaseReport.info(
                            LogFileHandling.createLogFile(baseJSON.memberJsonToSend.toString(),LOG_FILE_DIRECTORY,"userDataPost",i+1));
                    userDataResponse.body().close();
                    continue;


                }
           if(userDataResponse == null){
               ExReTrenEndOnePhaseReport.fail("ERROR -- post userDataResponse is NULL");
               continue;


           }else{
               if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
                   System.out.println("***ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                           responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
                   ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                           responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
                   ExReTrenEndOnePhaseReport.info(
                           LogFileHandling.createLogFile(baseJSON.memberJsonToSend.toString(), LOG_FILE_DIRECTORY, "userDatacall",i+1));
                   ExReTrenEndOnePhaseReport.info(
                           LogFileHandling.createLogFile(userDataResponse_String, LOG_FILE_DIRECTORY, "userDataResponse",i+1));
                   userDataResponse.body().close();
                   continue;


               }
           }
//
// make  trenEnd One Phase
                try{
                    //System.out.println("makeTrenEndOnePhase");
                    trenEndOnePhaseResponse = trenEndOnePhaseFunctions.makeTrenEndOnePhase(i);
                    trenEndOnePhaseResponse_String = MainFunction.convertOkHttpResponseToString(trenEndOnePhaseResponse);
                    avgTimeTrenEndOnePhase.add(BaseAPI.getResponseTime_OkHttp(trenEndOnePhaseResponse));
                    //System.out.println(trenEndOnePhaseResponse_String);
                    trenEndOnePhaseResponse.body().close();


                }catch (SocketTimeoutException e){
                    ExReTrenEndOnePhaseReport.fail("ERROR(trenEndOnePhaseResponse) --- Socket Timeout Exception ");
                    ExReTrenEndOnePhaseReport.info(
                            LogFileHandling.createLogFile(trenEndOnePhaseResponse_String,LOG_FILE_DIRECTORY,"trenEndOnePhaseResponse",i+1));
                    ExReTrenEndOnePhaseReport.info(
                            LogFileHandling.createLogFile(baseJSON.tranEndOnePhaseToSend.toString(),LOG_FILE_DIRECTORY,"trenEndOnePhasePost",i+1));
                    trenEndOnePhaseResponse.close();
                    continue;

                }
                //response null check
                if(trenEndOnePhaseResponse == null){
                    ExReTrenEndOnePhaseReport.fail("ERROR -- trenEndOnePhaseResponse is NULL");
                    continue;

                }else {
                    if (!(trenEndOnePhaseResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String).equals("0"))) {
                        System.out.println("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                "                            responseHandling.getErrorCodeStatusJson(userDataResponse)" + ")");
                        ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
                        ExReTrenEndOnePhaseReport.info(
                                LogFileHandling.createLogFile(baseJSON.tranEndOnePhaseToSend.toString(), LOG_FILE_DIRECTORY, "trenEndOnePhasecall",i+1));
                        ExReTrenEndOnePhaseReport.info(
                                LogFileHandling.createLogFile(trenEndOnePhaseResponse_String, LOG_FILE_DIRECTORY, "trenEndOnePhaseResponse",i+1));
                        userDataResponse.body().close();
                        continue;

                    }
                }


               //System.out.println(BaseAPI.getResponseTime_OkHttp(trenEndOnePhaseResponse));

//get post deal points
               try {
                   //System.out.println(" post userDataResponse");
                   userDataResponse = trenEndOnePhaseFunctions.getUserData(i);
                   userDataResponse_String = MainFunction.convertOkHttpResponseToString(userDataResponse);
                   userDataResponse.body().close();
                   trenEndOnePhaseFunctions.getPostDealPointsAccums(i, userDataResponse_String);
                   //System.out.println(preDeal);

               }catch (SocketTimeoutException e){

                   ExReTrenEndOnePhaseReport.fail("ERROR(post Transaction userDataResponse) --- Socket Timeout Exception ");
                   ExReTrenEndOnePhaseReport.info(
                           LogFileHandling.createLogFile(userDataResponse_String,LOG_FILE_DIRECTORY,"userDataResponse",i+1));
                   ExReTrenEndOnePhaseReport.info(
                           LogFileHandling.createLogFile(baseJSON.memberJsonToSend.toString(),LOG_FILE_DIRECTORY,"userDataPost",i+1));


               }
               //response Null check
               if(userDataResponse == null){
                   ExReTrenEndOnePhaseReport.fail("ERROR -- post userDataResponse is NULL");

               }else {
                   if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
                       System.out.println("***ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                               "                            responseHandling.getErrorCodeStatusJson(userDataResponse)" + ")");
                       ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                               responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
                       ExReTrenEndOnePhaseReport.info(
                               LogFileHandling.createLogFile(baseJSON.memberJsonToSend.toString(), LOG_FILE_DIRECTORY, "userDatacall",i+1));
                       ExReTrenEndOnePhaseReport.info(
                               LogFileHandling.createLogFile(userDataResponse_String, LOG_FILE_DIRECTORY, "userDataResponse",i+1));
                       userDataResponse.body().close();


                   }
               }
               trenEndOnePhaseFunctions.getPostDealVouchers(i, userDataResponse_String);
               //System.out.println(postDeal);


               ////////////
               updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
               updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "tranKey", responseHandling.getTrenEndTranReferenceNumber(trenEndOnePhaseResponse_String));

               transactionViewResponse = APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
               if (!(transactionViewResponse.getStatusCode() == 200)) {
                   System.out.println("****ERROR xml--- status code is not 200 ");
                   ExReAccumReport.fail("ERROR xml(transactionViewResponse)--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
                   ExReTrenEndOnePhaseReport.info(
                           LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                           LOG_FILE_DIRECTORY, "XmlTransactionViewcall",i+1));
                   ExReTrenEndOnePhaseReport.info(
                           LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse",i+1));



               }
               //System.out.println(transactionViewResponse.getBody().asString());

               // "nodeList" is for using in discountLoop
               nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());


               //this for loop run on all the Discounts  and sum theme(sumDealPoints)
               trenEndOnePhaseFunctions.discountLoop(i, nodeList);


               trenEndOnePhaseFunctions.EarnedChecks(i);




       }//end of for loop
        if(!(avgTimeTrenEndOnePhase.size() == 0) ) {
            ExReTrenEndOnePhaseReport.info("avgTimeTrenEndOnePhase: " + MainFunction.getAvgTime(avgTimeTrenEndOnePhase) + "ms");
            ExReTrenEndOnePhaseReport.info("TimeTrenEndOnePhase: " + avgTimeTrenEndOnePhase + "ms");

        }
    }//end of test








}//end of class
