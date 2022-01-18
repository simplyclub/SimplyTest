package JSON;
import Tests.BasePage;



import io.restassured.response.Response;
import utilities.MainFunction;

import java.io.IOException;

public class JSONCompare extends BasePage {
    //SoftAssert softAssertion = new SoftAssert();
    ResponseHandling responseHandling = new ResponseHandling();
    JSONGetData JSONGetData = new JSONGetData();
    String CBPromoId = null;
    String TBPromoId = null;
    int  flag1 = 0 ;

    public boolean responseVSTestJson(int arrayIndex, String response) throws IOException {
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



    public void TestJSONVSResponse (int arrayIndex,String  response) throws IOException {

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


            }
        }


    }

}




