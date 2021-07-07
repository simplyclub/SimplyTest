package JSON;
import Tests.BasePage;

import org.testng.asserts.SoftAssert;

import io.restassured.response.Response;

public class JSONCompare extends BasePage {
    SoftAssert softAssertion = new SoftAssert();
    ResponseHandling responseHandling = new ResponseHandling();
    JSONGetData JSONGetData = new JSONGetData();
    String CBPromoId = null;
    String TBPromoId = null;
    int  flag1 = 0 ;

    public void responVSTestJson (int arrayIndex,Response response){

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
                    ExReApiTestReport.info("CashBackDiscounts:"+i);
                    ExReApiTestReport.info("Response promoID: "+ CBPromoId + "Test JSON promoID: " + temp ).assignCategory("responVSTestJson");


                    softAssertion.assertEquals(responseHandling.getAmount(response,"CashBackDiscounts",i),
                            JSONGetData.getAmount(TestJSONToSend,arrayIndex,j,"CashBackDiscounts")
                    );
                    softAssertion.assertEquals(responseHandling.getDescription(response,"CashBackDiscounts",i),
                            JSONGetData.getDescription(TestJSONToSend,arrayIndex,j,"CashBackDiscounts")
                    );
                    softAssertion.assertEquals(responseHandling.getIsAuto(response,"CashBackDiscounts",i),
                            JSONGetData.getIsAuto(TestJSONToSend,arrayIndex,j,"CashBackDiscounts"));



                    softAssertion.assertAll();

                    break;
                }

            }
            if( flag1 == 0 ){
                ExReApiTestReport.warning("PromoId: " + CBPromoId + " not found in the \"Test JSON \"").assignCategory("responVSTestJson");
               // ExReApiTestReport.info(response.getBody().asString());


            }

        }
        // this loop will run on the respone "TotalDiscounts"
        for (int i=0 , flag1 = 0 ; i < responseHandling.getCaseBackDiscountsArrSize(response);i++, flag1 = 0){

            TBPromoId = responseHandling.getPromoId(response,"TotalDiscounts",i);

            //this loop run on the "TestJSON" TotalDiscounts arr and check if CBPromoId is in this list

            for (int j=0; j< JSONGetData.getArraySizeCashBackDiscounts
                    (JSONGetData.getCashBackDiscounts(TestJSONToSend, arrayIndex)) ;j++,flag1 = 0){

                String temp = JSONGetData.getPromoId(TestJSONToSend, arrayIndex ,j,"TotalDiscounts");

                if(TBPromoId.equals(temp)){
                    flag1 = 1 ;
                    //System.out.println("TotalDiscounts:");
                    //System.out.println("response: "+TBPromoId+ ", test JSON: " + temp+", Index "+ j);
                    ExReApiTestReport.info("TotalDiscounts:"+i);
                    ExReApiTestReport.info("Response promoID: "+ TBPromoId + "Test JSON promoID: " + temp ).assignCategory("responVSTestJson");
                    softAssertion.assertEquals(responseHandling.getAmount(response,"TotalDiscounts",i),
                            JSONGetData.getAmount(TestJSONToSend,arrayIndex,j,"TotalDiscounts")
                    );
                    softAssertion.assertEquals(responseHandling.getDescription(response,"TotalDiscounts",i),
                            JSONGetData.getDescription(TestJSONToSend,arrayIndex,j,"TotalDiscounts")
                    );
                    softAssertion.assertEquals(responseHandling.getAllItemsDiscountPercent(response,i),
                            JSONGetData.getAllItemsDiscountPercent(TestJSONToSend,arrayIndex,j,"TotalDiscounts")
                    );
                    softAssertion.assertEquals(responseHandling.getIsAuto(response,"TotalDiscounts",i),
                            JSONGetData.getIsAuto(TestJSONToSend,arrayIndex,j,"TotalDiscounts"));
                    softAssertion.assertAll();

                    break;


                }

            }
            if( flag1 == 0 ){
                ExReApiTestReport.warning("12PromoId: " + TBPromoId + " not found in the \"Test JSON \"").assignCategory("responVSTestJson");
               // ExReApiTestReport.info(response.getBody().asString());

            }

        }


        //return  ;
    }



    public void TestJSONVSResponse (int arrayIndex,Response response){

        // this loop will run on the Test JSON "CashBackDiscounts"
        for (int i=0 ,flag1 = 0; i<JSONGetData.getArraySizeCashBackDiscounts(
                JSONGetData.getCashBackDiscounts(TestJSONToSend, arrayIndex)); i++,flag1 = 0){

            CBPromoId =  JSONGetData.getPromoId(TestJSONToSend, arrayIndex ,i,"CashBackDiscounts");

            //this loop run on the "response" CashBackDiscounts arr and check if CBPromoId is in this list

            for (int j=0; j< responseHandling.getCaseBackDiscountsArrSize(response) ;j++){
                String temp = responseHandling.getPromoId(response,"CashBackDiscounts",j);
                if (temp.equals(CBPromoId)){
                    flag1 = 1 ;
//                    System.out.println("CashBackDiscounts:");
//                    System.out.println("response: "+CBPromoId+ ", test JSON: " + temp+", Index "+ j);
//                    ExReApiTestReport.info("CashBackDiscounts:");
//                    ExReApiTestReport.info("Test JSON promoID: "+ temp + " Response  promoID: " + CBPromoId ).assignCategory("TestJSONVSResponse");
//
//
//                    softAssertion.assertEquals(responseHandling.getAmount(response,"CashBackDiscounts",j),
//                            JSONGetData.getAmount(TestJSONToSend,arrayIndex,i,"CashBackDiscounts")
//                    );
//                    softAssertion.assertEquals(responseHandling.getDescription(response,"CashBackDiscounts",j),
//                            JSONGetData.getDescription(TestJSONToSend,arrayIndex,i,"CashBackDiscounts")
//                    );
//
//
//                    softAssertion.assertAll();

                    break;

                }
            }
            if( flag1 == 0 ){

                ExReApiTestReport.warning("PromoId: " + CBPromoId + " not found in the response ").assignCategory("TestJSONVSResponse");
               // ExReApiTestReport.info(response.getBody().asString());


            }
        }


        for (int i=0,flag1 = 0 ; i < JSONGetData.getArraySizeCashBackDiscounts
                (JSONGetData.getCashBackDiscounts(TestJSONToSend, arrayIndex));i++,flag1 = 0){

            TBPromoId = responseHandling.getPromoId(response,"TotalDiscounts",i);

            //this loop run on the "TestJSON" TotalDiscounts arr and check if CBPromoId is in this list

            for (int j=0; j<responseHandling.getCaseBackDiscountsArrSize(response) ; j++ ){

                String temp = JSONGetData.getPromoId(TestJSONToSend, arrayIndex ,j,"TotalDiscounts");

                if(TBPromoId.equals(temp)){
                    flag1 = 1 ;

//                    System.out.println("TotalDiscounts:");
//                    System.out.println("response: "+TBPromoId+ ", test JSON: " + temp+", Index "+ j);
//                    ExReApiTestReport.info("TotalDiscounts:");
//                    ExReApiTestReport.info("Test JSON promoID: "+ temp + " Response  promoID: " + TBPromoId ).assignCategory("TestJSONVSResponse");
//
//
//                    softAssertion.assertEquals(responseHandling.getAmount(response,"TotalDiscounts",j),
//                            JSONGetData.getAmount(TestJSONToSend,arrayIndex,i,"TotalDiscounts")
//                    );
//                    softAssertion.assertEquals(responseHandling.getDescription(response,"TotalDiscounts",j),
//                            JSONGetData.getDescription(TestJSONToSend,arrayIndex,i,"TotalDiscounts")
//                    );
//                    softAssertion.assertEquals(responseHandling.getAllItemsDiscountPercent(response,j),
//                            JSONGetData.getAllItemsDiscountPercent(TestJSONToSend,arrayIndex,i,"TotalDiscounts")
//                    );
//
//                    softAssertion.assertAll();
                    break;

                }
            }

            if( flag1 == 0 ){

                ExReApiTestReport.warning("PromoId: " + TBPromoId + " not found in the response ").assignCategory("TestJSONVSResponse");
               // ExReApiTestReport.info(response.getBody().asString());


            }
        }


    }

}




