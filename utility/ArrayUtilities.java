package Bankers_Algo.utility;

public class ArrayUtilities {
    public static int[] arrayMinus(int[] array1, int[] array2){
        int[] res = new int[array2.length];
        for(int i = 0; i < array1.length; i++){
            res[i] = array1[i] - array2[i];
        }
        return res;
    }
    public static int[] arrayPlus(int[] array1,int[] array2) {
        int[] res = new int[array2.length];
        for(int i = 0; i < array1.length; i++){
            res[i] = array1[i] + array2[i];
        }
        return res;
    }
}
