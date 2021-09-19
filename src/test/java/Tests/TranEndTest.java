package Tests;

import BaseClass.BaseXML;
import Tests.TestFunctions.TranEndFunctions;
import Utilities.LogFileHandling;
import com.sun.org.glassfish.gmbal.Description;
import org.testng.annotations.Test;
import utilities.MainFunction;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import static BaseClass.BaseAPI.TEST_API_SYSTEM_URI;


public class TranEndTest extends BasePage {


    @Test(testName = "TrenEnd Test ", priority = 1)
    @Description("Will run on each of the accumulators  ")
    public void tranEndTest() throws TransformerException, IOException {
        TranEndFunctions tranEndFunctions = new TranEndFunctions();


        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {
            if (JSONGetData.getDealTypeFlag(TestJSONToSend, i).equals("0")) {
                //TODO : add a "if " to check the "DealTypeFlag" after the loop start all deals in this test need to be DealTypeFlag = 0
                ExReAccumReport.info("~~~~~~~~~~~~~~~~~~~~~~Deal: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~Deal: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");
                MainFunction.RestGlobals();

                // resat the base json  that i send in the infrastructure
                baseJSON.BaseJSONCopy();

                //check for points before the deal
                userDataResponse = tranEndFunctions.getUserData(i);
                userDataResponse_String = MainFunction.convertOkHttpResponseToString(userDataResponse);
                userDataResponse.body().close();

                //check that the AccID from the response equal to the AccID from my  test JSON
                tranEndFunctions.getPreDealPointsAccums(i, userDataResponse_String);

                //make a deal subTotal
                subTotalResponse = tranEndFunctions.makeDealSubTotal(i);
                subTotalResponse_String = MainFunction.convertOkHttpResponseToString(subTotalResponse);
                System.out.println("subTotalResponse: " + subTotalResponse.body().string());
                if (!(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0"))) {
                    System.out.println("*ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                    ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                            responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                    LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "subTotalCall");
                    LogFileHandling.createLogFile(subTotalResponse_String, LOG_FILE_DIRECTORY, "subTotalResponse");
                    subTotalResponse.body().close();
                    break;
                }


                //this if check if we are using  points or vouchers
                if (JSONGetData.getDealsToUse(TestJSONToSend, i).size() == 0) {
                    //****************************************************************deal without Using Points****************************************************************
                    trenEndResponse = tranEndFunctions.makeDealTrenEnd(i, subTotalResponse_String);
                    trenEndResponse_String = MainFunction.convertOkHttpResponseToString(trenEndResponse);
                    System.out.println("trenEndResponse: " + trenEndResponse.body().string());
                    if (!(trenEndResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse_String).equals("0"))) {
                        System.out.println("**ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + trenEndResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "trenEndCall");
                        LogFileHandling.createLogFile(trenEndResponse_String, LOG_FILE_DIRECTORY, "trenEndResponse");
                        trenEndResponse.body().close();
                        break;
                    }

                    //check for points after the deal
                    userDataResponse = tranEndFunctions.getUserData(i);
                    userDataResponse_String = MainFunction.convertOkHttpResponseToString(userDataResponse);
                    if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
                        System.out.println("***ERROR --- status code is not 200" + "(" + userDataResponse.code()+ ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                "                            responseHandling.getErrorCodeStatusJson(userDataResponse)" + ")");
                        ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.USER_JSON_TO_SEND), LOG_FILE_DIRECTORY, "userDatacall");
                        LogFileHandling.createLogFile(userDataResponse_String, LOG_FILE_DIRECTORY, "userDataResponse");
                        userDataResponse.body().close();
                        break;
                    }
                    tranEndFunctions.getPostDealVouchers(i, userDataResponse_String);
                    userDataResponse.body().close();


                    //get Transaction View, with the data of all Discount Data in the end of the deal
                    updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
                    updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "tranKey", responseHandling.getServiceTranNumber(subTotalResponse_String));

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
                    tranEndFunctions.discountLoop(i, nodeList);
                    ExReAccumReport.info("Accumulation Test - without Using Points");
                    ExReAccumReport.info("sumDealPoints --> " + sumDealPoints.toString());

                    // Checks if the amount of points earned, for each of the Accums , is correct and equal to what I expected to receive
                    tranEndFunctions.earnedChecks(i);


                    tranEndFunctions.totalDealPaidCalculation(responseHandling.getXMLFilePaidTotal(transactionViewResponse.getBody().asString()), i);

                    trenEndResponse.body().close();


                } else {
                    //****************************************************************deal Using Points****************************************************************
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    trenEndResponse = tranEndFunctions.makeDealWithUsingPointsTrenEnd(i, subTotalResponse_String);
                    trenEndResponse_String = MainFunction.convertOkHttpResponseToString(trenEndResponse);
                    System.out.println(trenEndResponse.body().string());
                    if (!(trenEndResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse_String).equals("0"))) {
                        System.out.println("*****ERROR --- status code is not 200" + "(" + trenEndResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + trenEndResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.JSON_TO_SEND, LOG_FILE_DIRECTORY, "trenEndCall");
                        LogFileHandling.createLogFile(trenEndResponse.toString(), LOG_FILE_DIRECTORY, "trenEndResponse");
                        trenEndResponse.body().close();
                        break;
                    }
                    //System.out.println(trenEndResponse.getBody().asString());

                    //check for points after the deal
                    userDataResponse = tranEndFunctions.getUserData(i);
                    userDataResponse_String = MainFunction.convertOkHttpResponseToString(userDataResponse);
                    if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
                        System.out.println("******ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
                        ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.USER_JSON_TO_SEND), LOG_FILE_DIRECTORY, "userDatacall");
                        LogFileHandling.createLogFile(userDataResponse.toString(), LOG_FILE_DIRECTORY, "userDataResponse");
                        userDataResponse.body().close();
                        break;
                    }
                    userDataResponse.body().close();

                    tranEndFunctions.getPostDealVouchers(i, userDataResponse_String);

                    //get Transaction View, with the data of all Discount Data in the end of the deal
                    updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
                    updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "tranKey", responseHandling.getServiceTranNumber(subTotalResponse_String));

                    transactionViewResponse = APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
                    if (!(transactionViewResponse.getStatusCode() == 200)) {
                        System.out.println("ERROR xml--- status code is not 200 ");
                        ExReAccumReport.fail("ERROR xml--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
                        LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                                LOG_FILE_DIRECTORY, "XmlTransactionViewcall");
                        LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse");
                        break;

                    }
                    System.out.println(transactionViewResponse.getBody().asString());

                    // "nodeList" is for using in discountLoop
                    nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());


                    //this for loop run on all the Discounts  and sum theme
                    tranEndFunctions.discountLoop(i, nodeList);


                    tranEndFunctions.sumBurnd(i);
                    tranEndFunctions.pointUseCalculation(i);
                    tranEndFunctions.sumDealToUsePointsCheck(i);

                    // Checks if the amount of points earned, for each of the Accums , is correct and equal to what I expected to receive
                    tranEndFunctions.earnedChecks(i);
                    tranEndFunctions.DiscountConfirmTest(nodeList, i);
                    tranEndFunctions.totalDealPaidCalculation(responseHandling.getXMLFilePaidTotal(transactionViewResponse.getBody().asString()), i);

                    trenEndResponse.body().close();


                }

                TranEndFunctions.AccumsNotInUse(userDataResponse_String);


            }//end main for loop
        }//end of DealTypeFlag "if"
    }//end ot test





















}//class end

