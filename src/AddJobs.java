import java.util.ArrayList;

public class AddJobs {

    public static void  addJobs1(double jobArrivalRate, double jobServiceRate, ArrayList jobIndex,ArrayList jobBeginTime,ArrayList jobServiceTime, double Time)
    {
        double beginTime=0;
        int counter=0;

        while(beginTime<Time) {
            jobIndex.add(counter++);
            double random=Math.random();
            double InterArrivalTime= -(1/jobArrivalRate)*Math.log(random);
            beginTime=beginTime+InterArrivalTime;
            jobBeginTime.add(beginTime);

            double random0=Math.random();
            double randomServiceTime= -(1/jobServiceRate)*Math.log(random0);
            jobServiceTime.add(randomServiceTime);
        }
    }



}
