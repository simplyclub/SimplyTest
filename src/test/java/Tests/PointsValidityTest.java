package Tests;

import BaseClass.BaseAPI;
import Tests.TestFunctions.PointsValidityCases;
import Tests.TestFunctions.PointsValidityFunctions;
import Utilities.LogFileHandling;
import XML.XMLGetData;
import org.testng.annotations.Test;
import utilities.MainFunction;
import utilities.RetryAnalyzer;

import java.io.IOException;




public class PointsValidityTest extends BasePage {
    PointsValidityFunctions pointsValidityFunctions = new PointsValidityFunctions();
    PointsValidityCases pointsValidityCases = new PointsValidityCases();


    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void pointsValidityTest() throws IOException {
        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {
            MainFunction.RestGlobals();
            // this if  check for deal type flag = 1
            if (JSONGetData.getDealTypeFlag(TestJSONToSend, i).equals("1")) {
                //make a deal subtotal + tranEnd not using points
                ExRePointsValiditReport.info("~~~~~~~~~~~~~~~~~~~~~~Transaction: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println(MainFunction.BaseLogStringFunc()+"~~~~~~~~~~~~~~~~~~~~~~Deal: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");
                MainFunction.RestGlobals();

                // resat the base json  that i send in the infrastructure
                baseJSON.BaseJSONCopy();

                //make a deal subTotal
                try {
                    subTotalResponse = pointsValidityFunctions.makeDealSubTotal(i);
                    subTotalResponse_String = MainFunction.convertOkHttpResponseToString(subTotalResponse);
                    if (!(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0"))) {
                        System.out.println(MainFunction.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                        ExRePointsValiditReport.fail("ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "subTotalCall",i+1);
                        LogFileHandling.createLogFile(subTotalResponse.toString(), LOG_FILE_DIRECTORY, "subTotalResponse",i+1);
                        subTotalResponse.body().close();
                        MainFunction.onTestFailure("pointsValidityTest");
                        continue;
                    }else{
                        avgTimeSubTotal.add(BaseAPI.getResponseTime_OkHttp(subTotalResponse));

                    }
                }catch (NullPointerException e){
                    System.out.println(MainFunction.BaseLogStringFunc()+"ERROE (subTotalResponse) --- The server is currently busy, please try again later ");
                }

                //make trenEnd deal
                try {
                    trenEndResponse = pointsValidityFunctions.makeDealTrenEnd(i, subTotalResponse_String);
                    trenEndResponse_String = MainFunction.convertOkHttpResponseToString(trenEndResponse);
                    if (!(trenEndResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse_String).equals("0"))) {
                        System.out.println(MainFunction.BaseLogStringFunc()+"**ERROR" +
                                "  --- status code is not 200" + "(" + trenEndResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        ExRePointsValiditReport.fail("ERROR --- status code is not 200" + "(" + trenEndResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "trenEndCall",i+1);
                        LogFileHandling.createLogFile(trenEndResponse.toString(), LOG_FILE_DIRECTORY, "trenEndResponse",i+1);
                        trenEndResponse.body().close();
                        MainFunction.onTestFailure("pointsValidityTest");
                        continue;
                    }else{
                        avgTimeTrenEnd.add(BaseAPI.getResponseTime_OkHttp(trenEndResponse));

                    }
                }catch (NullPointerException e){
                    System.out.println(MainFunction.BaseLogStringFunc()+"ERROE (trenEndResponse) --- The server is currently busy, please try again later ");
                }
                try {
                    userDataResponse = pointsValidityFunctions.getUserData(i, JSONGetData.getCardNumber(TestJSONToSend, i));
                    userDataResponse_String = MainFunction.convertOkHttpResponseToString(userDataResponse);
                }catch (Exception e){
                    System.out.println(MainFunction.BaseLogStringFunc()+"ERROR ---  userDataResponse Exception");

                }

                // post to API and get a response with Member benefit list
                getMemberBenefitListResponse = pointsValidityFunctions.getMemberBenefitList(responseHandling.getSysId(userDataResponse_String));

                if (!(getMemberBenefitListResponse.getStatusCode() == 200)) {
                    System.out.println(MainFunction.BaseLogStringFunc()+"****ERROR xml--- status code is not 200 ");
                    ExRePointsValiditReport.fail("ERROR xml--- status code is not 200" + "(" + getMemberBenefitListResponse.getStatusCode() + ")");
                    LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                            LOG_FILE_DIRECTORY,"XmlMemberBenefitListcall",i+1);
                    LogFileHandling.createLogFile(getMemberBenefitListResponse.asString(), LOG_FILE_DIRECTORY,"XmlMemberBenefitListResponse",i+1);
                    MainFunction.onTestFailure("pointsValidityTest");
                    continue;

                }

                //System.out.println(getMemberBenefitListResponse.getBody().asString());

                nodeList = responseHandling.getXMLFileTranViewMemberBenefitData(getMemberBenefitListResponse.getBody().asString());

                //this for loop run on the response array from the getMemberBenefitListResponse
                for (int NLIndex = 0; NLIndex < nodeList.getLength(); NLIndex++){

                    // 1st "if" : check for the TrxNumber = TrenEndTranReferenceNumber
                    if(responseHandling.getTrenEndTranReferenceNumber(trenEndResponse_String).equals(XMLGetData.getXmlMBLSysTrxNumber(nodeList,NLIndex))){

                        //2nd "if" : check for promoId in the XML response vs TestJsonToSend
                        for(int accumulatIndex = 0 ; accumulatIndex < JSONGetData.getArraySizeAccumulates(TestJSONToSend,i); accumulatIndex++){
                            if(XMLGetData.getXmlMBLPromoId(nodeList,NLIndex).equals(JSONGetData.getPromoId(TestJSONToSend,i,accumulatIndex,"Accumulates"))){
                                //System.out.println("1");
                                //System.out.println(XMLGetData.getXmlMBLViewAmount(nodeList,NLIndex));


                                //3rd "if" : check for the amount
                                String xmlMBLViewAmount = MainFunction.converToDoubleAsString(XMLGetData.getXmlMBLViewAmount(nodeList,NLIndex));
                                String  testJsonAmount = MainFunction.converToDoubleAsString(XMLGetData.getXmlMBLViewAmount(nodeList,NLIndex));

                                if (xmlMBLViewAmount.equals(testJsonAmount)){
                                    //System.out.println("2");
                                    pointsValidityCases.pointsValidityCases(nodeList,NLIndex,Integer.parseInt(JSONGetData.getPVFlag(TestJSONToSend,i)),i);
                                }

                            }

                        }//end accumulatIndex for loop



                    }else {
                        System.out.println(MainFunction.BaseLogStringFunc()+"NLIndex: "+NLIndex);
                        break;
                    }



                }



                subTotalResponse.body().close();
                trenEndResponse.body().close();

            }//end main if


            //subTotalResponse.close();
            //trenEndResponse.close();
        }//end main for loop
        System.out.println(MainFunction.BaseLogStringFunc()+"avgTimeSubTotal: "+avgTimeSubTotal);
        System.out.println(MainFunction.BaseLogStringFunc()+"avgTimeTrenEnd: "+avgTimeTrenEnd);
        ExRePointsValiditReport.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
                .info("avgTimeSubTotal: "+ (MainFunction.getAvgTime(avgTimeSubTotal)+"ms"))
                .info("avgTimeTrenEnd: "+MainFunction.getAvgTime(avgTimeTrenEnd) +"ms");

    }//end of test

}//end class
