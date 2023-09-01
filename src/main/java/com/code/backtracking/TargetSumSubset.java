package com.code.backtracking;

import java.util.ArrayList;
import java.util.List;

public class TargetSumSubset {

    public static void main(String[] args) {

        int arr[] = {10,20,30,40,50,60};
        int target = 60;
        findTargetSumSubsets(arr, 0, target, new ArrayList<Integer>());

    }

//    public static void findTargetSumSubsets(int[] arr , int index , int target , List<Integer> ans) {
//        if(index >= arr.length) {
//            return ;
//        }
//        if(target ==0) {
//            System.out.println(ans);
//            return ;
//        }
//        if(target < 0) {
//            ans.remove(arr[index]);
//            target = target+arr[index];
//            index = index+1;
//            return ;
//        }
//
//        if(target > 0) {
//            ans.add(arr[index]);
//            findTargetSumSubsets(arr , index+1 , target - arr[index] , ans);
//            ans.remove(ans.size()-1);
//            return;
//        }
//        findTargetSumSubsets(arr , index+1 , target - arr[index] , ans);
//    }


    public static void findTargetSumSubsets(int[] arr , int index , int target , List<Integer> ans) {
//        if(target  < 0) {
//            return ;
//        }
        if(index == arr.length) {
            if(target == 0) {
                System.out.println(ans);
            }

            return ;
        }
        //selects the ith element
        ans.add(arr[index]);
        findTargetSumSubsets(arr ,index+1 , target-arr[index] , ans );
        ans.remove(ans.size()-1);

        //rejects the element ;
        findTargetSumSubsets(arr , index+1,target , ans);
    }

}
