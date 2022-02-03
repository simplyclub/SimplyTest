package Tests.TestFunctions;

import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import BaseClass.BaseXML;
import FunctionsClass.UpdateJSONFile;
import JSON.JSONGetData;
import JSON.ResponseHandling;
import Tests.BasePage;
import Utilities.LogFileHandling;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utilities.MainFunctions;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static BaseClass.BaseAPI.TEST_API_SYSTEM_URI;
import static utilities.MainFunctions.BaseLogStringFunc;

public class Stage6And7TestFunctions extends BasePage {
    UpdateJSONFile updateJSONFile = new UpdateJSONFile();
    JSON.JSONGetData jsonGetData = new JSONGetData();
    BaseXML baseXML = new BaseXML();
    BaseJSON baseJSON = new BaseJSON();
    ResponseHandling responseHandling = new ResponseHandling();

    public okhttp3.Response makeTrenEndOnePhase(int i, int dealNumber) throws IOException {

        //small fix: Match the name of the test to the location of the item in the array(Stage6And7ParametersToSendJson.json)
        dealNumber--;
        //
        updateJSONFile.upDateTranEndOnePhase(jsonGetData.getAccoundID(TestJSONToSend, i), jsonGetData.getUser(TestJSONToSend, i), jsonGetData.getPassword(TestJSONToSend, i),
                jsonGetData.getCardNumber(TestJSONToSend, i), getStage6Items(dealNumber, baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS)));


