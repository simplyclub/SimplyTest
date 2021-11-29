package Tests;

import BaseClass.BaseJSON;
import JSON.ResponseHandling;
import Tests.TestFunctions.JoinSmsTeatFunction;
import Utilities.LogFileHandling;
import com.sun.org.glassfish.gmbal.Description;
import org.testng.annotations.Test;
import utilities.MainFunction;
import utilities.RetryAnalyzer;

import javax.swing.text.Element;
import java.io.IOException;

public class JoinSmsTest extends  BasePage{
    JoinSmsTeatFunction joinSmsTeatFunction = new JoinSmsTeatFunction();
    ResponseHandling responseHandling = new ResponseHandling();

    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Description("")
    public void JoinSms () throws IOException, InterruptedException {


        joinSmsResponse = joinSmsTeatFunction.sendJoinSms(CELL_PHONE,Equal,"0534320757");
        joinSmsResponse_String = MainFunction.convertOkHttpResponseToString(joinSmsResponse);
        //System.out.println(MainFunction.BaseLogStringFunc()+joinSmsResponse_String);

        if(!(joinSmsResponse.code() == 200 && responseHandling.getResultCode(joinSmsResponse_String).equals("0"))){
            System.out.println(MainFunction.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + joinSmsResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                    responseHandling.getErrorCodeStatusJson(joinSmsResponse_String) + ")");
            ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + joinSmsResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(joinSmsResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(BaseJSON.SEND_JOIN_SMS_JSON), LOG_FILE_DIRECTORY, "memberAdd",0);
            LogFileHandling.createLogFile(joinSmsResponse_String, LOG_FILE_DIRECTORY, "memberAdd",0);
            joinSmsResponse.body().close();

        }


        if(responseHandling.getResultCode(joinSmsResponse_String).equals("0")){
            ExReJoinSmsReport.pass(" Send Join SMS --- PASS");
            System.out.println(MainFunction.BaseLogStringFunc()+" Send Join SMS2 --- PASS");
        }




    }//End JoinSms

    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Description("")
    public void JoinSms2 () throws IOException, InterruptedException {
        Thread.sleep(10000);
        joinSmsResponse = joinSmsTeatFunction.sendJoinSms2(CELL_PHONE,Equal,"0534320757");
        joinSmsResponse_String = MainFunction.convertOkHttpResponseToString(joinSmsResponse);
        //System.out.println(MainFunction.BaseLogStringFunc()+joinSmsResponse_String);

        if(!(joinSmsResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(joinSmsResponse_String).equals("0"))){
            System.out.println(MainFunction.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + joinSmsResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                    responseHandling.getErrorCodeStatusJson(joinSmsResponse_String) + ")");
            ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + joinSmsResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(joinSmsResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(BaseJSON.SEND_JOIN_SMS_JSON), LOG_FILE_DIRECTORY, "memberAdd",0);
            LogFileHandling.createLogFile(joinSmsResponse_String, LOG_FILE_DIRECTORY, "memberAdd",0);
            joinSmsResponse.body().close();

        }


        if(responseHandling.getErrorCodeStatusJson(joinSmsResponse_String).equals("0")){
            ExReJoinSmsReport.pass(" Send Join SMS2 --- PASS");
            System.out.println(MainFunction.BaseLogStringFunc()+" Send Join SMS2 --- PASS");


        }

    }//End JoinSms

    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Description("|")
    public void MemberGetDetailsAndCodeTest () throws IOException {

        final String numbar = "123456789";

        memberGetDetailsAndCodeResponse = joinSmsTeatFunction.sendMemberGetDetailsAndCode(CELL_PHONE,numbar);
        memberGetDetailsAndCodeResponse_String = MainFunction.convertOkHttpResponseToString(memberGetDetailsAndCodeResponse);
        //System.out.println(MainFunction.BaseLogStringFunc()+MainFunction.BaseLogStringFunc()+memberGetDetailsAndCodeResponse_String);

        if(!(memberGetDetailsAndCodeResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberGetDetailsAndCodeResponse_String).equals("0"))){
            System.out.println(MainFunction.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + memberGetDetailsAndCodeResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsAndCodeResponse_String) + ")");
            ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + memberGetDetailsAndCodeResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsAndCodeResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(BaseJSON.MEMBER_GET_DETAILS_AND_CODE_JSON), LOG_FILE_DIRECTORY, "memberAdd",0);
            LogFileHandling.createLogFile(memberGetDetailsAndCodeResponse_String, LOG_FILE_DIRECTORY, "memberAdd",0);
            memberGetDetailsAndCodeResponse.body().close();

        }



        String str = responseHandling.getCode(memberGetDetailsAndCodeResponse_String);
        if (!(str).equals("")){
            System.out.println(MainFunction.BaseLogStringFunc()+responseHandling.getCode(memberGetDetailsAndCodeResponse_String));
            ExReJoinSmsReport.pass("MemberGetDetailsAndCode Test --- PASS");
        }else{
            System.out.println(MainFunction.BaseLogStringFunc()+"Cell phone "+numbar+" NOT found!");
            ExReJoinSmsReport.fail("MemberGetDetailsAndCode Test --- FAIL");
            ExReJoinSmsReport.fail("Cell phone "+numbar+" NOT found!").assignCategory("fail");
        }

    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    @Description("|")
    public void MemberGetDetails() throws IOException {
        baseJSON.ResatMemberGetDetails();
        String num ="0534320757";

        //flag = true
        memberGetDetailsResponse = joinSmsTeatFunction.sendMemberGetDetails(CELL_PHONE,num,"true","sendJoinSMS");
        memberGetDetailsResponse_String = MainFunction.convertOkHttpResponseToString(memberGetDetailsResponse);
        if(!(memberGetDetailsResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String).equals("104"))){
            System.out.println(MainFunction.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + memberGetDetailsResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String) + ")");
            ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + memberGetDetailsResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(BaseJSON.MEMBER_GET_DETAILS_JSON), LOG_FILE_DIRECTORY, "memberAdd",0);
            LogFileHandling.createLogFile(memberGetDetailsResponse_String, LOG_FILE_DIRECTORY, "memberAdd",0);
            memberGetDetailsResponse.body().close();

        }



         String str =responseHandling.getAdditionalInfo(memberGetDetailsResponse_String,"FieldValue");


         if(MainFunction.SearchWordInString("true",str)==1 && MainFunction.SearchWordInString(num,str)==1 ){
             System.out.println(MainFunction.BaseLogStringFunc()+"MemberGetDetails (flag = true) --- PASS ");
             ExReJoinSmsReport.pass("MemberGetDetails (flag = true) --- PASS ");
         }else{
             System.out.println(MainFunction.BaseLogStringFunc()+"MemberGetDetails (flag = true) --- FAIL ");
             ExReJoinSmsReport.pass("MemberGetDetails (flag = true) --- FAIL ").assignCategory("fail");

         }


         //flag = false
         baseJSON.ResatMemberGetDetails();
         baseJSON.MemberGetDetailsJSONCopy();
         memberGetDetailsResponse = joinSmsTeatFunction.sendMemberGetDetails(CELL_PHONE,num,"false","sendJoinSMS");
         memberGetDetailsResponse_String = MainFunction.convertOkHttpResponseToString(memberGetDetailsResponse);
        if(!(memberGetDetailsResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String).equals("104"))){
            System.out.println(MainFunction.BaseLogStringFunc()+"*ERROR --- status code is not 200" + "(" + memberGetDetailsResponse.code() + ")" + "or ErrorCodeStatus is not 0" + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String) + ")");
            ExReNewMemberTestReport.fail("ERROR --- status code is not 200" + "(" + memberGetDetailsResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(memberGetDetailsResponse_String) + ")");
            LogFileHandling.createLogFile(baseJSON.getString(BaseJSON.MEMBER_GET_DETAILS_JSON), LOG_FILE_DIRECTORY, "memberAdd",0);
            LogFileHandling.createLogFile(memberGetDetailsResponse_String, LOG_FILE_DIRECTORY, "memberAdd",0);
            memberGetDetailsResponse.body().close();

        }
         //System.out.println(memberGetDetailsResponse_String);
         ExReJoinSmsReport.info(memberGetDetailsResponse_String);


         str =responseHandling.getAdditionalInfo(memberGetDetailsResponse_String,"FieldValue");
        if(MainFunction.SearchWordInString("false",str)==1 && MainFunction.SearchWordInString(num,str)==1 ){
            System.out.println(MainFunction.BaseLogStringFunc()+"MemberGetDetails (flag = false) --- PASS ");
            ExReJoinSmsReport.pass("MemberGetDetails (flag = false) --- PASS ");
        }else{
            System.out.println(MainFunction.BaseLogStringFunc()+"MemberGetDetails (flag = false) --- FAIL ");
            ExReJoinSmsReport.pass("MemberGetDetails (flag = false) --- FAIL ").assignCategory("fail");
        }




    }//End Test

}//End class
