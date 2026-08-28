package com.abmaintenance.service;

import com.abmaintenance.entity.Cause;

public class DiagnosticResultat {

    private Cause cause;
    private double score;

    public DiagnosticResultat(
            Cause cause,
            double score) {

        this.cause = cause;
        this.score = score;
    }

    public Cause getCause() {
        return cause;
    }

    public double getScore() {
        return score;
    }
}