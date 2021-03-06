package Tests.TestFunctions;

import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import BaseClass.BaseXML;
import FunctionsClass.ReadXMLFile;
import FunctionsClass.UpdateJSONFile;
import JSON.JSONGetData;
import JSON.ResponseHandling;
import Tests.BasePage;
import Utilities.LogFileHandling;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import utilities.MainFunctions;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static BaseClass.BaseAPI.TEST_API_SYSTEM_URI;

public class NewMemberAPIFunctions extends BasePage {
    UpdateJSONFile updateJSONFile = new UpdateJSONFile();
    JSONGetData jsonGetData = new JSONGetData();
    ResponseHandling responseHandling = new ResponseHandling();
    BaseXML baseXML = new BaseXML();



    protected okhttp3.Response MemberAdd(int i,String int_random_string,String id_random_string ,String cardNumber ) throws IOException {
        updateJSONFile.upDateMemberAddlJSONFile(jsonGetData.getAccoundID(TestJSONToSend,i),
                jsonGetData.getUser(TestJSONToSend,i),jsonGetData.getPassword(TestJSONToSend,i),int_random_string,id_random_string,cardNumber);

        return APIPost.postMemberAdd_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.MEMBER_ADD_JSON);
    }

    protected okhttp3.Response MemberSearch(String cardNumber, int i,String cardField) throws IOException {
        updateJSONFile.upDateMemberSerachJSONFile(jsonGetData.getAccoundID(TestJSONToSend,i),
                jsonGetData.getUser(TestJSONToSend,i),jsonGetData.getPassword(TestJSONToSend,i),cardField,Equal,cardNumber);

        return APIPost.postMemberSearch_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.MEMBER_SERACH_JSON);
    }

    protected okhttp3.Response MemberUpdate(int i,String CardNumber, String int_random_string,String id_random_string,String cardFilde) throws IOException {
        updateJSONFile.upDateMemberUpdateJSONFile(jsonGetData.getAccoundID(TestJSONToSend,i),
                jsonGetData.getUser(TestJSONToSend,i),jsonGetData.getPassword(TestJSONToSend,i),cardFilde,CardNumber,int_random_string
                ,id_random_string);

        return APIPost.postMemberUpdate_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.MEMBER_UPDATE_JSON);

    }

    protected okhttp3.Response MemberSwitchRecognition(int i,String oldCardNumber,String newCardNumber  ) throws IOException {

        updateJSONFile.upDateMemberSwitchRecognitionJSONFile(jsonGetData.getAccoundID(TestJSONToSend,i),
                jsonGetData.getUser(TestJSONToSend,i),jsonGetData.getPassword(TestJSONToSend,i),oldCardNumber,newCardNumber);

        return APIPost.postMemberSwitchRecognition_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.MEMBER_SWITCH_RECOGNITION_JSON);
    }

    protected  okhttp3.Response getUserData(int i,String newCardNumber) throws IOException {
        updateJSONFile.upDateUserJSONFile(
                JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getFieldId(TestJSONToSend, i),newCardNumber);
        return APIPost.postUserGetData_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.MEMBER_JSON_TO_SEND);

    }

    protected okhttp3.Response makeDealSubTotal(int i,String memberCardNumber) throws IOException {
        updateJSONFile.upDateBaseJSONFile(JSONGetData.getUser(TestJSONToSend, i), JSONGetData.getPassword(TestJSONToSend, i),
                JSONGetData.getAccoundID(TestJSONToSend, i), JSONGetData.getTranItems(TestJSONToSend, i),memberCardNumber);
        return APIPost.postSubTotal_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.JSON_TO_SEND);

    }//func end

    protected okhttp3.Response makeDealTrenEnd(int i, String subTotalResponse,String memberCardNumber) throws IOException {
        updateJSONFile.upDateTranEndJSON(responseHandling.getServiceTranNumber(subTotalResponse), JSONGetData.getAccoundID(TestJSONToSend, i),
                JSONGetData.getUser(TestJSONToSend, i),
                JSONGetData.getPassword(TestJSONToSend, i), memberCardNumber,
                JSONGetData.getTranItems(TestJSONToSend, i), JSONGetData.getDealsToUse(TestJSONToSend, i), baseJSON.jsonToSend);
        return APIPost.postTranEnd_OkHttp(BaseAPI.TEST_REST_API_URI, baseJSON.JSON_TO_SEND);
    }//func end

    protected okhttp3.Response makeTrenEndOnePhase(int i, String memberCardNumber) throws IOException {
        updateJSONFile.upDateTranEndOnePhase(jsonGetData.getAccoundID(TestJSONToSend,i),jsonGetData.getUser(TestJSONToSend,i),
                jsonGetData.getPassword(TestJSONToSend,i),
                memberCardNumber,jsonGetData.getTranItems(TestJSONToSend,i));

        //System.out.println("****"+i+"***"+baseJSON.tranEndOnePhaseToSend.toString());

        return APIPost.postTrenEndOnePhase_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.TREN_END_ONE_PHASE);

    }//func end

    protected boolean checkJoinPromoActivition(String userDataResponse) throws IOException {
        String s = null;
        JSONArray AllAccums =  responseHandling.getAllAccums(userDataResponse);
        int flag = 0;
        for (int r = 0; r < AllAccums.size(); r++) {
            JSONObject x = (JSONObject) AllAccums.get(r);

            switch (x.get("AccID").toString()) {
                case "282":
                    s=x.get("BenefitValue").toString();
                    System.out.println(MainFunctions.BaseLogStringFunc()+"checkJoinPromoActivition --- case 282: "+s);


                    if (!(s.equals("100.00"))){
                        ExReNewMemberTestReport.fail(".checkJoinPromoActivition  case "+x.get("AccID").toString()+" --- Fail");
                        System.out.println(MainFunctions.BaseLogStringFunc()+"BenefitValue = "+s);
                        flag =1;

                    }
                    break;

                case "284":
                    s=x.get("BenefitValue").toString();
                    System.out.println(MainFunctions.BaseLogStringFunc()+"checkJoinPromoActivition---case 284: "+s);


                    if (!(s.equals("10001"))){
                        ExReNewMemberTestReport.fail(".checkJoinPromoActivition  case "+x.get("AccID").toString()+" --- Fail");
                        System.out.println(MainFunctions.BaseLogStringFunc()+"BenefitValue = "+s);
                        flag =1;

                    }
                    break;
                case "285":
                    s=x.get("BenefitValue").toString();
                    System.out.println(MainFunctions.BaseLogStringFunc()+"checkJoinPromoActivition --- case 285: "+s);


                    if (!(s.equals("500"))){
                        ExReNewMemberTestReport.fail(".checkJoinPromoActivition  case "+x.get("AccID").toString()+" --- Fail");
                        flag =1;

                    }
                    break;
                case "286":
                    s=x.get("BenefitValue").toString();
                    System.out.println(MainFunctions.BaseLogStringFunc()+"checkJoinPromoActivition --- case 286: "+s);


                    if (!(s.equals("40.70"))){
                        ExReNewMemberTestReport.fail(".checkJoinPromoActivition  case "+x.get("AccID").toString()+" --- Fail");
                        flag =1;

                    }
                    break;
                default:

                    break;
            }

        }
        if(flag==1){
            return false;
        }else{
            return true;
        }


    }


    protected okhttp3.Response changeMemberStatus(int i , String FildeValue , String MemberStatus) throws IOException {
        updateJSONFile.upDateMemberSwitchStatus(jsonGetData.getAccoundID(TestJSONToSend,i),jsonGetData.getUser(TestJSONToSend,i),
                jsonGetData.getPassword(TestJSONToSend,i),
                FildeValue,MemberStatus);

        return APIPost.postMemberSwitchStatus_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.MEMBER_SWITCH_STATUS_JSON);

    }

    protected int memberAddCheck(String memberAddResponse,String idMember, String phoneNumber) {
        int flag = 0 ;

        JSONObject obj = (JSONObject) baseJSON.convertStringToJSONObj(memberAddResponse);
        JSONObject member = (JSONObject) obj.get("Member");
        JSONArray memberFields = (JSONArray) member.get("MemberFields");
        //System.out.println("memberFields: "+memberFields);
        //System.out.println("idMember: "+idMember);

        for (int index = 0; index < memberFields.size(); index++) {
            JSONObject temp = (JSONObject) memberFields.get(index);
            //System.out.println("temp: "+temp);

            if(temp.get("FieldId").toString().equals("birthday")){
                if (temp.get("FieldValue").toString().equals("01/01/2021")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: birthday -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("first_name")){
                if (MainFunctions.SearchWordInString("Test",temp.get("FieldValue").toString()) == 1){
                    flag = 1 + flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: first_name -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("last_name")){
                if (MainFunctions.SearchWordInString("automation",temp.get("FieldValue").toString()) == 1){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: last_name -- NOT update  ");

                }

            }

            if(temp.get("FieldId").toString().equals("email")){
                if (MainFunctions.SearchWordInString("Test",temp.get("FieldValue").toString()) == 1){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: email -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("personal_id")){
                if (MainFunctions.SearchWordInString(idMember,temp.get("FieldValue").toString()) == 1){
                    flag = 1 + flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: personal_id -- NOT update  ");

                }

            }

            if(temp.get("FieldId").toString().equals("if_send_sms")){
                if (temp.get("FieldValue").toString().equals("True")){
                    flag = 1 +flag ;
                }else{
                    flag = 0;
                    ExReNewMemberTestReport.fail("Member Add --> field: if_send_sms -- NOT update  ");
                }

            }

            if(temp.get("FieldId").toString().equals("cell_number")){

                if (temp.get("FieldValue").toString().equals(phoneNumber)){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: cell_number -- NOT update  ");
                }

            }
            if(temp.get("FieldId").toString().equals("udf_100")){

                if (temp.get("FieldValue").toString().equals("100")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: cell_number -- NOT update  ");
                }

            }
            if(temp.get("FieldId").toString().equals("udf_99")){

                if (MainFunctions.SearchWordInString("String",temp.get("FieldValue").toString()) == 1){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: udf_99 -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("udf_90")){

                if (temp.get("FieldValue").toString().equals("02/02/2002")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: udf_90 -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("udf_89")){

                if (temp.get("FieldValue").toString().equals("false")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: udf_89 -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("udf_88")){

                if (temp.get("FieldValue").toString().equals("12345678910")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: udf_88 -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("udf_87")){

                if (temp.get("FieldValue").toString().equals("20.20")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Add --> field: udf_87 -- NOT update  ");

                }

            }





        }// end for loop

        if(flag == 13 ){
            ExReNewMemberTestReport.pass("Member Add field check -- PASS");
            return 1;
        }else{
            ExReNewMemberTestReport.fail("Member Add field check -- FAIL");
            return 0;
        }






    }//End Function

    protected int memberUpdateCheck(String memberUpdateResponse,String idMember, String phoneNumber) {
        int flag = 0 ;

        JSONObject obj = (JSONObject) baseJSON.convertStringToJSONObj(memberUpdateResponse);
        JSONObject member = (JSONObject) obj.get("Member");
        JSONArray memberFields = (JSONArray) member.get("MemberFields");
        //System.out.println("memberFields: "+memberFields);
        //System.out.println("idMember: "+idMember);

        for (int index = 0; index < memberFields.size(); index++) {
            JSONObject temp = (JSONObject) memberFields.get(index);
            //System.out.println("temp: "+temp);

            if(temp.get("FieldId").toString().equals("birthday")){
                if (temp.get("FieldValue").toString().equals("03/01/2021")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: birthday -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("first_name")){
                if (MainFunctions.SearchWordInString("_update",temp.get("FieldValue").toString()) == 1){
                    flag = 1 + flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: first_name -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("last_name")){
                if (MainFunctions.SearchWordInString("_update",temp.get("FieldValue").toString()) == 1){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: last_name -- NOT update  ");

                }

            }

            if(temp.get("FieldId").toString().equals("email")){
                if (MainFunctions.SearchWordInString("_update",temp.get("FieldValue").toString()) == 1){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: email -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("personal_id")){
                if (MainFunctions.SearchWordInString(idMember,temp.get("FieldValue").toString()) == 1){
                    flag = 1 + flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: personal_id -- NOT update  ");

                }

            }

            if(temp.get("FieldId").toString().equals("if_send_sms")){
                if (temp.get("FieldValue").toString().equals("False")){
                    flag = 1 +flag ;
                }else{
                    flag = 0;
                    ExReNewMemberTestReport.fail("Member Update --> field: if_send_sms -- NOT update  ");
                }

            }

            if(temp.get("FieldId").toString().equals("cell_number")){

                if (temp.get("FieldValue").toString().equals(phoneNumber)){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: cell_number -- NOT update  ");
                }

            }
            if(temp.get("FieldId").toString().equals("udf_100")){

                if (temp.get("FieldValue").toString().equals("200")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: cell_number -- NOT update  ");
                }

            }
            if(temp.get("FieldId").toString().equals("udf_99")){

                if (MainFunctions.SearchWordInString("_update",temp.get("FieldValue").toString()) == 1){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: udf_99 -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("udf_90")){

                if (temp.get("FieldValue").toString().equals("03/03/2003")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: udf_90 -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("udf_89")){

                if (temp.get("FieldValue").toString().equals("truth")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: udf_89 -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("udf_88")){

                if (temp.get("FieldValue").toString().equals("10987654321")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: udf_88 -- NOT update  ");

                }

            }
            if(temp.get("FieldId").toString().equals("udf_87")){

                if (temp.get("FieldValue").toString().equals("40.40")){
                    flag = 1 +flag ;
                }else {
                    flag = 0 ;
                    ExReNewMemberTestReport.fail("Member Update --> field: udf_87 -- NOT update  ");

                }

            }





        }// end for loop

        if(flag == 13 ){
            ExReNewMemberTestReport.pass("Member Update -- PASS");
            return 1;
        }else{
            ExReNewMemberTestReport.fail("Member Update -- FAIL");

            return 0;
        }






    }//End Function

    protected  void makeJoinItemDeal(String memberCardNumber  ) throws IOException {

        //System.out.println("tranEndOnePhaseToSend_pre: "+ baseJSON.tranEndOnePhaseToSend.toString());
        try {
            userDataResponse = getUserData(0, memberCardNumber);
            //System.out.println(baseJSON.memberJsonToSend.toString());
            userDataResponse_String = MainFunctions.convertOkHttpResponseToString(userDataResponse);
        }catch (SocketTimeoutException e){
            System.out.println("ERROE(MemberTest5)  --- The server is currently busy, please try again later ");
            ExReNewMemberTestReport.fail("ERROE(MemberTest5)  --- The server is currently busy, please try again later ");
        }catch (NullPointerException e){
            System.out.println("ERROE(MemberTest5)  --- NullPointerException ");
            ExReNewMemberTestReport.fail("ERROE(MemberTest5)  --- NullPointerException ");

        }

        if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
            System.out.println("***ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    "                            responseHandling.getErrorCodeStatusJson(userDataResponse)" + ")");
            ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
            ExReTrenEndOnePhaseReport.info(
                    LogFileHandling.createLogFile(baseJSON.memberJsonToSend.toString(), LOG_FILE_DIRECTORY, "userDatacall",1));
            ExReTrenEndOnePhaseReport.info(
                    LogFileHandling.createLogFile(userDataResponse_String, LOG_FILE_DIRECTORY, "userDataResponse",1));
            userDataResponse.body().close();
            MainFunctions.onTestFailure("MemberTest5");


        }

        //System.out.println(userDataResponse_String);
        String ExpDate_pre = responseHandling.getUserDetailsExpDate(userDataResponse_String);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        int Year_pre = LocalDate.parse(ExpDate_pre,formatter).getYear() ;
        int Mount_pre =LocalDate.parse(ExpDate_pre,formatter).getMonthValue();
        int Day_pre =LocalDate.parse(ExpDate_pre,formatter).getDayOfMonth();

//        System.out.println("year_pre: "+ Year_pre);
//        System.out.println("Mount_pre: "+ Mount_pre);
//        System.out.println("Day_pre: "+ Day_pre);

        //make trenEndOne Phase

        updateJSONFile.upDateTranEndOnePhase(jsonGetData.getAccoundID(TestJSONToSend,0),jsonGetData.getUser(TestJSONToSend,0),
                jsonGetData.getPassword(TestJSONToSend,0),
                memberCardNumber,jsonGetData.getJoinOrRenewItems("JoinItem",JoinRenewClubJSONToSend));

        //System.out.println("tranEndOnePhaseToSend_post: "+baseJSON.tranEndOnePhaseToSend);
        System.out.println(MainFunctions.BaseLogStringFunc()+"Response: "+(APIPost.postTrenEndOnePhase_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.TREN_END_ONE_PHASE)).body().string());

        try {
            userDataResponse = getUserData(0, memberCardNumber);
            userDataResponse_String = MainFunctions.convertOkHttpResponseToString(userDataResponse);
        }catch (SocketTimeoutException e){
            System.out.println("ERROE(MemberTest5)  --- The server is currently busy, please try again later ");
            ExReNewMemberTestReport.fail("ERROE(MemberTest5)  --- The server is currently busy, please try again later ");
        }catch (NullPointerException e){
            System.out.println("ERROE(MemberTest5)  --- NullPointerException ");
            ExReNewMemberTestReport.fail("ERROE(MemberTest5)  --- NullPointerException ");

        }
        if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
            System.out.println("***ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    "                            responseHandling.getErrorCodeStatusJson(userDataResponse)" + ")");
            ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
            ExReTrenEndOnePhaseReport.info(
                    LogFileHandling.createLogFile(baseJSON.memberJsonToSend.toString(), LOG_FILE_DIRECTORY, "userDatacall",1));
            ExReTrenEndOnePhaseReport.info(
                    LogFileHandling.createLogFile(userDataResponse_String, LOG_FILE_DIRECTORY, "userDataResponse",1));
            userDataResponse.body().close();
            MainFunctions.onTestFailure("MemberTest5");


        }

        String ExpDate_post = responseHandling.getUserDetailsExpDate(userDataResponse_String);


        int Year_post = LocalDate.parse(ExpDate_post,formatter).getYear() ;
        int Mount_post =LocalDate.parse(ExpDate_post,formatter).getMonthValue();
        int Day_post =LocalDate.parse(ExpDate_post,formatter).getDayOfMonth();
//        System.out.println("year_post: "+ Year_post);
//        System.out.println("Mount_post: "+ Mount_post);
//        System.out.println("Day_post: "+ Day_post);

        //fixme :  fix if -- Change so that it will check the difference between dates in days

        if (Year_pre+1 == Year_post&& Mount_pre == Mount_post&& (Day_pre == Day_post||Day_pre+1 == Day_post)){
            ExReNewMemberTestReport.pass("Member Buy join item -- PASS");
        }else{
            ExReNewMemberTestReport.fail("Member Buy join item -- FAIL");
            MainFunctions.onTestFailure("MemberTest5");

        }




    }
    /**
     * make deal eith renew item Quantity=2
     * @param memberCardNumber
     * @throws IOException
     */
    protected  void makeRenewItemDeal(String memberCardNumber  ) throws IOException {

        //System.out.println("tranEndOnePhaseToSend_pre: "+ baseJSON.tranEndOnePhaseToSend.toString());
        try {
            userDataResponse = getUserData(0, memberCardNumber);
            //System.out.println(baseJSON.memberJsonToSend.toString());
            userDataResponse_String = MainFunctions.convertOkHttpResponseToString(userDataResponse);
        }catch (SocketTimeoutException e){
            System.out.println("ERROE(MemberTest5)  --- The server is currently busy, please try again later ");
            ExReNewMemberTestReport.fail("ERROE(MemberTest5)  --- The server is currently busy, please try again later ");
        }catch (NullPointerException e){

            System.out.println("ERROE(MemberTest5)  --- NullPointerException ");
            ExReNewMemberTestReport.fail("ERROE(MemberTest5)  --- NullPointerException ");
        }

        if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
            System.out.println("***ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    "                            responseHandling.getErrorCodeStatusJson(userDataResponse)" + ")");
            ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
            ExReTrenEndOnePhaseReport.info(
                    LogFileHandling.createLogFile(baseJSON.memberJsonToSend.toString(), LOG_FILE_DIRECTORY, "userDatacall",1));
            ExReTrenEndOnePhaseReport.info(
                    LogFileHandling.createLogFile(userDataResponse_String, LOG_FILE_DIRECTORY, "userDataResponse",1));
            userDataResponse.body().close();
            MainFunctions.onTestFailure("MemberTest5");


        }
       // System.out.println(userDataResponse_String);
        String ExpDate_pre = responseHandling.getUserDetailsExpDate(userDataResponse_String);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        int Year_pre = LocalDate.parse(ExpDate_pre,formatter).getYear() ;
        int Mount_pre =LocalDate.parse(ExpDate_pre,formatter).getMonthValue();
        int Day_pre =LocalDate.parse(ExpDate_pre,formatter).getDayOfMonth();
        //System.out.println("year_pre: "+ Year_pre);
        //System.out.println("Mount_pre: "+ Mount_pre);
        //System.out.println("Day_pre: "+ Day_pre);

        //make trenEndOne Phase

        updateJSONFile.upDateTranEndOnePhase(jsonGetData.getAccoundID(TestJSONToSend,0),jsonGetData.getUser(TestJSONToSend,0),
                jsonGetData.getPassword(TestJSONToSend,0),
                memberCardNumber,jsonGetData.getJoinOrRenewItems("RenewItem",JoinRenewClubJSONToSend));

        //System.out.println("tranEndOnePhaseToSend_post: "+baseJSON.tranEndOnePhaseToSend);
        System.out.println(MainFunctions.BaseLogStringFunc()+"Response: "+(APIPost.postTrenEndOnePhase_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.TREN_END_ONE_PHASE)).body().string());

        try {
            userDataResponse = getUserData(0, memberCardNumber);
            userDataResponse_String = MainFunctions.convertOkHttpResponseToString(userDataResponse);
        }catch (SocketTimeoutException e){
            System.out.println("ERROE(MemberTest5)  --- The server is currently busy, please try again later ");
            ExReNewMemberTestReport.fail("ERROE(MemberTest5)  --- The server is currently busy, please try again later ");
        }catch (NullPointerException e){
            System.out.println("ERROE(MemberTest5)  --- NullPointerException ");
            ExReNewMemberTestReport.fail("ERROE(MemberTest5)  --- NullPointerException ");

        }

        if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
            System.out.println("***ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    "                            responseHandling.getErrorCodeStatusJson(userDataResponse)" + ")");
            ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
            ExReTrenEndOnePhaseReport.info(
                    LogFileHandling.createLogFile(baseJSON.memberJsonToSend.toString(), LOG_FILE_DIRECTORY, "userDatacall",1));
            ExReTrenEndOnePhaseReport.info(
                    LogFileHandling.createLogFile(userDataResponse_String, LOG_FILE_DIRECTORY, "userDataResponse",1));
            userDataResponse.body().close();
            MainFunctions.onTestFailure("MemberTest5");


        }
        String ExpDate_post = responseHandling.getUserDetailsExpDate(userDataResponse_String);


        int Year_post = LocalDate.parse(ExpDate_post,formatter).getYear() ;
        int Mount_post =LocalDate.parse(ExpDate_post,formatter).getMonthValue();
        int Day_post =LocalDate.parse(ExpDate_post,formatter).getDayOfMonth();
       // System.out.println("year_post: "+ Year_post);
       // System.out.println("Mount_post: "+ Mount_post);
        //System.out.println("Day_post: "+ Day_post);

        if (Year_pre+2 == Year_post&& Mount_pre == Mount_post && Day_pre == Day_post){

            ExReNewMemberTestReport.pass("Member Buy Renew item -- PASS");
        }else{
            ExReNewMemberTestReport.fail("Member Buy Renew item -- FAIL");
            ExReNewMemberTestReport.info("year_pre: "+ Year_pre);
            ExReNewMemberTestReport.info("Mount_pre: "+ Mount_pre);
            ExReNewMemberTestReport.info("Day_pre: "+ Day_pre);

            ExReNewMemberTestReport.info("year_post: "+ Year_post);
            ExReNewMemberTestReport.info("Mount_post: "+ Mount_post);
            ExReNewMemberTestReport.info("Day_post: "+ Day_post);
            MainFunctions.onTestFailure("MemberTest5");



        }




    }

    /**
     * make deal with renew item Quantity=1
     * @param memberCardNumber
     * @throws IOException
     */
    protected  void makeRenewItem2Deal(String memberCardNumber  ) throws IOException {

        //System.out.println("tranEndOnePhaseToSend_pre: "+ baseJSON.tranEndOnePhaseToSend.toString());

        userDataResponse = getUserData(0,memberCardNumber);
        //System.out.println(baseJSON.memberJsonToSend.toString());
        userDataResponse_String = MainFunctions.convertOkHttpResponseToString(userDataResponse);
        if (!(userDataResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(userDataResponse_String).equals("0"))) {
            System.out.println("***ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    "                            responseHandling.getErrorCodeStatusJson(userDataResponse)" + ")");
            ExReAccumReport.fail("ERROR --- status code is not 200" + "(" + userDataResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(userDataResponse_String) + ")");
            ExReTrenEndOnePhaseReport.info(
                    LogFileHandling.createLogFile(baseJSON.memberJsonToSend.toString(), LOG_FILE_DIRECTORY, "userDatacall",1));
            ExReTrenEndOnePhaseReport.info(
                    LogFileHandling.createLogFile(userDataResponse_String, LOG_FILE_DIRECTORY, "userDataResponse",1));
            userDataResponse.body().close();
            MainFunctions.onTestFailure("makeRenewItem2Deal");


        }
        //System.out.println(userDataResponse_String);
        String ExpDate_pre = responseHandling.getUserDetailsExpDate(userDataResponse_String);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        int Year_pre = LocalDate.parse(ExpDate_pre,formatter).getYear() ;
        int Mount_pre =LocalDate.parse(ExpDate_pre,formatter).getMonthValue();
        int Day_pre =LocalDate.parse(ExpDate_pre,formatter).getDayOfMonth();
       // System.out.println("year_pre: "+ Year_pre);
        //System.out.println("Mount_pre: "+ Mount_pre);
       // System.out.println("Day_pre: "+ Day_pre);

        //make trenEndOne Phase

        updateJSONFile.upDateTranEndOnePhase(jsonGetData.getAccoundID(TestJSONToSend,0),jsonGetData.getUser(TestJSONToSend,0),
                jsonGetData.getPassword(TestJSONToSend,0),
                memberCardNumber,jsonGetData.getJoinOrRenewItems("RenewItem2",JoinRenewClubJSONToSend));

        //System.out.println("tranEndOnePhaseToSend_post: "+baseJSON.tranEndOnePhaseToSend);
        System.out.println(MainFunctions.BaseLogStringFunc()+"Response: "+(APIPost.postTrenEndOnePhase_OkHttp(BaseAPI.TEST_REST_API_URI, BaseJSON.TREN_END_ONE_PHASE)).body().string());


        userDataResponse = getUserData(0,memberCardNumber);
        userDataResponse_String = MainFunctions.convertOkHttpResponseToString(userDataResponse);
        String ExpDate_post = responseHandling.getUserDetailsExpDate(userDataResponse_String);


        int Year_post = LocalDate.parse(ExpDate_post,formatter).getYear() ;
        int Mount_post =LocalDate.parse(ExpDate_post,formatter).getMonthValue();
        int Day_post =LocalDate.parse(ExpDate_post,formatter).getDayOfMonth();
       // System.out.println("year_post: "+ Year_post);
       // System.out.println("Mount_post: "+ Mount_post);
        //System.out.println("Day_post: "+ Day_post);

        if (Year_pre+1 == Year_post&& Mount_pre == Mount_post && Day_pre == Day_post){

            ExReNewMemberTestReport.pass("Member Buy Renew item2 -- PASS");
        }else{
            ExReNewMemberTestReport.fail("Member Buy Renew item2 -- FAIL");
            ExReNewMemberTestReport.info("year_pre: "+ Year_pre);
            ExReNewMemberTestReport.info("Mount_pre: "+ Mount_pre);
            ExReNewMemberTestReport.info("Day_pre: "+ Day_pre);

            ExReNewMemberTestReport.info("year_post: "+ Year_post);
            ExReNewMemberTestReport.info("Mount_post: "+ Mount_post);
            ExReNewMemberTestReport.info("Day_post: "+ Day_post);
            MainFunctions.onTestFailure("MemberTest5");


        }




    }
    protected String makeManualMemberCard(){
        String rangeFrom = null ;
        String totalUsed = null ;
        updateXMLFile.updateXMLGetMemberRecognizeList(baseXML.xmlToDocGetMemberRecognizeList(),"loginKey",updateXMLFile.getSysLoginBuisness2828());
        Response x = APIPost.postXMLToMemberRecognizeList(TEST_API_SYSTEM_URI);
        Document doc =  BaseXML.convertStringToXMLDocument(x.getBody().asString());
        for (int index = 0; index < doc.getElementsByTagName("MemberRecognizeData").getLength(); index++) {

            String str =  ReadXMLFile.getStringValueFromXML("Description", doc.getDocumentElement(), index);
            //System.out.println("str: "+str);
            if (str.equals("?????????? ?????????? ?????????? ?????????? ????????")){
               rangeFrom = ReadXMLFile.getStringValueFromXML("RangeFrom", doc.getDocumentElement(), index);
               totalUsed = ReadXMLFile.getStringValueFromXML("TotalUsed", doc.getDocumentElement(), index);
                break;
            }else{
                continue;
            }


            
        }//End for loop
        return String.valueOf(Integer.parseInt(rangeFrom)+Integer.parseInt(totalUsed) );



    }









}//end class
