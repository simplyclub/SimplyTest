package Tests;

import JSON.ResponseHandling;
import Tests.TestFunctions.JoinSmsTeatFunction;
import Utilities.LogFileHandling;

import org.testng.annotations.Test;
import utilities.MainFunctions;
import utilities.RetryAnalyzer;

import java.io.IOException;

public class JoinSmsTest extends  BasePage{
    JoinSmsTeatFunction joinSmsTeatFunction = new JoinSmsTeatFunction();
    ResponseHandling responseHandling = new ResponseHandling();

    @Test(description = "", retryAnalyzer = RetryAnalyzer.class)
    public void JoinSmsTest() throws IOException, InterruptedException {


        joinSmsResponse = joinSmsTeatFunction.sendJoinSms(CELL_PHONE,Equal,"0534320757");
        joinSmsResponse_String = MainFunctions.convertOkHttpResponseToString(joinSmsResponse);
        //System.out.println(MainFunction.BaseLogStringFunc()+joinSmsResponse_String);

        if(!(joinSmsResponse.code() == 200 && responseHandling.getResultCode(joinSmsResponse_String).equals("0"))){
            System.out.println(MainFunctions.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + joinSmsResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                    responseHandling.getErrorCodeStatusJson(joinSmsResponse_String) + ")");
            ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + joinSmsResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(joinSmsResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(baseJSON.SEND_JOIN_SMS_JSON), LOG_FILE_DIRECTORY, "JoinSmsTest",0);
            LogFileHandling.createLogFile(joinSmsResponse_String, LOG_FILE_DIRECTORY, "joinSmsTestResponse",0);
            joinSmsResponse.body().close();
            MainFunctions.onTestFailure("JoinSmsTest");

        }


        if(responseHandling.getResultCode(joinSmsResponse_String).equals("0")){
            ExReJoinSmsReport.pass(" Send Join SMS --- PASS");
            System.out.println(MainFunctions.BaseLogStringFunc()+" Send Join SMS --- PASS");
        }else{
            ExReJoinSmsReport.pass(" Send Join SMS --- FAIL");
            System.out.println(MainFunctions.BaseLogStringFunc()+" Send Join SMS --- FAIL");
            MainFunctions.onTestFailure("JoinSmsTest");

        }




    }//End JoinSms

    @Test(retryAnalyzer = RetryAnalyzer.class)

    public void JoinSms2Test() throws IOException, InterruptedException {
        Thread.sleep(10000);
        joinSmsResponse = joinSmsTeatFunction.sendJoinSms2(CELL_PHONE,Equal,"0534320757");
        joinSmsResponse_String = MainFunctions.convertOkHttpResponseToString(joinSmsResponse);
        //System.out.println(MainFunction.BaseLogStringFunc()+joinSmsResponse_String);

        if(!(joinSmsResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(joinSmsResponse_String).equals("0"))){
            System.out.println(MainFunctions.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + joinSmsResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                    responseHandling.getErrorCodeStatusJson(joinSmsResponse_String) + ")");
            ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + joinSmsResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(joinSmsResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(baseJSON.SEND_JOIN_SMS_JSON), LOG_FILE_DIRECTORY, "joinSms",0);
            LogFileHandling.createLogFile(joinSmsResponse_String, LOG_FILE_DIRECTORY, "joinSmsResponse",0);
            joinSmsResponse.body().close();
            MainFunctions.onTestFailure("JoinSms2Test");

        }


        if(responseHandling.getErrorCodeStatusJson(joinSmsResponse_String).equals("0")){
            ExReJoinSmsReport.pass(" Send Join SMS2 --- PASS");
            System.out.println(MainFunctions.BaseLogStringFunc()+" Send Join SMS2 --- PASS");
        }else{
            ExReJoinSmsReport.pass(" Send Join SMS2 --- FAIL");
            System.out.println(MainFunctions.BaseLogStringFunc()+" Send Join SMS2 --- FAIL");
            MainFunctions.onTestFailure("JoinSms2Test");

        }

    }//End JoinSms

    @Test(retryAnalyzer = RetryAnalyzer.class)

