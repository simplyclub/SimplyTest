package Tests;

import BaseClass.BaseAPI;
import BaseClass.BaseJSON;
import JSON.JSONGetData;
import JSON.ResponseHandling;
import Tests.TestFunctions.NewMemberAPIFunctions;
import Utilities.LogFileHandling;
import org.testng.annotations.Test;
import utilities.MainFunction;
import utilities.RetryAnalyzer;

import java.io.IOException;
import java.net.SocketTimeoutException;


public class NewMemberTests extends NewMemberAPIFunctions {

    private static String MEMBER_CARD_NUMBER = null;
    private static String NEW_MEMBER_CARD_NUMBER = null;
    private static String INT_RANDOM_NUMBER = null;
    protected static String ID_RANDOM_NUMBER = null;
    protected static String ID_RANDOM_NUMBER_2 = null;
    private static String MANUAL_CARD_NUMBER = null;
    ResponseHandling responseHandling = new ResponseHandling();
    JSONGetData jsonGetData = new JSONGetData();
    BaseAPI baseAPI = new BaseAPI();


    @Test(priority = 1,retryAnalyzer = RetryAnalyzer.class,
            description = "this test will Create a new member, search  for it, and then make an update ")
    public void MemberTest1 ()  throws IOException  {


        //member Add
        try {
            ID_RANDOM_NUMBER= MainFunction.RandomNumber();
            INT_RANDOM_NUMBER = MainFunction.RandomNumber();
            memberAddResponse = MemberAdd(0,INT_RANDOM_NUMBER,ID_RANDOM_NUMBER,"**auto**");
            memberAddResponse_String = MainFunction.convertOkHttpResponseToString(memberAddResponse);

            if(!(memberAddResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberAddResponse_String).equals("0"))){
                System.out.println(MainFunction.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + memberAddResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                        responseHandling.getErrorCodeStatusJson(memberAddResponse_String) + ")");
                ExReNewMemberTestReport.fail("*ERROR --- status code is not 200" + "(" + memberAddResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                        responseHandling.getErrorCodeStatusJson(memberAddResponse_String) + ")");
                LogFileHandling.createLogFile(baseJSON.getString(BaseJSON.MEMBER_ADD_JSON), LOG_FILE_DIRECTORY, "memberAdd",0);
                LogFileHandling.createLogFile(subTotalResponse_String, LOG_FILE_DIRECTORY, "memberAddResponse",0);
                memberAddResponse.body().close();
                MainFunction.onTestFailure("MemberTest1");

            }

            System.out.println(MainFunction.BaseLogStringFunc()+memberAddResponse_String);

            MEMBER_CARD_NUMBER = responseHandling.getCardNumber(memberAddResponse_String, "memberAddResponse");



            //member Search by card number
            memberSearchResponse = MemberSearch(MEMBER_CARD_NUMBER, 0,CARD_FIELD);
            memberSearchResponse_String = MainFunction.convertOkHttpResponseToString(memberSearchResponse);

            if(!(memberSearchResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberSearchResponse_String).equals("0"))){
                System.out.println(MainFunction.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + memberSearchResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                        responseHandling.getErrorCodeStatusJson(memberSearchResponse_String) + ")");
                ExReNewMemberTestReport.fail("**ERROR --- status code is not 200" + "(" + memberSearchResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                        responseHandling.getErrorCodeStatusJson(memberSearchResponse_String) + ")");
                LogFileHandling.createLogFile(baseJSON.getString(BaseJSON.MEMBER_SERACH_JSON), LOG_FILE_DIRECTORY, "memberSearch",0);
                LogFileHandling.createLogFile(memberSearchResponse_String, LOG_FILE_DIRECTORY, "memberSearchResponse",0);
                memberAddResponse.body().close();
                MainFunction.onTestFailure("MemberTest1");

            }


            //System.out.println(baseJSON.memberSerachJsonToSend);
            System.out.println(MainFunction.BaseLogStringFunc()+memberSearchResponse_String);


