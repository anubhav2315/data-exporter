package com.code.arrays;

public class SpiralMatrix {

    public static void main(String[] args) {

//        int arr[][] = new int[][]{
//                {1, 2, 3, 4},
//                {5, 6, 7, 8},
//                {9, 10, 11, 12},
//                {13, 14, 15, 16}};

        int arr[][] = {
                {1,2,3,4,5,6,7,8},
                {9,10,11,12,13,14,15,16},
                {17,18,19,20,21,22,23,24},
                {25,26,27,28,29,30,31,32},
                {33,34,35,36,37,38,39,40},
                {41,42,43,44,45,46,47,48}
        };

        int row = arr.length;
        int col = arr[0].length;
        for(int i=0;i<(row+1)/2 ;i++) {
            traverseRowForward(arr , i , i , arr[i].length-1-i );
            traverseColDown(arr , col -1 - i , i , arr.length-i);
            traverseRowBackward(arr , arr.length-1 -i ,i ,arr[i].length-1-i  );
            traverseColUp(arr , i , i , arr.length-1-i);
        }
    }


    public static void traverseRowForward(int arr[][], int row , int startCol , int endCol) {
        for (int i = startCol; i < endCol; i++) {
            System.out.print(arr[row][i]);
            System.out.print("\t");
        }
    }


    public static void traverseRowBackward(int arr[][], int row , int startCol , int endCol) {
        for (int i = endCol-1; i >= startCol; i--) {
            System.out.print(arr[row][i]);
            System.out.print("\t");
        }
    }

    public static void traverseColDown(int arr[][], int col, int startRow , int endRow) {
        for (int i = startRow; i < endRow; i++) {
            System.out.print(arr[i][col]);
            System.out.print("\t");
        }
    }

    public static void traverseColUp(int arr[][], int col, int startRow , int endRow) {
        for (int i = endRow-1; i>startRow ; i--) {
            System.out.print(arr[i][col]);
            System.out.print("\t");
        }
    }



}
