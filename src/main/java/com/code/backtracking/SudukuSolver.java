package com.code.backtracking;

public class SudukuSolver {

    public static void main(String[] args) {

        int arr[][] = {
                {5,3,-1,-1,7,-1,-1,-1,-1},
                {6,-1,-1,1,9,5,-1,-1,-1},
                {-1,9,8,-1,-1,-1,-1,6,-1},
                {8,-1,-1,-1,6,-1,-1,-1,3},
                {4,-1,-1,8,-1,3,-1,-1,1},
                {7,-1,-1,-1,2,-1,-1,-1,6},
                {-1,6,-1,-1,-1,-1,2,8,-1},
                {-1,-1,-1,4,1,9,-1,-1,5},
                {-1,-1,-1,-1,8,-1,-1,7,9}



        };


        fetchSudoku(arr , 0 , 0);
    }


    public static boolean fetchSudoku(int arr[][] , int row , int col) {

        if(col == arr.length) {
            row++;
            col = 0;
        }

        if(row == arr.length) {
            System.out.println(arr);
            return true ;
        }





        //position is not filled
        if(arr[row][col] ==  -1) {
                for(int i=1;i<=9;i++) {
                if(isNumberAvailable(arr , row , col , i)) {
                    arr[row][col] = i;
                    if(!fetchSudoku(arr , row , col+1)) {
                        arr[row][col] = -1;
                    }
                    else {
                        break;
                    }
                }
            }
        }
        else {
           return fetchSudoku(arr , row , col+1);
        }
        return false;
    }


    public static boolean isNumberAvailable(int arr[][] , int row , int col , int val) {

        // checking on row
        for(int i=0;i<9;i++) {
            if(val == arr[row][i]) {
                return false;
            }
            //checking on column
            if(val == arr[i][col]) {
                return false ;
            }
        }

        int rmax = fetchMaxGridNo(row);
        int rmin = fetchMinGridNo(row);

        int cmax = fetchMaxGridNo(col);
        int cmin = fetchMinGridNo(col);

        for(int i=rmin;i<=rmax;i++) {
            for(int j=cmin;j<=cmax;j++) {
                if(arr[i][j] == val) {
                    return false ;
                }
            }
        }


        return true ;




    }



    public static int fetchMaxGridNo(int val) {
        if(val <=2) {
            return 2;
        }
        else if (val>2 && val <=5){
            return 5;
        }
        else {
            return 8;
        }
    }

    public static int fetchMinGridNo(int val) {
        if(val <=2) {
            return 0;
        }
        else if (val>2 && val <=5){
            return 3;
        }
        else {
            return 6;
        }
    }


}
