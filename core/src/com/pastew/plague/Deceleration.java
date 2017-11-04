package com.pastew.plague;

enum Deceleration {
    slow(3), normal(2), fast(3);

    private final int decelaration;
    Deceleration(int decelaration) { this.decelaration = decelaration; }
    public int getValue() { return decelaration; }
}
