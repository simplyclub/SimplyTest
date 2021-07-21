package Tests.TestFunctions;

import APIHandling.APIPost;
import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import FunctionsClass.UpdateJSONFile;
import JSON.JSONGetData;
import JSON.ResponseHandling;
import JSON.UserHandling;
import Tests.BasePage;

import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.DecimalFormat;

import static java.lang.Math.abs;

public class TranEndFunctions extends BasePage {
    UpdateJSONFile updateJSONFile = new UpdateJSONFile();

    public TranEndFunctions() {
    }
    //todo: add her all the help function from TrenEndTest

    /**
     *  Checks if the amount of points earned, for each of the Accums , is correct and equal to what I expected to receive
     *  in the TestJSON
     *
     * @param i
     */
    public void earnedChecks(int i){
        // Checks if the amount of points earned, for each of the Accums , is correct and equal to what I expected to receive
        for (String key : sumDealPoints.keySet()) {
            for (int q = 0; q <= JSONGetData.getArraySizeSumAccum(TestJSONToSend, i); q++) {


                //Compares  the Accum from the "sumDealPoints " to the Accum in the Test JSON
                if (key.equals(JSONGetData.getSumAccumKey(TestJSONToSend, i, q))) {
                    String dx = null;
                    DecimalFormat df = new DecimalFormat("#.##");
                    double d = 0.0 ;

                    //Corrects the figure to two digits after the decimal point
                    if (sumDealToUsePoints.get(key) == null) {
                        d = (postDeal.get(key) - preDeal.get(key));
                        dx = df.format(d);
                        d = Double.valueOf(dx);
                    }else{
                        if (sumBurnd.get(key)!= null) {
                            d = (postDeal.get(key) - preDeal.get(key) + sumDealToUsePoints.get(key) - sumBurnd.get(key));
                            dx = df.format(d);
                            d = Double.valueOf(dx);
                        }
                        else{
                            d = (postDeal.get(key) - preDeal.get(key) + sumDealToUsePoints.get(key));
                            dx = df.format(d);
                            d = Double.valueOf(dx);

                        }

                    }


                    String sumDeal = sumDealPoints.get(key).toString();
                    dx = df.format(Double.valueOf(sumDeal));
                    sumDeal = Double.valueOf(dx).toString();

                    String SumAccumValue = JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q);

                    double m = Double.valueOf(JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                    dx = df.format(m);
                    m = Double.valueOf(dx);

                    if (sumDeal.equals(SumAccumValue) && d == m) {

                        ExReAccumReport.pass(sumDeal + " equals to " + SumAccumValue);
                        ExReAccumReport.pass(d + " equals to " + JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                    } else {
                        ExReAccumReport.fail("*sumDealPoints: " + df.format(sumDealPoints.get(key)) + " NOT equals to "
                                + "Test Json sumAccum: " + JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                        ExReAccumReport.fail("(postDeal.get(" + key + ") - preDeal.get(" + key + ")): " + d + " NOT equals to "
                                + "Test Json sumAccum: " + JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                    }
                    break;
                }
            }
        }

    }



    public static void  AccumsNotInUse(Response userDataResponse ){
        String accID=null ;
        for(int i = 0; i< ResponseHandling.getAllAccums(userDataResponse).size() ; i++) {

            accID = UserHandling.getVoucher(ResponseHandling.getAllAccums(userDataResponse), "AccID",i);

            if(sumDealPoints.get(accID)== null && sumDealToUsePoints.get(accID)== null){
                if(preDeal.get(accID).equals(postDeal.get(accID))) {
                    //System.out.println("*-*aacID: " + accID);
                    ExReAccumReport.pass("AccumID: "+ accID +"is not change ").assignCategory("AccumsNotInUse");
                    ExReAccumReport.info("preDeal: "+preDeal.get(accID) + " postDeal: "+postDeal.get(accID)).assignCategory("AccumsNotInUse");
                }

            }

        }//end  main for loop
    }//end function


    /**this function return the DiscountType for a given AccumID in the TestJSON
     *
     * @param key AccumID
     * @param i The test deal number from the TestJson
     * @return DiscountType - String
     */

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
    public  Response getUserData(int i) {
        updateJSONFile.upDateUserJSONFile(
                JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getFieldId(TestJSONToSend, i), JSONGetData.getFieldValue(TestJSONToSend, i)
        );
        return APIHandling.APIPost.postUserGetData(BaseAPI.TEST_REST_API_URI, BaseJSON.USER_JSON_TO_SEND);

    }//func end
    public Response makeDealSubTotal(int i) {
        updateJSONFile.upDateBaseJSONFile(JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getAccoundID(TestJSONToSend, i), JSONGetData.getTranItems(TestJSONToSend, i),JSONGetData.getCardNumber(TestJSONToSend, i));
        return APIPost.postSubTotal(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

    }//func end
    //todo:     change JSONGetData.getDealsToUse(TestJSONToSend, i) --> null (nned to check
    //          Need to check with future dells without using points if it works.
    //          if it works i can  delete the second function "makeDealWithUsingPointsTrenEnd"
    public Response makeDealTrenEnd(int i,Response subTotalResponse) {
        updateJSONFile.upDateTranEndJSON(ResponseHandling.getServiceTranNumber(subTotalResponse), JSONGetData.getAccoundID(TestJSONToSend, i),JSONGetData.getCardNumber(TestJSONToSend, i),
                JSONGetData.getTranItems(TestJSONToSend, i), JSONGetData.getDealsToUse(TestJSONToSend, i), baseJSON.jsonToSend);
        return APIPost.postTranEnd(BaseAPI.TEST_REST_API_URI, baseJSON.JSON_TO_SEND);

    }//func end

    public Response makeDealWithUsingPointsTrenEnd(int i,Response subTotalResponse) {
        updateJSONFile.upDateTranEndJSON(ResponseHandling.getServiceTranNumber(subTotalResponse), JSONGetData.getAccoundID(TestJSONToSend, i),JSONGetData.getCardNumber(TestJSONToSend, i),
                JSONGetData.getTranItems(TestJSONToSend, i), JSONGetData.getDealsToUse(TestJSONToSend, i), baseJSON.jsonToSend);
        return APIPost.postTranEnd(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

    }//func end

    /**this function return the Burned for a given AccumID in the TestJSON
     *
     * @param key AccumID
     * @param i The test deal number from the TestJson
     * @return Burned - String
     */
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

    public void sumDealToUsePointsCheck(int i) {

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
                    ExReAccumReport.fail("*AccumID: " + key + " did NOT pass Accumulation Test - Using Points, Deal number: "+i).assignCategory("fail");
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
                        try {
                            double d = preDeal.get(key) - sumBurnd.get(key);
                            DecimalFormat df = new DecimalFormat("#.##");
                            String dx = df.format(d);
                            d = Double.valueOf(dx);

                            //System.out.println(postDeal);
                            if (postDeal.get(key) == d) {
                                // System.out.println("ok2");
                                ExReAccumReport.pass("AccumID: " + key + " pass Accumulation Test - Using Points").assignCategory("Using Points");
                                ExReAccumReport.info("AccumID: " + key + "DiscountType = 1 , coupon");

                            }else{
                                ExReAccumReport.fail("**AccumID: " + key + " did NOT pass Accumulation Test - Using Points, Deal number: "+i).assignCategory("fail");
                                ExReAccumReport.info(" coupon: preDeal: " + preDeal.get(key) +
                                        " sumDealToUsePoints: " + sumDealToUsePoints.get(key) +
                                        " postDeal: " + postDeal.get(key));

                            }
                        }catch (NullPointerException e){
                            System.out.println("ERROR -- coupon : null data in the calculation ");
                        }
                    } //end coupon
                    else {

                        //buy-get

                        //System.out.println(key);
                        //sumDealPoints
                        //sumDealToUsePoints
                        if (preDeal.get(key) + sumDealPoints.get(key) - Double.valueOf(getBurned(key, i)) == postDeal.get(key)) {
                            //System.out.println("ok3");
                            ExReAccumReport.pass("AccumID: " + key + " pass Accumulation Test - Using Points").assignCategory("Using Points");
                            ExReAccumReport.info("AccumID: " + key + " DiscountType = 0");

                        } else {
                            ExReAccumReport.fail("***AccumID: " + key + " did NOT pass Accumulation Test - Using Points, Deal number: "+i).assignCategory("fail");
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
     * this function will check and coumper the discounts in the "DealToUse"
     * @param nodeList
     * @param i the index for the deal in the "TestJson"
     */
    public void DiscountConfirmTest(NodeList nodeList , int i) {
        String xmlPromoID = null;
        String DealToUsePromoID = null;
        Double xmlTotalAmount = 0.0;
        Double jsonTotalAmount = 0.0;


        for (int q = 0; q < nodeList.getLength(); q++) {
            Node xrdl = nodeList.item(q);
            if (xrdl.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) xrdl;


                //this for loop run on the "DealToUse" array and check if the promoID of a Discount from
                //the xml is in the TestJson DealToUse

                for (int t = 0; t < JSONGetData.getDealsToUse(TestJSONToSend, i).size(); t++) {
                    xmlPromoID = eElement.getElementsByTagName("PromoID").item(0).getTextContent();
                    DealToUsePromoID = JSONGetData.getDealsToUsePromoID(TestJSONToSend, i, t);

                    if (xmlPromoID.equals(DealToUsePromoID)) {

                        // this if check if there is Conversion ratio from points to shekels"PToSValue"
                        String x =  JSONGetData.getDealsToUsePToSValue(TestJSONToSend, i, t);
                        if (!(JSONGetData.getDealsToUsePToSValue(TestJSONToSend, i, t).equals("0")) ){
                            try {


                                xmlTotalAmount = abs(Double.valueOf(eElement.getElementsByTagName("Amount").item(0).getTextContent()));
                                jsonTotalAmount = Double.valueOf(JSONGetData.getDealsToUseAmount(TestJSONToSend, i, t)) /
                                        Double.valueOf(JSONGetData.getDealsToUsePToSValue(TestJSONToSend, i, t));

                                if (xmlTotalAmount.equals(jsonTotalAmount)) {
                                    ExReAccumReport.pass("xmlPromoID " + xmlPromoID + " found in TestJSON");
                                    ExReAccumReport.info("xmlTotalAmount: " + xmlTotalAmount + " jsonTotalAmount: " + jsonTotalAmount)
                                            .assignCategory("Confirm Discounts Amount");
                                    break;

                                }else {

                                    ExReAccumReport.fail("xmlTotalAmount: " + xmlTotalAmount + " jsonTotalAmount: " + jsonTotalAmount)
                                            .assignCategory("Confirm Discounts Amount");
                                    ExReAccumReport.info("additional information : test number("+i+")"+" xmlPromoID("+xmlPromoID+")"+" jsonDealToUsePromoID("+DealToUsePromoID+")"+
                                            "Please note, this \"buy-get\" Discounts IS ALL OR NOTHING")
                                            .assignCategory("Confirm Discounts Amount");
                                    break;


                                }


                            }catch (ArithmeticException e){
                                System.out.println("ERROR -- Division by zero , promoID: "+DealToUsePromoID+" , test num: "+i);
                                ExReAccumReport.warning("ERROR -- Division by zero , promoID: "+DealToUsePromoID +" , test num: "+i);
                            }catch (Exception e){
                                System.out.println("ERROR -- Error unknown , promoID: "+DealToUsePromoID+" , test num: "+i);
                                ExReAccumReport.warning("ERROR -- Error unknown , promoID: "+DealToUsePromoID +" , test num: "+i);

                            }

                        } else {
                            xmlTotalAmount = abs(Double.valueOf(eElement.getElementsByTagName("Amount").item(0).getTextContent()));
                            jsonTotalAmount = Double.valueOf(JSONGetData.getDealsToUseAmount(TestJSONToSend, i, t));
                            if (xmlTotalAmount.equals(jsonTotalAmount)) {
                                ExReAccumReport.pass("xmlPromoID " + xmlPromoID + " found in TestJSON");
                                ExReAccumReport.info("xmlTotalAmount: " + xmlTotalAmount + " jsonTotalAmount: " + jsonTotalAmount)
                                        .assignCategory("Confirm Discounts Amount");
                                break;

                            }else{
                                ExReAccumReport.fail("xmlTotalAmount: " + xmlTotalAmount + " jsonTotalAmount: " + jsonTotalAmount)
                                        .assignCategory("Confirm Discounts Amount");
                                ExReAccumReport.info("additional information : test number("+i+")"+" xmlPromoID("+xmlPromoID+")"+" jsonDealToUsePromoID("+DealToUsePromoID+")")
                                        .assignCategory("Confirm Discounts Amount");
                                break;

                            }
                        }
                    }

                }
            }
        }
    }//end of func

    public void discountLoop(int i,NodeList nodeList) {
        for (int XRIndex = 0; XRIndex < (nodeList.getLength()); XRIndex++) {//this loop run on the response xml
            for (int TJIndex = 0; TJIndex < JSONGetData.getArraySizeAccumulates(JSONGetData.getTotalAccumulates(TestJSONToSend, i)); TJIndex++) { //this loop run on the testJSON Accumulates

                //this if check that the promoID from the xml response of end of the deal is equal to the promoID from my test JSON in the "Accumulates" part
                if (ResponseHandling.getXMLResponsePromoID(nodeList, XRIndex).equals(JSONGetData.getPromoId(TestJSONToSend, i, TJIndex, "Accumulates"))) {

                    //this if check if the amount of the points that received from the specific ofer (by promoID) is equal to the a mount in the test JSON
                    if (ResponseHandling.getXMLResponseAmount(nodeList, XRIndex).equals(
                            JSONGetData.getAmount(TestJSONToSend, i, TJIndex, "Accumulates"))) {


                        //this if check if the accumID exists in the "sum deal point " map list
                        if (sumDealPoints.get(JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates")) != null) {

                            String key = JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates");
                            //           sumDealPoint current amount
                            //  sunDealPoints = (sumDealPoints) + (Discount Amount from the next promoID)
                            Double val = sumDealPoints.get(key) + Double.valueOf(ResponseHandling.getXMLResponseAmount(nodeList, XRIndex));
                            sumDealPoints.replace(key, val);


                        } else {//if the accumID do not exists
                            sumDealPoints.put(String.valueOf(JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates")),
                                    Double.valueOf(ResponseHandling.getXMLResponseAmount(nodeList, XRIndex)));


                        }

                    }//end 2nd if
                    break;

                }//end 1st  if
            }//end of TJIndex loop

        }

    }//func end


    public void totalDealPaidCalculation(String paidTotal, int i){
//        double _negativeCashBack = Double.valueOf(negativeCashBack);
//        DecimalFormat df = new DecimalFormat("#.##");
//        String dx = df.format(_negativeCashBack);
//        _negativeCashBack = Double.valueOf(dx);

        double _paidTotal =Double.valueOf(paidTotal);
        DecimalFormat df2 = new DecimalFormat("#.##");
        String dx2 = df2.format(_paidTotal);
        _paidTotal = Double.valueOf(dx2);



        if(_paidTotal==Double.valueOf(JSONGetData.getTotalDealAmount(TestJSONToSend,i))){
            ExReAccumReport.info("----------------------------------------------------------------------");
            ExReAccumReport.pass("Total Deal Paid Calculation -- pass"+" deal number: "+i);
            ExReAccumReport.info("----------------------------------------------------------------------");


        }else{
            ExReAccumReport.info("----------------------------------------------------------------------");
            ExReAccumReport.fail("Total Deal Paid Calculation-- fail roung total amount "+" deal number: "+i).assignCategory("fail");
            ExReAccumReport.info("_paidTotal: "+_paidTotal+ " Json TotalDealAmount: "+JSONGetData.getTotalDealAmount(TestJSONToSend,i));
            ExReAccumReport.info("----------------------------------------------------------------------");
        }






    }

    /**
     * this function will add all the pre deal accums value to the pre deal HaseMap
     * @param i
     */
    public void getPreDealPointsAccums(int i, Response userDataResponse) {
        //first loop run on the size of array, from the response of the user benefit status
        for (int index = 0; index < (ResponseHandling.getAllAccums(userDataResponse)).size(); index++) {
            s = UserHandling.getVoucher(ResponseHandling.getAllAccums(userDataResponse), "AccID", index);

            preDeal.put(s, Double.parseDouble(UserHandling.getVoucher(ResponseHandling.getAllAccums(userDataResponse), "BenefitValue",index)));
        }


    }//func end

    public void getPostDealVouchers(int i, Response userDataResponse) {
        //this function will add all the post deal vouchers value to the post deal HaseMap
        for (int index = 0; index < (ResponseHandling.getAllAccums(userDataResponse)).size(); index++) {
            s = UserHandling.getVoucher(ResponseHandling.getAllAccums(userDataResponse), "AccID", index);
            postDeal.put(s, Double.parseDouble(UserHandling.getVoucher(ResponseHandling.getAllAccums(userDataResponse), "BenefitValue", index)));
        }


    }//func end
    public void pointUseCalculation(int i) {

        // this loop run on the DealToUse array
        for (int t = 0; t < JSONGetData.getDealsToUse(TestJSONToSend, i).size(); t++) {


            double val = 0.0;
            // this if check if the AccumID is in sumDealToUsePoints and sum it if not she creates one
            if (sumDealToUsePoints.get(JSONGetData.getDealsToUseAccumId(TestJSONToSend, i, t)) != null) {
                val = Double.valueOf(JSONGetData.getDealsToUseAmount(TestJSONToSend, i, t)) /
                        Double.valueOf(JSONGetData.getDealsToUsePToSValue(TestJSONToSend, i, t));
                //System.out.println("val"+ val);
                sumVal = val + sumDealToUsePoints.get(JSONGetData.getDealsToUseAccumId(TestJSONToSend, i, t));

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






    /**
     * this functions get data from the "dealToUse" part in the TestJson(JSONParametersToSend.json)
     */


    /**this function return the PromoID for a given AccumID in the TestJSON
     *
     * @param key AccumID
     * @param i The test deal number from the TestJson
     * @return promoId - String
     */
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

    public void sumBurnd(int i){
        double sumVal = 0.0 ;
        double d = 0.0 ;
        JSONArray arr = JSONGetData.getDealsToUse(TestJSONToSend, i);
        for (int q = 0; q < arr.size(); q++) {
            JSONObject x = (JSONObject) arr.get(q);
            String key = x.get("AccumId").toString();
            if(!(x.get("Burned").equals("0")) && !(x.get("Burned").equals(""))) {
                d = Double.valueOf(x.get("Burned").toString());
                if (sumBurnd.get(key) != null) {
                    sumVal = sumBurnd.get(key) +  d ;
                    sumBurnd.replace(key, sumVal);
                } else {
                    sumBurnd.put(key,d);
                }
            }
        }
        // System.out.println(sumBurnd);
    }








}//end class