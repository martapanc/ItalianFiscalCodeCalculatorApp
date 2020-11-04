package com.pancaldim.fiscalcode_app.fiscalcode.computations;


import static com.pancaldim.fiscalcode_app.fiscalcode.constants.FiscalCodeConstants.getEvenPositionMap;
import static com.pancaldim.fiscalcode_app.fiscalcode.constants.FiscalCodeConstants.getOddPositionMap;

class EvenThread implements Runnable {
    private final String fisCode;
    private int evenSum;

    public EvenThread(String fisCode, int evenSum) {
        this.fisCode = fisCode;
        this.evenSum = evenSum;
    }

    @Override
    public void run() {
        for (int i = 2; i <= 14; i += 2) {
            evenSum += getEvenPositionMap().get(fisCode.charAt(i - 1));
        }
    }

    public int getEvenSum() {
        return evenSum;
    }
}

class OddThread implements Runnable {
    private final String fisCode;
    private int oddSum;

    public OddThread(String fisCode, int oddSum) {
        this.fisCode = fisCode;
        this.oddSum = oddSum;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 15; i += 2) {
            oddSum += getOddPositionMap().get(fisCode.charAt(i - 1));
        }
    }

    public int getOddSum() {
        return oddSum;
    }
}
