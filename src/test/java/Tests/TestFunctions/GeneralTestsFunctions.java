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
import utilities.MainFunctions;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static BaseClass.BaseAPI.TEST_API_SYSTEM_URI;

public class GeneralTestsFunctions extends BasePage {
    UpdateJSONFile updateJSONFile = new UpdateJSONFile();
    JSON.JSONGetData jsonGetData = new JSONGetData();
    BaseXML baseXML = new BaseXML();
    BaseJSON baseJSON = new BaseJSON();
    ResponseHandling responseHandling = new ResponseHandling();

    public okhttp3.Response makeSubTotal(int i,String user,String pass) throws IOException {
        updateJSONFile.upDateBaseJSONFile(user,pass,
                jsonGetData.getAccoundID(TestJSONToSend,i),
                jsonGetData.getTranItems(TestJSONToSend,i),
                jsonGetData.getCardNumber(TestJSONToSend,i));

        return APIPost.postSubTotal_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.JSON_TO_SEND);
    }

    public okhttp3.Response makeTranEnd(int i, String subTotalResponse, String user,String pass) throws IOException {

        updateJSONFile.upDateTranEndJSON(
                responseHandling.getServiceTranNumber(subTotalResponse),
                jsonGetData.getAccoundID(TestJSONToSend,i),
                user,
                pass,
                jsonGetData.getCardNumber(TestJSONToSend,i),
                jsonGetData.getTranItems(TestJSONToSend,i),
                jsonGetData.getDealsToUse(TestJSONToSend,i),
                baseJSON.jsonToSend);

        return APIPost.postTranEnd_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.JSON_TO_SEND);
    }

    public Boolean subTotalCheck() throws IOException {
        int flag = 0;
        try{
            subTotalResponse= makeSubTotal(0,"pos2","2pos");
            subTotalResponse_String = MainFunctions.convertOkHttpResponseToString(subTotalResponse);
        }catch (SocketTimeoutException e){
            ExReGeneralTests.warning("ERROR(subTotalResponse)---- Socket Timeout Exception  ");
            System.out.println(MainFunctions.BaseLogStringFunc() + "ERROR(subTotalResponse)---- Socket Timeout Exception  ");
            throw new SocketTimeoutException();

        }catch ( NullPointerException e) {
            ExReGeneralTests.warning("ERROR(subTotalResponse)---- Null lPointer Exceptionn  ");
            System.out.println(MainFunctions.BaseLogStringFunc() + "ERROR(subTotalResponse)---- Null Pointer Exception  ");
            throw new NullPointerException();

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
        System.out.println(MainFunctions.BaseLogStringFunc()+subTotalResponse_String);


        for (int q = 0;q < responseHandling.getCaseBackDiscountsArrSize(subTotalResponse_String) ;q++){

            System.out.println(responseHandling.getCaseBackDiscountsArrSize(subTotalResponse_String));

            //check if the array CaseBackDiscounts  size is bigger than 2 if it bigger -->  fail
            if (responseHandling.getCaseBackDiscountsArrSize(subTotalResponse_String) != 2){

            ExReGeneralTests.fail("subTotalCheck ---- fail");
            ExReGeneralTests.info("subTotalCheck ---- CaseBackDiscounts array size is not 2 ");

            }else {
                String[] pos2PromoId = {"1839","1935"};

                for(String str : pos2PromoId){
                    System.out.println("str: "+str);

                    for(int t =0 ; t< responseHandling.getCaseBackDiscountsArrSize(subTotalResponse_String);t++ ) {

                        String promoId = responseHandling.getPromoId(subTotalResponse_String, "CashBackDiscounts", t);
                        System.out.println("promoId: "+ promoId);
                        if(str.equals(promoId)){
                            flag ++;

                        }

                    }

                }

                if(flag == 2){
                    return true;
                }else {
                    return false;
                }
            }
        }//end for loop
        System.out.println(MainFunctions.BaseLogStringFunc()+"CaseBackDiscountsArrSize is 0");
        ExReGeneralTests.warning("CaseBackDiscountsArrSize from the response is 0");
        return false;
    }

    public Boolean tranEndCheck() throws IOException {
        String[] promoId = {"1820","1826","1945"};
        int flag =0;
        try {
            trenEndResponse = makeTranEnd(0,subTotalResponse_String,"pos2", "2pos");
            trenEndResponse_String = MainFunctions.convertOkHttpResponseToString(trenEndResponse);

        }catch (SocketTimeoutException e){
            ExReGeneralTests.warning("ERROR(trenEndResponse)---- Socket Timeout Exception  ");
            System.out.println(MainFunctions.BaseLogStringFunc() + "ERROR(trenEndResponse)---- Socket Timeout Exception  ");
            throw new SocketTimeoutException();

        }catch ( NullPointerException e) {
            ExReGeneralTests.warning("ERROR(trenEndResponse)---- Null lPointer Exceptionn  ");
            System.out.println(MainFunctions.BaseLogStringFunc() + "ERROR(trenEndResponse)---- Null Pointer Exception  ");
            throw new NullPointerException();

        }

        try {
            transactionViewResponse = getTransactionView(trenEndResponse_String);

        }catch (SocketTimeoutException e){
            ExReStage6And7Report.warning("ERROR(transactionViewResponse)---- Socket Timeout Exception  ");
            System.out.println(MainFunctions.BaseLogStringFunc() + "ERROR(transactionViewResponse)---- Socket Timeout Exception  ");
            throw new SocketTimeoutException();

        }catch ( NullPointerException e) {
            ExReStage6And7Report.warning("ERROR(transactionViewResponse)---- Null lPointer Exceptionn  ");
            System.out.println(MainFunctions.BaseLogStringFunc() + "ERROR(transactionViewResponse)---- Null Pointer Exception  ");
            throw new NullPointerException();

        }

        nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());

        if(nodeList.getLength() !=3 ){
            ExReGeneralTests.fail("tranEndCheck ---- fail");
            ExReGeneralTests.info("tranEndCheck ----  array size is not 3 ");
        }else {
            for(String str : promoId){
                System.out.println(str);

                for(int q=0;q< nodeList.getLength();q++){
                    System.out.println(responseHandling.getXMLResponsePromoID(nodeList,q));

                    if(str.equals(responseHandling.getXMLResponsePromoID(nodeList,q))){
                        System.out.println(1);
                        flag ++;

                        break;
                    }

                }

            }
            if(flag == 3){
                return true;
            }

        }
        return false;
    }
    private Response getTransactionView(String tranEndResponse) throws IOException {
        updateXMLFile.updateGetTransactionView(baseXML.xmlToDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
        updateXMLFile.updateGetTransactionView(baseXML.xmlToDocGetTransactionView(), "tranKey", responseHandling.getTrenEndTranReferenceNumber(tranEndResponse));

        return APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
    }









}
