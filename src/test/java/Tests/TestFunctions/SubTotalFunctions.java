package Tests.TestFunctions;

import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import FunctionsClass.UpdateJSONFile;
import JSON.JSONGetData;
import JSON.ResponseHandling;
import Tests.BasePage;
import Utilities.LogFileHandling;
import utilities.MainFunction;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class SubTotalFunctions extends BasePage {
    private UpdateJSONFile updateJSONFile = new UpdateJSONFile();
    private JSONGetData jsonGetData = new JSONGetData();

    private ResponseHandling responseHandling = new ResponseHandling();
    private JSONGetData JSONGetData = new JSONGetData();
    private  String CBPromoId = null;
    private  String TBPromoId = null;
    private int  flag1 = 0 ;



    private okhttp3.Response makeSubTotal(int i) throws IOException {
        updateJSONFile.upDateBaseJSONFile(
                JSONGetData.getUser(TestJSONToSend, i),
                JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getAccoundID(TestJSONToSend, i),
                JSONGetData.getTranItems(TestJSONToSend, i),
                JSONGetData.getCardNumber(TestJSONToSend, i));

        return APIPost.postSubTotal_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.JSON_TO_SEND);
    }
    private okhttp3.Response makeTranCancel(int i) throws IOException {
        updateJSONFile.upDateTrenCancelJSONFile(JSONGetData.getAccoundID(TestJSONToSend, i),
                JSONGetData.getUser(TestJSONToSend, i),
                JSONGetData.getPassword(TestJSONToSend, i),
                responseHandling.getServiceTranNumber(subTotalResponse_String));
        return APIPost.postTrenCancel_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.TREN_CONCEL_JSON);
    }

    private boolean responseVSTestJson(int arrayIndex, String response) throws IOException {
        int SFFlag=0 ;

        // this loop will run on the respone "CashBackDiscounts"
        for (int i=0 ,flag1 = 0 ; i < responseHandling.getCaseBackDiscountsArrSize(response);i++,flag1 = 0){

            CBPromoId = responseHandling.getPromoId(response,"CashBackDiscounts",i);

            //this loop run on the "TestJSON" CashBackDiscounts arr and check if CBPromoId is in this list

            for (int j=0; j< JSONGetData.getArraySizeCashBackDiscounts
                    (JSONGetData.getCashBackDiscounts(TestJSONToSend, arrayIndex)) ;j++){

                String temp = JSONGetData.getPromoId(TestJSONToSend, arrayIndex ,j,"CashBackDiscounts");

                if(CBPromoId.equals(temp)){
                    // System.out.println("CashBackDiscounts:");
                    // System.out.println("response: "+CBPromoId+ ", test JSON: " + temp+", Index "+ j);
                    flag1 = 1;



                    if (!(responseHandling.getAmount(response,"CashBackDiscounts",i).equals(
                            JSONGetData.getAmount(TestJSONToSend,arrayIndex,j,"CashBackDiscounts")))){
                        ExReApiTestReport.fail("response amount: "+responseHandling.getAmount(response,"CashBackDiscounts",i)+" is NOT equals to " +
                                "Test JSON amount: "+JSONGetData.getAmount(TestJSONToSend,arrayIndex,j,"CashBackDiscounts"));
                        ExReApiTestReport.info("CashBackDiscounts: "+ arrayIndex);
                        ExReApiTestReport.info("Response promoID: "+ CBPromoId + "Test JSON promoID: " + temp ).assignCategory("responVSTestJson");
                        SFFlag = 1;

                    }


                    if (!(responseHandling.getDescription(response,"CashBackDiscounts",i).equals(
                            JSONGetData.getDescription(TestJSONToSend,arrayIndex,j,"CashBackDiscounts")))){
                        ExReApiTestReport.fail("response Description: "+responseHandling.getDescription(response,"CashBackDiscounts",i)+"is NOT equals to " +
                                "Test JSON Description: "+JSONGetData.getDescription(TestJSONToSend,arrayIndex,j,"CashBackDiscounts"));
                        SFFlag = 1;


                    }


                    if (!(responseHandling.getIsAuto(response,"CashBackDiscounts",i).equals(
                            JSONGetData.getIsAuto(TestJSONToSend,arrayIndex,j,"CashBackDiscounts")))){
                        ExReApiTestReport.fail("response Description: "+responseHandling.getIsAuto(response,"CashBackDiscounts",i)+"is NOT equals to " +
                                "Test JSON Description: "+JSONGetData.getIsAuto(TestJSONToSend,arrayIndex,j,"CashBackDiscounts"));
                        SFFlag = 1;
                    }



                    break;
                }

            }
            if( flag1 == 0 ){
                ExReApiTestReport.warning("PromoId: " + CBPromoId + " not found in the \"Test JSON \"").assignCategory("responVSTestJson");
                // ExReApiTestReport.info(response.getBody().asString());
                SFFlag = 1;


            }

        }
        // this loop will run on the respone "TotalDiscounts"
        for (int i=0 , flag1 = 0 ; i < responseHandling.getTotalDiscountsArrSize(response);i++, flag1 = 0){

            TBPromoId = responseHandling.getPromoId(response,"TotalDiscounts",i);

            //this loop run on the "TestJSON" TotalDiscounts arr and check if CBPromoId is in this list

            for (int j=0; j < JSONGetData.getArraySizeTotalDiscounts(JSONGetData.getTotalDiscounts(TestJSONToSend, arrayIndex));j++,flag1 = 0){

                String temp = JSONGetData.getPromoId(TestJSONToSend, arrayIndex ,j,"TotalDiscounts");

                if(TBPromoId.equals(temp)){
                    flag1 = 1 ;



                    if (!(responseHandling.getAmount(response,"TotalDiscounts",i).equals(
                            JSONGetData.getAmount(TestJSONToSend,arrayIndex,j,"TotalDiscounts")))){
                        ExReApiTestReport.fail("response amount: "+responseHandling.getAmount(response,"TotalDiscounts",i)+" is NOT equals to " +
                                "Test JSON amount: "+JSONGetData.getAmount(TestJSONToSend,arrayIndex,j,"TotalDiscounts"));
                        ExReApiTestReport.info("TotalDiscounts:"+arrayIndex);
                        ExReApiTestReport.info("Response promoID: "+ TBPromoId + "Test JSON promoID: " + temp ).assignCategory("responVSTestJson");
                        SFFlag = 1;
                    }


                    if (!(responseHandling.getDescription(response,"TotalDiscounts",i).equals(
                            JSONGetData.getDescription(TestJSONToSend,arrayIndex,j,"TotalDiscounts")))){
                        ExReApiTestReport.fail("response Description: "+responseHandling.getDescription(response,"TotalDiscounts",i)+"is NOT equals to " +
                                "Test JSON Description: "+JSONGetData.getDescription(TestJSONToSend,arrayIndex,j,"TotalDiscounts"));
                        SFFlag = 1;

                    }



                    if (!(responseHandling.getAllItemsDiscountPercent(response,i).equals(
                            JSONGetData.getAllItemsDiscountPercent(TestJSONToSend,arrayIndex,j,"TotalDiscounts")))){
                        ExReApiTestReport.fail("response ItemsDiscountPercent: "+responseHandling.getAllItemsDiscountPercent(response,i)+"is NOT equals to " +
                                "Test JSON ItemsDiscountPercent: "+JSONGetData.getAllItemsDiscountPercent(TestJSONToSend,arrayIndex,j,"TotalDiscounts"));
                        SFFlag=1;
                    }


                    if (!(responseHandling.getIsAuto(response,"TotalDiscounts",i).equals(
                            JSONGetData.getIsAuto(TestJSONToSend,arrayIndex,j,"TotalDiscounts")))) {
                        ExReApiTestReport.fail("response IsAuto: " + responseHandling.getIsAuto(response, "TotalDiscounts", i) + "is NOT equals to " +
                                "Test JSON IsAuto: " + JSONGetData.getIsAuto(TestJSONToSend, arrayIndex, j, "TotalDiscounts"));
                        SFFlag = 1;

                    }

                    break;


                }

            }
            if( flag1 == 0 ){
                ExReApiTestReport.warning("12PromoId: " + TBPromoId + " not found in the \"Test JSON \"").assignCategory("responVSTestJson");
                System.out.println(MainFunction.BaseLogStringFunc()+baseJSON.jsonToSend.toString());
                // ExReApiTestReport.info(response.getBody().asString());
                SFFlag = 1;
            }

        }



        if(SFFlag == 1) {
            return false;
        } else {
            return true;
        }
    }

    private boolean TestJSONVSResponse (int arrayIndex,String  response) throws IOException {

        // this loop will run on the Test JSON "CashBackDiscounts"
        for (int i=0 ,flag1 = 0; i<JSONGetData.getArraySizeCashBackDiscounts(
                JSONGetData.getCashBackDiscounts(TestJSONToSend, arrayIndex)); i++,flag1 = 0){

            CBPromoId =  JSONGetData.getPromoId(TestJSONToSend, arrayIndex ,i,"CashBackDiscounts");

            //this loop run on the "response" CashBackDiscounts arr and check if CBPromoId is in this list

            for (int j=0; j< responseHandling.getCaseBackDiscountsArrSize(response) ;j++){
                String temp = responseHandling.getPromoId(response,"CashBackDiscounts",j);
                if (temp.equals(CBPromoId)){
                    flag1 = 1 ;

                    break;

                }
            }
            if( flag1 == 0 ){

                ExReApiTestReport.warning("PromoId: " + CBPromoId + " not found in the response ").assignCategory("TestJSONVSResponse");
                // ExReApiTestReport.info(response.getBody().asString());


            }
        }


        for (int i=0,flag1 = 0 ; i < JSONGetData.getArraySizeTotalDiscounts(JSONGetData.getTotalDiscounts(TestJSONToSend, arrayIndex));i++,flag1 = 0){


            TBPromoId = JSONGetData.getPromoId(TestJSONToSend, arrayIndex ,i,"TotalDiscounts");

            //this loop run on the "TestJSON" TotalDiscounts arr and check if CBPromoId is in this list

            for (int j=0; j<responseHandling.getTotalDiscountsArrSize(response) ; j++ ){

                String temp = responseHandling.getPromoId(response,"TotalDiscounts",j);

                if(TBPromoId.equals(temp)){
                    flag1 = 1 ;
                    break;

                }
            }

            if( flag1 == 0 ){

                ExReApiTestReport.warning("PromoId: " + TBPromoId + " not found in the response ").assignCategory("TestJSONVSResponse");
                // ExReApiTestReport.info(response.getBody().asString());
                MainFunction.onTestFailure("subTotalTest");
                return false;


            }
        }

        return true;
    }

    public Boolean SubTotalCheck() throws IOException {
        int SFFlag = 0;

        // this test check sub total
        //Sending a transaction, checking that the correct deals have appeared and then closing the transaction without using points

        //this loop run on the the tests in the "test JSON to send"
        //i <= JSONGetData.getArraySize(TestJSONToSend) - 1
        for (int i = 0; i <= JSONGetData.getArraySize(TestJSONToSend) - 1; i++) {
            ExReApiTestReport.info("~~~~~~~~~~~~~~~~~~~~~~~~ Transaction " + (i + 1) + " ~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println(MainFunction.BaseLogStringFunc() + "~~~~~~~~~~~~~~~~~~~~~~~~ Deal " + (i + 1) + " ~~~~~~~~~~~~~~~~~~~~~~~~");

            try {
                subTotalResponse = makeSubTotal(i);
                subTotalResponse_String = MainFunction.convertOkHttpResponseToString(subTotalResponse);

            } catch (SocketTimeoutException e) {
                ExReGeneralTests.warning("ERROR(subTotalResponse)---- Socket Timeout Exception  ");
                System.out.println(MainFunction.BaseLogStringFunc() + "ERROR(subTotalResponse)---- Socket Timeout Exception  ");
                throw new SocketTimeoutException();

            } catch (NullPointerException e) {
                ExReGeneralTests.warning("ERROR(subTotalResponse)---- Null lPointer Exceptionn  ");
                System.out.println(MainFunction.BaseLogStringFunc() + "ERROR(subTotalResponse)---- Null Pointer Exception  ");
                throw new NullPointerException();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(MainFunction.BaseLogStringFunc() + subTotalResponse_String);

            if (!(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0"))) {
                System.out.println(MainFunction.BaseLogStringFunc() + "*ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                ExReApiTestReport.fail("ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                        responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "subTotalCall", i + 1);
                LogFileHandling.createLogFile(subTotalResponse.toString(), LOG_FILE_DIRECTORY, "subTotalResponse", i + 1);
                subTotalResponse.body().close();
                MainFunction.onTestFailure("pointsValidityTest");
                return false;
            }


            if (responseVSTestJson(i, subTotalResponse_String)) {
                if (!TestJSONVSResponse(i, subTotalResponse_String)) {
                    SFFlag = 1;


                }

            }
            ExReApiTestReport.info("subTotalResponse Time : " + BaseAPI.getResponseTime_OkHttp(subTotalResponse) + "ms");
            avgTimeSubTotal.add(BaseAPI.getResponseTime_OkHttp(subTotalResponse));



            try {
                trenCancelResponse = makeTranCancel(i);
                trenCancelResponse_string = MainFunction.convertOkHttpResponseToString(trenCancelResponse);
            } catch (SocketTimeoutException e) {
                ExReGeneralTests.warning("ERROR(trenCancelResponse)---- Socket Timeout Exception  ");
                System.out.println(MainFunction.BaseLogStringFunc() + "ERROR(trenCancelResponse)---- Socket Timeout Exception  ");
                throw new SocketTimeoutException();

            } catch (NullPointerException e) {
                ExReGeneralTests.warning("ERROR(trenCancelResponse)---- Null lPointer Exceptionn  ");
                System.out.println(MainFunction.BaseLogStringFunc() + "ERROR(trenCancelResponse)---- Null Pointer Exception  ");
                throw new NullPointerException();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (trenCancelResponse.code() != 200 && !(responseHandling.getErrorCodeStatusJson(trenCancelResponse_string).equals("0"))) {
                System.out.println(MainFunction.BaseLogStringFunc() + "ERROR --- the Transaction " + responseHandling.getServiceTranNumber(trenCancelResponse_string) + " did not cancel");
                ExReApiTestReport.warning("ERROR --- the Transaction " + responseHandling.getServiceTranNumber(trenCancelResponse_string) + " did not cancel").assignCategory("warning");
                SFFlag = 1;
            } else {
                ExReApiTestReport.info("trenCancelResponse Time : " + BaseAPI.getResponseTime_OkHttp(trenCancelResponse) + "ms");
                BasePage.avgTimeTranRefund.add(BaseAPI.getResponseTime_OkHttp(trenCancelResponse));
            }

            subTotalResponse.body().close();
            trenCancelResponse.body().close();

        }
        if (SFFlag == 1) {
            return false;
        }
        return true;
    }

}// end class
