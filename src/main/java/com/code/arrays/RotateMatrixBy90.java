package com.code.arrays;

public class RotateMatrixBy90 {

    public static void main(String[] args) {

        int arr[][] = {
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,14,15,16}
        };

        transpose(arr , arr.length , arr[0].length);
        reverse(arr , arr.length , arr[0].length);
    }

    public static void reverse(int arr[][] , int row , int col) {

        int j = 0;
        int k = col-1;
        while (j<k) {
            for(int i=0 ; i< row;i++) {
                int temp = arr[i][j];
                arr[i][j] = arr[i][k];
                arr[i][k] = temp;
            }
            j++;
            k--;
        }
        System.out.println(arr);
    }

    public static void transpose(int arr[][] , int row , int col) {

        // Base condition
        if(row < 2 || col < 2) {
            return ;
        }
        for(int i=0;i<row ;i++) {
            for(int j=i+1 ; j < col ; j++) {
                int temp = arr[i][j];
                arr[i][j] = arr[j][i];
                arr[j][i] = temp;
            }
        }
        System.out.println(arr);

    }


    public static void swap(int source , int target) {
        int temp = source;
        source = target;
        target = temp;
    }
}
