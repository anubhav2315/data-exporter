package com.code.arrays;

import java.util.Arrays;

public class ThreeSum {
    public static void main(String[] args) {

        int arr[] = {2,1,-2,2,-1,3,4,5,2,3,6,4,-1};
        Arrays.sort(arr);
        System.out.println(arr);
        int target = 6;
        int firstStoredElement = -1;
        int lastStoredElement = -1;
        for(int i=0;i<arr.length;i++) {
            int j=i+1;
            int k = arr.length -1;
            while (j<=k) {
                int sum = arr[j]+arr[k];
                if(firstStoredElement != -1 && arr[j] == arr[firstStoredElement]) {
                    firstStoredElement = j;
                    j++;
                }
                else if(lastStoredElement != -1 && arr[k] == arr[lastStoredElement]) {
                    lastStoredElement = k;
                    k--;
                }
                else if(sum == (target- arr[i])) {
                    System.out.println(arr[i] + "   " + arr[j] + "  " + arr[k]);
                    firstStoredElement = j;
                    lastStoredElement = k;
                    j++;
                    k--;
                }
                else if(sum < (target-arr[i])) {
                    firstStoredElement = j;
                    j++;
                }
                else {
                    lastStoredElement = k;
                    k--;
                }
            }
        }
    }
}
