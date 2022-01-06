package Tests.TestFunctions;

import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import FunctionsClass.UpdateJSONFile;
import JSON.JSONGetData;
import Tests.BasePage;
import Tests.JoinSmsTest;

import java.io.IOException;

public class JoinSmsTeatFunction extends BasePage {
    UpdateJSONFile updateJSONFile = new UpdateJSONFile();
    JSONGetData jsonGetData = new JSONGetData();
    BaseJSON baseJSON = new BaseJSON();



    public okhttp3.Response sendJoinSms(String field, String operator, String value) throws IOException {
        updateJSONFile.upDateSendJoinSms(jsonGetData.getAccoundID(TestJSONToSend,0),jsonGetData.getUser(TestJSONToSend,0),
                jsonGetData.getPassword(TestJSONToSend,0),field,operator,value);

        return APIPost.postSendJoinSms_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.SEND_JOIN_SMS_JSON);


    }
    public okhttp3.Response sendJoinSms2(String field, String operator, String value) throws IOException {
        updateJSONFile.upDateSendJoinSms(jsonGetData.getAccoundID(TestJSONToSend,0),jsonGetData.getUser(TestJSONToSend,0),
                jsonGetData.getPassword(TestJSONToSend,0),field,operator,value);

        return APIPost.postSendJoinSms2_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.SEND_JOIN_SMS_JSON);


    }
    public okhttp3.Response sendMemberGetDetailsAndCode(String fieldId, String fieldValue) throws IOException {
        updateJSONFile.upDateMemberGetDetailsAndCode(jsonGetData.getAccoundID(TestJSONToSend,0),jsonGetData.getUser(TestJSONToSend,0),
                jsonGetData.getPassword(TestJSONToSend,0),fieldId,fieldValue);

        return APIPost.postMemberGetDetailsAndCode_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.MEMBER_GET_DETAILS_AND_CODE_JSON);

    }

    public okhttp3.Response sendMemberGetDetails( String FieldId, String fieldValue, String AddInfoFieldValue, String AddInfoFieldId) throws IOException {
        updateJSONFile.upDateMemberGetDetails(jsonGetData.getAccoundID(TestJSONToSend,0),jsonGetData.getUser(TestJSONToSend,0),
                jsonGetData.getPassword(TestJSONToSend,0),FieldId,fieldValue,AddInfoFieldValue,AddInfoFieldId);

        return APIPost.postMemberGetDetails_OkHttp(BaseAPI.TEST_REST_API_URI,baseJSON.MEMBER_GET_DETAILS_JSON);
    }


}//End class
