package com.code.backtracking;

import com.code.Cell;

import java.util.ArrayList;
import java.util.List;

public class NQueen1Problem {

    public static void main(String[] args) {

        findQueenSolution(0 , 0 , new boolean[4][4] , new ArrayList<>());




    }


    public static void findQueenSolution(int row , int col , boolean[][] arr,  List<Cell> ans) {
        if(row  == arr.length) {
            for(Cell  cell : ans) {
                System.out.print("("+cell.getRow() + "," + cell.getCol() + ")" + "\t");
            }
            System.out.println();
            return ;
        }
        if(col == arr.length) {
            return ;
        }

            for(int i=0;i<arr[0].length;i++) {
                if(!isAttacking(row , i , arr)) {
                    ans.add(new Cell(row, i));
                    arr[row][i] = true;
                    findQueenSolution(row + 1, i, arr, ans);
                    ans.remove(ans.size() - 1);
                    arr[row][i] = false;
                }

            }




    }


    public static boolean isAttacking(int row , int col , boolean[][] arr) {

        if(row ==0 && col ==0 ) {
            return false;
        }
        // checking vertical (row basis )
        for(int i= row-1 ;i>=0;i--) {
            if(arr[i][col] == true) {
                return true;
            }
        }

        // left diagonal
        int i=row-1;
        int j = col-1;
        while (i >=0 && j >= 0) {
            if(arr[i][j]) {
                return true;
            }
            i--;j--;

        }

        // right diagonal
        i=row-1;
        j = col+1;
        while (i >=0 && j <arr[0].length) {
            if(arr[i][j]) {
                return true;
            }
            i--;j++;
        }
        return false;
    }
}
