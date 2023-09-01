package com.code.binarysearch;

public class searchInRotatedAndSortedArray {

    public static void main(String[] args) {

        //int arr[] = {9,11,14,15,20,22,25,1,3,5,7};
        //int arr[] = {1,3,5,7,9,11,14,15,20,22,25};
        int arr[] = {14,15,20,22,25,1,3,5,7,9,11};
        System.out.println(findPivot(arr , 0 , arr.length-1));

    }

    public static int findPivot(int[] arr , int low , int high) {

        if(high <= low) {
            System.out.println("test");
            return -1;
        }
        int mid = (high+low)/2;
        if(arr[mid] < arr[mid-1]) {
            return mid;
        }
        else if (arr[mid] > arr[mid+1]) {
            return mid+1;
        }
        if(arr[low] > arr[mid]) {
            return findPivot(arr , low , mid-1);
        }
        if(arr[mid] > arr[high]) {
            return findPivot(arr, mid+1 , high);
        }

        return -1;

    }
}
