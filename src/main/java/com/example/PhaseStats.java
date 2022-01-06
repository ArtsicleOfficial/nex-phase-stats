package com.example;

public class PhaseStats {
    public PhaseStats(int phaseNumber, int hits, int misses) {
        this.phaseNumber = phaseNumber;
        this.hits = hits;
        this.misses = misses;
    }
    public PhaseStats(int phaseNumber) {
        this.phaseNumber = phaseNumber;
        this.hits = 0;
        this.misses = 0;
    }

    public int phaseNumber;
    public int hits;
    public int misses;
}
