package Tests.TestFunctions;

import org.w3c.dom.NodeList;

public class PointsValidityCases extends PointsValidityFunctions {
    PointsValidityFunctions pointsValidityFunctions = new PointsValidityFunctions();

    public void pointsValidityCases(NodeList nodeList , int NLIndex, int x ,int i  ) {

        switch (x) {
            case 36 :
                //from: Immediately , Until: Several days
                if(pointsValidityFunctions.calcDaysUntilEnd(nodeList,NLIndex,i) == 1){
                    ExRePointsValiditReport.info("DaysUntilEnd");
                    ExRePointsValiditReport.pass("Deal : "+(i+1)+" case "+ x +" pass");
                    ExRePointsValiditReport.info("from: Immediately , Until: Several days");
                }else{
                    ExRePointsValiditReport.info("DaysUntilEnd");
                    ExRePointsValiditReport.fail("Deal :"+(i+1)+" case "+ x +" fail");
                    ExRePointsValiditReport.info("from: Immediately , Until: Several days");
                }

                break;

            case 2 :
                //from: Several days  , Until: Unlimited
                if (pointsValidityFunctions.calcDaysUntilStart(nodeList,NLIndex,i) == 1){
                    ExRePointsValiditReport.info("DaysUntilStart");
                    ExRePointsValiditReport.pass("Deal : "+(i+1)+" case "+ x +" pass");
                    ExRePointsValiditReport.info("from: Several days  , Until: Unlimited");
                }else{
                    ExRePointsValiditReport.info("DaysUntilStart");
                    ExRePointsValiditReport.fail("Deal :"+(i+1)+" case "+ x +" fail");
                    ExRePointsValiditReport.info("from: Several days  , Until: Unlimited");

                }
                break;


            case 32 :
                //from: Several days  , Until: Several days
                if (pointsValidityFunctions.calcDaysUntilStart(nodeList,NLIndex,i) == 1){
                    ExRePointsValiditReport.info("DaysUntilStart");
                    ExRePointsValiditReport.pass("Deal : "+(i+1)+" case "+ x +" pass");
                    ExRePointsValiditReport.info("from: Several days  , Until: Several days");
                }else{
                    ExRePointsValiditReport.info("DaysUntilStart");
                    ExRePointsValiditReport.fail("Deal :"+(i+1)+" case "+ x +" fail");
                    ExRePointsValiditReport.info("from: Several days  , Until: Several days");
                }


                if(pointsValidityFunctions.calcDaysUntilEnd(nodeList,NLIndex,i) == 1){
                    ExRePointsValiditReport.info("DaysUntilEnd");
                    ExRePointsValiditReport.pass("Deal : "+(i+1)+" case "+ x +" pass");
                    ExRePointsValiditReport.info("from: Several days  , Until: Several days");
                }else{
                    ExRePointsValiditReport.info("DaysUntilEnd");
                    ExRePointsValiditReport.fail("Deal :"+(i+1)+" case "+ x +" fail");
                    ExRePointsValiditReport.info("from: Several days  , Until: Several days");
                }

                break;

            case 47 :
                //from: Next deal , Until: Several months

               if (pointsValidityFunctions.calcDaysUntilStart(nodeList,NLIndex,i) == 1){
                   ExRePointsValiditReport.info("DaysUntilStart");
                   ExRePointsValiditReport.pass("Deal : "+(i+1)+" case "+ x +" pass");
                   ExRePointsValiditReport.info("from: Next deal , Until: Several months");
               }else{
                   ExRePointsValiditReport.info("DaysUntilStart");
                   ExRePointsValiditReport.fail("Deal :"+(i+1)+" case "+ x +" fail");
                   ExRePointsValiditReport.info("from: Next deal , Until: Several months");
               }

               if(pointsValidityFunctions.calcDaysUntilEnd(nodeList,NLIndex,i) == 1){
                   ExRePointsValiditReport.info("DaysUntilEnd");
                   ExRePointsValiditReport.pass("Deal : "+(i+1)+" case "+ x +" pass");
                   ExRePointsValiditReport.info("from: Next deal , Until: Several months");
               }else{
                   ExRePointsValiditReport.info("DaysUntilEnd");
                   ExRePointsValiditReport.fail("Deal :"+(i+1)+" case "+ x +" fail");
                   ExRePointsValiditReport.info("from: Next deal , Until: Several months");


               }

                break;

            case 40 :
                //from: Next business day , Until: Several weeks
                if(pointsValidityFunctions.calcWeeksUntilEnd(nodeList,NLIndex,i)==1){
                    ExRePointsValiditReport.info("WeeksUntilEnd");
                    ExRePointsValiditReport.pass("Deal : "+(i+1)+" case "+ x +" pass");
                    ExRePointsValiditReport.info("from: Next business day , Until: Several weeks");
                }else{
                    ExRePointsValiditReport.info("WeeksUntilEnd");
                    ExRePointsValiditReport.fail("Deal :"+(i+1)+" case "+ x +" fail");
                    ExRePointsValiditReport.info("from: Next business day , Until: Several weeks");


                }



                break;

            case 12 :
                //from: Immediately   , Until: End of the year

                if(pointsValidityFunctions.calcUntilEndOfYear(nodeList,NLIndex)==1){
                    ExRePointsValiditReport.pass("Deal : "+(i+1)+"case "+ x +" pass");
                    ExRePointsValiditReport.info("from: Immediately   , Until: End of the year");
                }else{
                    ExRePointsValiditReport.fail("Deal :"+(i+1)+" case "+ x +" fail");
                    ExRePointsValiditReport.info("from: Immediately   , Until: End of the year");

                }
                break;
            case 18 :
                //from: Immediately   , Until: End of mount
                if (pointsValidityFunctions.calcUntilEndOfMonth(nodeList,NLIndex) == 1){
                    ExRePointsValiditReport.pass("Deal : "+(i+1)+"case "+ x +" pass");
                    ExRePointsValiditReport.info("from: Immediately   , Until: End of mount");
                }else{
                    ExRePointsValiditReport.fail("Deal :"+(i+1)+" case "+ x +" fail");
                    ExRePointsValiditReport.info("from: Immediately   , Until: End of mount");

                }
            case 24 :
                //from: Immediately   , Until: End of week
                if (pointsValidityFunctions.calcntilEndOfWeek(nodeList,NLIndex) == 1){
                    ExRePointsValiditReport.pass("Deal : "+(i+1)+"case "+ x +" pass");
                    ExRePointsValiditReport.info("from: Immediately   , Until: End of mount");
                }else{
                    ExRePointsValiditReport.fail("Deal :"+(i+1)+" case "+ x +" fail");
                    ExRePointsValiditReport.info("from: Immediately   , Until: End of mount");
                }




            default:
        }



    }

}
