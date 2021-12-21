package Tests.TestFunctions;

import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import BaseClass.BaseXML;
import FunctionsClass.UpdateJSONFile;
import JSON.JSONGetData;
import JSON.ResponseHandling;
import Tests.BasePage;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

import static BaseClass.BaseAPI.TEST_API_SYSTEM_URI;

public class Stage6And7TestFunctions extends BasePage {
    UpdateJSONFile updateJSONFile = new UpdateJSONFile();
    JSON.JSONGetData jsonGetData = new JSONGetData();
    BaseXML baseXML =new BaseXML();
    ResponseHandling responseHandling = new ResponseHandling();

    public okhttp3.Response makeTrenEndOnePhase(int i,int dealNumber) throws IOException {

        //small fix: Match the name of the test to the location of the item in the array
        dealNumber--;
        //
        updateJSONFile.upDateTranEndOnePhase(jsonGetData.getAccoundID(TestJSONToSend,i),jsonGetData.getUser(TestJSONToSend,i),jsonGetData.getPassword(TestJSONToSend,i),
                jsonGetData.getCardNumber(TestJSONToSend,i),getStage6Items(dealNumber,baseJSON.getObj(JSON_STAGE_6_AND_7_DEAL_ITEMS)));


        return APIPost.postTrenEndOnePhase_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.TREN_END_ONE_PHASE);

    }

    public Response GetTransactionView(String response) throws IOException {
        updateXMLFile.updateGetTransactionView(baseXML.xmlToDocGetTransactionView(), "loginKey", updateXMLFile.getSysLogin());
        updateXMLFile.updateGetTransactionView(baseXML.xmlToDocGetTransactionView(), "tranKey", responseHandling.getTrenEndTranReferenceNumber(response));

        return APIPost.postXMLToGetTransactionView(TEST_API_SYSTEM_URI, BaseXML.GET_TREN_FILE_LOCATION);
    }

    private JSONArray getStage6Items (int dealNumber , Object obj){
        JSONObject JSONObj = (JSONObject) obj ;
        JSONArray stage = (JSONArray) JSONObj.get("stage_6");
        JSONObject deal = (JSONObject) stage.get(dealNumber);
        JSONArray x =(JSONArray) deal.get("deal_"+(dealNumber+1));
        //System.out.println(x);
        return x;





    }


}
