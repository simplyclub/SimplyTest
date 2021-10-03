package Tests;

import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
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



    @Test
    public void trenEndOnePhaseTest() throws IOException {
       for (int i = 0; i <= jsonGetData.getArraySize(TestJSONToSend) - 1; i++) {
           ExReTrenEndOnePhaseReport.info("~~~~~~~~~~~~~~~~~~~~~~Deal: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");
           MainFunction.RestGlobals();
           try {
               System.out.println("i: " + i);


//get the pre deal points

               userDataResponse = trenEndOnePhaseFunctions.getUserData(i);
               userDataResponse_String = MainFunction.convertOkHttpResponseToString(userDataResponse);
               userDataResponse.body().close();
               trenEndOnePhaseFunctions.getPreDealPointsAccums(i, userDataResponse_String);
               System.out.println(preDeal);
//
// make  trenEnd One Phase

               trenEndOnePhaseResponse = trenEndOnePhaseFunctions.makeTrenEndOnePhase(i);
               trenEndOnePhaseResponse_String = MainFunction.convertOkHttpResponseToString(trenEndOnePhaseResponse);
               avgTimeTrenEndOnePhase.add(BaseAPI.getResponseTime_OkHttp(trenEndOnePhaseResponse));
               System.out.println(trenEndOnePhaseResponse_String);
               //System.out.println(BaseAPI.getResponseTime_OkHttp(trenEndOnePhaseResponse));

//get post deal points
               userDataResponse = trenEndOnePhaseFunctions.getUserData(i);
               userDataResponse_String = MainFunction.convertOkHttpResponseToString(userDataResponse);

               if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
                   System.out.println("***ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                           "                            responseHandling.getErrorCodeStatusJson(userDataResponse)" + ")");
                   ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                           responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
                   LogFileHandling.createLogFile(baseJSON.getString(baseJSON.USER_JSON_TO_SEND), LOG_FILE_DIRECTORY, "userDatacall");
                   LogFileHandling.createLogFile(userDataResponse_String, LOG_FILE_DIRECTORY, "userDataResponse");
                   userDataResponse.body().close();
                   break;
               }
               trenEndOnePhaseFunctions.getPostDealVouchers(i, userDataResponse_String);
               System.out.println(postDeal);


               ////////////
               updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
               updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "tranKey", responseHandling.getTrenEndTranReferenceNumber(trenEndOnePhaseResponse_String));

               transactionViewResponse = APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
               if (!(transactionViewResponse.getStatusCode() == 200)) {
                   System.out.println("****ERROR xml--- status code is not 200 ");
                   ExReAccumReport.fail("ERROR xml--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
                   LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                           LOG_FILE_DIRECTORY, "XmlTransactionViewcall");
                   LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse");

                   break;

               }
               System.out.println(transactionViewResponse.getBody().asString());
               // "nodeList" is for using in discountLoop
               nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());


               //this for loop run on all the Discounts  and sum theme(sumDealPoints)
               trenEndOnePhaseFunctions.discountLoop(i, nodeList);


               trenEndOnePhaseFunctions.earnedChecks(i);


           }catch (SocketTimeoutException e){
               System.out.println("ERROR---- Timeout Exception  ");
               ExReTrenEndOnePhaseReport.warning("ERROR---- Timeout Exception  ");

               MainFunction.RestGlobals();

           }


       }//end of for loop
        ExReTrenEndOnePhaseReport.info("avgTimeTrenEndOnePhase: "+ MainFunction.getAvgTime(avgTimeTrenEndOnePhase)+"ms");
    }//end of test








}//end of class