    public void MemberGetDetailsAndCodeTest () throws IOException {

        final String numbar = "123456789";

        memberGetDetailsAndCodeResponse = joinSmsTeatFunction.sendMemberGetDetailsAndCode(CELL_PHONE,numbar);
        memberGetDetailsAndCodeResponse_String = MainFunctions.convertOkHttpResponseToString(memberGetDetailsAndCodeResponse);
        //System.out.println(MainFunction.BaseLogStringFunc()+MainFunction.BaseLogStringFunc()+memberGetDetailsAndCodeResponse_String);

        if(!(memberGetDetailsAndCodeResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberGetDetailsAndCodeResponse_String).equals("0"))){
            System.out.println(MainFunctions.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + memberGetDetailsAndCodeResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsAndCodeResponse_String) + ")");
            ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + memberGetDetailsAndCodeResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsAndCodeResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(baseJSON.MEMBER_GET_DETAILS_AND_CODE_JSON), LOG_FILE_DIRECTORY, "MemberGetDetailsAndCodeTest",0);
            LogFileHandling.createLogFile(memberGetDetailsAndCodeResponse_String, LOG_FILE_DIRECTORY, "memberGetDetailsAndCodeResponse",0);
            memberGetDetailsAndCodeResponse.body().close();
            MainFunctions.onTestFailure("MemberGetDetailsAndCodeTest");

        }



        String str = responseHandling.getCode(memberGetDetailsAndCodeResponse_String);
        if (!(str).equals("")){
            System.out.println(MainFunctions.BaseLogStringFunc()+responseHandling.getCode(memberGetDetailsAndCodeResponse_String));
            ExReJoinSmsReport.pass("MemberGetDetailsAndCode Test --- PASS");
        }else{
            System.out.println(MainFunctions.BaseLogStringFunc()+"Cell phone "+numbar+" NOT found!");
            ExReJoinSmsReport.fail("MemberGetDetailsAndCode Test --- FAIL");
            ExReJoinSmsReport.fail("Cell phone "+numbar+" NOT found!").assignCategory("fail");
            MainFunctions.onTestFailure("MemberGetDetailsAndCodeTest");
        }

    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void MemberGetDetailsTest() throws IOException {
        baseJSON.ResatMemberGetDetails();
        String num ="0534320757";

        //flag = true
        memberGetDetailsResponse = joinSmsTeatFunction.sendMemberGetDetails(CELL_PHONE,num,"true","sendJoinSMS");
        memberGetDetailsResponse_String = MainFunctions.convertOkHttpResponseToString(memberGetDetailsResponse);
        if(!(memberGetDetailsResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String).equals("104"))){
            System.out.println(MainFunctions.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + memberGetDetailsResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String) + ")");
            ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + memberGetDetailsResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(baseJSON.MEMBER_GET_DETAILS_JSON), LOG_FILE_DIRECTORY, "MemberGetDetailsTest_flag=true",0);
            LogFileHandling.createLogFile(memberGetDetailsResponse_String, LOG_FILE_DIRECTORY, "MemberGetDetailsTestResponse_flag=true",0);
            memberGetDetailsResponse.body().close();
            MainFunctions.onTestFailure("MemberGetDetailsTest");

        }



         String str =responseHandling.getAdditionalInfo(memberGetDetailsResponse_String,"FieldValue");


         if(MainFunctions.SearchWordInString("true",str)==1 && MainFunctions.SearchWordInString(num,str)==1 ){
             System.out.println(MainFunctions.BaseLogStringFunc()+"MemberGetDetails (flag = true) --- PASS ");
             ExReJoinSmsReport.pass("MemberGetDetails (flag = true) --- PASS ");
         }else{
             System.out.println(MainFunctions.BaseLogStringFunc()+"MemberGetDetails (flag = true) --- FAIL ");
             ExReJoinSmsReport.pass("MemberGetDetails (flag = true) --- FAIL ").assignCategory("fail");
             MainFunctions.onTestFailure("MemberGetDetailsTest");

         }


         //flag = false
         baseJSON.ResatMemberGetDetails();
         baseJSON.MemberGetDetailsJSONCopy();
         memberGetDetailsResponse = joinSmsTeatFunction.sendMemberGetDetails(CELL_PHONE,num,"false","sendJoinSMS");
         memberGetDetailsResponse_String = MainFunctions.convertOkHttpResponseToString(memberGetDetailsResponse);

        if(!(memberGetDetailsResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String).equals("104"))){
            System.out.println(MainFunctions.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + memberGetDetailsResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String) + ")");
            ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + memberGetDetailsResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(baseJSON.MEMBER_GET_DETAILS_JSON), LOG_FILE_DIRECTORY, "MemberGetDetailsTest_flag=false",0);
            LogFileHandling.createLogFile(memberGetDetailsResponse_String, LOG_FILE_DIRECTORY, "memberGetDetailsResponse_flag=false",0);
            memberGetDetailsResponse.body().close();
            MainFunctions.onTestFailure("MemberGetDetailsTest");

        }
         //System.out.println(memberGetDetailsResponse_String);
         ExReJoinSmsReport.info(memberGetDetailsResponse_String);


         str =responseHandling.getAdditionalInfo(memberGetDetailsResponse_String,"FieldValue");
        if(MainFunctions.SearchWordInString("false",str)==1 && MainFunctions.SearchWordInString(num,str)==1 ){
            System.out.println(MainFunctions.BaseLogStringFunc()+"MemberGetDetails (flag = false) --- PASS ");
            ExReJoinSmsReport.pass("MemberGetDetails (flag = false) --- PASS ");
        }else{
            System.out.println(MainFunctions.BaseLogStringFunc()+"MemberGetDetails (flag = false) --- FAIL ");
            ExReJoinSmsReport.pass("MemberGetDetails (flag = false) --- FAIL ").assignCategory("fail");
            MainFunctions.onTestFailure("MemberGetDetailsTest");
        }




    }//End Test

}//End class
