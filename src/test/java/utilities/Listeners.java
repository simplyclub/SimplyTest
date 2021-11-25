package utilities;

import Tests.BasePage;
import com.aventstack.extentreports.*;
//import com.aventstack.extentreports.service.ExtentTestManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import okhttp3.OkHttpClient;
import org.testng.ITestContext ;
import org.testng.ITestListener ;
import org.testng.ITestResult ;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.util.concurrent.TimeUnit;


public class Listeners extends BasePage implements ITestListener  {

    @Override
    public void onFinish(ITestContext arg0) {
        MainFunction.RestGlobals();


    }

    @Override
    public void onStart(ITestContext arg0) {
       //System.out.println("*********************~~~~~~**********************");

    }
    @Override
    public void onTestSuccess(ITestResult arg0) {
        String str = arg0.getName();

        if (str.equals("subTotalTest")) {
            ExReApiTestReport.pass(arg0.getName() + " Test passed");
        }
        if (str.equals("tranEndTest")) {
            ExReAccumReport.pass(arg0.getName() + " Test passed");
        }
        if (str.equals("tranRefundTest")) {
            ExReTernRefundReport.pass(arg0.getName() + " Test passed");
        }
        if (str.equals("trenEndOnePhaseTest")) {
            ExReTrenEndOnePhaseReport.pass(arg0.getName() + " Test passed");
        }
        if (str.equals("newMemberTests")) {
            ExReNewMemberTestReport.pass(arg0.getName() + " Test passed");
        }
            if (str.equals("pointsValidityTest")) {
            ExRePointsValiditReport.pass(arg0.getName() + "Test passed");
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {


    }

    @Override
    public void onTestFailure(ITestResult arg0) {

        //System.out.println("~~~~~~~~~~~~!!!!@@@@@ "+arg0+" ~~~~~~~~~~~~!!!!@@@@@ ");

        String str = arg0.getName();

        if (str.equals("subTotalTest")) {
            ExReApiTestReport.fail(arg0.getName() + " Test fail");
        }
        if (str.equals("tranEndTest")) {
            ExReAccumReport.fail(arg0.getName() + " Test fail");
        }
        if (str.equals("tranRefundTest")) {
            ExReTernRefundReport.fail(arg0.getName() + " Test fail");
        }
        if (str.equals("trenEndOnePhaseTest")) {
            ExReTrenEndOnePhaseReport.fail(arg0.getName() + " Test fail");
        }
        if (str.equals("newMemberTests")) {
            ExReNewMemberTestReport.fail(arg0.getName() + " Test fail");
        }
        if (str.equals("pointsValidityTest")) {
            ExRePointsValiditReport.fail(arg0.getName() + "Test fail");
        }



    }

    @Override
    public void onTestSkipped(ITestResult arg0) {



    }




}