        return APIPost.postTrenEndOnePhase_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.TREN_END_ONE_PHASE);

    }

    public okhttp3.Response makeSubTotal(int i,int itemNum) throws IOException {
        updateJSONFile.upDateBaseJSONFile(jsonGetData.getUser(TestJSONToSend, i), jsonGetData.getPassword(TestJSONToSend, i),
                jsonGetData.getAccoundID(TestJSONToSend, i), getStage7Item(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS),itemNum), jsonGetData.getCardNumber(TestJSONToSend, i));
        return APIPost.postSubTotal_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.JSON_TO_SEND);
    }

    public okhttp3.Response makeTranEnd(int i, String subTotalResponse,int itemNum,JSONArray dealToUse) throws IOException {

        updateJSONFile.upDateTranEndJSON(
                responseHandling.getServiceTranNumber(subTotalResponse),
                jsonGetData.getAccoundID(TestJSONToSend,i),
                jsonGetData.getUser(TestJSONToSend,i),
                jsonGetData.getPassword(TestJSONToSend,i),
                jsonGetData.getCardNumber(TestJSONToSend,i),
                getStage7Item(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS),itemNum),
                dealToUse,
                baseJSON.jsonToSend);

        return APIPost.postTranEnd_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.JSON_TO_SEND);
    }

    public Response getTransactionView(String tranEndResponse) throws IOException {
        updateXMLFile.updateGetTransactionView(baseXML.xmlToDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
        updateXMLFile.updateGetTransactionView(baseXML.xmlToDocGetTransactionView(), "tranKey", responseHandling.getTrenEndTranReferenceNumber(tranEndResponse));

        return APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
    }

    private JSONArray getStage6Items(int dealNumber, Object obj) {
        JSONObject JSONObj = (JSONObject) obj;
        JSONArray stage = (JSONArray) JSONObj.get("stage_6");
        JSONObject deal = (JSONObject) stage.get(dealNumber);
        JSONArray x = (JSONArray) deal.get("deal_" + (dealNumber + 1));
        //System.out.println(x);
        return x;


    }

    private JSONArray getStage7Item(Object obj,int itemNum) {

        JSONObject JSONObj = (JSONObject) obj;
        JSONObject stage = (JSONObject) JSONObj.get("stage_7");

        //System.out.println(item);
        if (itemNum==0){
            JSONArray item = (JSONArray) stage.get("Item");
            return item;
        }
        if (itemNum == 1){
            JSONArray item = (JSONArray) stage.get("Item2");
            return item;

        }


        return null;

    }

    private JSONArray getStage7JsonPromoIdDealsArray(Object obj) {
        JSONObject JSONObj = (JSONObject) obj;
        JSONObject promoIdDeals = (JSONObject) JSONObj.get("stage_7");
        //System.out.println(promoIdDeals);
        JSONObject tranEndPromoId = (JSONObject) promoIdDeals.get("promoIdDeals");
        JSONArray arr = (JSONArray) tranEndPromoId.get("tranEndPromoId");
       // System.out.println(arr);



        return arr;

    }


    public JSONArray getDealToUseStage7(Object obj){
        JSONObject JSONObj = (JSONObject) obj;
        JSONObject stage = (JSONObject) JSONObj.get("stage_7");
        JSONArray dealToUse = (JSONArray) stage.get("DealToUse");

        return dealToUse;



    }



    private JSONArray getStage7SubTotalTotalDiscountsPromoIdArray(Object obj) {
        JSONObject JSONObj = (JSONObject) obj;
        JSONObject stage_7 = (JSONObject) JSONObj.get("stage_7");
        //System.out.println(stage_7);
        JSONObject promoIdDeals = (JSONObject) stage_7.get("promoIdDeals");
        JSONObject subTotalPromoId = (JSONObject) promoIdDeals.get("subTotalPromoId");
        JSONArray TotalDiscounts = (JSONArray) subTotalPromoId.get("TotalDiscounts");

        return TotalDiscounts;

    }

    private JSONArray getStage7SubTotalCashBackDiscountsPromoIdArray(Object obj) {
        JSONObject JSONObj = (JSONObject) obj;
        JSONObject stage_7 = (JSONObject) JSONObj.get("stage_7");
        //System.out.println(stage_7);
        JSONObject promoIdDeals = (JSONObject) stage_7.get("promoIdDeals");
        JSONObject subTotalPromoId = (JSONObject) promoIdDeals.get("subTotalPromoId");
        JSONArray CashBackDiscounts = (JSONArray) subTotalPromoId.get("CashBackDiscounts");

        return CashBackDiscounts;

    }

    public Boolean subTotalPromoIdCheck() throws IOException {
        int CBFlag = 0;
        int TDFlag = 0;


        try {
            subTotalResponse = makeSubTotal(0,0);
            subTotalResponse_String = MainFunctions.convertOkHttpResponseToString(subTotalResponse);

        } catch (SocketTimeoutException e) {
            ExReStage6And7Report.warning("ERROR(subTotalPromoIdCheck)---- Socket Timeout Exception  ");
            System.out.println(MainFunctions.BaseLogStringFunc() + "ERROR(subTotalPromoIdCheck)---- Socket Timeout Exception  ");
            return false;

        } catch (NullPointerException e) {
            ExReStage6And7Report.warning("ERROR(subTotalPromoIdCheck)---- Nul lPointer Exceptionn  ");
            System.out.println(MainFunctions.BaseLogStringFunc() + "ERROR(subTotalPromoIdCheck)---- Null Pointer Exception  ");
            return false;


        }

        if (!(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0"))) {
            System.out.println("ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
            ExReStage6And7Report.fail("ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "subTotalCall", 0 + 1);
            LogFileHandling.createLogFile(subTotalResponse_String, LOG_FILE_DIRECTORY, "subTotalResponse", 0 + 1);
            subTotalResponse.body().close();
            return false;
        }
//run on CashBackDiscounts
        for (int i = 0 ; i < getStage7SubTotalCashBackDiscountsPromoIdArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS)).size(); i++) {

            String JsonPromoId = getStage7SubTotalCashBackDiscountsPromoIdArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS))
                    .get(i)
                    .toString();

            for (int j = 0; j < responseHandling.getCaseBackDiscountsArrSize(subTotalResponse_String); j++) {

                String ResponseCBPromoId = responseHandling.getPromoId(subTotalResponse_String, "CashBackDiscounts", j);

                if (JsonPromoId.equals(ResponseCBPromoId)) {
                    System.out.println(MainFunctions.BaseLogStringFunc()+"JsonPromoId: " + JsonPromoId);
                    System.out.println(MainFunctions.BaseLogStringFunc()+"RespnseCBPromoId: " + ResponseCBPromoId);
                    CBFlag++;
                    break;
                }
            }

        }//end main for loop


//run on TotalTotalDiscounts
        for (int i = 0; i < getStage7SubTotalTotalDiscountsPromoIdArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS)).size(); i++) {
            String JsonPromoId = getStage7SubTotalTotalDiscountsPromoIdArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS))
                    .get(i)
                    .toString();

            for (int j = 0; j < responseHandling.getTotalDiscountsArrSize(subTotalResponse_String); j++) {

                String ResponseTDPromoId = responseHandling.getPromoId(subTotalResponse_String, "TotalDiscounts", j);

                if (JsonPromoId.equals(ResponseTDPromoId)) {
                    System.out.println(MainFunctions.BaseLogStringFunc()+"JsonPromoId: " + JsonPromoId);
                    System.out.println(MainFunctions.BaseLogStringFunc()+"RespnseTDPromoId: " + ResponseTDPromoId);
                    TDFlag++;
                    break;
                }


            }



        }
        if (CBFlag==getStage7SubTotalCashBackDiscountsPromoIdArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS)).size()&&
        TDFlag==getStage7SubTotalTotalDiscountsPromoIdArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS)).size()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * this function will do subTotal deal with  item 7 X 1 times
     * and check thet we don't gat the  main promo
     */
    public Boolean falseSubTotalCheck() throws IOException {
        try {
            subTotalResponse = makeSubTotal(0,1);
            subTotalResponse_String = MainFunctions.convertOkHttpResponseToString(subTotalResponse);

        } catch (SocketTimeoutException e) {
            ExReStage6And7Report.warning("ERROR(subTotalPromoIdCheck)---- Socket Timeout Exception  ");
            System.out.println(MainFunctions.BaseLogStringFunc() + "ERROR(subTotalPromoIdCheck)---- Socket Timeout Exception  ");
            return false;

        } catch (NullPointerException e) {
            ExReStage6And7Report.warning("ERROR(subTotalPromoIdCheck)---- Nul lPointer Exceptionn  ");
            System.out.println(MainFunctions.BaseLogStringFunc() + "ERROR(subTotalPromoIdCheck)---- Null Pointer Exception  ");
            return false;


        }

        if (!(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0"))) {
            System.out.println("ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
            ExReStage6And7Report.fail("ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "subTotalCall", 0 + 1);
            LogFileHandling.createLogFile(subTotalResponse_String, LOG_FILE_DIRECTORY, "subTotalResponse", 0 + 1);
            subTotalResponse.body().close();
            return false;
        }
        for (int i = 0 ; i < getStage7SubTotalCashBackDiscountsPromoIdArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS)).size(); i++) {

            String JsonPromoId = getStage7SubTotalCashBackDiscountsPromoIdArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS))
                    .get(i)
                    .toString();

            for (int j = 0; j < responseHandling.getCaseBackDiscountsArrSize(subTotalResponse_String); j++) {

                String ResponseCBPromoId = responseHandling.getPromoId(subTotalResponse_String, "CashBackDiscounts", j);

                if (JsonPromoId.equals(ResponseCBPromoId)) {

                    return  false;
                }
            }

        }//end main for loop


