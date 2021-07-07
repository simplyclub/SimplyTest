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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utilities.MainFunction;
import javax.xml.transform.TransformerException;
import java.text.DecimalFormat;

import static BaseClass.BaseAPI.TEST_API_SYSTEM_URI;
import static java.lang.Math.abs;

public class TranEndTest extends BasePage {

    @Test(testName = "TrenEnd Test ", priority = 1)
    @Description("Will run on each of the accumulators  ")
    public void tranEndTest() throws TransformerException {
        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++){
            MainFunction.RestGlobals();

            // resat the base json  that i send in the infrastructure
            baseJSON.BaseJSONCopy();

            //check for points before the deal

            userDataResponse = getUserData(i);


            //check that the AccID from the response equal to the AccID from my  test JSON
            getPreDealPointsAccums(i);

            //make a deal subTotal+trenEnd
            subTotalResponse = makeDealSubTotal(i);
            if(!(subTotalResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse).equals("0"))) {
                System.out.println("ERROR --- status code is not 200 or ErrorCodeStatus is not 0 ");
                ExReApiTestReport.fail("ERROR --- status code is not 200"+"("+subTotalResponse.getStatusCode()+")" +" or ErrorCodeStatus is not 0 "+"("+
                        responseHandling.getErrorCodeStatusJson(subTotalResponse)+")");
                break;
            }


            //this if check if we are using  points or vouchers
            if(JSONGetData.getDealsToUse(TestJSONToSend,i).size()== 0){
                //****deal without Using Points*****

                trenEndResponse = makeDealTrenEnd(i);
                if(!(trenEndResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse).equals("0"))) {
                    System.out.println("ERROR --- status code is not 200 or ErrorCodeStatus is not 0 ");
                    ExReApiTestReport.fail("ERROR --- status code is not 200"+"("+subTotalResponse.getStatusCode()+")" +" or ErrorCodeStatus is not 0 "+"("+
                            responseHandling.getErrorCodeStatusJson(subTotalResponse)+")");
                    break;
                }

                //check for points after the deal
                userDataResponse = getUserData(i);
                if(!(userDataResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse).equals("0"))) {
                    System.out.println("ERROR --- status code is not 200 or ErrorCodeStatus is not 0 ");
                    ExReApiTestReport.fail("ERROR --- status code is not 200"+"("+subTotalResponse.getStatusCode()+")" +" or ErrorCodeStatus is not 0 "+"("+
                            responseHandling.getErrorCodeStatusJson(subTotalResponse)+")");
                    break;
                }
                getPostDealVouchers(i);


                //get Transaction View, with the data of all Discount Data in the end of the deal
                updateXMLFile.updateGetTransactionView(BaseXML.xmlDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
                updateXMLFile.updateGetTransactionView(BaseXML.xmlDocGetTransactionView(), "tranKey", responseHandling.getServiceTranNumber(subTotalResponse, i));

                transactionViewResponse = APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
                if(!(transactionViewResponse.getStatusCode()==200)){
                    System.out.println("ERROR xml--- status code is not 200 ");
                    ExReApiTestReport.fail("ERROR xml--- status code is not 200"+"("+subTotalResponse.getStatusCode()+")");
                    break;

                }
                System.out.println(transactionViewResponse.getBody().asString());
                // "nodeList" is for using in discountLoop
                nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());


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


                            String sumDeal =sumDealPoints.get(key).toString();
                            dx = df.format(Double.valueOf(sumDeal));
                            sumDeal = Double.valueOf(dx).toString();

                            String SumAccumValue=JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q);

                            double m = Double.valueOf(JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                            dx = df.format(m);
                            m = Double.valueOf(dx);

                            if (sumDeal.equals(SumAccumValue) && d == m ) {

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
                totalDealPaidCalculation(responseHandling.getXMLFilePaidTotal(transactionViewResponse.getBody().asString()),i);


            }else{
                //*******deal Using Points*******
                trenEndResponse = makeDealWithUsingPointsTrenEnd(i);
                if(!(trenEndResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse).equals("0"))) {
                    System.out.println("ERROR --- status code is not 200 or ErrorCodeStatus is not 0 ");
                    ExReApiTestReport.fail("ERROR --- status code is not 200"+"("+subTotalResponse.getStatusCode()+")" +" or ErrorCodeStatus is not 0 "+"("+
                            responseHandling.getErrorCodeStatusJson(subTotalResponse)+")");
                    break;
                }
                System.out.println(trenEndResponse.getBody().asString());

                //check for points after the deal
                userDataResponse = getUserData(i);
                if(!(userDataResponse.getStatusCode() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse).equals("0"))) {
                    System.out.println("ERROR --- status code is not 200 or ErrorCodeStatus is not 0 ");
                    ExReApiTestReport.fail("ERROR --- status code is not 200"+"("+subTotalResponse.getStatusCode()+")" +" or ErrorCodeStatus is not 0 "+"("+
                            responseHandling.getErrorCodeStatusJson(subTotalResponse)+")");
                    break;
                }
                getPostDealVouchers(i);

                //get Transaction View, with the data of all Discount Data in the end of the deal
                updateXMLFile.updateGetTransactionView(BaseXML.xmlDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
                updateXMLFile.updateGetTransactionView(BaseXML.xmlDocGetTransactionView(), "tranKey", responseHandling.getServiceTranNumber(subTotalResponse, i));

                transactionViewResponse = APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
                if(!(transactionViewResponse.getStatusCode()==200)){
                    System.out.println("ERROR xml--- status code is not 200 ");
                    ExReApiTestReport.fail("ERROR xml--- status code is not 200"+"("+subTotalResponse.getStatusCode()+")");
                    break;

                }
                System.out.println(transactionViewResponse.getBody().asString());

                // "nodeList" is for using in discountLoop
                nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());
                System.out.println(nodeList);


                //this for loop run on all the Discounts  and sum theme
                discountLoop(i);
                pointUseCalculation(i);
                sumDealToUsePointsCheck(i);
                DiscountConfirmTest(nodeList,i);
                totalDealPaidCalculation(responseHandling.getXMLFilePaidTotal(transactionViewResponse.getBody().asString()),i);



            }







        }//end main for loop

    }//end ot test




