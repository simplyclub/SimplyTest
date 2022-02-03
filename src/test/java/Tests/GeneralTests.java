package Tests;

import Tests.TestFunctions.GeneralTestsFunctions;
import org.testng.annotations.Test;
import utilities.MainFunctions;
import utilities.RetryAnalyzer;

import java.io.IOException;

public class GeneralTests extends BasePage {
    GeneralTestsFunctions generalTestsFunctions = new  GeneralTestsFunctions() ;


    @Test(retryAnalyzer = RetryAnalyzer.class,description = "")
    public void PosTest () throws IOException {

        if(generalTestsFunctions.subTotalCheck()){
            if(!generalTestsFunctions.tranEndCheck()){
                System.out.println("PosTest(tranEndCheck) --- fail");
                ExReGeneralTests.fail("PosTest(tranEndCheck) --- fail");
                MainFunctions.onTestFailure("PosTest");
            }
        }else{
            System.out.println("PosTest(subTotalCheck) --- fail");
            ExReGeneralTests.fail("PosTest(subTotalCheck) --- fail");
            MainFunctions.onTestFailure("PosTest");
        }







    }
}
