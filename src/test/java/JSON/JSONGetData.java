package JSON;

import BaseClass.BaseJSON;
import  org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.json.Json;

import java.util.Set;

public class JSONGetData  {
    public static void getDealsToUseAccumId(Object testJSONToSend, int t) {
    }
    //this class is for gating data from the "JSON_TEST_FILE" for comparing

    /**
     * Thos function returns the size of  the TestJson
     * @param obj TestJSONToSend
     * @return
     */
    public Integer getArraySize (Object obj){
        try {
            JSONObject JSONObj = (JSONObject) obj;
            JSONArray tests = (JSONArray) JSONObj.get("array");
            return  tests.size();
        }catch (NullPointerException e){
            System.out.println("ERROR -- problem with array in Test Json");
        }
        return null;

    }
    public Integer getArraySizeCashBackDiscounts (JSONObject obj){
        JSONObject JSONObj =  obj ;
        JSONArray tests = (JSONArray) JSONObj.get("CashBackDiscounts");
        return  tests.size();
    }


    public Integer getArraySizeTotalDiscounts (JSONObject obj){
        JSONObject JSONObj =  obj ;
        JSONArray tests = (JSONArray) JSONObj.get("TotalDiscounts");
        return  tests.size();
    }

    /**
     * get the size of  Accumulates array
     * @param obj TestJSONToSend
     * @param index i
     * @return
     */
    public static Integer getArraySizeAccumulates(Object obj,int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject z = (JSONObject) tests.get(index);
        JSONArray response = (JSONArray) z.get("Response");
        JSONObject Accumulates = (JSONObject) response.get(2);
        JSONArray t = (JSONArray) Accumulates.get("Accumulates");
        return  t.size();
    }
    public static Integer getArraySizeSumAccum(Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject sumAccum = (JSONObject) tests.get(index);
        JSONArray accum = (JSONArray) sumAccum.get("sumAccum");

        return  accum.size();
    }



    public static String getUser(Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  userJSONObj = (JSONObject) tests.get(index);
        return   userJSONObj.get("User").toString();

    }
    public static String getPassword(Object obj, Integer index) {
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  PasswordJSONObj = (JSONObject) tests.get(index);
        return   PasswordJSONObj.get("Password").toString();
    }

    public static JSONArray getTranItems(Object obj, Integer index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  tranItemsJSONObj = (JSONObject) tests.get(index);
        JSONArray x = (JSONArray)tranItemsJSONObj.get("TranItems");
        return x;

    }

    public String getTestName (Object obj,Integer index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  testNameJSONObj = (JSONObject) tests.get(index);
        return  testNameJSONObj.get("TestName").toString();


    }
    public static JSONObject getCashBackDiscounts(Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject response =  (JSONObject) tests.get(index);
        JSONArray x = (JSONArray) response.get("Response");
        JSONObject cashBackDiscounts  = (JSONObject) x.get(0);

        return cashBackDiscounts;

    }

    public static JSONObject getTotalDiscounts(Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject response =  (JSONObject) tests.get(index);
        JSONArray x = (JSONArray) response.get("Response");
        JSONObject totalDiscounts  = (JSONObject) x.get(1);
       // System.out.println(totalDiscounts);
        return totalDiscounts;

    }
    public static JSONObject getTotalAccumulates(Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject response =  (JSONObject) tests.get(index);
        JSONArray x = (JSONArray) response.get("Response");
        JSONObject totalDiscounts  = (JSONObject) x.get(2);
       // System.out.println(totalDiscounts);
        return totalDiscounts;

    }

    public String getDescription(Object obj , int arrayIndex,int index,String discountsType){
        if (discountsType.equals("CashBackDiscounts")){
            JSONObject x = getCashBackDiscounts(obj,arrayIndex);
            JSONArray x2 = (JSONArray) x.get("CashBackDiscounts");
            JSONObject description = (JSONObject) x2.get(index);
            return  description.get("Description").toString();

        }else
        if(discountsType.equals("TotalDiscounts")){
            JSONObject x = getTotalDiscounts(obj,arrayIndex);
            JSONArray x2 = (JSONArray) x.get("TotalDiscounts");
            JSONObject description = (JSONObject) x2.get(index);
            return  description.get("Description").toString();

        }

        return null;

    }

