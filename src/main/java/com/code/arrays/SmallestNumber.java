package com.code.arrays;

public class SmallestNumber {


    // we can also use array of numbers with freq defined instead of string s
    public static void main(String[] args) {

        int result = 100;
        String s = "";
        int last = -1;
        for(int i=9;i>1;i--) {
            if(result%i == 0) {
                result = result/i;
                s = i+s;
                i++;
            }
        }
        if(result == 1) {
            System.out.println(s);
        }
        else {
            System.out.println("Not possible");
        }


    }
}
