package utilities;

import Tests.BasePage;
import Utilities.LogFileHandling;
//import com.aventstack.extentreports.service.ExtentTestManager;
import org.testng.ITestContext ;
import org.testng.ITestListener ;
import org.testng.ITestResult ;


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
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult arg0) {
        String str = arg0.getName();

        if (str.equals("subTotalTest")) {
            ExReApiTestReport.pass(arg0.getName() + " Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
            LogFileHandling.WriteToFile("EmailLog","    avgTimeSubTotal: "+ avgTimeSubTotal,HOME_DIRECTORY);
        }
        if (str.equals("tranEndTest")) {
            ExReAccumReport.pass(arg0.getName() + " Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
            LogFileHandling.WriteToFile("EmailLog","    avgTimeTranEnd: "+ avgTimeTrenEnd,HOME_DIRECTORY);
        }
        if (str.equals("tranRefundTest")) {
            ExReTernRefundReport.pass(arg0.getName() + " Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
            LogFileHandling.WriteToFile("EmailLog","    TimeTranRefund: "+ avgTimeTranRefund,HOME_DIRECTORY);
        }
        if (str.equals("tranEndOnePhaseTest")) {
            ExReTrenEndOnePhaseReport.pass(arg0.getName() + " Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
            LogFileHandling.WriteToFile("EmailLog","    TimeTrenEndOnePhase: "+ avgTimeTrenEndOnePhase,HOME_DIRECTORY);
        }
        if (str.equals("newMemberTests")) {
            ExReNewMemberTestReport.pass(arg0.getName() + " Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("pointsValidityTest")) {
            ExRePointsValiditReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("MemberTest1")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("MemberTest2")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("MemberTest3")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("MemberTest4")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
            LogFileHandling.WriteToFile("EmailLog","    avgTimeSubTotal NewMember: "+ MainFunction.getAvgTime(avgTimeSubTotal),HOME_DIRECTORY);
            LogFileHandling.WriteToFile("EmailLog","    avgTimeTrenEnd NewMember: "+ MainFunction.getAvgTime(avgTimeTrenEnd),HOME_DIRECTORY);
            LogFileHandling.WriteToFile("EmailLog","    avgTimeTrenEndOnePhase NewMember: "+ MainFunction.getAvgTime(avgTimeTrenEndOnePhase),HOME_DIRECTORY);

        }
        if (str.equals("MemberTest5")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("MemberTest6")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("MemberTest7")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("MemberTest8")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("JoinSms2Test")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("JoinSmsTest")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("MemberGetDetailsAndCodeTest")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("MemberGetDetailsTest")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal1Test")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal2Test")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal3Test")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal4Test")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal5Test")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal6Test")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("Stage7Deal1Test")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("Stage7Deal2Test")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("Stage7Deal3Test")) {
            ExReJoinSmsReport.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }
        if (str.equals("PosTest")) {
            ExReGeneralTests.pass(arg0.getName() + "Test passed");
            LogFileHandling.WriteToFile("EmailLog","V-"+arg0.getName() + " Test passed",HOME_DIRECTORY);
        }

    }


    @Override
    public void onTestFailure(ITestResult arg0) {

        String str = arg0.getName();

        if (str.equals("SubTotalTest")) {
            ExReApiTestReport.fail(arg0.getName() + " Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);


        }
        if (str.equals("tranEndTest")) {
            ExReAccumReport.fail(arg0.getName() + " Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);

        }
        if (str.equals("tranRefundTest")) {
            ExReTernRefundReport.fail(arg0.getName() + " Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);

        }
        if (str.equals("tranEndOnePhaseTest")) {
            ExReTrenEndOnePhaseReport.fail(arg0.getName() + " Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);

        }
        if (str.equals("newMemberTests")) {
            ExReNewMemberTestReport.fail(arg0.getName() + " Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);
        }
        if (str.equals("pointsValidityTest")) {
            ExRePointsValiditReport.fail(arg0.getName() + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);
        }
        if (str.equals("MemberGetDetailsAndCodeTest")) {
            ExRePointsValiditReport.fail(arg0.getName() + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal1Test")) {
            ExReStage6And7Report.fail(arg0.getName() + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal2Test")) {
            ExReStage6And7Report.fail(arg0.getName() + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal3Test")) {
            ExReStage6And7Report.fail(arg0.getName() + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal4Test")) {
            ExReStage6And7Report.fail(arg0.getName() + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal5Test")) {
            ExReStage6And7Report.fail(arg0.getName() + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);
        }
        if (str.equals("Stage6Deal6Test")) {
            ExReStage6And7Report.fail(arg0.getName() + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+arg0.getName() + " Test fail",HOME_DIRECTORY);
        }




    }

    @Override
    public void onTestSkipped(ITestResult arg0) {
        System.out.println("skip");



    }




}