/*
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
            System.out.println(nodeList);


            //this for loop run on all the Discounts  and sum theme
            discountLoop(i);


            pointUseCalculation(i);
            sumDealToUsePointsCheck(i);


        }//main for loop end
    }
*/
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
                                            ExReAccumReport.info("additional information : test number("+i+")"+" xmlPromoID("+xmlPromoID+")"+" jsonDealToUsePromoID("+DealToUsePromoID+")")
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

    /**
     * The functions here are for fixed operations that occur only within the current test
     */

    private Response getUserData(int i) {
        updateJSONFile.upDateUserJSONFile(
                JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getFieldId(TestJSONToSend, i), JSONGetData.getFieldValue(TestJSONToSend, i)
        );
        return APIPost.postUserGetData(BaseAPI.TEST_REST_API_URI, baseJSON.USER_JSON_TO_SEND);

    }//func end

    private Response makeDealSubTotal(int i) {
        updateJSONFile.upDateBaseJSONFile(JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getAccoundID(TestJSONToSend, i), JSONGetData.getTranItems(TestJSONToSend, i));
        return APIPost.postSubTotal(BaseAPI.TEST_REST_API_URI, baseJSON.JSON_TO_SEND);

    }//func end
    private Response makeDealTrenEnd(int i) {
        updateJSONFile.upDateTranEndJSON(responseHandling.getServiceTranNumber(subTotalResponse, i), JSONGetData.getAccoundID(TestJSONToSend, i),
                JSONGetData.getTranItems(TestJSONToSend, i), null, baseJSON.jsonToSend);
        return APIPost.postTranEnd(BaseAPI.TEST_REST_API_URI, baseJSON.JSON_TO_SEND);

    }//func end
    private Response makeDealWithUsingPointsTrenEnd(int i) {
        updateJSONFile.upDateTranEndJSON(responseHandling.getServiceTranNumber(subTotalResponse, i), JSONGetData.getAccoundID(TestJSONToSend, i),
                JSONGetData.getTranItems(TestJSONToSend, i), JSONGetData.getDealsToUse(TestJSONToSend, i), baseJSON.jsonToSend);
        return APIPost.postTranEnd(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

    }//func end
    private void discountLoop(int i) {
        for (int XRIndex = 0; XRIndex < (nodeList.getLength()); XRIndex++) {//this loop run on the response xml
            for (int TJIndex = 0; TJIndex < JSONGetData.getArraySizeAccumulates(JSONGetData.getTotalAccumulates(TestJSONToSend, i)); TJIndex++) { //this loop run on the testJSON Accumulates

                //this if check that the promoID from the xml response of end of the deal is equal to the promoID from my test JSON in the "Accumulates" part
                if (responseHandling.getXMLResponsePromoID(nodeList, XRIndex).equals(JSONGetData.getPromoId(TestJSONToSend, i, TJIndex, "Accumulates"))) {

                    //this if check if the amount of the points that received from the specific ofer (by promoID) is equal to the a mount in the test JSON
                    if (responseHandling.getXMLResponseAmount(nodeList, XRIndex).equals(
                            JSONGetData.getAmount(TestJSONToSend, i, TJIndex, "Accumulates"))) {


                        //this if check if the accumID exists in the "sum deal point " map list
                        if (sumDealPoints.get(JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates")) != null) {

                            String key = JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates");
                            //           sumDealPoint current amount
                            //  sunDealPoints = (sumDealPoints) + (Discount Amount from the next promoID)
                            Double val = sumDealPoints.get(key) + Double.valueOf(responseHandling.getXMLResponseAmount(nodeList, XRIndex));
                            sumDealPoints.replace(key, val);


                        } else {//if the accumID do not exists
                            sumDealPoints.put(String.valueOf(JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates")),
                                    Double.valueOf(responseHandling.getXMLResponseAmount(nodeList, XRIndex)));


                        }

                    }//end 2nd if
                    break;

                }//end 1st  if
            }//end of TJIndex loop

        }

    }//func end
    private void totalDealPaidCalculation( String paidTotal, int i ){
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
    private void getPreDealPointsAccums(int i) {
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
                                double d = preDeal.get(key) - Double.valueOf(getBurned(key, i));
                                DecimalFormat df = new DecimalFormat("#.##");
                                String dx = df.format(d);
                                d = Double.valueOf(dx);


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

                            System.out.println(key);
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
 * this functions get data from the "dealToUse" part in the TestJson(JSONParametersToSend.json)
 */

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







}//class end

