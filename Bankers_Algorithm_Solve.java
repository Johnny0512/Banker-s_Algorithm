package Bankers_Algo;

import Bankers_Algo.entity.MyProcess;
import Bankers_Algo.utility.ArrayUtilities;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Bankers_Algorithm_Solve {
    static int pcb_nums;  // Number of processes
    static int res_nums;  // Number of resources
    static int[] ava;     // Initial available resources

    static MyProcess[] p;

    static void bank_init() {
        Scanner in = new Scanner(System.in);

        Random rand = new Random();
        System.out.println("Please enter number of Processes: ");
        pcb_nums = in.nextInt();
        System.out.println("Please enter number of Resources: ");
        res_nums = in.nextInt();

        //initialize process sequence
        p = new MyProcess[pcb_nums];
        for (int i = 0;i < p.length; i++) {
            //Randomly initialize max demand vector
            int[] max_set = new int[res_nums];
            for (int j = 0; j < res_nums; j++) {
                max_set[j] = rand.nextInt(10);
            }
            p[i] = new MyProcess(max_set);

            //Randomly initialize allocated resource vector
            int[] alloc_set = new int[res_nums];
            Arrays.fill(alloc_set, Integer.MAX_VALUE);
            for (int j = 0; j < res_nums; j++) {
                while (alloc_set[j] - max_set[j] > 0) {
                    alloc_set[j] = rand.nextInt(10);
                }
            }
            p[i].allocate(alloc_set);

            //Randomly initialize category of processes
            if (p[i].getAllMax()<=10) p[i].setTag(MyProcess.SYSTEM); else p[i].setTag(MyProcess.USER);
        }

        //Randomly initialize available resources
        ava = new int[res_nums];
        for (int j = 0; j < res_nums; j++) {
            ava[j] = rand.nextInt(10);
        }

    }

    //Check if the elements of m is all greater than n
    static boolean check(int[] m, int[] n) {
        for (int i = 0; i < m.length; i++) {
            if (m[i] < n[i])
                return false;
        }
        return true;
    }
    static int safe_new() {
        long before = System.nanoTime();
        //Available resource
        int[] work = new int[res_nums];
        System.arraycopy(ava, 0, work, 0, res_nums);

        //Counters indicates whether system is in deadlock situation
        int switch_count = 0;
        int release_count = 0;

        //return status, 0 -> safe | 1 -> executable with unsafe condition | 2 -> unavoidable deadlock
        int stat = 0;

        for(int i = 0; i < p.length; i++) {

            //A process cannot be allocated
            if (!check(work,p[i].getNeed())) {

                //Switching elements
                switch_count+=1;
                MyProcess temp = p[i];
                p[i] = p[p.length-switch_count-1];
                p[p.length-switch_count-1]= temp;
                //All the elements after p[i] is switched
                if(switch_count == p.length-i-2) {
                    //The system is impossible to solve
                    if (release_count == p.length - i + 1) {
                        //indicates unavoidable deadlock
                        return 2;
                    }
                    stat = 1;
                    System.out.println("Unsafe condition detected, solving...");
                    /*
                    Solving deadlock
                     */
                    work = ArrayUtilities.arrayPlus(work,p[i].getAllocated());
                    p[i].deallocate(p[i].getAllocated());
                    release_count++;

                    MyProcess temp2 = p[i];
                    p[i] = p[p.length-1];
                    p[p.length-1]= temp2;
                    switch_count = 0;
                }
                i--;
            } else {
                work = ArrayUtilities.arrayPlus(work, p[i].getMax());
                switch_count = 0;
            }
        }
        System.out.println("Status: safe");
        System.out.println("Time: "+(System.nanoTime()-before));
        return stat;
    }

    static int safe_origin() {
        long before = System.nanoTime();
        //Available resource
        int[] work = new int[res_nums];

        int count = 0;
        boolean flag = true;
        System.arraycopy(ava, 0, work, 0, res_nums);
        //Every time, start from the first one.
        //Stop if count doesn't change for two iterations.
        while (flag) {
            flag = false;
            for (MyProcess myProcess : p) {
                if (count == p.length) break;
                if (myProcess.getStatus() == MyProcess.EXECUTED) {
                    //Skip once
                    continue;
                } else {
                    if (check(work, myProcess.getNeed())) {
                        myProcess.setStatus(MyProcess.EXECUTED);
                        flag = true;
                        count++;
                        work = ArrayUtilities.arrayPlus(work, myProcess.getMax());
                    }
                }
            }
        }
        for (int i = 0; i < pcb_nums; i++) {
            if (p[i].getStatus() != MyProcess.EXECUTED){
                return 1;
            }
        }

        for (MyProcess pi: p) {
            pi.setStatus(MyProcess.WAITING);
        }
        System.out.println("Status: safe");
        System.out.println("Time: "+(System.nanoTime()-before));
        return 0;
    }
    public static void main(String[] args) throws Exception {
            bank_init();
            safe_new();

    }
}


