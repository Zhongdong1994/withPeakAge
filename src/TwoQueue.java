import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TwoQueue {
    public static void main(String[] args) throws IOException {

        int computationNum=10;
        File  FinalInfo=new File("FinalInfo.txt");
        FileWriter out0= new FileWriter(FinalInfo);
        double[]  matrix= new double[10];
        for(int i=0;i<computationNum;i++){
            double[] returnArray=twoQueue();
            for(int j=0;j<10;j++){
                matrix[j]=matrix[j]+returnArray[j];
            }
            System.out.println(i);
        }

        System.out.println("jobAvgWaitingTime: "+matrix[0]/computationNum);
        out0.write(matrix[0]/computationNum+"\t");
        System.out.println("jobAvgSystemTime: "+matrix[1]/computationNum);
        out0.write(matrix[1]/computationNum+"\t");
        System.out.println("jobAvgWaitingNum: "+matrix[2]/computationNum);
        out0.write(matrix[2]/computationNum+"\t");
        System.out.println("jobAvgPeakAge: "+matrix[6]/computationNum);
        out0.write(matrix[6]/computationNum+"\t");
        System.out.println("jobAvgAge: "+matrix[8]/computationNum);
        out0.write(matrix[8]/computationNum+"\t");
        System.out.println("requestAvgWaitingTime: "+matrix[3]/computationNum);
        out0.write(matrix[3]/computationNum+"\t");
        System.out.println("requestAvgSystemTime: "+matrix[4]/computationNum);
        out0.write(matrix[4]/computationNum+"\t");
        System.out.println("requestAvgWaitingNum: "+matrix[5]/computationNum);
        out0.write(matrix[5]/computationNum+"\t");
        System.out.println("requestAvgPeakAge: "+matrix[7]/computationNum);
        out0.write(matrix[7]/computationNum+"\t");
        System.out.println("requestAvgAge: "+matrix[9]/computationNum);
        out0.write(matrix[9]/computationNum+"\t");
        out0.close();

    }
    public static double[] twoQueue() throws IOException {


        double Time=100000;
        int threshold=2;
        double timeInterval=0.001;

        double jobArrivalRate=1/3d, jobServiceRate=1d,requestArrivalRate=1/3d,requestServiceRate=1d;
        ArrayList<Integer> jobIndex=new ArrayList<>(); ArrayList<Double> jobServiceTime=new ArrayList<>();ArrayList<Double> jobBeginTime=new ArrayList<>();
        ArrayList<Integer> requestIndex=new ArrayList<>(); ArrayList<Double> requestServiceTime=new ArrayList<>();ArrayList<Double> requestBeginTime=new ArrayList<>();
        AddJobs.addJobs1(jobArrivalRate,jobServiceRate,jobIndex,jobBeginTime,jobServiceTime,Time);
        Jobs jobs[]=new Jobs[jobIndex.size()];
        for(int i=0;i<jobs.length;i++){
            jobs[i]=new Jobs(jobIndex.get(i), jobServiceTime.get(i),jobBeginTime.get(i));
        }

        AddJobs.addJobs1(requestArrivalRate,requestServiceRate,requestIndex,requestBeginTime,requestServiceTime,Time);
        Jobs request[]=new Jobs[requestIndex.size()];
        for(int i=0;i<request.length;i++){
            request[i]=new Jobs(requestIndex.get(i), requestServiceTime.get(i),requestBeginTime.get(i));
        }


        double[] returnArray= findAvgTime(jobs, jobs.length,request,request.length,threshold,timeInterval,Time);
        return returnArray;


    }



    static double[] findAvgTime(Jobs jobs1[], int n, Jobs request2[], int m, int threshold,double timeInterval, double originalTime) throws IOException {
        double[] returnArray= new double[10];
        ArrayList<Integer> jobWaitingNum=new ArrayList<Integer>();
        ArrayList<Integer> requestWaitingNum=new ArrayList<Integer>();

        double jobWaitingTime1[]=new double[n], jobSystemTime1[]=new double[n],jobFinishTime1[]=new double[n],jobPeakAge[]=new double[n],jobAge[]=new double[n];
        double jobTotalWaitingTime1 = 0, jobTotalSystemTime1 = 0,jobTotalPeakAge=0,jobTotalAge=0;

        double requestWaitingTime2[]=new double[m], requestSystemTime2[]=new double[m],requestFinishTime2[]=new double[m],requestPeakAge[]=new double[m],requestAge[]=new double[m];
        double requestTotalWaitingTime2 = 0, requestTotalSystemTime2 = 0,requestTotalPeakAge=0,requestTotalAge=0;

        findWaitingTime(jobs1,jobWaitingTime1,jobFinishTime1,request2,requestWaitingTime2,requestFinishTime2,threshold,timeInterval,
                jobWaitingNum,requestWaitingNum,originalTime);
        findSystemTime(jobs1,n,jobWaitingTime1,jobSystemTime1,request2,m,requestWaitingTime2,requestSystemTime2);
        findJobPeakAge(jobs1, n, jobWaitingTime1,jobSystemTime1,jobPeakAge);
        findRequestPeakAge(request2,m,requestWaitingTime2,requestSystemTime2,requestPeakAge);
        findJobAge(jobs1,n,jobFinishTime1,jobAge,jobWaitingTime1);
        findRequestAge(request2,m,requestFinishTime2,requestAge,requestWaitingTime2);


        File jobTotalWaitingTimeFile=new File("jobTotalWaitingTime.txt");
        File jobTotalSystemTimeFile=new File("jobTotalSystemTime.txt");
        File jobTotalPeakAgeFile=new File("jobTotalPeakAge.txt");
        File jobTotalAgeFile=new File("jobTotalAge.txt");
        FileWriter out1 = new FileWriter(jobTotalWaitingTimeFile);
        FileWriter out2 = new FileWriter(jobTotalSystemTimeFile);
        FileWriter out3 = new FileWriter(jobTotalPeakAgeFile);
        FileWriter out30=new FileWriter(jobTotalAgeFile);

        int noneZeroCounter=0;
        for (int i = 0; i < n; i++)
        {
            if(jobWaitingTime1[i]>0){
                noneZeroCounter++;
            }
            jobTotalWaitingTime1 = jobTotalWaitingTime1+ jobWaitingTime1[i];
            out1.write(jobWaitingTime1[i]+"\t");
            jobTotalSystemTime1 = jobTotalSystemTime1 + jobSystemTime1[i];
            out2.write(jobSystemTime1[i]+"\t");
            jobTotalPeakAge=jobTotalPeakAge+jobPeakAge[i];
            out3.write(jobPeakAge[i]+"\t");
            jobTotalAge=jobTotalAge+jobAge[i];
            out30.write(jobAge[i]+"\t");
        }
        out1.close();
        out2.close();
        out3.close();
        out30.close();




        double jobAvgWaitingTime=jobTotalWaitingTime1/noneZeroCounter;
        returnArray[0]=jobAvgWaitingTime;
        double jobAvgSystemTime=jobTotalSystemTime1/noneZeroCounter;
        returnArray[1]=jobAvgSystemTime;
        double jobAvgPeakAge=jobTotalPeakAge/noneZeroCounter;
        returnArray[6]=jobAvgPeakAge;
        double jobAvgAge=jobTotalAge/originalTime;
        returnArray[8]=jobAvgAge;



        File jobWaitingNumFile=new File("jobWaitingNum.txt");
        FileWriter out4=new FileWriter(jobWaitingNumFile);
        int sumOfjobWaitingNum=0;
        for(int i=0;i<jobWaitingNum.size();i++){
            //System.out.println(jobWaitingNum.get(i));
            sumOfjobWaitingNum=sumOfjobWaitingNum+jobWaitingNum.get(i);
            out4.write(jobWaitingNum.get(i)+"\t");
        }
        double jobAvgWaitingNum=(double)sumOfjobWaitingNum/ jobWaitingNum.size();
        returnArray[2]=jobAvgWaitingNum;
        out4.close();
        //System.out.println("Average waiting number of jobs= "+jobAvgWaitingNum);





        File requestTotalWaitingTimeFile=new File("requestTotalWaitingTime.txt");
        File requestTotalSystemTimeFile=new File("requestTotalSystemTime.txt");
        File requestTotalPeakAgeFile=new File("requestTotalPeakAge.txt");
        File reqeustTotalAgeFile=new File("requestTotalAge.txt");
        FileWriter out5 = new FileWriter(requestTotalWaitingTimeFile);
        FileWriter out6 = new FileWriter(requestTotalSystemTimeFile);
        FileWriter out8=new FileWriter(requestTotalPeakAgeFile);
        FileWriter out80= new FileWriter(reqeustTotalAgeFile);

        int noneZeroCounter2=0;
        for (int i = 0; i < m; i++)
        {
            if(requestWaitingTime2[i]>0){
                noneZeroCounter2++;
            }
            requestTotalWaitingTime2 = requestTotalWaitingTime2+ requestWaitingTime2[i];
            out5.write(requestWaitingTime2[i]+"\t");
            requestTotalSystemTime2 = requestTotalSystemTime2 + requestSystemTime2[i];
            out6.write(requestSystemTime2[i]+"\t");
            requestTotalPeakAge=requestTotalPeakAge+requestPeakAge[i];
            out8.write(requestPeakAge[i]+"\t");
            requestTotalAge=requestTotalAge+requestAge[i];
            out80.write(requestAge[i]+"\t");
        }
        out5.close();
        out6.close();
        out8.close();
        out80.close();



        double requestAvgWaitingTime= requestTotalWaitingTime2/noneZeroCounter2;
        returnArray[3]=requestAvgWaitingTime;
        double requestAvgSystemTime=requestTotalSystemTime2/noneZeroCounter2;
        returnArray[4]=requestAvgSystemTime;
        double requestAvgPeakAge=requestTotalPeakAge/noneZeroCounter2;
        returnArray[7]=requestAvgPeakAge;
        double requestAvgAge=requestTotalAge/originalTime;
        returnArray[9]=requestAvgAge;

        File requestWaitingNumFile=new File("requestWaitingNum.txt");
        FileWriter out7= new FileWriter(requestWaitingNumFile);
        int sumOfrequestWaitingNum=0;
        for(int i=0;i<requestWaitingNum.size();i++){
            sumOfrequestWaitingNum=sumOfrequestWaitingNum+requestWaitingNum.get(i);
            out7.write(requestWaitingNum.get(i)+"\t");
        }
        out7.close();
        double requestAvgWaitingNum=(double)sumOfrequestWaitingNum/requestWaitingNum.size();
        returnArray[5]=requestAvgWaitingNum;


        return returnArray;

    }

    static void findSystemTime(Jobs jobs1[], int n, double jobWaitingTime1[],double jobSystemTime1[],
                               Jobs request2[], int m,double requestWaitingTime2[],double requestSystemTime2[]
    ){
        for(int i=0;i<n;i++){
            if(jobWaitingTime1[i]>0){
                jobSystemTime1[i]=jobs1[i].jobServiceTime+jobWaitingTime1[i];

            }

        }
        for(int i=0;i<m;i++) {
            if (requestWaitingTime2[i] > 0) {
                requestSystemTime2[i] = request2[i].jobServiceTime + requestWaitingTime2[i];
            }
        }
    }


    static void findJobPeakAge(Jobs jobs1[], int n, double jobWaitingTime1[], double jobSystemTime1[],double jobPeakAge[]){
        for(int i=1;i<n;i++){
            if(jobWaitingTime1[i]>0){
                jobPeakAge[i]=jobSystemTime1[i]+(jobs1[i].jobArrivalTime-jobs1[i-1].jobArrivalTime);

            }
        }
    }

    static void findRequestPeakAge(Jobs request2[],int m,double requestWaitingTime2[],double requestSystemTime2[],double requestPeakAge[]){
        for(int i=1;i<m;i++){
            if(requestWaitingTime2[i]>0){
                requestPeakAge[i]=requestSystemTime2[i]+(request2[i].jobArrivalTime-request2[i-1].jobArrivalTime);
            }
        }
    }

    static void findJobAge(Jobs jobs1[],int n, double jobFinishTime[],double jobAge[],double jobWaitingTime[]){
        for(int i=1;i<n;i++){
            if(jobWaitingTime[i]>0){
                jobAge[i]=0.5*(jobs1[i].jobArrivalTime-jobs1[i-1].jobArrivalTime)*(2*jobFinishTime[i]-jobs1[i].jobArrivalTime-jobs1[i-1].jobArrivalTime);
            }
        }
    }

    static void findRequestAge(Jobs request[], int m, double requestFinishTime[],double requestAge[],double requestWaitingTime[]){
        for(int i=1;i<m;i++){
            if(requestWaitingTime[i]>0){
                requestAge[i]=0.5*(request[i].jobArrivalTime-request[i-1].jobArrivalTime)*(2*requestFinishTime[i]-request[i].jobArrivalTime-request[i-1].jobArrivalTime);
            }
        }
    }

    static void findWaitingTime(Jobs jobs1[], double jobWaitingTime1[],double jobFinishTime1[],
                                Jobs request2[], double requestWaitingTime2[],double requestFinishTime2[],
                                int threshold, double timeInterval, ArrayList jobWaitingNum, ArrayList requestWaitingNum, double originalTime) {

        int RQI = 0, JQI=0;
        int currentJob = 0, currentRequest = 0;
        double Time = 0, finishTime = 0;//preJobFinishTime=0,preRequestFinishTime=0;
        boolean checkJobQueue = false;
        boolean checkRequestQueue = false;


        double jobServiceTime1[] = new double[jobs1.length];
        for (int i = 0; i < jobs1.length; i++) {
            jobServiceTime1[i] = jobs1[i].jobServiceTime;
        }
        double requestServiceTime2[] = new double[request2.length];
        for (int i = 0; i < request2.length; i++) {
            requestServiceTime2[i] = request2[i].jobServiceTime;
        }

        while (Time<originalTime) {

                if(jobs1[currentJob].jobArrivalTime<request2[currentRequest].jobArrivalTime){
                    //System.out.println("jobs1["+currentJob+"].jobArrivalTime="+jobs1[currentJob].jobArrivalTime);
                    //System.out.println("jobServiceTime1["+currentJob+"]="+jobServiceTime1[currentJob]);
                    if(currentJob==0&&currentRequest==0){
                        Time=jobs1[currentJob].jobArrivalTime+jobServiceTime1[currentJob];
                    }else {
                        if(finishTime>jobs1[currentJob].jobArrivalTime){
                            Time=finishTime+jobServiceTime1[currentJob];
                        }else {
                            Time=jobs1[currentJob].jobArrivalTime+jobServiceTime1[currentJob];
                        }

                    }

                    finishTime = Time;
                    //System.out.println("finishTime="+finishTime);
                    jobFinishTime1[currentJob] = finishTime;
                    jobWaitingTime1[currentJob] = finishTime - jobs1[currentJob].jobArrivalTime - jobs1[currentJob].jobServiceTime;
                    //System.out.println("jobWaitingTime1["+currentJob+"]="+jobWaitingTime1[currentJob]);

                    if(currentJob<jobs1.length-1){
                        currentJob++;
                    }

                    JQI=jobQueueInfo(jobs1,jobServiceTime1,Time)[2];
                    jobWaitingNum.add(JQI);
                }else {
                    //System.out.println("request2["+currentRequest+"].jobArrivalTime="+request2[currentRequest].jobArrivalTime);
                    //System.out.println("requestServiceTime2["+currentRequest+"]="+requestServiceTime2[currentRequest]);
                    if(currentRequest==0&&currentJob==0){
                        Time=request2[currentRequest].jobArrivalTime+requestServiceTime2[currentRequest];
                    }else {
                        if(finishTime>request2[currentRequest].jobArrivalTime){
                            Time = finishTime+ requestServiceTime2[currentRequest];
                        }else {
                            Time = request2[currentRequest].jobArrivalTime+ requestServiceTime2[currentRequest];
                        }

                    }

                    finishTime = Time;
                    //System.out.println("finishTime="+finishTime);
                    requestFinishTime2[currentRequest] = finishTime;
                    requestWaitingTime2[currentRequest] = finishTime - request2[currentRequest].jobArrivalTime - request2[currentRequest].jobServiceTime;
                   // System.out.println("requestWaitingTime2["+currentRequest+"]="+ requestWaitingTime2[currentRequest] );

                    if(currentRequest<request2.length-1){
                        currentRequest++;
                    }
                    RQI = requestQueueInfo(request2,requestServiceTime2, Time)[2];
                    requestWaitingNum.add(RQI);
                }

        }

    }

    public static int [] requestQueueInfo(Jobs request[], double requestServiceTime2[], double t){
        int[] RQI=new int[3];// To check the Request Queue Informaiton
        int indexBegin=0;
        int indexEnd=0;
        while(indexBegin<requestServiceTime2.length&&requestServiceTime2[indexBegin]<0){
            indexBegin++;
        }
        while(indexEnd<request.length&&request[indexEnd].jobArrivalTime<t){
            indexEnd++;
        }
        RQI[0]=indexBegin;
        RQI[1]=indexEnd;
        RQI[2]=indexEnd-indexBegin;

        return RQI;
    }


    public static int [] jobQueueInfo(Jobs jobs[], double jobServiceTime1[], double t){
        int[] JQI=new int[3];
        int indexBegin=0;
        int indexEnd=0;
        while(indexBegin<jobServiceTime1.length&&jobServiceTime1[indexBegin]<0){
            indexBegin++;
        }
        while(indexEnd<jobs.length&&jobs[indexEnd].jobArrivalTime<t){
            indexEnd++;
        }
        JQI[0]=indexBegin;
        JQI[1]=indexEnd;
        JQI[2]=indexEnd-indexBegin;

        return JQI;
    }


}
