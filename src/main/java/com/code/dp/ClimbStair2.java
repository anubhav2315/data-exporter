package com.code.dp;

public class ClimbStair2 {


    static int globalMinCost = Integer.MAX_VALUE;
    public static void main(String[] args) {

        int arr[] = {1,2,4,3,6,5,2,7,1};
        fetchMinCosts(arr , arr.length-1,0);
        System.out.println(fetchMinCostsTab(arr , arr.length-1 ,new int[arr.length] ));
        System.out.println(fetchMinCost(arr , arr.length, new int[arr.length]));
        System.out.println(globalMinCost);
    }




    public static int fetchMinCostsTab(int[] arr , int n , int dp[] ) {
        dp[0] = arr[0];
        dp[1] = arr[0]+arr[1];
        for(int i = 2;i< arr.length;i++) {
            int first = dp[i-2]+ arr[i];
            int second = dp[i-1] + arr[i];
            if(first < second) {
                dp[i] = first;
            }
            else {
                dp[i] = second;
            }
        }
        return dp[n];

    }

    

    public static void fetchMinCosts(int[] arr , int n , int minCost) {
        if(n == 0) {
            minCost = minCost + arr[0];
            if(minCost < globalMinCost) {
                globalMinCost = minCost;
            }
            return ;
        }
        if(n<0) {
            return ;
        }

        fetchMinCosts(arr , n-1 , minCost+arr[n]);
        fetchMinCosts(arr , n-2 , minCost+arr[n]);

    }



    public static int fetchMinCost(int arr[] , int n , int dp[]) {
        if(n==1) {
            return arr[0];
        }
        if(n==2) {
            return arr[0]+arr[1];
        }

        int f1 = dp[n-1];
        int f2 = dp[n-2];
        if(f1 == 0) {
            f1 = fetchMinCost(arr , n-1,dp);
        }
        if(f2 == 0) {
            f2 = fetchMinCost(arr , n-2,dp);
        }
        int ans = Math.min(f1,f2)+arr[n-1];
        return ans ;
    }
}
