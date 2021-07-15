package Tests.TestFunctions;

import JSON.JSONGetData;
import Tests.BasePage;

import java.text.DecimalFormat;

public class TranEndFunctions extends BasePage {
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
                    System.out.println(sumDealToUsePoints.get(key));
                    System.out.println(postDeal.get(key) - preDeal.get(key));
                    System.out.println(sumBurnd.get(key));
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







}//end class
