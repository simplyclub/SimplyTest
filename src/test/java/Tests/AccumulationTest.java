package Tests;

import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import BaseClass.BaseXML;
import JSON.UserHandling;

import com.sun.org.glassfish.gmbal.Description;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import utilities.MainFunction;
import javax.xml.transform.TransformerException;
import java.text.DecimalFormat;

import static BaseClass.BaseAPI.TEST_API_SYSTEM_URI;

public class AccumulationTest extends BasePage {

    @Test(testName = "Accumulation Test - without Using Points", priority = 1)
    @Description("Will run on each of the accumulators  ")
    public void withoutUsingPointsTest() throws TransformerException {
        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {
            System.out.println("i: " + i);
            MainFunction.RestGlobals();

            //check for points before the deal
            userDataResponse = getUserData(i);

            //check that the AccID from the response equal to the AccID from my  test JSON
            getPreDealPointsVouchers(i);


            //make a deal subTotal+trenEnd
            subTotalResponse = makeDealSubTotal(i);
            trenEndResponse = makeDealTrenEnd(i);

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

            //System.out.println("node list size : "+ nodeList.getLength());

            //this for loop run on all the Discounts  and sum theme(sumDealPoints)
            discountLoop(i);
            ExReAccumReport.info("Accumulation Test - without Using Points");
            ExReAccumReport.info("sumDealPoints --> " + sumDealPoints.toString());

            // Checks if the amount of points earned, for each of the Accums , is correct and equal to what I expected to receive
            for (String key : sumDealPoints.keySet()) {
                for (int q = 0; q <= JSONGetData.getArraySizeSumAccum(TestJSONToSend, i); q++) {


                    //Compares  the Accum from the "sumDealPoints " to the Accum in the Test JSON
                    if (key.equals(JSONGetData.getSumAccumKey(TestJSONToSend, i, q))) {
                        //Corrects the figure to two digits after the decimal point
                        double d = (postDeal.get(key) - preDeal.get(key));
                        DecimalFormat df = new DecimalFormat("#.##");
                        String dx = df.format(d);
                        d = Double.valueOf(dx);

                        if (sumDealPoints.get(key).toString().equals(JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q)) && d ==
                                Double.valueOf(JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q))) {

                            ExReAccumReport.pass(sumDealPoints.get(key) + " equals to " + JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                            ExReAccumReport.pass(d + " equals to " + JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                        } else {
                            ExReAccumReport.fail("*sumDealPoints: " + sumDealPoints.get(key) + " NOT equals to "
                                    + "Test Json sumAccum: " + JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                            ExReAccumReport.fail("(postDeal.get(" + key + ") - preDeal.get(" + key + ")): " + d + " NOT equals to "
                                    + "Test Json sumAccum: " + JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                        }
                        break;
                    }

                }

            }

        }//end main for loop
    }// main test end

    @Test(testName = "Accumulation Test - Using Points", priority = 2)
    @Description("Will run on each of the accumulators  ")
    public void UsingPointsTest() throws TransformerException {
        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {
            MainFunction.RestGlobals();

            //check for points before the deal
            userDataResponse = getUserData(i);

            //check that the AccID from the response equal to the AccID from my  test JSON
            getPreDealPointsVouchers(i);

            //make a deal subTotal+trenEnd
            subTotalResponse = makeDealSubTotal(i);
            trenEndResponse = makeDealWithUsingPointsTrenEnd(i);
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
            //System.out.println(sumDealPoints);
            pointUseCalculation(i);
            sumDealToUsePointsCheck(i);


        }//main for loop end
    }




    /**
     * The functions here are for fixed operations that occur only within the current test
     */
    private Response getUserData(int i) {
        updateJSONFile.upDateUserJSONFile(
                JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getFieldId(TestJSONToSend, i), JSONGetData.getFieldValue(TestJSONToSend, i)
        );
        return APIPost.postUserGetData(BaseAPI.TEST_REST_API_URI, BaseJSON.USER_JSON_TO_SEND);

    }//func end
    private Response makeDealSubTotal(int i) {
        updateJSONFile.upDateBaseJSONFile(JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i), JSONGetData.getTranItems(TestJSONToSend, i));
        return APIPost.postSubTotal(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

    }//func end
    private Response makeDealTrenEnd(int i) {
        updateJSONFile.upDateTranEndJSON(responseHandling.getServiceTranNumber(subTotalResponse, i), JSONGetData.getTranItems(TestJSONToSend, i), null, baseJSON.jsonToSend);
        return APIPost.postTranEnd(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

    }//func end
    private Response makeDealWithUsingPointsTrenEnd(int i) {
        updateJSONFile.upDateTranEndJSON(responseHandling.getServiceTranNumber(subTotalResponse, i), JSONGetData.getTranItems(TestJSONToSend, i), JSONGetData.getDealsToUse(TestJSONToSend, i), baseJSON.jsonToSend);
        return APIPost.postTranEnd(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

    }//func end
    private void discountLoop(int i) {
        for (int XRIndex = 0; XRIndex < (nodeList.getLength()); XRIndex++) {//this loop run on the response xml
            for (int TJIndex = 0; TJIndex < JSONGetData.getArraySizeAccumulates(JSONGetData.getTotalAccumulates(TestJSONToSend, i)); TJIndex++) { //this loop run on the testJSON Accumulates

                //this if check that the promoID from the xml response of end of the deal is equal to the promoID from my test JSON in the "Accumulates" part
                //System.out.println("XRIndex: "+XRIndex +" TJIndex: "+TJIndex);
                //System.out.println("response XML PromoID: "+responseHandling.getXmlResponsePromoID(nodeList, XRIndex)+" TestJson PromoID: "+JSONGetData.getPromoId(TestJSONToSend, i, TJIndex, "Accumulates"));
                if (responseHandling.getXmlResponsePromoID(nodeList, XRIndex).equals(JSONGetData.getPromoId(TestJSONToSend, i, TJIndex, "Accumulates"))) {

                    //this if check if the amount of the points that received from the specific ofer (by promoID) is equal to the a mount in the test JSON
                    //System.out.println("response XML Amount: "+responseHandling.getXmlResponseAmount(nodeList, XRIndex)+" TestJson Amount: "+JSONGetData.getAmount(TestJSONToSend, i, TJIndex, "Accumulates"));
                    if (responseHandling.getXmlResponseAmount(nodeList, XRIndex).equals(
                            JSONGetData.getAmount(TestJSONToSend, i, TJIndex, "Accumulates"))) {

                        // System.out.println("sumDealPoints: "+ sumDealPoints.get(JSONGetData.getAccumID(TestJSONToSend,i,TJIndex,"Accumulates")));

                        //this if check if the accumID exists in the "sum deal point " map list
                        if (sumDealPoints.get(JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates")) != null) {

                            String key = JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates");
                            //           sumDealPoint current amount
                            //  sunDealPoints = (sumDealPoints) + (Discount Amount from the next promoID)
                            Double val = sumDealPoints.get(key) + Double.valueOf(responseHandling.getXmlResponseAmount(nodeList, XRIndex));
                            sumDealPoints.replace(key, val);


                        } else {//if the accumID do not exists
                            sumDealPoints.put(String.valueOf(JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates")),
                                    Double.valueOf(responseHandling.getXmlResponseAmount(nodeList, XRIndex)));


                        }

                    }//end 2nd if
                    break;

                }//end 1st  if
            }//end of TJIndex loop

        }

    }//func end

    /**
     * this function will add all the pre deal vouchers value to the pre deal HaseMap
     * @param i
     */
    private void getPreDealPointsVouchers(int i) {
        //first loop run on the size of array, from the response of the user benefit status
        for (int index = 0; index < (responseHandling.getAllAccums(userDataResponse)).size(); index++) {
            s = UserHandling.getVoucher(responseHandling.getAllAccums(userDataResponse), "AccID", index);

            // this if is for Security check, to see if  the AllAccums array size is ok
            if(responseHandling.getAllAccums(userDataResponse).size() == JSONGetData.getAllAccumsArray(TestJSONToSend,i).size()) {
                for (int j = 0; j < responseHandling.getAllAccums(userDataResponse).size(); j++) {

                    if (s.equals(UserHandling.getVoucher(JSONGetData.getAllAccumsArray(TestJSONToSend, i), "AccID", j))) {
                        preDeal.put(s, Double.parseDouble(UserHandling.getVoucher(responseHandling.getAllAccums(userDataResponse), "BenefitValue", j)));
                        //System.out.println("****preDeal: "+Double.parseDouble(UserHandling.getVoucher(responseHandling.getAllAccums(userDataResponse), "BenefitValue", j)));
                        break;
                    } else if (j >= responseHandling.getAllAccums(userDataResponse).size()) {
                        ExReAccumReport.warning((responseHandling.getAllAccums(userDataResponse)) + ": " + "AccID " + s + " not found!");
                    }
                }//end 2nd for loop (j)
            }else {
                System.out.println("ERROR -- Response AllAccums array is not equal to Test JSON  AllAccums array size");
                ExReAccumReport.warning("ERROR -- Response AllAccums array is not equal to Test JSON  AllAccums array size");
                break;
            }//end of if/else Security check

        }
    }//func end
    private void getPostDealVouchers(int i) {

        //this function will add all the post deal vouchers value to the pre deal HaseMap
        for (int index = 0; index < (responseHandling.getAllAccums(userDataResponse)).size(); index++) {
            s = UserHandling.getVoucher(responseHandling.getAllAccums(userDataResponse), "AccID", index);
            for (int j = 0; j < responseHandling.getAllAccums(userDataResponse).size(); j++) {
                if (s.equals(UserHandling.getVoucher(JSONGetData.getAllAccumsArray(TestJSONToSend, i), "AccID", j))) {
                    postDeal.put(s, Double.parseDouble(UserHandling.getVoucher(responseHandling.getAllAccums(userDataResponse), "BenefitValue", j)));
                    break;
                } else if (j >= responseHandling.getAllAccums(userDataResponse).size()) {
                    ExReAccumReport.warning((responseHandling.getAllAccums(userDataResponse)) + ": " + "AccID " + s + " not found!");
                }
            }

        }
    }//func end
    private void pointUseCalculation(int i) {

        // this loop run on the DealToUse array
        for (int t = 0; t < JSONGetData.getDealsToUse(TestJSONToSend, i).size(); t++) {


            double val = 0.0;
            // this if check if the AccumID is in sumDealToUsePoints and sum it if not she creates one
            if (sumDealToUsePoints.get(JSONGetData.getDealsToUseAccumId(TestJSONToSend, i, t)) != null) {
                val = Double.valueOf(JSONGetData.getDealsToUseAmount(TestJSONToSend, i, t)) /
                        Double.valueOf(JSONGetData.getDealsToUsePToSValue(TestJSONToSend, i, t));
                //System.out.println("val"+ val);
                sumVal += val;

                sumDealToUsePoints.replace(JSONGetData.getDealsToUseAccumId(TestJSONToSend, i, t), sumVal);

            } else {
                //Security check for  DiscountType
                if (JSONGetData.getDealsToUseDiscountType(TestJSONToSend, i, t).equals("1")) {
                    val = Double.valueOf(JSONGetData.getDealsToUseAmount(TestJSONToSend, i, t)) /
                            Double.valueOf(JSONGetData.getDealsToUsePToSValue(TestJSONToSend, i, t));
                } else {
                    val = Double.valueOf(JSONGetData.getDealsToUseAmount(TestJSONToSend, i, t));
                }
                sumDealToUsePoints.put(JSONGetData.getDealsToUseAccumId(TestJSONToSend, i, t), val);
                // System.out.println("else val : "+val);

            }
            // System.out.println(sumDealToUsePoints);


        }//end for loop

    }//end func
    private void sumDealToUsePointsCheck(int i) {
//        for (int t = 0; t < JSONGetData.getDealsToUse(TestJSONToSend, i).size(); t++) {
//            System.out.println("t=" + t);
//            System.out.println("****************");
            //System.out.println(sumDealToUsePoints);
            for (String key : sumDealToUsePoints.keySet()) {
                //System.out.println(key);


                //Security check for  DiscountType
                if (getDiscountType(key, i).equals("1")) {
                    //System.out.println(getDiscountType(key, i));



                    double d = preDeal.get(key) + sumDealPoints.get(key) - sumDealToUsePoints.get(key);
                    DecimalFormat df = new DecimalFormat("#.##");
                    String dx = df.format(d);
                    d = Double.valueOf(dx);


                    if (postDeal.get(key) == d) {
                        //System.out.println("ok");
                        ExReAccumReport.pass("*AccumID: " + key + " pass Accumulation Test - Using Points").assignCategory("Using Points");
                        ExReAccumReport.info("AccumID: " + key + " DiscountType = 1");
                        //sumDealToUsePoints.remove(key);
                    }else {
                        ExReAccumReport.fail("*AccumID: " + key + " did NOT pass Accumulation Test - Using Points").assignCategory("fail");
                        ExReAccumReport.info(" preDeal: " + preDeal.get(key) +
                                " sumDealToUsePoints: " + sumDealToUsePoints.get(key) +
                                " postDeal: " + postDeal.get(key));
                    }
                }//end of DiscountType = 1

                else {
                    if (getDiscountType(key, i).equals("0")) {
                       // System.out.println(getDiscountType(key, i));

                        //coupon
                        if (sumDealPoints.get(key) == null) {
                            double d = preDeal.get(key) - Double.valueOf(getBurned(key,i));
                            DecimalFormat df = new DecimalFormat("#.##");
                            String dx = df.format(d);
                            d = Double.valueOf(dx);


                            if (postDeal.get(key) == d) {
                               // System.out.println("ok2");
                                ExReAccumReport.pass("AccumID: " + key + " pass Accumulation Test - Using Points").assignCategory("Using Points");
                                ExReAccumReport.info("AccumID: " + key + "DiscountType = 1 , coupon");
                            }else{
                                ExReAccumReport.fail("AccumID: " + key + " did NOT pass Accumulation Test - Using Points").assignCategory("fail");
                                ExReAccumReport.info(" coupon: preDeal: " + preDeal.get(key) +
                                        " sumDealToUsePoints: " + sumDealToUsePoints.get(key) +
                                        " postDeal: " + postDeal.get(key));

                            }

                        } //end coupon
                        else {

                            //buy-get
                           // System.out.println(preDeal.get(key));
                           // System.out.println(sumDealToUsePoints.get(key));
                            //System.out.println(Double.valueOf(getBurned(key, i)));
                           // System.out.println(postDeal.get(key));

                            if (preDeal.get(key) + sumDealToUsePoints.get(key) - Double.valueOf(getBurned(key, i)) == postDeal.get(key)) {
                                //System.out.println("ok3");
                                ExReAccumReport.pass("AccumID: " + key + " pass Accumulation Test - Using Points").assignCategory("Using Points");
                                ExReAccumReport.info("AccumID: " + key + " DiscountType = 0");

                            } else {
                                ExReAccumReport.fail("AccumID: " + key + " did NOT pass Accumulation Test - Using Points").assignCategory("fail");
                                ExReAccumReport.info(" preDeal: " + preDeal.get(key) +
                                        " sumDealToUsePoints: " + sumDealToUsePoints.get(key) +
                                        " Burned: " + Double.valueOf(getBurned(key, i)) +
                                        " postDeal: " + postDeal.get(key));
                            }
                        }

                    }
                }

            }//end sumDealToUsePoint for loop
       // }//end main for loop


    }//func end

/**
 * this functions get data from the "dealToUse" part in the TestJson(JSONParametersToSend.json)
 */

    //this function return the DiscountType for a given AccumID in the TestJSON
    private String getDiscountType(String key, int i) {
        JSONArray arr = JSONGetData.getDealsToUse(TestJSONToSend, i);
        for (int q = 0; q < arr.size(); q++) {
            JSONObject x = (JSONObject) arr.get(q);
            if (key.equals(x.get("AccumId"))) {
                return x.get("DiscountType").toString();
            }

        }
        return null;
    }
    //this function return the Burned for a given AccumID in the TestJSON
    private String getBurned(String key, int i){
        JSONArray arr = JSONGetData.getDealsToUse(TestJSONToSend, i);
        for (int q = 0; q < arr.size(); q++) {
            JSONObject x = (JSONObject) arr.get(q);
            if (key.equals(x.get("AccumId"))) {
                return x.get("Burned").toString();
            }

        }
        return null;
    }
    //this function return the PromoID for a given AccumID in the TestJSON
    private String getPromoID(String key, int i){
        JSONArray arr = JSONGetData.getDealsToUse(TestJSONToSend, i);
        for (int q = 0; q < arr.size(); q++) {
            JSONObject x = (JSONObject) arr.get(q);
            if (key.equals(x.get("AccumId"))) {
                return x.get("Burned").toString();
            }

        }
        return null;
    }







}//class end

