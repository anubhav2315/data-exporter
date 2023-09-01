package com.code.backtracking;

import java.util.ArrayList;
import java.util.Arrays;

public class PrintDistinctPermutation {


    public static void main(String[] args) {
        int arr[] = {1,2,3};
        findPermutation(arr , 0, new boolean[arr.length] , new ArrayList<>());
    }

    public static void findPermutation(int[] arr , int pos , boolean[] selected , ArrayList<Integer> ans) {
        if(pos == arr.length) {
            System.out.println(ans);
            return ;
        }
        for(int i=0;i<arr.length;i++) {
            if(!selected[i]) {
                ans.add(arr[i]);
                selected[i] = true;
                findPermutation(arr ,pos+1 ,selected , ans  );
                //have to remove the selection post coming back so other fields can also be selected
                selected[i] = false;
                // remove last element from list so new fresh element can occupy
                ans.remove(ans.size()-1);
            }
        }

    }
}
