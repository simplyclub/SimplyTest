package Tests;

import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import BaseClass.BaseXML;
import JSON.UserHandling;

import com.sun.org.glassfish.gmbal.Description;
import com.sun.xml.internal.org.jvnet.fastinfoset.ExternalVocabulary;
import io.restassured.response.Response;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.xml.sax.SAXException;
import utilities.MainFunction;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import static BaseClass.BaseAPI.TEST_API_SYSTEM_URI;

public class AccumulationTest extends BasePage {

    @Test(testName = "Accumulation Test - without Using Points",priority = 1)
    @Description("Will run on each of the accumulators  ")
    public void withoutUsingPointsTest() throws TransformerException {
        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {
            System.out.println("i: "+i);
            MainFunction.RestGlobals();
            //check for points before the deal
            userDataResponse = getUserData(i);
            //check that the AccID from the response equal to the AccID from my  test JSON
            getPreDealPointsVouchers(i);
           // System.out.println(preDeal);
            //make a deal subTotal+trenEnd
            subTotalResponse = makeDealSubTotal(i);
            trenEndResponse = makeDealTrenEnd(i);

            //check for points after the deal
            userDataResponse = getUserData(i);
            getPostDealVouchers(i);
          //  System.out.println(postDeal);

            //get Transaction View, with the data of all Discount Data in the end of the deal
            updateXMLFile.updateGetTransactionView(BaseXML.xmlDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
            updateXMLFile.updateGetTransactionView(BaseXML.xmlDocGetTransactionView(), "tranKey", responseHandling.getServiceTranNumber(subTotalResponse, i));

            transactionViewResponse = APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
            System.out.println(transactionViewResponse.getBody().asString());
            // "nodeList" is for using in discountLoop
            nodeList = MainFunction.ReadXMLFile(transactionViewResponse.getBody().asString());

            //System.out.println("node list size : "+ nodeList.getLength());

            //this for loop run on all the Discounts  and sum theme
            discountLoop(i);
            ExReAccumReport.info("Accumulation Test - without Using Points");
            ExReAccumReport.info("sumDealPoints --> " +  sumDealPoints.toString());
           // Checks if the amount of points earned, for each of the Accums , is correct and equal to what I expected to receive
            for(String key : sumDealPoints.keySet()){
                for (int q =0 ;q <= JSONGetData.getArraySizeSumAccum(TestJSONToSend,i);q++){


                    //Compares  the Accum from the "sumDealPoints " to the Accum in the Test JSON
                    if(key.equals(JSONGetData.getSumAccumKey(TestJSONToSend,i,q))){

                        if(sumDealPoints.get(key).toString().equals(JSONGetData.getSumAccumValue(TestJSONToSend,key,i,q)) ){
                            ExReAccumReport.pass(sumDealPoints.get(key) + " equals to " + JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                        }else{
                            ExReAccumReport.fail("sumDealPoints: "+sumDealPoints.get(key) + " NOT equals to "
                                    + "Test Json sumAccum: "+JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                        }
                        break;
                    }

                }

            }

        }//end main for loop
    }// main test end
    @Test(testName = "Accumulation Test - Using Points",priority = 2)
    public void UsingPointsTest() throws TransformerException {
        // ToDo :   1)  Get pre deal vouchers status - done
        //          2)  make sub total deal - done
        //          3)  close the deal with using points - done
        //          4)  Get post deal vouchers status - done
        //          5)  Check if the correct number of points decreased depending on the number of points I used
        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {
            MainFunction.RestGlobals();
            //check for points before the deal
            userDataResponse = getUserData(i);
            //check that the AccID from the response equal to the AccID from my  test JSON
            getPreDealPointsVouchers(i);
            //make a deal subTotal+trenEnd
            subTotalResponse = makeDealSubTotal(i);
            trenEndResponse= makeDealWithUsingPointsTrenEnd(i);
            System.out.println(trenEndResponse.getBody().asString());

            //check for points after the deal
            userDataResponse = getUserData(i);
            getPostDealVouchers(i);
            //get Transaction View, with the data of all Discount Data in the end of the deal
            updateXMLFile.updateGetTransactionView(BaseXML.xmlDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
            updateXMLFile.updateGetTransactionView(BaseXML.xmlDocGetTransactionView(), "tranKey", responseHandling.getServiceTranNumber(subTotalResponse, i));

            transactionViewResponse = APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
            System.out.println(transactionViewResponse.getBody().asString());
            // "nodeList" is for using in discountLoop
            nodeList = MainFunction.ReadXMLFile(transactionViewResponse.getBody().asString());
            //this for loop run on all the Discounts  and sum theme
            discountLoop(i);















        }//main for loop end
    }



        private Response getUserData(int i){
            updateJSONFile.upDateUserJSONFile(
                    JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                    JSONGetData.getFieldId(TestJSONToSend, i), JSONGetData.getFieldValue(TestJSONToSend, i)
            );
            return APIPost.postUserGetData(BaseAPI.TEST_REST_API_URI, BaseJSON.USER_JSON_TO_SEND);

        }
        private Response makeDealSubTotal (int i){
            updateJSONFile.upDateBaseJSONFile(JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i), JSONGetData.getTranItems(TestJSONToSend, i));
            return APIPost.postSubTotal(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

        }
        private Response makeDealTrenEnd (int i){
            updateJSONFile.upDateTranEndJSON(responseHandling.getServiceTranNumber(subTotalResponse, i),JSONGetData.getTranItems(TestJSONToSend,i),null, baseJSON.jsonToSend);
            return  APIPost.postTranEnd(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

        }
        private Response makeDealWithUsingPointsTrenEnd (int i){
            updateJSONFile.upDateTranEndJSON(responseHandling.getServiceTranNumber(subTotalResponse, i),JSONGetData.getTranItems(TestJSONToSend,i),JSONGetData.getDealsToUse(TestJSONToSend,i), baseJSON.jsonToSend);
            return  APIPost.postTranEnd(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

        }
        private void discountLoop(int i){
            for (int XRIndex = 0 ; XRIndex < (nodeList.getLength()) ; XRIndex++ ){//this loop run on the response xml
                for (int TJIndex = 0;TJIndex < JSONGetData.getArraySizeAccumulates(JSONGetData.getTotalAccumulates(TestJSONToSend,i)); TJIndex++){ //this loop run on the testJSON Accumulates

                    //this if check that the promoID from the xml response of end of the deal is equal to the promoID from my test JSON in the "Accumulates" part
                    if (responseHandling.getXmlResponsePromoID(nodeList,XRIndex).equals(JSONGetData.getPromoId(TestJSONToSend,i,TJIndex,"Accumulates"))){

                        //this if check if the amount of the points that received from the specific ofer (by promoID) is equal to the a mount in the test JSON
                        if (responseHandling.getXmlResponseAmount(nodeList,XRIndex).equals(
                                JSONGetData.getAmount(TestJSONToSend,i,TJIndex,"Accumulates"))){

                           // System.out.println("sumDealPoints: "+ sumDealPoints.get(JSONGetData.getAccumID(TestJSONToSend,i,TJIndex,"Accumulates")));

                            //this if check if the accumID exists in the "sum deal point " map list
                            if(sumDealPoints.get(JSONGetData.getAccumID(TestJSONToSend,i,TJIndex,"Accumulates")) != null){

                                String key =JSONGetData.getAccumID(TestJSONToSend,i,TJIndex,"Accumulates");
                                Double val=sumDealPoints.get(JSONGetData.getAccumID(TestJSONToSend,i,TJIndex,"Accumulates")) +
                                        Double.valueOf(responseHandling.getXmlResponseAmount(nodeList,XRIndex));
                                sumDealPoints.replace(key,val);

                            }else {//if the accumID do not exists
                                sumDealPoints.put(String.valueOf(JSONGetData.getAccumID(TestJSONToSend,i,TJIndex,"Accumulates")),
                                        Double.valueOf(responseHandling.getXmlResponseAmount(nodeList,XRIndex)));


                            }

                        }//end 2nd if
                        break;

                    }//end 1st  if
                }//end of TJIndex loop

        }

    }
        private void getPreDealPointsVouchers(int i){
        //this function will add all the pre deal vouchers value to the pre deal HaseMap
        for(int index = 0;index<(responseHandling.getAllAccums(userDataResponse)).size();index++){
             s  = UserHandling.getVoucher(responseHandling.getAllAccums(userDataResponse), "AccID",index);
             for (int j=0;j<responseHandling.getAllAccums(userDataResponse).size();j++) {
                 if (s.equals(UserHandling.getVoucher(JSONGetData.getAllAccumsParaJson(TestJSONToSend, i), "AccID", j))) {
                     preDeal.put(s, Double.parseDouble(UserHandling.getVoucher(responseHandling.getAllAccums(userDataResponse), "BenefitValue", j)));
                     break;
                 } else if (j >= responseHandling.getAllAccums(userDataResponse).size()) {
                     ExReAccumReport.warning((responseHandling.getAllAccums(userDataResponse)) + ": " + "AccID " + s + " not found!");
                 }
             }

        }
    }//func end
        private void getPostDealVouchers(int i ){

        //this function will add all the post deal vouchers value to the pre deal HaseMap
        for(int index = 0;index<(responseHandling.getAllAccums(userDataResponse)).size();index++){
            s  = UserHandling.getVoucher(responseHandling.getAllAccums(userDataResponse), "AccID",index);
            for (int j=0;j<responseHandling.getAllAccums(userDataResponse).size();j++) {
                if (s.equals(UserHandling.getVoucher(JSONGetData.getAllAccumsParaJson(TestJSONToSend, i), "AccID", j))) {
                    postDeal.put(s, Double.parseDouble(UserHandling.getVoucher(responseHandling.getAllAccums(userDataResponse), "BenefitValue", j)));
                    break;
                } else if (j >= responseHandling.getAllAccums(userDataResponse).size()) {
                    ExReAccumReport.warning((responseHandling.getAllAccums(userDataResponse)) + ": " + "AccID " + s + " not found!");
                }
            }

        }
    }//func end




}//class end

