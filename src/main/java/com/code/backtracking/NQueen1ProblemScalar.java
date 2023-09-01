package com.code.backtracking;

public class NQueen1ProblemScalar {
    public static void main(String[] args) {

        int n = 4;
        char[][] arr = new char[n][n];
        for(int i=0;i< arr.length;i++) {
            for(int j=0;j< arr.length;j++) {
                arr[i][j] = '.';
            }
        }

        nQueens(arr , 0  , new boolean[n] ,new boolean[2*n - 1] , new boolean[2*n-1]  );

    }

    public static void nQueens(char arr[][] , int row , boolean[] cols , boolean d1[] , boolean d2[]) {

        if(row == arr.length) {
            System.out.println(arr) ;
        }
        for(int col = 0;col < arr.length;col++) {
            if(!cols[col] && !d1[row+col] && !d2[row-col+ arr.length-1]) {
                arr[row][col] =  'Q' ;
                cols[col] = true;
                d1[row+col] = true;
                d2[row-col+ arr.length-1] =  true;
                nQueens(arr , row+1 , cols,d1,d2);
                //resetting the value again
                arr[row][col] = '.';
                cols[col] = false;
                d1[row+col] = false;
                d2[row-col+ arr.length-1] =  false;
            }
        }



    }



    //Can be used in

//    public static boolean isSafe(char arr[][] , int row , int col) {
//        //checking verticals
//        for(int i=0;i<row ;i++) {
//            if(arr[i][col] == 'Q' ) {
//                return false;
//            }
//        }
//        // checking diagonal 1 --> row decrease and column increase
//        for(int i = row-1 , j = col+1;i>=0 && j < arr[0].length ; i--,j++) {
//            if(arr[i][j] == 'Q') {
//                return false ;
//            }
//        }
//
//        // checking diagonal 2
//        for(int i=row-1 , j = col-1 ; i>=0 && j>=0 ; i--,j--) {
//            if(arr[i][j] == 'Q') {
//                return false ;
//            }
//        }
//
//        return true;
//    }


    //public
}
