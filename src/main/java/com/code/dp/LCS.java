package com.code.dp;

public class LCS {


    public static void main(String[] args) {

        String str1 = "abcd";
        String str2 = "acdb";


        System.out.println(lcs(str1 , str2 , 0 , 0 ));
        System.out.println(lcsScalar(str1, str2 ,0,0));
        System.out.println(lcsScalarDp(str1, str2 ,0,0 , new int[str1.length()][str2.length()]));
        lcsTab(str1 , str2 , new int[str1.length()+1][str2.length()+1]);
    }



    public static void lcsTab(String str1 , String str2 , int dp[][] ) {


        for(int i=1;i<=str1.length();i++) {
            for(int j=1;j<=str2.length();j++) {
                if(str1.charAt(i-1) == str2.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1]+1;
                }
                else {
                    dp[i][j] = Math.max(dp[i][j-1] , dp[j][i-1]);
                }
            }
        }

        System.out.println("");

    }


    public static int lcsScalar(String str1 , String str2 , int i , int j) {

        if(i == str1.length() || j == str2.length()) {
            return 0;
        }

        int ans = 0;
        if(str1.charAt(i) == str2.charAt(j)) {
            ans = 1+lcsScalar(str1 , str2 , i+1 , j+1);
        }
        else {
            ans = Math.max(lcsScalar(str1 , str2 , i , j+1),lcsScalar(str1 , str2 , i+1 , j));
        }
        return ans;
    }

    public static int lcsScalarDp(String str1 , String str2 , int i , int j , int[][] dp) {

        if(i == str1.length() || j == str2.length()) {
            return 0;
        }

        int ans = 0;
        if(dp[i][j] != 0) {
            return dp[i][j];
        }
        if(str1.charAt(i) == str2.charAt(j)) {
            ans = 1+lcsScalarDp(str1 , str2 , i+1 , j+1 , dp);
        }
        else {
            ans = Math.max(lcsScalarDp(str1 , str2 , i , j+1 , dp),lcsScalarDp(str1 , str2 , i+1 , j,dp));
        }
        dp[i][j] = ans;
        return ans;
    }






    public static int lcs(String str1 , String str2 , int index , int maxIndex) {

        if(index == str1.length()) {
            return 0;
        }

        int ans1 = 0+ lcs(str1 , str2 , index+1 , maxIndex);
        int ans2 = 0 ;

        int num = fetch(str2 , str1.charAt(index) , maxIndex);
        if(num != -1) {
            maxIndex =  num+1;
            ans2 = 1+lcs(str1, str2 , index+1 , maxIndex);
        }
        return Math.max(ans1,ans2);

    }


    public static int fetch(String str , Character ch , int maxIndex) {

        for(int i=maxIndex;i<str.length();i++) {
            if(str.charAt(i) == ch) {
                return i;
            }
        }
        return -1 ;
    }
}
