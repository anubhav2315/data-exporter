package com.code.binarysearch;

public class SearchInSortedMatrix {


    public static void main(String[] args) {

        int[][] arr = {{1,4,6,8,10} ,
                {2,7,9,12,15} ,
                {3,11,20,22,24},
                {5,16,25,30,40} };
        int row = 0;
        int col = arr[0].length-1;
        int target = 1111;
        while (row < arr.length && col >= 0) {
            int num = arr[row][col];
            if(num == target) {
                System.out.println("row :"+ row + ", col :"+ col);
                break;
            }
            else if(num < target) {
                row++;
            }
            else {
                col--;
            }
        }

    }
}
