package com.code.dp;

public class fabonacci {

    public static void main(String[] args) {
        int n = 10;
        int arr[] = new int[n+1];
        //Memoization
        System.out.println(calcFabonacci(n , arr));
        //Tabulization
        System.out.println(calcFabonacciTab(n , arr));
    }

    public static int calcFabonacci(int n , int[] dp) {
        if(n ==0 || n== 1) {
            return n;
        }
        if(dp[n] != 0) {
            return dp[n];
        }
        dp[n] = calcFabonacci(n-1,dp)+calcFabonacci(n-2,dp);
        return dp[n];
    }

    public static int calcFabonacciTab(int n , int[] dp) {

        dp[0] = 0;
        dp[1] = 1;
        for(int i=2 ; i<=n;i++) {
            dp[i] = dp[i-1]+dp[i-2];
        }
        return dp[n];
    }

}
