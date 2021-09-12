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

                //check that the AccID from the response equal to the AccID from my  test JSON
                tranEndFunctions.getPreDealPointsAccums(i, userDataResponse);

                //make a deal subTotal
                subTotalResponse = tranEndFunctions.makeDealSubTotal(i);
                System.out.println("subTotalResponse: " + subTotalResponse.getBody().asString());
                if (!(subTotalResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse).equals("0"))) {
                    System.out.println("*ERROR --- status code is not 200" + "(" + subTotalResponse.getStatusCode() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenEndResponse) + ")");
                    ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + subTotalResponse.getStatusCode() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                            responseHandling.getErrorCodeStatusJson(subTotalResponse) + ")");
                    LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "subTotalCall");
                    LogFileHandling.createLogFile(subTotalResponse.asString(), LOG_FILE_DIRECTORY, "subTotalResponse");
                    break;
                }


                //this if check if we are using  points or vouchers
                if (JSONGetData.getDealsToUse(TestJSONToSend, i).size() == 0) {
                    //****************************************************************deal without Using Points****************************************************************
                    trenEndResponse = tranEndFunctions.makeDealTrenEnd(i, subTotalResponse);
                    System.out.println("trenEndResponse: " + trenEndResponse.getBody().asString());
                    if (!(trenEndResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse).equals("0"))) {
                        System.out.println("**ERROR --- status code is not 200" + "(" + subTotalResponse.getStatusCode() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenEndResponse) + ")");
                        ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + trenEndResponse.getStatusCode() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "trenEndCall");
                        LogFileHandling.createLogFile(trenEndResponse.asString(), LOG_FILE_DIRECTORY, "trenEndResponse");
                        break;
                    }

                    //check for points after the deal
                    userDataResponse = tranEndFunctions.getUserData(i);
                    if (!(userDataResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse).equals("0"))) {
                        System.out.println("***ERROR --- status code is not 200" + "(" + userDataResponse.getStatusCode() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                "                            responseHandling.getErrorCodeStatusJson(userDataResponse)" + ")");
                        ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.getStatusCode() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(userDataResponse) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.USER_JSON_TO_SEND), LOG_FILE_DIRECTORY, "userDatacall");
                        LogFileHandling.createLogFile(userDataResponse.asString(), LOG_FILE_DIRECTORY, "userDataResponse");
                        break;
                    }
                    tranEndFunctions.getPostDealVouchers(i, userDataResponse);


                    //get Transaction View, with the data of all Discount Data in the end of the deal
                    updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
                    updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "tranKey", responseHandling.getServiceTranNumber(subTotalResponse));

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


                } else {
                    //****************************************************************deal Using Points****************************************************************
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    trenEndResponse = tranEndFunctions.makeDealWithUsingPointsTrenEnd(i, subTotalResponse);
                    System.out.println(trenEndResponse.getBody().asString());
                    if (!(trenEndResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse).equals("0"))) {
                        System.out.println("*****ERROR --- status code is not 200" + "(" + trenEndResponse.getStatusCode() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse) + ")");
                        ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + trenEndResponse.getStatusCode() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse) + ")");
                        LogFileHandling.createLogFile(baseJSON.JSON_TO_SEND, LOG_FILE_DIRECTORY, "trenEndCall");
                        LogFileHandling.createLogFile(trenEndResponse.asString(), LOG_FILE_DIRECTORY, "trenEndResponse");
                        break;
                    }
                    //System.out.println(trenEndResponse.getBody().asString());

                    //check for points after the deal
                    userDataResponse = tranEndFunctions.getUserData(i);
                    if (!(userDataResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse).equals("0"))) {
                        System.out.println("******ERROR --- status code is not 200" + "(" + userDataResponse.getStatusCode() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(userDataResponse) + ")");
                        ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.getStatusCode() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(userDataResponse) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.USER_JSON_TO_SEND), LOG_FILE_DIRECTORY, "userDatacall");
                        LogFileHandling.createLogFile(userDataResponse.asString(), LOG_FILE_DIRECTORY, "userDataResponse");
                        break;
                    }
                    tranEndFunctions.getPostDealVouchers(i, userDataResponse);

                    //get Transaction View, with the data of all Discount Data in the end of the deal
                    updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
                    updateXMLFile.updateGetTransactionView(BaseXML.xmlToDocGetTransactionView(), "tranKey", responseHandling.getServiceTranNumber(subTotalResponse));

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


                }

                TranEndFunctions.AccumsNotInUse(userDataResponse);


            }//end main for loop
        }//end of DealTypeFlag "if"
    }//end ot test





















}//class end

