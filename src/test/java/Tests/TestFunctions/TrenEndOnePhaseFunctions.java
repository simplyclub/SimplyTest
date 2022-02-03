package Tests.TestFunctions;

import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import FunctionsClass.UpdateJSONFile;
import JSON.JSONGetData;
import JSON.ResponseHandling;
import JSON.UserHandling;
import Tests.BasePage;
import org.w3c.dom.NodeList;
import utilities.MainFunctions;

import java.io.IOException;
import java.text.DecimalFormat;

public class TrenEndOnePhaseFunctions extends BasePage {
    UpdateJSONFile updateJSONFile = new UpdateJSONFile();
    JSONGetData jsonGetData = new JSONGetData();
    BaseJSON baseJSON = new BaseJSON();

    public  okhttp3.Response getUserData(int i) throws IOException {
        updateJSONFile.upDateUserJSONFile(
                JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getFieldId(TestJSONToSend, i), JSONGetData.getCardNumber(TestJSONToSend, i)
        );
        return APIPost.postUserGetData_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.MEMBER_JSON_TO_SEND);

    }

    public okhttp3.Response makeTrenEndOnePhase(int i) throws IOException {
        updateJSONFile.upDateTranEndOnePhase(jsonGetData.getAccoundID(TestJSONToSend,i),jsonGetData.getUser(TestJSONToSend,i),jsonGetData.getPassword(TestJSONToSend,i),
                jsonGetData.getCardNumber(TestJSONToSend,i),jsonGetData.getTranItems(TestJSONToSend,i));


       return APIPost.postTrenEndOnePhase_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.TREN_END_ONE_PHASE);

    }

    public void discountLoop ( int i, NodeList nodeList){
        for (int XRIndex = 0; XRIndex < (nodeList.getLength()); XRIndex++) {//this loop run on the response xml
            for (int TJIndex = 0; TJIndex < JSONGetData.getArraySizeAccumulates(TestJSONToSend,i); TJIndex++) { //this loop run on the testJSON Accumulates

                //this if check that the promoID from the xml response of "end of the deal" is equal to the promoID from my test JSON in the "Accumulates" part
                if (ResponseHandling.getXMLResponsePromoID(nodeList, XRIndex).equals(JSONGetData.getPromoId(TestJSONToSend, i, TJIndex, "Accumulates"))) {

                    //this if check if the amount of the points that received from the specific ofer (by promoID) is equal to the a mount in the test JSON
                    if (MainFunctions.converToDoubleAsString(ResponseHandling.getXMLResponseAmount(nodeList, XRIndex)).equals(
                            JSONGetData.getAmount(TestJSONToSend, i, TJIndex, "Accumulates"))) {


                        //this if check if the accumID exists in the "sum deal point " map list
                        if (sumDealPoints.get(JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates")) != null) {
                            //System.out.println(JSONGetData.getPromoId(TestJSONToSend, i, TJIndex, "Accumulates"));
                            String key = JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates");
                            //           sumDealPoint current amount
                            //  sunDealPoints = (sumDealPoints) + (Discount Amount from the next promoID)
                            Double val = sumDealPoints.get(key) + Double.valueOf(ResponseHandling.getXMLResponseAmount(nodeList, XRIndex));
                            sumDealPoints.replace(key, val);


                        } else {//if the accumID do not exists
                            //System.out.println(JSONGetData.getPromoId(TestJSONToSend, i, TJIndex, "Accumulates"));

                            sumDealPoints.put(String.valueOf(JSONGetData.getAccumID(TestJSONToSend, i, TJIndex, "Accumulates")),
                                    Double.valueOf(ResponseHandling.getXMLResponseAmount(nodeList, XRIndex)));


                        }

                    }//end 2nd if
                    break;

                }//end 1st  if
            }//end of TJIndex loop

        }

    }//func end

    /**
     * this function will chack that the points you get from the Deal are equal  to TestJSON
     * @param i
     */

    public boolean EarnedChecks(int i){
        for (String key : sumDealPoints.keySet()) {
            for (int q = 0; q < JSONGetData.getArraySizeSumAccum(TestJSONToSend, i); q++) {
                //System.out.println(q);
                System.out.println(MainFunctions.BaseLogStringFunc()+sumDealPoints);


                //Compares  the Accum from the "sumDealPoints " to the Accum in the Test JSON
                if (key.equals(JSONGetData.getSumAccumKey(TestJSONToSend, i, q))) {
                    String dx = null;
                    DecimalFormat df = new DecimalFormat("#.##");
                    double d = 0.0 ;


                    System.out.println(MainFunctions.BaseLogStringFunc()+postDeal.get(key));
                    System.out.println(MainFunctions.BaseLogStringFunc()+preDeal.get(key));
                    d = (postDeal.get(key) - preDeal.get(key) );
                    dx = df.format(d);
                    d = Double.valueOf(dx);


                    String sumDeal = sumDealPoints.get(key).toString();
                    dx = df.format(Double.valueOf(sumDeal));
                    sumDeal = Double.valueOf(dx).toString();

                    String SumAccumValue = JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q);

                    double m = Double.valueOf(JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                    dx = df.format(m);
                    m = Double.valueOf(dx);

                    if (sumDeal.equals(SumAccumValue) && d == m) {
                        ExReTrenEndOnePhaseReport.pass("EarnedChecks Accum("+ key +"),transaction "+(i+1) + " ---- as PASS");
                        //ExReTrenEndOnePhaseReport.pass(sumDeal + " equals to " + SumAccumValue);
                        //ExReTrenEndOnePhaseReport.pass(d + " equals to " + JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                        break;
                    } else {
                        ExReTrenEndOnePhaseReport.fail("*sumDealPoints: " + df.format(sumDealPoints.get(key)) + " NOT equals to "
                                + "Test Json sumAccum: " + JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                        ExReTrenEndOnePhaseReport.fail("(postDeal.get(" + key + ") - preDeal.get(" + key + ")): " + d + " NOT equals to "
                                + "Test Json sumAccum: " + JSONGetData.getSumAccumValue(TestJSONToSend, key, i, q));
                        return false;
                    }
                    //break;
                }
            }
            //break;
        }
        return true;
    }

    /**
     * this function will add all the pre deal accums value to the pre deal HaseMap
     * @param i
     */
    public void getPreDealPointsAccums ( int i, String  userDataResponse) throws IOException {
        //first loop run on the size of array, from the response of the user benefit status
        for (int index = 0; index < (ResponseHandling.getAllAccums(userDataResponse)).size(); index++) {

            s = UserHandling.getVoucher(ResponseHandling.getAllAccums(userDataResponse), "AccID", index);
            preDeal.put(s, Double.parseDouble(UserHandling.getVoucher(ResponseHandling.getAllAccums(userDataResponse), "BenefitValue", index)));
        }

    }//func end

    public void getPostDealPointsAccums ( int i, String  userDataResponse) throws IOException {
        //first loop run on the size of array, from the response of the user benefit status
        for (int index = 0; index < (ResponseHandling.getAllAccums(userDataResponse)).size(); index++) {

            s = UserHandling.getVoucher(ResponseHandling.getAllAccums(userDataResponse), "AccID", index);
            postDeal.put(s, Double.parseDouble(UserHandling.getVoucher(ResponseHandling.getAllAccums(userDataResponse), "BenefitValue", index)));
        }
    }//func end

    public void getPostDealVouchers ( int i, String userDataResponse) throws IOException {
        //this function will add all the post deal vouchers value to the post deal HaseMap
        for (int index = 0; index < (ResponseHandling.getAllAccums(userDataResponse)).size(); index++) {
            s = UserHandling.getVoucher(ResponseHandling.getAllAccums(userDataResponse), "AccID", index);
            postDeal.put(s, Double.parseDouble(UserHandling.getVoucher(ResponseHandling.getAllAccums(userDataResponse), "BenefitValue", index)));
        }


    }//func end


}//end of class
