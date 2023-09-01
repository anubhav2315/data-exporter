package com.code.dp;

public class ClimbStair1 {

    public static void main(String[] args) {
        int n=4;
        System.out.println(fetchNoOfPath(n));
        System.out.println(fetchNoOfPathTab(n , new int[n+1]));
    }

    public static int fetchNoOfPath(int n) {

        if(n==0) {
            return 1;
        }
        if(n < 0) {
            return 0;
        }
       return  fetchNoOfPath(n-1)+ fetchNoOfPath(n-2)+fetchNoOfPath(n-3);
    }


    public static int fetchNoOfPathTab(int n , int dp[]) {

        if(n==0) {
            return 1 ;
        }
       dp[1] = 1;
       dp[2] = 2;
       dp[3] = 4;
       for(int i=4;i<=n;i++) {
           dp[n] = dp[n-3]+ dp[n-2] + dp[n-1];
       }

       return dp[n];


    }



}
