package com.poltora.infres2d.math;

public class PascalTriangle {
    private int[][] values;
    private int n;

    public PascalTriangle() {
        this(6);
    }

    public PascalTriangle(int n) {
        this.n = n;
        values = new int[n + 1][];
        for (int i = 0; i < values.length; i++) {
            values[i] = new int[i + 1];

            values[i][0] = 1;
            values[i][i] = 1;
            for (int j = 1; j < i; j++) {
                values[i][j] = values[i - 1][j - 1] + values[i - 1][j];
            }
        }
    }

    private void generate(int n) {
        int[][] valuesNew;
        int nNew = this.n;

        while (nNew < n) {
            nNew <<= 1;
        }

        valuesNew = new int[nNew + 1][];
        for (int i = 0; i < values.length; i++) {
            valuesNew[i] = new int[i + 1];
            System.arraycopy(values[i], 0, valuesNew[i], 0, values[i].length);
        }

        for (int i = values.length; i < valuesNew.length; i++) {
            valuesNew[i] = new int[i + 1];

            valuesNew[i][0] = 1;
            valuesNew[i][i] = 1;
            for (int j = 1; j < i; j++) {
                valuesNew[i][j] = valuesNew[i - 1][j - 1] + valuesNew[i - 1][j];
            }
        }

        this.n = nNew;
        this.values = valuesNew;
    }

    public int get(int n, int k) {
        if (n > this.n) {
            generate(n);
        }
        return values[n][k];
    }
}
