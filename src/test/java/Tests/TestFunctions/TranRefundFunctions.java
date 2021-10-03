package Tests.TestFunctions;

import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import FunctionsClass.UpdateJSONFile;
import JSON.JSONGetData;
import JSON.ResponseHandling;
import JSON.UserHandling;
import Tests.BasePage;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import utilities.MainFunction;

import java.io.IOException;

public class TranRefundFunctions extends BasePage {
    UpdateJSONFile updateJSONFile = new UpdateJSONFile();
    ResponseHandling responseHandling = new ResponseHandling();
    UserHandling userHandling = new UserHandling();

    public okhttp3.Response makeDealSubTotal(int i) throws IOException {
        updateJSONFile.upDateBaseJSONFile(JSON.JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getAccoundID(TestJSONToSend, i), JSONGetData.getTranItems(TestJSONToSend, i), JSONGetData.getCardNumber(TestJSONToSend, i));
        return APIPost.postSubTotal_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

    }//func end

    /**
     *
     * @param i
     * @param subTotalResponse subTotalResponse
     * @return TrendEnd Response
     */

    public okhttp3.Response makeDealTrenEnd(int i, String subTotalResponse) throws IOException {
        updateJSONFile.upDateTranEndJSON(ResponseHandling.getServiceTranNumber(subTotalResponse), JSONGetData.getAccoundID(TestJSONToSend, i), JSONGetData.getUser(TestJSONToSend, i),
                JSONGetData.getPassword(TestJSONToSend, i), JSONGetData.getCardNumber(TestJSONToSend, i),
                JSONGetData.getTranItems(TestJSONToSend, i), JSONGetData.getDealsToUse(TestJSONToSend, i), baseJSON.jsonToSend);
        return APIPost.postTranEnd_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.JSON_TO_SEND);
    }//func end

    /**
     *
     * @param i
     * @param TrenEndResponse TrenEndResponse
     * @return TranRefund response
     */

public okhttp3.Response makeTranRefund (int i,String TrenEndResponse ) throws IOException {
        updateJSONFile.upDateTrenRefundJSONFile(JSONGetData.getAccoundID(TestJSONToSend, i), JSONGetData.getUser(TestJSONToSend, i),
                JSONGetData.getPassword(TestJSONToSend, i),responseHandling.getTrenEndTranReferenceNumber(TrenEndResponse));

        return APIPost.postTrenRefund_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.TREN_REFUND_JSON);
}


    private   String getUserData(int i) throws IOException {
        updateJSONFile.upDateUserJSONFile(
                JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getFieldId(TestJSONToSend, i), JSONGetData.getCardNumber(TestJSONToSend, i)
        );
        return (APIPost.postUserGetData(BaseAPI.TEST_REST_API_URI, BaseJSON.USER_JSON_TO_SEND)).getBody().asString();


    }//func end


    public void fillPreAllAccumsPoints (int i) throws IOException {
        String  userData = getUserData(i);
        JSONArray arr = responseHandling.getAllAccums(userData);

        //System.out.println(arr);
        preAllAccumsPointsfill(arr);

    }

    private void preAllAccumsPointsfill(JSONArray arr){

        for(int l=0; l < arr.size();l++){

            preAllAccumsPoints.put(userHandling.getVoucher(arr ,"AccID", l),
                    Double.valueOf(userHandling.getVoucher(arr,"BenefitValue",l)));

            }
    }

    public void fillPostAllAccumsPoints(int i) throws IOException {
        String  userData = getUserData(i);
        JSONArray arr = responseHandling.getAllAccums(userData);
        postAllAccumsPointsfill(arr);

    }
    private void postAllAccumsPointsfill(JSONArray arr){

        for(int l=0; l < arr.size();l++){

            postAllAccumsPoints.put(userHandling.getVoucher(arr ,"AccID", l),
                    Double.valueOf(userHandling.getVoucher(arr,"BenefitValue",l)));

        }
    }
    public void  prePostCompere(){

        if(postAllAccumsPoints.size() == preAllAccumsPoints.size()){

            if (preAllAccumsPoints.equals(postAllAccumsPoints)){
                System.out.println("preAllAccumsPoints is equal to postAllAccumsPoints ");
                ExReTernRefundReport.pass("preAllAccumsPoints is equal to postAllAccumsPoints");
            }else{
                System.out.println("preAllAccumsPoints is NOT equal to postAllAccumsPoints ");
                ExReTernRefundReport.fail("preAllAccumsPoints is NOT equal to postAllAccumsPoints");
                ExReTernRefundReport.info("preAllAccumsPoints: "+ preAllAccumsPoints).info("postAllAccumsPoints: "+ postAllAccumsPoints);

            }


        }else {
            System.out.println("preAllAccumsPoints size is not equal to postAllAccumsPoints size");
            ExReTernRefundReport.fail("preAllAccumsPoints size is not equal to postAllAccumsPoints size");
            ExReTernRefundReport.info("postAllAccumsPoints size: "+ postAllAccumsPoints.size()).info("preAllAccumsPoints size: "+ preAllAccumsPoints.size());

        }

    }











}//end of class
