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
                    System.out.println("CashBackDiscounts:");
                    System.out.println("response: "+CBPromoId+ ", test JSON: " + temp+", Index "+ j);
                    flag1 = 1;
                    ExReApiTestReport.info("CashBackDiscounts:");
                    ExReApiTestReport.info("Response promoID: "+ CBPromoId + "Test JSON promoID: " + temp ).assignCategory("responVSTestJson");


                    softAssertion.assertEquals(responseHandling.getAmount(response,"CashBackDiscounts",i),
                            JSONGetData.getAmount(TestJSONToSend,arrayIndex,j,"CashBackDiscounts")
                    );
                    softAssertion.assertEquals(responseHandling.getDescription(response,"CashBackDiscounts",i),
                            JSONGetData.getDescription(TestJSONToSend,arrayIndex,j,"CashBackDiscounts")
                    );


                    softAssertion.assertAll();

                    break;
                }

            }
            if( flag1 == 0 ){
                // TODO : Create a new JSON file of the response
                ExReApiTestReport.warning("PromoId: " + CBPromoId + " not found in the \"Test JSON \"").assignCategory("responVSTestJson");


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
                    System.out.println("TotalDiscounts:");
                    System.out.println("response: "+TBPromoId+ ", test JSON: " + temp+", Index "+ j);
                    ExReApiTestReport.info("TotalDiscounts:");
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
                    softAssertion.assertAll();

                    break;


                }

            }
            if( flag1 == 0 ){
                // TODO : Create a new JSON file of the response
                ExReApiTestReport.warning("12PromoId: " + TBPromoId + " not found in the \"Test JSON \"").assignCategory("responVSTestJson");

            }

        }

