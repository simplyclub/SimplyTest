package Tests;

import JSON.ResponseHandling;
import Tests.TestFunctions.Stage6And7TestFunctions;
import Utilities.LogFileHandling;

import org.testng.SkipException;
import org.testng.annotations.Test;
import utilities.MainFunction;
import utilities.RetryAnalyzer;


import java.io.IOException;
import java.net.SocketTimeoutException;

import static utilities.MainFunction.BaseLogStringFunc;


public class Stage6And7Test extends BasePage {
    Stage6And7TestFunctions stage6And7TestFunctions = new Stage6And7TestFunctions();

    @Test(description = "inputFlug=1,Including a cash register sale")
    public void Stage6Deal1Test() throws IOException {

        trenEndOnePhaseResponse = stage6And7TestFunctions.makeTrenEndOnePhase(0,1);
        trenEndOnePhaseResponse_String = MainFunction.convertOkHttpResponseToString(trenEndOnePhaseResponse);

        if (!(trenEndOnePhaseResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String).equals("0"))) {
            System.out.println("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.fail("ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(baseJSON.tranEndOnePhaseToSend.toString(), LOG_FILE_DIRECTORY, "Stage6Deal1Test",0));
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(trenEndOnePhaseResponse_String, LOG_FILE_DIRECTORY, "Stage6Deal1Test",0));
            trenEndOnePhaseResponse.body().close();
            MainFunction.onTestFailure("Stage6Deal1Test");
            throw  new SkipException("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
        }
        trenEndOnePhaseResponse.body().close();

        System.out.println(BaseLogStringFunc()+trenEndOnePhaseResponse_String);

        transactionViewResponse = stage6And7TestFunctions.getTransactionView(trenEndOnePhaseResponse_String);
        System.out.println(BaseLogStringFunc()+transactionViewResponse.body().asString());

        if (!(transactionViewResponse.getStatusCode() == 200)) {
            System.out.println("ERROR xml--- status code is not 200 ");
            ExReStage6And7Report.fail("ERROR xml--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
            LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                    LOG_FILE_DIRECTORY, "XmlTransactionViewcall",0);
            LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse",0);
            throw  new SkipException("ERROR xml--- status code is not 200 ");


        }

        nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());

        int flag=0;
        for (int XRIndex = 0; XRIndex < (nodeList.getLength()); XRIndex++) {

            if (ResponseHandling.getXMLResponsePromoID(nodeList, XRIndex).equals("1905")){
                flag = 1;
                if(MainFunction.converToDoubleAsString(ResponseHandling.getXMLResponseAmount(nodeList, XRIndex)).equals("980.0")){
                    ExReStage6And7Report.pass("Stage6Deal1Test --- PASS");
                    System.out.println(BaseLogStringFunc()+"Stage6Deal1Test --- PASS");
                    break;
                }else{
                    ExReStage6And7Report.fail("Stage6Deal1Test(Amount) --- Fail");
                    ExReStage6And7Report.info("Amount 980 is not equal to transactionViewResponse promoId 1905 amount( "
                            +ResponseHandling.getXMLResponseAmount(nodeList, XRIndex)+" )");
                    System.out.println(BaseLogStringFunc()+"Stage6Deal1Test(Amount) --- Fail");
                    MainFunction.onTestFailure("Stage6Deal1Test");
                }

            }



        }//end for loop

        if(flag == 0){
            ExReStage6And7Report.fail("Stage6Deal1Test(PromoId) --- Fail");
            ExReStage6And7Report.info("promoID 1905 for promotion check, not found in transactionViewResponse");
            System.out.println(BaseLogStringFunc()+"Stage6Deal1Test(PromoId) --- Fail");
            MainFunction.onTestFailure("Stage6Deal1Test");
        }

    }//end test

    @Test(retryAnalyzer = RetryAnalyzer.class,description = "inputFlug=1,NOT Including a cash register sale")
    public void Stage6Deal2Test() throws IOException {
        trenEndOnePhaseResponse = stage6And7TestFunctions.makeTrenEndOnePhase(0,2);
        trenEndOnePhaseResponse_String = MainFunction.convertOkHttpResponseToString(trenEndOnePhaseResponse);

        if (!(trenEndOnePhaseResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String).equals("0"))) {
            System.out.println("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.fail("ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(baseJSON.tranEndOnePhaseToSend.toString(), LOG_FILE_DIRECTORY, "Stage6Deal2Test",0));
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(trenEndOnePhaseResponse_String, LOG_FILE_DIRECTORY, "Stage6Deal2Test",0));
            trenEndOnePhaseResponse.body().close();
            throw  new SkipException("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
        }
        trenEndOnePhaseResponse.body().close();

        System.out.println(BaseLogStringFunc()+trenEndOnePhaseResponse_String);

        transactionViewResponse = stage6And7TestFunctions.getTransactionView(trenEndOnePhaseResponse_String);
        System.out.println(BaseLogStringFunc()+transactionViewResponse.body().asString());

        if (!(transactionViewResponse.getStatusCode() == 200)) {
            System.out.println("ERROR xml--- status code is not 200 ");
            ExReStage6And7Report.fail("ERROR xml--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
            LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                    LOG_FILE_DIRECTORY, "XmlTransactionViewcall",0);
            LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse",0);
            throw  new SkipException("ERROR xml--- status code is not 200 ");


        }

        nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());

        for (int XRIndex = 0; XRIndex < (nodeList.getLength()); XRIndex++) {

            if (!(ResponseHandling.getXMLResponsePromoID(nodeList, XRIndex).equals("1906"))){
                ExReStage6And7Report.pass("Stage6Deal1Test --- PASS");
                System.out.println(BaseLogStringFunc()+"Stage6Deal1Test --- PASS");
                break;
                }else {
                    ExReStage6And7Report.fail("Stage6Deal2Test(PromoId) --- Fail");
                    ExReStage6And7Report.info("promoID 1906 for promotion check,  found in transactionViewResponse");
                    System.out.println(BaseLogStringFunc() + "Stage6Deal2Test(PromoId) --- Fail");
                    MainFunction.onTestFailure("Stage6Deal2Test");

                 }
        }//end for loop

    }//end test

    @Test(retryAnalyzer = RetryAnalyzer.class,description = "inputFlug=2,Including a POS deposit")
    public void Stage6Deal3Test() throws IOException {
        trenEndOnePhaseResponse = stage6And7TestFunctions.makeTrenEndOnePhase(0,3);
        trenEndOnePhaseResponse_String = MainFunction.convertOkHttpResponseToString(trenEndOnePhaseResponse);

        if (!(trenEndOnePhaseResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String).equals("0"))) {
            System.out.println("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.fail("ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(baseJSON.tranEndOnePhaseToSend.toString(), LOG_FILE_DIRECTORY, "Stage6Deal2Test",0));
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(trenEndOnePhaseResponse_String, LOG_FILE_DIRECTORY, "Stage6Deal2Test",0));
            trenEndOnePhaseResponse.body().close();
            throw  new SkipException("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
        }
        trenEndOnePhaseResponse.body().close();

        System.out.println(BaseLogStringFunc()+trenEndOnePhaseResponse_String);

        transactionViewResponse = stage6And7TestFunctions.getTransactionView(trenEndOnePhaseResponse_String);
        System.out.println(BaseLogStringFunc()+transactionViewResponse.body().asString());

        if (!(transactionViewResponse.getStatusCode() == 200)) {
            System.out.println("ERROR xml--- status code is not 200 ");
            ExReStage6And7Report.fail("ERROR xml--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
            LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                    LOG_FILE_DIRECTORY, "XmlTransactionViewcall",0);
            LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse",0);
            throw  new SkipException("ERROR xml--- status code is not 200 ");


        }

        nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());

        int flag=0;
        for (int XRIndex = 0; XRIndex < (nodeList.getLength()); XRIndex++) {

            if (ResponseHandling.getXMLResponsePromoID(nodeList, XRIndex).equals("1907")){
                flag = 1;
                if(MainFunction.converToDoubleAsString(ResponseHandling.getXMLResponseAmount(nodeList, XRIndex)).equals("430.0")){
                    ExReStage6And7Report.pass("Stage6Deal3Test --- PASS");
                    System.out.println(BaseLogStringFunc()+"Stage6Deal3Test --- PASS");
                    break;
                }else{
                    ExReStage6And7Report.fail("Stage6Deal3Test(Amount) --- Fail");
                    ExReStage6And7Report.info("Amount 430 is not equal to transactionViewResponse promoId 1907 amount( "
                            +ResponseHandling.getXMLResponseAmount(nodeList, XRIndex)+" )");
                    System.out.println(BaseLogStringFunc()+"Stage6Deal3Test(Amount) --- Fail");
                    MainFunction.onTestFailure("Stage6Deal3Test");
                }

            }

        }//end for loop

        if(flag == 0){
            ExReStage6And7Report.fail("Stage6Deal3Test(PromoId) --- Fail");
            ExReStage6And7Report.info("promoID 1907 for promotion check, not found in transactionViewResponse");
            System.out.println(BaseLogStringFunc()+"Stage6Deal3Test(PromoId) --- Fail");
            MainFunction.onTestFailure("Stage6Deal3Test");
        }

    }//end test

    @Test(retryAnalyzer = RetryAnalyzer.class,description = "inputFlug=2,NOT Including a POS deposit")
    public void Stage6Deal4Test() throws IOException {
        trenEndOnePhaseResponse = stage6And7TestFunctions.makeTrenEndOnePhase(0,4);
        trenEndOnePhaseResponse_String = MainFunction.convertOkHttpResponseToString(trenEndOnePhaseResponse);

        if (!(trenEndOnePhaseResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String).equals("0"))) {
            System.out.println("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.fail("ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(baseJSON.tranEndOnePhaseToSend.toString(), LOG_FILE_DIRECTORY, "Stage6Deal4Test",0));
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(trenEndOnePhaseResponse_String, LOG_FILE_DIRECTORY, "Stage6Deal4Test",0));
            trenEndOnePhaseResponse.body().close();
            throw  new SkipException("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
        }

        trenEndOnePhaseResponse.body().close();

        System.out.println(BaseLogStringFunc()+trenEndOnePhaseResponse_String);

        transactionViewResponse = stage6And7TestFunctions.getTransactionView(trenEndOnePhaseResponse_String);
        System.out.println(BaseLogStringFunc()+transactionViewResponse.body().asString());

        if (!(transactionViewResponse.getStatusCode() == 200)) {
            System.out.println("ERROR xml--- status code is not 200 ");
            ExReStage6And7Report.fail("ERROR xml--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
            LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                    LOG_FILE_DIRECTORY, "XmlTransactionViewcall",0);
            LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse",0);
            throw  new SkipException("ERROR xml--- status code is not 200 ");


        }

        nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());

        for (int XRIndex = 0; XRIndex < (nodeList.getLength()); XRIndex++) {

            if (!(ResponseHandling.getXMLResponsePromoID(nodeList, XRIndex).equals("1908"))){
                ExReStage6And7Report.pass("Stage6Deal4Test --- PASS");
                System.out.println(BaseLogStringFunc()+"Stage6Deal4Test --- PASS");
                break;
            }else {
                ExReStage6And7Report.fail("Stage6Deal4Test(PromoId) --- Fail");
                ExReStage6And7Report.info("promoID 1908 for promotion check,  found in transactionViewResponse");
                System.out.println(BaseLogStringFunc() + "Stage6Deal4Test(PromoId) --- Fail");
                MainFunction.onTestFailure("Stage6Deal4Test");

            }
        }//end for loop

    }//end test

    @Test(retryAnalyzer = RetryAnalyzer.class,description = "inputFlug=3,Including a POS deposit + a cash register sale")
    public void Stage6Deal5Test() throws IOException {
        trenEndOnePhaseResponse = stage6And7TestFunctions.makeTrenEndOnePhase(0,5);
        trenEndOnePhaseResponse_String = MainFunction.convertOkHttpResponseToString(trenEndOnePhaseResponse);

        if (!(trenEndOnePhaseResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String).equals("0"))) {
            System.out.println("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.fail("ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(baseJSON.tranEndOnePhaseToSend.toString(), LOG_FILE_DIRECTORY, "Stage6Deal5Test",0));
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(trenEndOnePhaseResponse_String, LOG_FILE_DIRECTORY, "Stage6Deal5Test",0));
            trenEndOnePhaseResponse.body().close();
            throw  new SkipException("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
        }
        trenEndOnePhaseResponse.body().close();

        System.out.println(BaseLogStringFunc()+trenEndOnePhaseResponse_String);

        transactionViewResponse = stage6And7TestFunctions.getTransactionView(trenEndOnePhaseResponse_String);
        System.out.println(BaseLogStringFunc()+transactionViewResponse.body().asString());

        if (!(transactionViewResponse.getStatusCode() == 200)) {
            System.out.println("ERROR xml--- status code is not 200 ");
            ExReStage6And7Report.fail("ERROR xml--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
            LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                    LOG_FILE_DIRECTORY, "XmlTransactionViewcall",0);
            LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse",0);
            throw  new SkipException("ERROR xml--- status code is not 200 ");


        }

        nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());

        int flag=0;
        for (int XRIndex = 0; XRIndex < (nodeList.getLength()); XRIndex++) {

            if (ResponseHandling.getXMLResponsePromoID(nodeList, XRIndex).equals("1910")){
                flag = 1;
                if(MainFunction.converToDoubleAsString(ResponseHandling.getXMLResponseAmount(nodeList, XRIndex)).equals("760.0")){
                    ExReStage6And7Report.pass("Stage6Deal5Test --- PASS");
                    System.out.println(BaseLogStringFunc()+"Stage6Deal5Test --- PASS");
                    break;
                }else{
                    ExReStage6And7Report.fail("Stage6Deal5Test(Amount) --- Fail");
                    ExReStage6And7Report.info("Amount 760 is not equal to transactionViewResponse promoId 1910 amount( "
                            +ResponseHandling.getXMLResponseAmount(nodeList, XRIndex)+" )");
                    System.out.println(BaseLogStringFunc()+"Stage6Deal5Test(Amount) --- Fail");
                    MainFunction.onTestFailure("Stage6Deal5Test");
                }

            }

        }//end for loop

        if(flag == 0){
            ExReStage6And7Report.fail("Stage6Deal5Test(PromoId) --- Fail");
            ExReStage6And7Report.info("promoID 1907 for promotion check, not found in transactionViewResponse");
            System.out.println(BaseLogStringFunc()+"Stage6Deal3Test(PromoId) --- Fail");
            MainFunction.onTestFailure("Stage6Deal3Test");
        }

    }//end test

    @Test(retryAnalyzer = RetryAnalyzer.class,description = "inputFlug=3,NOT Including a POS deposit + a cash register sale")
    public void Stage6Deal6Test() throws IOException {
        trenEndOnePhaseResponse = stage6And7TestFunctions.makeTrenEndOnePhase(0,6);
        trenEndOnePhaseResponse_String = MainFunction.convertOkHttpResponseToString(trenEndOnePhaseResponse);

        if (!(trenEndOnePhaseResponse.code() == 200 && responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String).equals("0"))) {
            System.out.println("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.fail("ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(baseJSON.tranEndOnePhaseToSend.toString(), LOG_FILE_DIRECTORY, "Stage6Deal4Test",0));
            ExReStage6And7Report.info(
                    LogFileHandling.createLogFile(trenEndOnePhaseResponse_String, LOG_FILE_DIRECTORY, "Stage6Deal4Test",0));
            trenEndOnePhaseResponse.body().close();
            throw  new SkipException("***ERROR --- status code is not 200" + "(" + trenEndOnePhaseResponse.code() + ")" + " or ErrorCodeStatus is not 0 " + "(" +
                    responseHandling.getErrorCodeStatusJson(trenEndOnePhaseResponse_String) + ")");

        }

        trenEndOnePhaseResponse.body().close();

        System.out.println(BaseLogStringFunc()+trenEndOnePhaseResponse_String);

        transactionViewResponse = stage6And7TestFunctions.getTransactionView(trenEndOnePhaseResponse_String);
        System.out.println(BaseLogStringFunc()+transactionViewResponse.body().asString());

        if (!(transactionViewResponse.getStatusCode() == 200)) {
            System.out.println("ERROR xml--- status code is not 200 ");
            ExReStage6And7Report.fail("ERROR xml--- status code is not 200" + "(" + transactionViewResponse.getStatusCode() + ")");
            LogFileHandling.createLogFile(baseXML.convertXMLToString(baseXML.convertXMLFileToXMLDocument(baseXML.GET_TREN_FILE_LOCATION)),
                    LOG_FILE_DIRECTORY, "XmlTransactionViewcall",0);
            LogFileHandling.createLogFile(transactionViewResponse.asString(), LOG_FILE_DIRECTORY, "XmlTransactionViewResponse",0);
            throw  new SkipException("ERROR xml--- status code is not 200 ");


        }

        nodeList = responseHandling.getXMLFileTranViewDiscountData(transactionViewResponse.getBody().asString());

        for (int XRIndex = 0; XRIndex < (nodeList.getLength()); XRIndex++) {

            if (!(ResponseHandling.getXMLResponsePromoID(nodeList, XRIndex).equals("1911"))){
                ExReStage6And7Report.pass("Stage6Deal6Test --- PASS");
                System.out.println(BaseLogStringFunc()+"Stage6Deal6Test --- PASS");
                break;
            }else {
                ExReStage6And7Report.fail("Stage6Deal6Test(PromoId) --- Fail");
                ExReStage6And7Report.info("promoID 1911 for promotion check,  found in transactionViewResponse");
                System.out.println(BaseLogStringFunc() + "Stage6Deal6Test(PromoId) --- Fail");
                MainFunction.onTestFailure("Stage6Deal6Test");

            }
        }//end for loop

    }//end test

    @Test(retryAnalyzer = RetryAnalyzer.class,description = "make subTotal and check if the promoId is ok")
    public void Stage7Deal1Test() throws IOException {

        if(stage6And7TestFunctions.subTotalPromoIdCheck()){
            ExReStage6And7Report.pass("subTotalPromoIdCheck ---- PASS");
            System.out.println("subTotalPromoIdCheck ---- PASS");
        }else{
            ExReStage6And7Report.fail("subTotalPromoIdCheck ---- Fail ");
            System.out.println("subTotalPromoIdCheck ---- Fail ");
            ExReStage6And7Report.info("one of the PromoId in the TestJSON is not found in subTotal response");
            MainFunction.onTestFailure("Stage7Deal1Test");

//            SkipException skipException =  new SkipException("one of the PromoId in the TestJSON is not found in subTotal response");
//            System.out.println(MainFunction.BaseLogStringFunc()+skipException.getLocalizedMessage());

        }

    }//end test

    @Test(retryAnalyzer = RetryAnalyzer.class,description = "make false subTotal and check if there is nun of the promo")
    public void Stage7Deal2Test() throws IOException{


        if(stage6And7TestFunctions.falseSubTotalCheck()){
            ExReStage6And7Report.pass("Stage7Deal2Test ---- PASS");
            System.out.println("Stage7Deal2Test ---- PASS");
        }else{
            ExReStage6And7Report.fail("Stage7Deal2Test ---- Fail ");
            System.out.println("Stage7Deal2Test ---- Fail ");
            ExReStage6And7Report.info("one of the PromoId in the TestJSON is  found in subTotal response");
            MainFunction.onTestFailure("Stage7Deal2Test");

            //SkipException skipException =  new SkipException("one of the PromoId in the TestJSON is  found in subTotal response");
            //System.out.println(MainFunction.BaseLogStringFunc()+skipException.getLocalizedMessage());

        }

    }//end test

    @Test(retryAnalyzer = RetryAnalyzer.class,description = "",enabled = false)
    public void Stage7Deal3Test() throws IOException{
        try {
            subTotalResponse = stage6And7TestFunctions.makeSubTotal(0, 0);
            subTotalResponse_String = MainFunction.convertOkHttpResponseToString(subTotalResponse);
        }catch (SocketTimeoutException e){
        ExReStage6And7Report.warning("ERROR(subTotalResponse)---- Socket Timeout Exception  ");
        System.out.println(MainFunction.BaseLogStringFunc() + "ERROR(subTotalResponse)---- Socket Timeout Exception  ");
        throw new SocketTimeoutException();

        } catch ( NullPointerException e) {
            ExReStage6And7Report.warning("ERROR(subTotalPromoIdCheck)---- Null lPointer Exceptionn  ");
        System.out.println(MainFunction.BaseLogStringFunc() + "ERROR(subTotalPromoIdCheck)---- Null Pointer Exception  ");
        throw new NullPointerException();

    }
        try {
            trenEndResponse = stage6And7TestFunctions.makeTranEnd(0, subTotalResponse_String, 0);
            trenEndResponse_String = MainFunction.convertOkHttpResponseToString(trenEndResponse);
        }catch (SocketTimeoutException e){
            ExReStage6And7Report.warning("ERROR(trenEndResponse)---- Socket Timeout Exception  ");
            System.out.println(MainFunction.BaseLogStringFunc() + "ERROR(trenEndResponse)---- Socket Timeout Exception  ");
            throw new SocketTimeoutException();

        } catch ( NullPointerException e) {
            ExReStage6And7Report.warning("ERROR(trenEndResponse)---- Null lPointer Exceptionn  ");
            System.out.println(MainFunction.BaseLogStringFunc() + "ERROR(trenEndResponse)---- Null Pointer Exception  ");
            throw new NullPointerException();

        }


    }//end test


}//end class