            if (MEMBER_CARD_NUMBER.equals(responseHandling.getCardNumber(memberSearchResponse_String, "memberSearchResponse"))) {
                ExReNewMemberTestReport.pass("Member Add -- PASS").assignCategory("NewMemberTest");
                memberAddCheck(memberAddResponse_String,ID_RANDOM_NUMBER,INT_RANDOM_NUMBER);
                ExReNewMemberTestReport.pass("Member Search(by card number) -- PASS").assignCategory("NewMemberTest");


                System.out.println(MainFunction.BaseLogStringFunc()+"1");
            } else {
                ExReNewMemberTestReport.fail(" New Member Not created ");
                MainFunction.onTestFailure("MemberTest1");
            }

            //member search by phone
            memberSearchResponse = MemberSearch(INT_RANDOM_NUMBER, 0,CELL_PHONE);
            memberSearchResponse_String = MainFunction.convertOkHttpResponseToString(memberSearchResponse);

            if(!(memberSearchResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberSearchResponse_String).equals("0"))){
                System.out.println(MainFunction.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + memberSearchResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                        responseHandling.getErrorCodeStatusJson(memberSearchResponse_String) + ")");
                ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + memberSearchResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                        responseHandling.getErrorCodeStatusJson(memberSearchResponse_String) + ")");
                LogFileHandling.createLogFile(baseJSON.getString(BaseJSON.MEMBER_SERACH_JSON), LOG_FILE_DIRECTORY, "memberSearch",0);
                LogFileHandling.createLogFile(memberSearchResponse_String, LOG_FILE_DIRECTORY, "memberSearchResponse",0);
                memberAddResponse.body().close();
                MainFunction.onTestFailure("MemberTest1");

            }


            //System.out.println(baseJSON.memberSerachJsonToSend);
            System.out.println(MainFunction.BaseLogStringFunc()+memberSearchResponse_String);


            if (INT_RANDOM_NUMBER.equals(responseHandling.getMemberField(memberSearchResponse_String,"cell_number",0))) {
                ExReNewMemberTestReport.pass("Member Search(by phone) -- PASS").assignCategory("NewMemberTest");

            } else {
                ExReNewMemberTestReport.fail("Member Search(by phone) -- FAIL").assignCategory("NewMemberTest");
                MainFunction.onTestFailure("MemberTest1");

            }

            //member update
             ID_RANDOM_NUMBER_2= MainFunction.RandomNumber();
            System.out.println("ID_RANDOM_NUMBER: "+ID_RANDOM_NUMBER);
            System.out.println("ID_RANDOM_NUMBER_2 :" +ID_RANDOM_NUMBER_2);
            //System.out.println(x);
            INT_RANDOM_NUMBER = MainFunction.RandomNumber();
            System.out.println(MainFunction.BaseLogStringFunc()+baseJSON.memberUpdateJsonToSend);
            try {
                memberUpdateResponse = MemberUpdate(0,MEMBER_CARD_NUMBER,INT_RANDOM_NUMBER,ID_RANDOM_NUMBER_2,CARD_FIELD);
                memberUpdateResponse_String = MainFunction.convertOkHttpResponseToString(memberUpdateResponse);

            }catch (NullPointerException e){
                System.out.println("ERROR(memberUpdateResponse) --- NullPointerException ");
                ExReNewMemberTestReport.fail("ERROR(memberUpdateResponse) --- NullPointerException ");


            }

            if(!(memberUpdateResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberUpdateResponse_String).equals("0"))){
                System.out.println("*ERROR --- status code is not 200" + "(" + memberUpdateResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                        responseHandling.getErrorCodeStatusJson(memberUpdateResponse_String) + ")");
                ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + memberUpdateResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                        responseHandling.getErrorCodeStatusJson(memberUpdateResponse_String) + ")");
                LogFileHandling.createLogFile(baseJSON.getString(BaseJSON.MEMBER_UPDATE_JSON), LOG_FILE_DIRECTORY, "memberSearch",0);
                LogFileHandling.createLogFile(memberUpdateResponse_String, LOG_FILE_DIRECTORY, "memberSearchResponse",0);
                memberUpdateResponse.body().close();
                MainFunction.onTestFailure("MemberTest1");

            }

            System.out.println("memberUpdateResponse: "+memberUpdateResponse_String);
            //System.out.println("memberUpdatePOST: "+  baseJSON.memberUpdateJsonToSend.toString());


            if(!(memberUpdateResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberUpdateResponse_String).equals("0"))){
                System.out.println("*ERROR (memberUpdate) --- status code is not 200" + "(" + memberUpdateResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                        responseHandling.getErrorCodeStatusJson(memberUpdateResponse_String) + ")");
                System.out.println(memberUpdateResponse_String);
                ExReNewMemberTestReport.fail("ERROR (memberUpdate) --- status code is not 200" + "(" + memberUpdateResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                        responseHandling.getErrorCodeStatusJson(memberUpdateResponse_String) + ")");
                LogFileHandling.createLogFile(baseJSON.getString(BaseJSON.MEMBER_UPDATE_JSON), LOG_FILE_DIRECTORY, "memberUpdate",0);
                LogFileHandling.createLogFile(memberUpdateResponse_String, LOG_FILE_DIRECTORY, "memberUpdateResponse",0);
                memberUpdateResponse.body().close();
                MainFunction.onTestFailure("MemberTest1");

            }




            memberUpdateCheck(memberUpdateResponse_String,ID_RANDOM_NUMBER_2,INT_RANDOM_NUMBER);

        }catch (SocketTimeoutException e){
            System.out.println("ERROE(MemberTest1)  --- The server is currently busy, please try again later ");
            ExReNewMemberTestReport.fail("ERROE(MemberTest1)  --- The server is currently busy, please try again later ");
        }catch (NullPointerException e){
            System.out.println("ERROE(MemberTest1)  --- NullPointerException ");
            ExReNewMemberTestReport.fail("ERROE(MemberTest1)  --- NullPointerException ");

        }


    }//End Test 1

    @Test(priority = 2,retryAnalyzer = RetryAnalyzer.class,
    description = "this test will , switch recognition (card number) and search it using \"get details\" ")
    public void MemberTest2 () throws IOException {
        //temp
        //MEMBER_CARD_NUMBER="1000067";
        ///

        System.out.println("MEMBER_CARD_NUMBER: "+MEMBER_CARD_NUMBER);
        NEW_MEMBER_CARD_NUMBER=Integer.toString(Integer.parseInt(MEMBER_CARD_NUMBER)+1);
        System.out.println("NEW_MEMBER_CARD_NUMBER: "+NEW_MEMBER_CARD_NUMBER);

        memberSwitchRecognitionResponse = MemberSwitchRecognition(0,MEMBER_CARD_NUMBER,NEW_MEMBER_CARD_NUMBER);
        memberSwitchRecognitionResponse_String = MainFunction.convertOkHttpResponseToString(memberSwitchRecognitionResponse);

        if(!(memberSwitchRecognitionResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberSwitchRecognitionResponse_String).equals("0"))){
            System.out.println("*ERROR --- status code is not 200" + "(" + memberSwitchRecognitionResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                    responseHandling.getErrorCodeStatusJson(memberSwitchRecognitionResponse_String) + ")");
            ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + memberSwitchRecognitionResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(memberSwitchRecognitionResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(BaseJSON.MEMBER_SWITCH_RECOGNITION_JSON), LOG_FILE_DIRECTORY, "memberAdd",0);
            LogFileHandling.createLogFile(memberSwitchRecognitionResponse_String, LOG_FILE_DIRECTORY, "memberAdd",0);
            memberSwitchRecognitionResponse.body().close();
            MainFunction.onTestFailure("MemberTest2");

        }

        System.out.println("memberSwitchRecognitionResponse_String: "+memberSwitchRecognitionResponse_String);
        userDataResponse =  getUserData(0,NEW_MEMBER_CARD_NUMBER);
        userDataResponse_String = MainFunction.convertOkHttpResponseToString(userDataResponse);


        //todo : Check that we got the same user as before



    }//End Test 2

    @Test(priority = 3,retryAnalyzer = RetryAnalyzer.class,
    description = "This test will, check if the joining benefits have been activated")
    public void MemberTest3 () throws IOException {
        //NEW_MEMBER_CARD_NUMBER="1000070";
        userDataResponse =  getUserData(0,NEW_MEMBER_CARD_NUMBER);
        System.out.println(MainFunction.BaseLogStringFunc()+baseJSON.memberJsonToSend);
        System.out.println(MainFunction.BaseLogStringFunc()+NEW_MEMBER_CARD_NUMBER);
        userDataResponse_String = MainFunction.convertOkHttpResponseToString(userDataResponse);
        System.out.println(MainFunction.BaseLogStringFunc()+userDataResponse_String);

        if(!checkJoinPromoActivition(userDataResponse_String)){
            MainFunction.onTestFailure("MemberTest3");
        }

    }//End Test 3

    @Test(priority = 4,retryAnalyzer = RetryAnalyzer.class,
    description = "This test will, Make subTotal & TranEnd , TranEndOnePhase and And will measure times ")
    public void MemberTest4 () throws IOException {
        //NEW_MEMBER_CARD_NUMBER="1000071";
        // System.out.println("123"+baseJSON.tranEndOnePhaseToSend);
        MainFunction.RestTimeGlobals();

//jsonGetData.getArraySize(TestJSONToSend) - 1
        for (int i = 0; i < jsonGetData.getArraySize(TestJSONToSend) - 1 ; i++) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~Transaction: " + (i + 1) + "~~~~~~~~~~~~~~~~~~~~~~");

//subTotal
            subTotalResponse = makeDealSubTotal(i,NEW_MEMBER_CARD_NUMBER);
            subTotalResponse_String = MainFunction.convertOkHttpResponseToString(subTotalResponse);

            if (!(subTotalResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(subTotalResponse_String).equals("0"))) {
                System.out.println("*ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                ExReNewMemberTestReport.fail("*ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                        responseHandling.getErrorCodeStatusJson(subTotalResponse_String) + ")");
                LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "subTotalCall", i + 1);
                LogFileHandling.createLogFile(subTotalResponse_String, LOG_FILE_DIRECTORY, "subTotalResponse", i + 1);
                subTotalResponse.body().close();
                System.out.println(baseJSON.jsonToSend);
                System.out.println(subTotalResponse_String);
                MainFunction.onTestFailure("MemberTest4");
            }

            avgTimeSubTotal.add(baseAPI.getResponseTime_OkHttp(subTotalResponse));