// TODO : Think about adding a type of return that indicates that the test was successful
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
                    System.out.println("CashBackDiscounts:");
                    System.out.println("response: "+CBPromoId+ ", test JSON: " + temp+", Index "+ j);
                    ExReApiTestReport.info("CashBackDiscounts:");
                    ExReApiTestReport.info("Test JSON promoID: "+ temp + " Response  promoID: " + CBPromoId ).assignCategory("TestJSONVSResponse");
                    flag1 = 1 ;



                    softAssertion.assertEquals(responseHandling.getAmount(response,"CashBackDiscounts",j),
                            JSONGetData.getAmount(TestJSONToSend,arrayIndex,i,"CashBackDiscounts")
                    );
                    softAssertion.assertEquals(responseHandling.getDescription(response,"CashBackDiscounts",j),
                            JSONGetData.getDescription(TestJSONToSend,arrayIndex,i,"CashBackDiscounts")
                    );


                    softAssertion.assertAll();

                    break;

                }
            }
            if( flag1 == 0 ){
                // TODO : Create a new JSON file of the response
                ExReApiTestReport.warning("PromoId: " + CBPromoId + " not found in the response ").assignCategory("TestJSONVSResponse");


            }
        }


        for (int i=0,flag1 = 0 ; i < JSONGetData.getArraySizeCashBackDiscounts
                (JSONGetData.getCashBackDiscounts(TestJSONToSend, arrayIndex));i++,flag1 = 0){

            TBPromoId = responseHandling.getPromoId(response,"TotalDiscounts",i);

            //this loop run on the "TestJSON" TotalDiscounts arr and check if CBPromoId is in this list

            for (int j=0; j<responseHandling.getCaseBackDiscountsArrSize(response) ; j++ ){

                String temp = JSONGetData.getPromoId(TestJSONToSend, arrayIndex ,j,"TotalDiscounts");

                if(TBPromoId.equals(temp)){
                    System.out.println("TotalDiscounts:");
                    System.out.println("response: "+TBPromoId+ ", test JSON: " + temp+", Index "+ j);
                    ExReApiTestReport.info("TotalDiscounts:");
                    ExReApiTestReport.info("Test JSON promoID: "+ temp + " Response  promoID: " + TBPromoId ).assignCategory("TestJSONVSResponse");
                    flag1 = 1 ;

                    softAssertion.assertEquals(responseHandling.getAmount(response,"TotalDiscounts",j),
                            JSONGetData.getAmount(TestJSONToSend,arrayIndex,i,"TotalDiscounts")
                    );
                    softAssertion.assertEquals(responseHandling.getDescription(response,"TotalDiscounts",j),
                            JSONGetData.getDescription(TestJSONToSend,arrayIndex,i,"TotalDiscounts")
                    );
                    softAssertion.assertEquals(responseHandling.getAllItemsDiscountPercent(response,j),
                            JSONGetData.getAllItemsDiscountPercent(TestJSONToSend,arrayIndex,i,"TotalDiscounts")
                    );

                    softAssertion.assertAll();
                    break;

                }
            }

            if( flag1 == 0 ){
                // TODO : Create a new JSON file of the response
                ExReApiTestReport.warning("PromoId: " + TBPromoId + " not found in the response ").assignCategory("TestJSONVSResponse");


            }
        }


    }




















    /*
    public void expectedResultVSResponse(int arrayIndex,Response response) {

        //compere cash back
        // Go over all the wanted promos
       for (int CBIndex = 0; CBIndex <
                JSONGetData.getArraySizeCashBackDiscounts
                        (JSONGetData.getCashBackDiscounts(subTotalJSONObj, arrayIndex)); CBIndex++) {

          String CBPromoId = responseHandling.getPromoId( response, "CashBackDiscounts",CBIndex );
          String temp = JSONGetData.getPromoId(subTotalJSONObj, arrayIndex ,CBIndex,"CashBackDiscounts");

            if (CBPromoId.equals(temp)) {
                System.out.println("response: "+CBPromoId+ ", test JSON: " + temp+", CBIndex "+CBIndex);

                    softAssertion.assertEquals(
                            responseHandling.getDescription(response, "CashBackDiscounts", CBIndex),
                            JSONGetData.getDescription(subTotalJSONObj, arrayIndex, CBIndex, "CashBackDiscounts")
                    );
                    softAssertion.assertEquals(
                            responseHandling.getAmount(response, "CashBackDiscounts", CBIndex),
                            JSONGetData.getAmount(subTotalJSONObj, arrayIndex, CBIndex, "CashBackDiscounts")
                    );
                    softAssertion.assertEquals(
                            responseHandling.getIsAuto(response, "CashBackDiscounts", CBIndex),
                            JSONGetData.getIsAuto(subTotalJSONObj, arrayIndex, CBIndex, "CashBackDiscounts")
                    );



                softAssertion.assertAll();


            }


        }
        for ( int TDIndex = 0 ;TDIndex<
                JSONGetData.getArraySizeTotalDiscounts(
                        JSONGetData.getTotalDiscounts(subTotalJSONObj,arrayIndex) ); TDIndex++) {
            String CBPromoId = responseHandling.getPromoId( response, "TotalDiscounts",TDIndex );
            String temp = JSONGetData.getPromoId(subTotalJSONObj, arrayIndex ,TDIndex,"TotalDiscounts");

            if (CBPromoId.equals(temp)) {
                System.out.println(CBPromoId + "  " + temp + ", TDIndex " + TDIndex);

                softAssertion.assertEquals(
                        responseHandling.getDescription(response,"TotalDiscounts",TDIndex),
                        JSONGetData.getDescription(subTotalJSONObj,arrayIndex,TDIndex,"TotalDiscounts")
                );
                softAssertion.assertEquals(
                        responseHandling.getAmount(response,"TotalDiscounts",TDIndex),
                        JSONGetData.getAmount(subTotalJSONObj,arrayIndex,TDIndex,"TotalDiscounts")
                );
                softAssertion.assertEquals(
                        responseHandling.getIsAuto(response,"TotalDiscounts",TDIndex),
                        JSONGetData.getIsAuto(subTotalJSONObj,arrayIndex,TDIndex,"TotalDiscounts")
                );
                softAssertion.assertEquals(
                        responseHandling.getAllItemsDiscountPercent(response,TDIndex),
                        JSONGetData.getAllItemsDiscountPercent(subTotalJSONObj,arrayIndex,TDIndex,"TotalDiscounts")
                );
                softAssertion.assertAll();
            }
        }


    }*/



}




