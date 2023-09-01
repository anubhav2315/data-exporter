package com.code.dp;

import java.util.ArrayList;
import java.util.List;

public class LongestStrictlyIncreasingSubsequence {



    static int maxResultGlobal = 0;



    public static void main(String[] args) {


        //int arr[] = {5,4,3,2,1};
        int arr[] = {10,2,9,5,7,3,60,80,1};
        // first approach that came into mind --> lots of unnecessary recursion
        //fetchLongestIncreasingSubsequence(arr , arr.length ,0, 0 , new boolean[arr.length]);
       // fetchAllSubsequences(arr , arr.length , 0 , -1 , new ArrayList<Integer>());
        //System.out.println(fetchIncreasingSubsequenceMem(arr , 0 , -1 , new int[arr.length][arr.length]));


        fetchIncreasingSubsequenceTab(arr , new int[arr.length]);

        //find all the possible solutions

        //System.out.println(maxResultGlobal);
    }


    public static void fetchIncreasingSubsequenceTab(int arr[] , int dp[]) {
        int j=arr.length-1;
        dp[j] = 0;
        j--;
        int maxCount = 1;
        while (j>=0) {
            for(int i=j+1;i< arr.length;i++) {
                if(arr[j] < arr[i]) {
                    if(dp[i]+1 > dp[j]) {
                        dp[j] = dp[i] + 1;
                    }
                }

            }
            j--;
        }
        System.out.println("test");

    }





    public static int fetchIncreasingSubsequenceMem(int arr[] , int currentIndex , int prevIndex , int dp[][]) {

        if(currentIndex == arr.length) {
            return 0;
        }

        if((prevIndex != -1) && dp[currentIndex][prevIndex] >0 ) {
            return dp[currentIndex][prevIndex];
        }
        // Case of rejection of element
        int ans1 = 0+fetchIncreasingSubsequenceMem(arr , currentIndex+1 , prevIndex , dp);
        // Case of selection
        int ans2 = 0;
        if(prevIndex == -1 || arr[currentIndex] > arr[prevIndex]) {
            ans2 =  1+fetchIncreasingSubsequenceMem(arr , currentIndex+1 , currentIndex , dp);
        }
        if(prevIndex != -1) {
            dp[currentIndex][prevIndex] = Math.max(ans1 , ans2);
        }
        return Math.max(ans1 , ans2);
    }



    public static void fetchAllSubsequences(int[] arr , int n , int currentIndex , int nextIndex , List<Integer> ans ) {

        if(ans.size() >= 2) {
            if(ans.get(ans.size()-1) < ans.get(ans.size()-2)) {
                return ;
            }
        }
        if(currentIndex==arr.length) {
            System.out.println("test");
            System.out.println("ans :"+ ans);
            return ;
        }


        ans.add(arr[currentIndex]);
        fetchAllSubsequences(arr , n , currentIndex+1 , nextIndex+1 , ans );
        ans.remove(ans.size()-1);
        fetchAllSubsequences(arr , n , currentIndex+1 , nextIndex+1 , ans);

    }


    public static void fetchLongestIncreasingSubsequence(int arr[] , int n,int index, int maxResult , boolean[] selected ) {

        if(index==n) {
            return ;
        }

        if(index ==0) {
            for (int i=0;i< n;i++) {
                fetchLongestIncreasingSubsequence(arr , n , i+1 , maxResult+1, selected);

            }
            return;
        }



        for(int i=index;i<n;i++) {
            if(arr[index-1] < arr[i]) {
                fetchLongestIncreasingSubsequence(arr , n , i+1 , maxResult+1 , selected);

            }
        }

        maxResultGlobal = Math.max(maxResult , maxResultGlobal) ;
    }
}
