package Bankers_Algo.entity;

import Bankers_Algo.utility.ArrayUtilities;

import java.util.Arrays;
import java.util.UUID;

public class MyProcess implements Comparable<MyProcess>{

    private final String uuid;
    private int[] max;
    private int[] need;

    private int[] alloc;
    private int status;
    private int resourceNum;

    private int tag;



    public static final int WAITING = 0;
    public static final int EXECUTED = 1;

    public static final int REFUSED =2;

    public static final int SYSTEM = 777;

    public static final int USER = 666;


    public MyProcess(int[] max){
        this.resourceNum = max.length;
        this.max = max;
        this.need = max;
        this.alloc = ArrayUtilities.arrayMinus(this.max,need);
        this.status = MyProcess.WAITING;
        this.uuid = UUID.randomUUID().toString().replace("-","");
    }
    public MyProcess(int res_nums) {

        //Randomly initialize process
        int[] max_set = new int[res_nums];

        for (int j = 0; j < res_nums; j++) {
            max_set[j] = (int) (Math.random() * 10);
        }
        this.max = max_set;
        this.resourceNum = max.length;
        //Randomly initialize allocated resource vector
        int[] alloc_set = new int[res_nums];
        Arrays.fill(alloc_set, Integer.MAX_VALUE);
        for (int j = 0; j < res_nums; j++) {
            while (alloc_set[j] - max_set[j] > 0) {
                alloc_set[j] = (int) (Math.random() * 10);
            }
        }
        this.alloc = alloc_set;
        this.need = ArrayUtilities.arrayMinus(max,alloc);
        this.status = MyProcess.WAITING;
        this.uuid = UUID.randomUUID().toString().replace("-","");
    }

    public String getUuid(){
        return this.uuid;
    }

    public void setMax(int[] max) {
        this.max = max;
    }
    public int[] getMax() {
        return max;
    }
    public int[] getNeed() {
        return need;
    }
    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void allocate(int[] alloc){
            this.need = ArrayUtilities.arrayMinus(max,alloc);
            this.alloc = alloc;

    }

    public void deallocate(int[] alloc){
            this.need = ArrayUtilities.arrayPlus(need,alloc);
            Arrays.fill(alloc,0);
    }

    public int[] getAllocated() {
        return this.alloc;
    }

    public int getAllNeed() {
        int res = 0;
        for (int i:this.need) {
            res+=i;
        }
        return res;
    }

    public int getAllAllocated() {
        int res = 0;
        for (int i:this.alloc) {
            res+=i;
        }
        return res;
    }

    public int getAllMax() {
        int res = 0;
        for (int i:this.max) {
            res+=i;
        }
        return res;
    }
    public int getStatus() {
        return status;
    }
    public String getStatusAlter() {
        switch (status) {
            case 0: return "WAIT";
            case 1: return "EXECUTE";
            case 2: return "REFUSE";
            default: return "";
        }
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String toString() {
       return String.format("[Process] [%s]:\n %s: %s\n %s: %s\n %s: %s",
               uuid, "MAX", Arrays.toString(max),
               "ALLOC", Arrays.toString(alloc),
               "NEED", Arrays.toString(need));
    }

    @Override
    public int compareTo(MyProcess o) {
        if((this.getAllNeed()-o.getAllNeed())>=0) {
            return 1;
        } else
            return -1;
    }
}
