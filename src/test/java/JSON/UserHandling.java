package JSON;

import Tests.BasePage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public  class UserHandling extends BasePage {
    /**
     *
     * @param arr  "AllAccums" array
     * @param st what you whant to get from the array(BenefitName,BenefitValue,BenefitType,AccID)
     * @param index the index of the obj in the array
     * @return String with the Data
     */

    public static String getVoucher(JSONArray arr, String st,int index){
        JSONObject PointsVoucher = (JSONObject) arr.get(index);

        if (st.equals("BenefitName")){
            return getBenefitName(PointsVoucher);

        }else if(st.equals("BenefitValue")){
            return getBenefitValue(PointsVoucher);

        }else if (st.equals("BenefitType")){
            return getBenefitType(PointsVoucher);
        }else if (st.equals("AccID")){
            return getAccID(PointsVoucher);
        }

        return null;

    }

    private static String getBenefitName (JSONObject obj){
        return obj.get("BenefitName").toString();
    }
    private static String getBenefitValue (JSONObject obj){
        return obj.get("BenefitValue").toString();
    }
    private static String getBenefitType (JSONObject obj){
        return obj.get("BenefitType").toString();
    }
    private static String getAccID (JSONObject obj){
        return obj.get("AccID").toString();
    }
}