//run on TotalTotalDiscounts
        for (int i = 0; i < getStage7SubTotalTotalDiscountsPromoIdArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS)).size(); i++) {
            String JsonPromoId = getStage7SubTotalTotalDiscountsPromoIdArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS))
                    .get(i)
                    .toString();

            for (int j = 0; j < responseHandling.getTotalDiscountsArrSize(subTotalResponse_String); j++) {

                String ResponseTDPromoId = responseHandling.getPromoId(subTotalResponse_String, "TotalDiscounts", j);

                if (JsonPromoId.equals(ResponseTDPromoId)) {
                    return false;
                }
            }
        }
        return  true;
    }

    public boolean tranEndPromoIdCheck(String tranEndOnePhaseResponse_String ) throws IOException {


        try {
            transactionViewResponse = getTransactionView(tranEndOnePhaseResponse_String);
            System.out.println(BaseLogStringFunc() + transactionViewResponse.body().asString());

        }catch  (SocketTimeoutException e){
            ExReStage6And7Report.warning("ERROR(tranEndPromoIdCheck)---- Socket Timeout Exception  ");
            System.out.println(MainFunctions.BaseLogStringFunc()+"ERROR(tranEndPromoIdCheck)---- Socket Timeout Exception  ");
            return false;

        }catch (NullPointerException e){
            ExReStage6And7Report.warning("ERROR(transactionViewResponse)---- Nul lPointer Exceptionn  ");
            System.out.println(MainFunctions.BaseLogStringFunc()+"ERROR(transactionViewResponse)---- Null Pointer Exception  ");
            return false;


        }

        if (!(transactionViewResponse.getStatusCode() == 200)) {
            System.out.println("ERROR xml--- status code is not 200 ");
            ExReStage6And7Report.fail("ERROR xml--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
            LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                    LOG_FILE_DIRECTORY, "XmlTransactionViewcall",0);
            LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse",0);
            return false ;
        }

        nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());

        for (int JIndex=0 , flag = 0;JIndex < getStage7JsonPromoIdDealsArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS)).size();JIndex++){//run on Stage6And7ParametersToSendJson

            for (int XRIndex = 0; XRIndex < (nodeList.getLength()); XRIndex++){//run on XML File TranViewDiscount Data

                String promoId = getStage7JsonPromoIdDealsArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS))
                        .get(JIndex)
                        .toString();
                //System.out.println(promoId);

                if(ResponseHandling.getXMLResponsePromoID(nodeList, XRIndex).equals(promoId)){
                    flag ++;

                    if (flag == getStage7JsonPromoIdDealsArray(baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS)).size()){
                     return  true;
                    }

                }

            }//End JIndex for loop


        }//End XRIndex for loop


        return false;
    }



}
