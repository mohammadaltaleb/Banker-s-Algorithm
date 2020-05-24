package com.mohammadaltaleb.bankersalgorithm;

class MatrixUtils {

    static boolean lessThanOrEquals(int[] a, int[] b) {
        for (int i = 0, n = a.length; i < n; i++) {
            if (a[i] > b[i]) {
                return false;
            }
        }
        return true;
    }

    static int[] add1D(int[] a, int[] b) {
        int[] t = new int[a.length];
        for (int i = 0, n = a.length; i < n; i++) {
            t[i] = a[i] + b[i];
        }
        return t;
    }

    static int[] subtract1D(int[] a, int[] b) {
        int[] t = new int[a.length];
        for (int i = 0, n = a.length; i < n; i++) {
            t[i] = a[i] - b[i];
        }
        return t;
    }

    static void print1D(int[] a) {
        for (int i = 0, n = a.length; i < n; i++) {
            System.out.print(a[i] + " ");
        }
    }
}