    /**
     * this function return the promoId from the Test Json Response : CashBackDiscounts / TotalDiscounts / Accumulates
     * @param obj
     * @param dealIndex   deal index
     * @param index the index inside the array
     * @param discountsType CashBackDiscounts / TotalDiscounts / Accumulates
     * @return
     */
    public static String getPromoId(Object obj, int dealIndex, int index, String discountsType){
        if (discountsType.equals("CashBackDiscounts")){
            JSONObject x = getCashBackDiscounts(obj,dealIndex);
            JSONArray x2 = (JSONArray) x.get("CashBackDiscounts");
            JSONObject description = (JSONObject) x2.get(index);
            return  description.get("PromoId").toString();

        }else
            if(discountsType.equals("TotalDiscounts")){
                JSONObject x = getTotalDiscounts(obj,dealIndex);
                JSONArray x2 = (JSONArray) x.get("TotalDiscounts");
                JSONObject description = (JSONObject) x2.get(index);
                return  description.get("PromoId").toString();

        }else if(discountsType.equals("Accumulates")){
                JSONObject x = getTotalAccumulates(obj,dealIndex);
                JSONArray x2 = (JSONArray) x.get("Accumulates");
                JSONObject description = (JSONObject) x2.get(index);
                return  description.get("PromoID").toString();
            }



            return null;




    }
    public static String getAmount(Object obj, int arrayIndex, int CBIndex, String discountsType){
        if (discountsType.equals("CashBackDiscounts")){
            JSONObject x = getCashBackDiscounts(obj,arrayIndex);
            JSONArray x2 = (JSONArray) x.get("CashBackDiscounts");
            JSONObject description = (JSONObject) x2.get(CBIndex);

            return  description.get("Amount").toString();

        }else
            if(discountsType.equals("TotalDiscounts")){
                JSONObject x = getTotalDiscounts(obj,arrayIndex);
                JSONArray x2 = (JSONArray) x.get("TotalDiscounts");
                JSONObject description = (JSONObject) x2.get(CBIndex);

                return  description.get("Amount").toString();

        }else if(discountsType.equals("Accumulates")){
                JSONObject x = getTotalAccumulates(obj,arrayIndex);
                JSONArray x2 = (JSONArray) x.get("Accumulates");
                JSONObject description = (JSONObject) x2.get(CBIndex);
                return  description.get("Amount").toString();
            }

            return null;




    }public static String getAccumID(Object obj, int arrayIndex, int CBIndex, String discountsType){
        if (discountsType.equals("CashBackDiscounts")){
            JSONObject x = getCashBackDiscounts(obj,arrayIndex);
            JSONArray x2 = (JSONArray) x.get("CashBackDiscounts");
            JSONObject description = (JSONObject) x2.get(CBIndex);

            return  description.get("AccumId").toString();

        }else
            if(discountsType.equals("TotalDiscounts")){
                JSONObject x = getTotalDiscounts(obj,arrayIndex);
                JSONArray x2 = (JSONArray) x.get("TotalDiscounts");
                JSONObject description = (JSONObject) x2.get(CBIndex);

                return  description.get("AccumId").toString();

        }else if(discountsType.equals("Accumulates")){
                JSONObject x = getTotalAccumulates(obj,arrayIndex);
                JSONArray x2 = (JSONArray) x.get("Accumulates");
                JSONObject description = (JSONObject) x2.get(CBIndex);
                return  description.get("AccumId").toString();
            }

            return null;




    }
    public String getIsAuto (Object obj,int arrayIndex,int CBIndex,String discountsType){
        if (discountsType.equals("CashBackDiscounts")){
            JSONObject x = getCashBackDiscounts(obj,arrayIndex);
            JSONArray x2 = (JSONArray) x.get("CashBackDiscounts");
            JSONObject description = (JSONObject) x2.get(CBIndex);

            return  description.get("IsAuto").toString();

        }else
            if(discountsType.equals("TotalDiscounts")){
                JSONObject x = getTotalDiscounts(obj,arrayIndex);
                JSONArray x2 = (JSONArray) x.get("TotalDiscounts");
                JSONObject description = (JSONObject) x2.get(CBIndex);

                return  description.get("IsAuto").toString();

        }

            return null;




    }
    public String getAllItemsDiscountPercent (Object obj,int arrayIndex,int CBIndex,String discountsType){
        if (discountsType.equals("CashBackDiscounts")){
            JSONObject x = getCashBackDiscounts(obj,arrayIndex);
            JSONArray x2 = (JSONArray) x.get("CashBackDiscounts");
            JSONObject description = (JSONObject) x2.get(CBIndex);

            return  description.get("AllItemsDiscountPercent").toString();

        }else
        if(discountsType.equals("TotalDiscounts")){
            JSONObject x = getTotalDiscounts(obj,arrayIndex);
            JSONArray x2 = (JSONArray) x.get("TotalDiscounts");
            JSONObject description = (JSONObject) x2.get(CBIndex);

            return  description.get("AllItemsDiscountPercent").toString();

        }

        return null;




    }
    public static String getFieldId(Object obj, Integer index){
        JSONObject jsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) jsonObj.get("array");
        JSONObject member =  (JSONObject) tests.get(index);
        JSONArray x = (JSONArray) member.get("Member");
        JSONObject fieldId = (JSONObject) x.get(0);
        return fieldId.get("fieldId").toString();



    }
    public static String getFieldValue(Object obj, Integer index){
        JSONObject jsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) jsonObj.get("array");
        JSONObject member =  (JSONObject) tests.get(index);
        JSONArray x = (JSONArray) member.get("Member");
        JSONObject fieldValue = (JSONObject) x.get(0);
        //System.out.println(fieldValue.get("fieldValue").toString());
        return fieldValue.get("fieldValue").toString();


    }
    public JSONArray getAllAccumsArray(Object obj , Integer index){
        JSONObject jsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) jsonObj.get("array");
        JSONObject member =  (JSONObject) tests.get(index);
        JSONArray x = (JSONArray) member.get("Member");
        JSONObject fieldValue = (JSONObject) x.get(0);
        JSONArray allAccums = (JSONArray) fieldValue.get("AllAccums");

        return allAccums;

    }


    public static String   getSumAccumKey(Object obj, int index, int Aindex){
        JSONObject jsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) jsonObj.get("array");
        JSONObject sumAccum = (JSONObject) tests.get(index);
        JSONArray accum = (JSONArray) sumAccum.get("sumAccum");
        JSONObject x = (JSONObject) accum.get(Aindex);
        Set<String > key = x.keySet();

        return stringFix(key.toString());


    }

    private static String stringFix(String str){
        String str1 =str.replace("[","");
        String str2 =str1.replace("]","");
        return str2;
    }
    public static String getSumAccumValue(Object obj, String key, int index, int Aindex){
        JSONObject JsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JsonObj.get("array");
        JSONObject sumAccum = (JSONObject) tests.get(index);
        JSONArray accum = (JSONArray) sumAccum.get("sumAccum");
        JSONObject x =(JSONObject) accum.get(Aindex);
        return x.get(key).toString();

    }


    public static JSONArray getDealsToUse(Object obj, int index){
        JSONObject JsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JsonObj.get("array");
        JSONObject x =  (JSONObject) tests.get(index);
        JSONArray dealToUse = (JSONArray) x.get("DealsToUse");
        return dealToUse;
    }
    public String getDealsToUseData (Object obj ,int mainTestIndex,int DealIndex , String Key){
        JSONArray JsonArray = getDealsToUse(obj,mainTestIndex);
        JSONObject data = (JSONObject) JsonArray.get(DealIndex);
        return data.get(Key).toString();

    }

    public static String getDealsToUseAccumId(Object obj, int i, int DealIndex){
        JSONObject JsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JsonObj.get("array");
        JSONObject x =  (JSONObject) tests.get(i);
        JSONArray dealToUse = (JSONArray) x.get("DealsToUse");
        JSONObject d = (JSONObject) dealToUse.get(DealIndex);
        return d.get("AccumId").toString();

    }public String getDealsToUseBurned(Object obj,int index,int DealIndex){
        JSONObject JsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JsonObj.get("array");
        JSONObject x =  (JSONObject) tests.get(index);
        JSONArray dealToUse = (JSONArray) x.get("DealsToUse");
        JSONObject d = (JSONObject) dealToUse.get(DealIndex);
        return d.get("Burned").toString();

    }public static String getDealsToUseDiscountType(Object obj, int index, int DealIndex){
        JSONObject JsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JsonObj.get("array");
        JSONObject x =  (JSONObject) tests.get(index);
        JSONArray dealToUse = (JSONArray) x.get("DealsToUse");
        JSONObject d = (JSONObject) dealToUse.get(DealIndex);
        return d.get("DiscountType").toString();


    }

    /**
     *
     * @param obj TestJsonToSend
     * @param index  the index for what deal test is runing
     * @param DealIndex location inside the array of DealsToUse
     * @return String
     */
    public static String getDealsToUseAmount(Object obj, int index, int DealIndex){
        JSONObject JsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JsonObj.get("array");
        JSONObject x =  (JSONObject) tests.get(index);
        JSONArray dealToUse = (JSONArray) x.get("DealsToUse");
        JSONObject d = (JSONObject) dealToUse.get(DealIndex);
        return d.get("Amount").toString();

    }public static String getDealsToUsePToSValue(Object obj, int index, int DealIndex){
        JSONObject JsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JsonObj.get("array");
        JSONObject x =  (JSONObject) tests.get(index);
        JSONArray dealToUse = (JSONArray) x.get("DealsToUse");
        JSONObject d = (JSONObject) dealToUse.get(DealIndex);
        return d.get("PToSValue").toString();

    }

    /**
     *
     * @param obj TestJsonToSend
     * @param index the index for what deal test is runing
     * @param DealIndex location inside the array of DealsToUse
     * @return String
     */
    public static String getDealsToUsePromoID(Object obj, int index, int DealIndex){
        JSONObject JsonObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JsonObj.get("array");
        JSONObject x =  (JSONObject) tests.get(index);
        JSONArray dealToUse = (JSONArray) x.get("DealsToUse");
        JSONObject d = (JSONObject) dealToUse.get(DealIndex);
        return d.get("PromoID").toString();

    }

    public static String getAccoundID(Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  PasswordJSONObj = (JSONObject) tests.get(index);
        return   PasswordJSONObj.get("AccoundID").toString();

    }
    public static String getTotalDealAmount(Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  DealAmountJSONObj = (JSONObject) tests.get(index);
        return   DealAmountJSONObj.get("TotalDealAmount").toString();

    }
    public static String getCardNumber(Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  DealAmountJSONObj = (JSONObject) tests.get(index);
        return   DealAmountJSONObj.get("CardNumber").toString();

    }

    public static String getDealTypeFlag (Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  DealAmountJSONObj = (JSONObject) tests.get(index);
        return   DealAmountJSONObj.get("DealTypeFlag").toString();

    }
    public static String getPVFlag (Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  DealAmountJSONObj = (JSONObject) tests.get(index);
        return DealAmountJSONObj.get("PVFlag").toString();

    }
    public static String GetIfToCancelFlag (Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  DealAmountJSONObj = (JSONObject) tests.get(index);
        return DealAmountJSONObj.get("IfToCancelFlag").toString();

    }
    public static String getDaysToStart (Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  DealAmountJSONObj = (JSONObject) tests.get(index);
        return DealAmountJSONObj.get("DaysToStart").toString();

    }

    public static String getDaysToEnd (Object obj, int index){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray  tests = (JSONArray) JSONObj.get("array");
        JSONObject  DealAmountJSONObj = (JSONObject) tests.get(index);
        return DealAmountJSONObj.get("DaysToEnd").toString();

    }

    public static JSONArray getJoinOrRenewItems(String itemType , Object obj){

        if(itemType.equals("JoinItem")){
            JSONObject JSONObj = (JSONObject) obj ;
            JSONArray arr = (JSONArray) JSONObj.get("array");
            JSONObject join = (JSONObject) arr.get(1);
            System.out.println("join: "+ join);
            JSONArray joinItem = (JSONArray) join.get("JoinItem");
            System.out.println(joinItem.toString());



            return joinItem;

        }

        if (itemType.equals("RenewItem")){
            JSONObject JSONObj = (JSONObject) obj ;
            JSONArray arr = (JSONArray) JSONObj.get("array");
            JSONObject renew = (JSONObject) arr.get(0);
            JSONArray renewItem = (JSONArray) renew.get("RenewItem");
            System.out.println(renewItem.toString());


            return renewItem;
        }

        return null;

    }























}//End Class