//TrenEnd
            trenEndResponse = makeDealTrenEnd(i,subTotalResponse_String,NEW_MEMBER_CARD_NUMBER);
            trenEndResponse_String = MainFunction.convertOkHttpResponseToString(trenEndResponse);

            if (!(trenEndResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndResponse_String).equals("0"))) {
                System.out.println("**ERROR --- status code is not 200" + "(" + subTotalResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" + responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                ExReNewMemberTestReport.fail("**ERROR --- status code is not 200" + "(" + trenEndResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                        responseHandling.getErrorCodeStatusJson(trenEndResponse_String) + ")");
                LogFileHandling.createLogFile(baseJSON.getString(baseJSON.JSON_TO_SEND), LOG_FILE_DIRECTORY, "trenEndCall",i+1);
                LogFileHandling.createLogFile(trenEndResponse_String, LOG_FILE_DIRECTORY, "trenEndResponse",i+1);
                trenEndResponse.body().close();
                MainFunction.onTestFailure("MemberTest4");
            }

            avgTimeTrenEnd.add(baseAPI.getResponseTime_OkHttp(trenEndResponse));

//TrenEndOnePhase
            baseJSON.ResatTreEndOnePhase();
            //baseJSON.TranEndOnePhaseJSONCopy();
            System.out.println("postResat****"+i+"***"+baseJSON.tranEndOnePhaseToSend.toString());

            trenEndOnePhaseResponse = makeTrenEndOnePhase(i,NEW_MEMBER_CARD_NUMBER);
            trenEndOnePhaseResponse_String=MainFunction.convertOkHttpResponseToString(trenEndOnePhaseResponse);

            if (!(trenEndOnePhaseResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String).equals("0"))) {
                System.out.println("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                                                  responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
                System.out.println(i);
                ExReNewMemberTestReport.fail("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                        responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
                ExReTrenEndOnePhaseReport.info(
                        LogFileHandling.createLogFile(baseJSON.tranEndOnePhaseToSend.toString(), LOG_FILE_DIRECTORY, "trenEndOnePhasecall",i+1));
                ExReTrenEndOnePhaseReport.info(
                        LogFileHandling.createLogFile(trenEndOnePhaseResponse_String, LOG_FILE_DIRECTORY, "trenEndOnePhaseResponse",i+1));
                userDataResponse.body().close();
                MainFunction.onTestFailure("MemberTest4");


            }

            avgTimeTrenEndOnePhase.add(baseAPI.getResponseTime_OkHttp(trenEndOnePhaseResponse));


        }//End of for loop



        try {
            ExReNewMemberTestReport.info("New member : Time test");
            System.out.println("TimeSubTotal: " + avgTimeSubTotal);

            ExReNewMemberTestReport.info("avgTimeSubTotal: " + avgTimeSubTotal);
            System.out.println("avg: " + MainFunction.getAvgTime(avgTimeSubTotal));

            ExReNewMemberTestReport.info("avg: " + MainFunction.getAvgTime(avgTimeSubTotal));
            System.out.println("trenEndResponse: " + avgTimeTrenEnd);

            ExReNewMemberTestReport.info("trenEndResponse: " + avgTimeTrenEnd);
            System.out.println("avg: " + MainFunction.getAvgTime(avgTimeTrenEnd));

            ExReNewMemberTestReport.info("avg: " + MainFunction.getAvgTime(avgTimeTrenEnd));
            System.out.println("trenEndOnePhaseResponse: " + avgTimeTrenEndOnePhase);

            ExReNewMemberTestReport.info("trenEndOnePhaseResponse: " + avgTimeTrenEndOnePhase);
            System.out.println("avg: " + MainFunction.getAvgTime(avgTimeTrenEndOnePhase));
            ExReNewMemberTestReport.info("avg: " + MainFunction.getAvgTime(avgTimeTrenEndOnePhase));
        }
        catch (ArithmeticException e){
            ExReNewMemberTestReport.fail("java.lang.ArithmeticException: / by zero");
            System.out.println("ERROR(MemberTest4) --- ArithmeticException: / by zero ");
}


    }//End Test 4


    @Test(priority =  5,retryAnalyzer = RetryAnalyzer.class,
    description = "This test will check: Buy a joining item and a membership renewal item in the club and check that the date has been updated correctly")
    public void MemberTest5() throws IOException {
        //NEW_MEMBER_CARD_NUMBER="1000071";
        baseJSON.ResatTreEndOnePhase();
        baseJSON.TranEndOnePhaseJSONCopy();

        makeJoinItemDeal(NEW_MEMBER_CARD_NUMBER);


        baseJSON.ResatTreEndOnePhase();
        baseJSON.TranEndOnePhaseJSONCopy();
        makeRenewItemDeal(NEW_MEMBER_CARD_NUMBER);

        baseJSON.ResatTreEndOnePhase();
        baseJSON.TranEndOnePhaseJSONCopy();
        makeRenewItem2Deal(NEW_MEMBER_CARD_NUMBER);



    }

    @Test(priority = 6,retryAnalyzer = RetryAnalyzer.class,description = "this test will, change the member stats from active to nun active")
    public void MemberTest6 () throws IOException {
        memberSwitchStatusResponse =  changeMemberStatus(0,NEW_MEMBER_CARD_NUMBER,MEMBER_STATUS_NOT_ACTIVE);
        memberSwitchStatusResponse_String = MainFunction.convertOkHttpResponseToString(memberSwitchStatusResponse);
        System.out.println(baseJSON.memberSwitchStatusJsonToSend);
        System.out.println(memberSwitchStatusResponse_String);


        if(responseHandling.getMemberStatus(memberSwitchStatusResponse_String).equals(MEMBER_STATUS_NOT_ACTIVE)){
            ExReNewMemberTestReport.pass("Member SwitchStatus -- PASS");


        }else{
            ExReNewMemberTestReport.fail("Member SwitchStatus -- FAIL");
            ExReNewMemberTestReport.info("Member status is --> "+responseHandling.getMemberStatus(memberSwitchStatusResponse_String)+"/n"
                    +" and NOT -->"+MEMBER_STATUS_NOT_ACTIVE);
            MainFunction.onTestFailure("MemberTest6");
        }

    }//End Test 6


    @Test(priority = 7,retryAnalyzer = RetryAnalyzer.class,
    description = "This test will check member add with  manual card number and then change stats to nun active")
    public void MemberTest7() throws IOException {

        MANUAL_CARD_NUMBER= makeManualMemberCard();
        System.out.println("MANUAL_CARD_NUMBER: "+ MANUAL_CARD_NUMBER);

        if((MANUAL_CARD_NUMBER!= null)){
            ID_RANDOM_NUMBER= MainFunction.RandomNumber();
            INT_RANDOM_NUMBER = MainFunction.RandomNumber();
            memberAddResponse = MemberAdd(0,INT_RANDOM_NUMBER,ID_RANDOM_NUMBER,MANUAL_CARD_NUMBER);
            memberAddResponse_String = MainFunction.convertOkHttpResponseToString(memberAddResponse);
            System.out.println(memberAddResponse_String);

            if((memberAddResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberAddResponse_String).equals("0"))){

                memberSwitchStatusResponse =  changeMemberStatus(0,MANUAL_CARD_NUMBER,MEMBER_STATUS_NOT_ACTIVE);
                memberSwitchStatusResponse_String = MainFunction.convertOkHttpResponseToString(memberSwitchStatusResponse);
                System.out.println(baseJSON.memberSwitchStatusJsonToSend);
                System.out.println(memberSwitchStatusResponse_String);


                if(responseHandling.getMemberStatus(memberSwitchStatusResponse_String).equals(MEMBER_STATUS_NOT_ACTIVE)){
                    System.out.println("111");
                    ExReNewMemberTestReport.pass("Member manual cardID -- PASS");


                }else{
                    ExReNewMemberTestReport.fail("Member manual cardID -- FAIL");
                    ExReNewMemberTestReport.info("Member status is --> "+responseHandling.getMemberStatus(memberSwitchStatusResponse_String)+"/n"
                            +" and NOT -->"+MEMBER_STATUS_NOT_ACTIVE);
                    MainFunction.onTestFailure("MemberTest7");
                }

            }



        }else{

            ExReNewMemberTestReport.fail("MANUAL_CARD_NUMBER("+MANUAL_CARD_NUMBER+") is null");
            MainFunction.onTestFailure("MemberTest7");
        }






    }//End Test 7

    @Test(priority = 8,retryAnalyzer = RetryAnalyzer.class,
            description ="This test will do a fail test, will search for  a member that is  does not exist " )
    public void MemberTest8() throws IOException {

        memberSearchResponse = MemberSearch("1234",0,CARD_FIELD);
        System.out.println(baseJSON.memberSerachJsonToSend.toString());
        memberSearchResponse_String= MainFunction.convertOkHttpResponseToString(memberSearchResponse);
        System.out.println(memberSearchResponse_String);

        if(responseHandling.getMembersArraySize(memberSearchResponse_String)== 0){
            System.out.println(responseHandling.getErrorCodeStatusJson(memberSearchResponse_String));
            ExReNewMemberTestReport.pass("Member fail search  -- PASS");
            memberAddResponse.body().close();

        }else{
            ExReNewMemberTestReport.fail("Member fail search  -- FAIL");
            memberAddResponse.body().close();
            MainFunction.onTestFailure("MemberTest8");

        }
    }



}//end class
