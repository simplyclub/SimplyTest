package Tests;

import BaseClass.BaseAPI;
import Tests.TestFunctions.PointsValidityCases;
import Tests.TestFunctions.PointsValidityFunctions;
import Utilities.LogFileHandling;
import XML.XMLGetData;
import org.testng.annotations.Test;
import utilities.MainFunctions;
import utilities.RetryAnalyzer;

import java.io.IOException;




public class PointsValidityTest extends BasePage {
    PointsValidityFunctions pointsValidityFunctions = new PointsValidityFunctions();
    PointsValidityCases pointsValidityCases = new PointsValidityCases();


    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void pointsValidityTest() throws IOException {

        boolean refChaeckFlag = true ;
        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {
            MainFunctions.RestGlobals();
            // this if  check for deal type flag = 1
            if (JSONGetData.getDealTypeFlag(TestJSONToSend, i).equals("1")) {
                //make a deal subtotal + tranEnd not using points
                ExRePointsValiditReport.info("~~~~~~~~~~~~~~~~~~~~~~Transaction: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println(MainFunctions.BaseLogStringFunc() + "~~~~~~~~~~~~~~~~~~~~~~Deal: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");
                MainFunctions.RestGlobals();

                // resat the base json  that i send in the infrastructure
                baseJSON.BaseJSONCopy();

                //make a deal subTotal
                try {
                    subTotalResponse = pointsValidityFunctions.makeDealSubTotal(i);
                    subTotalResponse_String = MainFunctions.convertOkHttpResponseToString(subTotalResponse);
                    if (!(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0"))) {
                        System.out.println(MainFunctions.BaseLogStringFunc() + "*ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                        ExRePointsValiditReport.fail("ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "subTotalCall", i + 1);
                        LogFileHandling.createLogFile(subTotalResponse.toString(), LOG_FILE_DIRECTORY, "subTotalResponse", i + 1);
                        subTotalResponse.body().close();
                        MainFunctions.onTestFailure("pointsValidityTest");
                        continue;
                    } else {
                        avgTimeSubTotal.add(BaseAPI.getResponseTime_OkHttp(subTotalResponse));

                    }
                } catch (NullPointerException e) {
                    System.out.println(MainFunctions.BaseLogStringFunc() + "ERROE (subTotalResponse) --- The server is currently busy, please try again later ");
                }

                //make trenEnd deal
                try {
                    trenEndResponse = pointsValidityFunctions.makeDealTrenEnd(i, subTotalResponse_String);
                    trenEndResponse_String = MainFunctions.convertOkHttpResponseToString(trenEndResponse);
                    if (!(trenEndResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse_String).equals("0"))) {
                        System.out.println(MainFunctions.BaseLogStringFunc() + "**ERROR" +
                                "  --- status code is not 200" + "(" + trenEndResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        ExRePointsValiditReport.fail("ERROR --- status code is not 200" + "(" + trenEndResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "trenEndCall", i + 1);
                        LogFileHandling.createLogFile(trenEndResponse.toString(), LOG_FILE_DIRECTORY, "trenEndResponse", i + 1);
                        trenEndResponse.body().close();
                        MainFunctions.onTestFailure("pointsValidityTest");
                        continue;
                    } else {
                        avgTimeTrenEnd.add(BaseAPI.getResponseTime_OkHttp(trenEndResponse));

                    }
                } catch (NullPointerException e) {
                    System.out.println(MainFunctions.BaseLogStringFunc() + "ERROE (trenEndResponse) --- The server is currently busy, please try again later ");
                }
                try {
                    userDataResponse = pointsValidityFunctions.getUserData(i, JSONGetData.getCardNumber(TestJSONToSend, i));
                    userDataResponse_String = MainFunctions.convertOkHttpResponseToString(userDataResponse);
                } catch (Exception e) {
                    System.out.println(MainFunctions.BaseLogStringFunc() + "ERROR ---  userDataResponse Exception");

                }

                // post to API and get a response with Member benefit list
                getMemberBenefitListResponse = pointsValidityFunctions.getMemberBenefitList(responseHandling.getSysId(userDataResponse_String));

                if (!(getMemberBenefitListResponse.getStatusCode() == 200)) {
                    System.out.println(MainFunctions.BaseLogStringFunc() + "****ERROR xml--- status code is not 200 ");
                    ExRePointsValiditReport.fail("ERROR xml--- status code is not 200" + "(" + getMemberBenefitListResponse.getStatusCode() + ")");
                    LogFileHandling.createLogFile(String.valueOf((baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION))),
                            LOG_FILE_DIRECTORY, "XmlMemberBenefitListcall", i + 1);
                    LogFileHandling.createLogFile(getMemberBenefitListResponse.asString(), LOG_FILE_DIRECTORY, "XmlMemberBenefitListResponse", i + 1);
                    MainFunctions.onTestFailure("pointsValidityTest");
                    continue;

                }

                //System.out.println(getMemberBenefitListResponse.getBody().asString());

                nodeList = responseHandling.getXMLFileTranViewMemberBenefitData(getMemberBenefitListResponse.getBody().asString());
                System.out.println(MainFunctions.BaseLogStringFunc() + nodeList.getLength());

                //this for loop run on the response array from the getMemberBenefitListResponse
                int NLIndex ;
                int flag ;
                for ( NLIndex= 0, flag = 0; NLIndex < nodeList.getLength(); NLIndex++) {
                    if (flag == 0){

                        // 1st "if" : check for the TrxNumber = TrenEndTranReferenceNumber
                        System.out.println(MainFunctions.BaseLogStringFunc() + responseHandling.getTrenEndTranReferenceNumber(trenEndResponse_String));
                    System.out.println(MainFunctions.BaseLogStringFunc() + XMLGetData.getXmlMBLSysTrxNumber(nodeList, NLIndex));
                    if (responseHandling.getTrenEndTranReferenceNumber(trenEndResponse_String).equals(XMLGetData.getXmlMBLSysTrxNumber(nodeList, NLIndex))) {

                        refChaeckFlag = true ;

                        //2nd "if" : check for promoId in the XML response vs TestJsonToSend
                        for (int accumulatIndex = 0; accumulatIndex < JSONGetData.getArraySizeAccumulates(TestJSONToSend, i); accumulatIndex++) {
                            if (XMLGetData.getXmlMBLPromoId(nodeList, NLIndex).equals(JSONGetData.getPromoId(TestJSONToSend, i, accumulatIndex, "Accumulates"))) {
                                //System.out.println("1");
                                //System.out.println(XMLGetData.getXmlMBLViewAmount(nodeList,NLIndex));


                                //3rd "if" : check for the amount
                                String xmlMBLViewAmount = MainFunctions.convertToDoubleAsString(XMLGetData.getXmlMBLViewAmount(nodeList, NLIndex));
                                String testJsonAmount = MainFunctions.convertToDoubleAsString(XMLGetData.getXmlMBLViewAmount(nodeList, NLIndex));

                                if (xmlMBLViewAmount.equals(testJsonAmount)) {
                                    //System.out.println("2");
                                    if(pointsValidityCases.pointsValidityCases(nodeList, NLIndex, Integer.parseInt(JSONGetData.getPVFlag(TestJSONToSend, i)), i)){
                                        flag=1;
                                        break;
                                    }else {
                                        MainFunctions.onTestFailure("pointsValidityTest");
                                        flag =0 ;
                                    }


                                } else {
                                    System.out.println(MainFunctions.BaseLogStringFunc() + "xmlMBLViewAmount: " + xmlMBLViewAmount + " is not equal to testJsonAmount: " + testJsonAmount);
                                    ExRePointsValiditReport.fail("xmlMBLViewAmount: " + xmlMBLViewAmount + " is not equal to testJsonAmount: " + testJsonAmount);
                                    MainFunctions.onTestFailure("pointsValidityTest");
                                    flag=1;
                                    break;
                                }

                            } else {
                                /*
                                System.out.println(MainFunction.BaseLogStringFunc()+"getXmlMBLPromoId: "+XMLGetData.getXmlMBLPromoId(nodeList,NLIndex) +" is not equal to testJsonPromoId: "+
                                        JSONGetData.getPromoId(TestJSONToSend,i,accumulatIndex,"Accumulates"));
                                ExRePointsValiditReport.fail("getXmlMBLPromoId: "+XMLGetData.getXmlMBLPromoId(nodeList,NLIndex) +" is not equal to testJsonPromoId: "+
                                        JSONGetData.getPromoId(TestJSONToSend,i,accumulatIndex,"Accumulates"));

                                 */
                            }

                        }//end accumulatIndex for loop


                    } else {
                        System.out.println(MainFunctions.BaseLogStringFunc() + "NLIndex: " + NLIndex);

                        refChaeckFlag = false ;
//                        System.out.println(MainFunctions.BaseLogStringFunc() + "TrenEndTranReferenceNumber: " + responseHandling.getTrenEndTranReferenceNumber(trenEndResponse_String)
//                                + " is not equal to XmlMBLSysTrxNumber: " + XMLGetData.getXmlMBLSysTrxNumber(nodeList, NLIndex));


//                        ExRePointsValiditReport.fail("TrenEndTranReferenceNumber: " + responseHandling.getTrenEndTranReferenceNumber(trenEndResponse_String)
//                                + " is not equal to XmlMBLSysTrxNumber: " + XMLGetData.getXmlMBLSysTrxNumber(nodeList, NLIndex));
//                        MainFunctions.onTestFailure("pointsValidityTest");
//                        //System.out.println(MainFunctions.BaseLogStringFunc()+getMemberBenefitListResponse.getBody().asString());
//                        //System.out.println(MainFunctions.BaseLogStringFunc()+trenEndResponse_String);
//
//
//                        break;
                    }

                }else{
                  break;
                    }
                }
                if (!refChaeckFlag){
                    System.out.println(MainFunctions.BaseLogStringFunc() + "TrenEndTranReferenceNumber: " + responseHandling.getTrenEndTranReferenceNumber(trenEndResponse_String)
                                + " is not equal to XmlMBLSysTrxNumber: " + XMLGetData.getXmlMBLSysTrxNumber(nodeList, NLIndex));


                        ExRePointsValiditReport.fail("TrenEndTranReferenceNumber: " + responseHandling.getTrenEndTranReferenceNumber(trenEndResponse_String)
                                + " is not equal to XmlMBLSysTrxNumber: " + XMLGetData.getXmlMBLSysTrxNumber(nodeList, NLIndex));
                        MainFunctions.onTestFailure("pointsValidityTest");

                }


                    subTotalResponse.body().close();
                    trenEndResponse.body().close();

                }//end main if


            //subTotalResponse.close();
            //trenEndResponse.close();
        }//end main for loop
        System.out.println(MainFunctions.BaseLogStringFunc()+"avgTimeSubTotal: "+avgTimeSubTotal);
        System.out.println(MainFunctions.BaseLogStringFunc()+"avgTimeTrenEnd: "+avgTimeTrenEnd);
        ExRePointsValiditReport.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                .info("avgTimeSubTotal: "+ (MainFunctions.getAvgTime(avgTimeSubTotal)+"ms"))
                .info("avgTimeTrenEnd: "+ MainFunctions.getAvgTime(avgTimeTrenEnd) +"ms");

    }//end of test

}//end class
