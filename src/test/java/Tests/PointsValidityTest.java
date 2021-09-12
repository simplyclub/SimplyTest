package Tests;

import Tests.TestFunctions.PointsValidityCases;
import Tests.TestFunctions.PointsValidityFunctions;
import Utilities.LogFileHandling;
import XML.XMLGetData;
import org.testng.annotations.Test;
import utilities.MainFunction;

import java.io.IOException;


public class PointsValidityTest extends BasePage {
    PointsValidityFunctions pointsValidityFunctions = new PointsValidityFunctions();
    PointsValidityCases pointsValidityCases = new PointsValidityCases();

    @Test
    public void pointsValidityTest() throws IOException {
        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {

            // this if  check for deal type flag = 1
            if (JSONGetData.getDealTypeFlag(TestJSONToSend, i).equals("1")) {
                //make a deal subtotal + trenend notusing points
                ExRePointsValiditReport.info("~~~~~~~~~~~~~~~~~~~~~~Deal: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~Deal: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");
                MainFunction.RestGlobals();

                // resat the base json  that i send in the infrastructure
                baseJSON.BaseJSONCopy();

                //make a deal subTotal
                try {
                    subTotalResponse = pointsValidityFunctions.makeDealSubTotal(i);
                    if (!(subTotalResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse).equals("0"))) {
                        System.out.println("*ERROR --- status code is not 200" + "(" + subTotalResponse.getStatusCode() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenEndResponse) + ")");
                        ExRePointsValiditReport.fail("ERROR --- status code is not 200" + "(" + subTotalResponse.getStatusCode() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(subTotalResponse) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "subTotalCall");
                        LogFileHandling.createLogFile(subTotalResponse.asString(), LOG_FILE_DIRECTORY, "subTotalResponse");
                        break;
                    }
                }catch (NullPointerException e){
                    System.out.println("ERROE (subTotalResponse) --- The server is currently busy, please try again later ");
                }

                //make trenEnd deal
                try {
                    trenEndResponse = pointsValidityFunctions.makeDealTrenEnd(i, subTotalResponse);
                    if (!(trenEndResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse).equals("0"))) {
                        System.out.println("**ERROR" +
                                "  --- status code is not 200" + "(" + subTotalResponse.getStatusCode() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenEndResponse) + ")");
                        ExRePointsValiditReport.fail("ERROR --- status code is not 200" + "(" + trenEndResponse.getStatusCode() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                responseHandling.getErrorCodeStatusJson(trenEndResponse) + ")");
                        LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "trenEndCall");
                        LogFileHandling.createLogFile(trenEndResponse.asString(), LOG_FILE_DIRECTORY, "trenEndResponse");
                        break;
                    }
                }catch (NullPointerException e){
                    System.out.println("ERROE (trenEndResponse) --- The server is currently busy, please try again later ");
                }

                // post to API and get a response with Member benefit list
                getMemberBenefitListResponse = pointsValidityFunctions.getMemberBenefitList();
                if (!(getMemberBenefitListResponse.getStatusCode() == 200)) {
                    System.out.println("****ERROR xml--- status code is not 200 ");
                    ExRePointsValiditReport.fail("ERROR xml--- status code is not 200" + "(" + getMemberBenefitListResponse.getStatusCode() + ")");
                    LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                            LOG_FILE_DIRECTORY,"XmlMemberBenefitListcall");
                    LogFileHandling.createLogFile(getMemberBenefitListResponse.asString(), LOG_FILE_DIRECTORY,"XmlMemberBenefitListResponse");

                    break;

                }

                //System.out.println(getMemberBenefitListResponse.getBody().asString());

                nodeList = responseHandling.getXMLFileTranViewMemberBenefitData(getMemberBenefitListResponse.getBody().asString());

                //this for loop run on the response array from the getMemberBenefitListResponse
                for (int NLIndex = 0; NLIndex < nodeList.getLength(); NLIndex++){

                    // 1st "if" : check for the TrxNumber = TrenEndTranReferenceNumber
                    if(responseHandling.getTrenEndTranReferenceNumber(trenEndResponse).equals(XMLGetData.getXmlMBLSysTrxNumber(nodeList,NLIndex))){

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
                        //System.out.println("NLIndex: "+NLIndex);
                        break;
                    }



                }





            }//end main if
        }//end main for loop

    }//end of test

}//end class
