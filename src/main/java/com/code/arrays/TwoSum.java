package com.code.arrays;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

public class TwoSum {

    public static void main(String[] args) {
        int arr[] = {11,3,7,9,14,2};
        int result[] = new int[2];
        Map<Integer , Integer> hm = new HashedMap();
        int target = 17;
        for(int i=0;i< arr.length;i++) {

            if(hm.containsKey(target-arr[i])) {
                System.out.println(hm.get(target-arr[i]));
                System.out.println(i);
            }
            else {
                hm.put(arr[i],i );
            }
        }
    }
}
