package com.code.binarysearch;

public class FirstAndLastPosition {


    //Another approach to run a loop once an element in binary search is found

    static int minIndex = Integer.MAX_VALUE;
    static int maxIndex = Integer.MIN_VALUE;
    public static void main(String[] args) {
        int arr[] = {1,1,2,3,5,5,5,5,5,5,7,7,7,7,7,8,8};
        int target = 5;
        binarySearch(arr , 0 , arr.length-1 , target);
        System.out.println("Min Index :"+ minIndex);
        System.out.println("Max Index :"+ maxIndex);
    }


    public static void setMinIndex(int tempMin) {
        if(tempMin < minIndex) {
            minIndex = tempMin;
        }
    }
    public static void setMaxIndex(int tempMax) {
        if(tempMax > maxIndex) {
            maxIndex = tempMax;
        }
    }

    public static void binarySearch(int arr[] , int low , int high , int target) {
        if(low > high) {
            return ;
        }
        if(low == high) {
            if(arr[low] == target) {
                setMinIndex(low);
                setMaxIndex(high);
            }
        }
        int mid = (low+high)/2;
        if(arr[mid] == target) {
            if(arr[mid-1] < arr[mid]) {
                setMinIndex(mid);
            }
            else {
                binarySearch(arr , low , mid-1 , target);
            }
            if(arr[mid+1]> arr[mid]) {
                setMaxIndex(mid);
            }
            else {
                binarySearch(arr , mid+1 , high , target);
            }
        }
        else if(arr[mid] > target) {
            binarySearch(arr , low , mid-1 , target);
        }
        else {
            binarySearch(arr , mid+1 , high , target);
        }
    }
}
