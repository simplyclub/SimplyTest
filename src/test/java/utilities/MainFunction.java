package utilities;

import Tests.BasePage;
import Utilities.LogFileHandling;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Utf8;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class MainFunction extends BasePage {

    public static void openReport() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[] {"enable-automation"});
        options.addArguments("--disable-notifications");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().fullscreen();
        String dir = System.getProperty("user.dir");
        driver.get("file:///" + dir + "\\nullExtentReportResults.html");
    }

    public static void extentReportInit() throws IOException {
        spark.config().setEncoding("UTF-8");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Automation report");
        spark.config().setReportName("Simply Tests");
        BasePage.exReport.attachReporter(spark);
//        spark.loadXMLConfig(new File("C:\\Users\\User\\IdeaProjects\\SimplyTest\\extentconfig.xml"));


    }
    public static void convertFileEncoding(String sourcePath, String targetPath , String targetEncoding) throws IOException, InterruptedException{

        // Wait for file to exist - 10 seconds
        for (int i=0; i<20 ; i++) {
            if(!Files.exists(Paths.get(sourcePath)))
                Thread.sleep(500);
            else
                break;
        }

        File infile = new File(sourcePath);

       // String trgtPath = targetPath.orElse(sourcePath + "Temp") ;
        String trgtPath = targetPath+"Temp";
        String trgtEncoding = targetEncoding;
        File outfile = new File(trgtPath);

        // Convert
        InputStreamReader fis = new InputStreamReader(new FileInputStream(infile));
        Reader in = new InputStreamReader(new FileInputStream(infile),fis.getEncoding());
        Writer out = new OutputStreamWriter(new FileOutputStream(outfile), trgtEncoding);
        int c;
        while ((c = in.read()) != -1){
            out.write(c);}
        in.close();
        out.close();
        fis.close();
        // if target path not specified - change the original file to target file
        if (true) {
            infile.delete();
            outfile.renameTo(new File(sourcePath));
        }


    }
    //temp


    public static void RestGlobals(){
        preDeal.clear();
        postDeal.clear();
        sumDealPoints.clear();
        sumDealToUsePoints.clear();
        sumBurnd.clear();
        sumVal = 0.0;

    }

    public static void RestTimeGlobals(){
        avgTimeSubTotal.clear();
        avgTimeTrenEnd.clear();
        avgTimeTrenEndOnePhase.clear();

    }

    public static Node[] convertNodeListToNodeArray(NodeList list){
        int length = list.getLength();
        Node[] copy = new Node[length];

        for (int n = 0; n < length; ++n)
            copy[n] = list.item(n);

        return copy;
    }

    public static String converToDoubleAsString(String str ){

        double d = Double.parseDouble(str);
        DecimalFormat df = new DecimalFormat("#.##");
        String dx = df.format(d);
        d = Double.valueOf(dx);

        return Double.toString(d);

    }

    public static String convertOkHttpResponseToString(Response response) throws IOException {
        ResponseBody body = response.peekBody(Long.MAX_VALUE);
        String content = body.string();
        body.close();
        body.string();
        return content;
    }

    /**
     * This function, calculates the average times for any given Time array
     * @param arrayList avg Time Array
     * @return avg
     */
    public static int getAvgTime(ArrayList arrayList){
        int avg=0;


        for (int s=0 ; s < arrayList.size();s++){
            //System.out.println("s "+s);
           avg =  Math.toIntExact((Long) arrayList.get(s))+ avg;

        }

        return avg / arrayList.size();
    }

    public static int SearchWordInString (String WordToSearch , String str) {
            String strOrig = str ;

        int intIndex = strOrig.indexOf(WordToSearch);

            if(intIndex == -1 ) {
                System.out.println(BaseLogStringFunc()+WordToSearch +" not found in string");
                return 0;
            } else {
                System.out.println(BaseLogStringFunc()+"Found "+WordToSearch+ " in the String");
                return 1;
            }
    }

    public static String RandomNumber(){
        Random rand = new Random(); //instance of random class
        int upperbound = 9999999;
        //generate random values from 0-9999999
        int int_random = rand.nextInt(upperbound);
        String int_random_string = Integer.toString(int_random);

        return int_random_string;
    }

    public static String BaseLogStringFunc(){
         SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
         Date date = new Date();
           String BaseLogString = "[Log "+formatter.format(date)+" ] : ";
           return BaseLogString;

    }



    public static void onTestFailure(String str) {

        //String str = arg0.getName();

        if (str.equals("subTotalTest")) {
            ExReApiTestReport.fail(str + " Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");

        }
        if (str.equals("tranEndTest")) {
            ExReAccumReport.fail(str + " Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");

        }
        if (str.equals("tranRefundTest")) {
            ExReTernRefundReport.fail(str + " Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");

        }
        if (str.equals("trenEndOnePhaseTest")) {
            ExReTrenEndOnePhaseReport.fail(str + " Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");

        }
        if (str.equals("newMemberTests")) {
            ExReNewMemberTestReport.fail(str + " Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("pointsValidityTest")) {
            ExRePointsValiditReport.fail(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("MemberGetDetailsAndCodeTest")) {
            ExRePointsValiditReport.fail(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("Stage6Deal1Test")) {
            ExRePointsValiditReport.fail(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("Stage6Deal2Test")) {
            ExRePointsValiditReport.fail(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("Stage6Deal3Test")) {
            ExRePointsValiditReport.fail(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("Stage6Deal4Test")) {
            ExRePointsValiditReport.fail(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("Stage6Deal5Test")) {
            ExRePointsValiditReport.fail(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("Stage6Deal6Test")) {
            ExRePointsValiditReport.fail(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("MemberTest1")) {
            ExReJoinSmsReport.pass(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("MemberTest2")) {
            ExReJoinSmsReport.pass(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("MemberTest3")) {
            ExReJoinSmsReport.pass(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("MemberTest4")) {
            ExReJoinSmsReport.pass(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("MemberTest5")) {
            ExReJoinSmsReport.pass(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("MemberTest6")) {
            ExReJoinSmsReport.pass(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("MemberTest7")) {
            ExReJoinSmsReport.pass(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("MemberTest8")) {
            ExReJoinSmsReport.pass(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("JoinSms2Test")) {
            ExReJoinSmsReport.pass(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }
        if (str.equals("JoinSmsTest")) {
            ExReJoinSmsReport.pass(str + "Test fail");
            LogFileHandling.WriteToFile("EmailLog","X-"+str + " Test fail");
        }



    }



}//End Class



