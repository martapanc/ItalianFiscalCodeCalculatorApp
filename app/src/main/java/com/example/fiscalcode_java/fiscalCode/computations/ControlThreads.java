package com.example.fiscalcode_java.fiscalCode.computations;


class EvenThread implements Runnable {

    private String fisCode;
    private int evenSum;

    public EvenThread(String fisCode, int evenSum) {
        this.fisCode = fisCode;
        this.evenSum = evenSum;
    }

    @Override
    public void run() {
        // calculate sum of character values at even position
        for (int i = 2; i <= 14; i += 2) {
            switch (fisCode.charAt(i - 1)) {
                case '0': {
                    evenSum += 0;
                    break;
                }
                case '1': {
                    evenSum += 1;
                    break;
                }
                case '2': {
                    evenSum += 2;
                    break;
                }
                case '3': {
                    evenSum += 3;
                    break;
                }
                case '4': {
                    evenSum += 4;
                    break;
                }
                case '5': {
                    evenSum += 5;
                    break;
                }
                case '6': {
                    evenSum += 6;
                    break;
                }
                case '7': {
                    evenSum += 7;
                    break;
                }
                case '8': {
                    evenSum += 8;
                    break;
                }
                case '9': {
                    evenSum += 9;
                    break;
                }
                case 'A': {
                    evenSum += 0;
                    break;
                }
                case 'B': {
                    evenSum += 1;
                    break;
                }
                case 'C': {
                    evenSum += 2;
                    break;
                }
                case 'D': {
                    evenSum += 3;
                    break;
                }
                case 'E': {
                    evenSum += 4;
                    break;
                }
                case 'F': {
                    evenSum += 5;
                    break;
                }
                case 'G': {
                    evenSum += 6;
                    break;
                }
                case 'H': {
                    evenSum += 7;
                    break;
                }
                case 'I': {
                    evenSum += 8;
                    break;
                }
                case 'J': {
                    evenSum += 9;
                    break;
                }
                case 'K': {
                    evenSum += 10;
                    break;
                }
                case 'L': {
                    evenSum += 11;
                    break;
                }
                case 'M': {
                    evenSum += 12;
                    break;
                }
                case 'N': {
                    evenSum += 13;
                    break;
                }
                case 'O': {
                    evenSum += 14;
                    break;
                }
                case 'P': {
                    evenSum += 15;
                    break;
                }
                case 'Q': {
                    evenSum += 16;
                    break;
                }
                case 'R': {
                    evenSum += 17;
                    break;
                }
                case 'S': {
                    evenSum += 18;
                    break;
                }
                case 'T': {
                    evenSum += 19;
                    break;
                }
                case 'U': {
                    evenSum += 20;
                    break;
                }
                case 'V': {
                    evenSum += 21;
                    break;
                }
                case 'W': {
                    evenSum += 22;
                    break;
                }
                case 'X': {
                    evenSum += 23;
                    break;
                }
                case 'Y': {
                    evenSum += 24;
                    break;
                }
                case 'Z': {
                    evenSum += 25;
                    break;
                }
            }
        }
    }

    public int getEvenSum() {
        return evenSum;
    }
}

class OddThread implements Runnable {

    private String fisCode;
    private int oddSum;

    public OddThread(String fisCode, int oddSum) {
        this.fisCode = fisCode;
        this.oddSum = oddSum;
    }

    @Override
    public void run() {
        // calculate sum of character values at odd position
        for (int i = 1; i <= 15; i += 2) {
            switch (fisCode.charAt(i - 1)) {
                case '0': {
                    oddSum += 1;
                    break;
                }
                case '1': {
                    oddSum += 0;
                    break;
                }
                case '2': {
                    oddSum += 5;
                    break;
                }
                case '3': {
                    oddSum += 7;
                    break;
                }
                case '4': {
                    oddSum += 9;
                    break;
                }
                case '5': {
                    oddSum += 13;
                    break;
                }
                case '6': {
                    oddSum += 15;
                    break;
                }
                case '7': {
                    oddSum += 17;
                    break;
                }
                case '8': {
                    oddSum += 19;
                    break;
                }
                case '9': {
                    oddSum += 21;
                    break;
                }
                case 'A': {
                    oddSum += 1;
                    break;
                }
                case 'B': {
                    oddSum += 0;
                    break;
                }
                case 'C': {
                    oddSum += 5;
                    break;
                }
                case 'D': {
                    oddSum += 7;
                    break;
                }
                case 'E': {
                    oddSum += 9;
                    break;
                }
                case 'F': {
                    oddSum += 13;
                    break;
                }
                case 'G': {
                    oddSum += 15;
                    break;
                }
                case 'H': {
                    oddSum += 17;
                    break;
                }
                case 'I': {
                    oddSum += 19;
                    break;
                }
                case 'J': {
                    oddSum += 21;
                    break;
                }
                case 'K': {
                    oddSum += 2;
                    break;
                }
                case 'L': {
                    oddSum += 4;
                    break;
                }
                case 'M': {
                    oddSum += 18;
                    break;
                }
                case 'N': {
                    oddSum += 20;
                    break;
                }
                case 'O': {
                    oddSum += 11;
                    break;
                }
                case 'P': {
                    oddSum += 3;
                    break;
                }
                case 'Q': {
                    oddSum += 6;
                    break;
                }
                case 'R': {
                    oddSum += 8;
                    break;
                }
                case 'S': {
                    oddSum += 12;
                    break;
                }
                case 'T': {
                    oddSum += 14;
                    break;
                }
                case 'U': {
                    oddSum += 16;
                    break;
                }
                case 'V': {
                    oddSum += 10;
                    break;
                }
                case 'W': {
                    oddSum += 22;
                    break;
                }
                case 'X': {
                    oddSum += 25;
                    break;
                }
                case 'Y': {
                    oddSum += 24;
                    break;
                }
                case 'Z': {
                    oddSum += 23;
                    break;
                }
            }
        }
    }

    public int getOddSum() {
        return oddSum;
    }
}
