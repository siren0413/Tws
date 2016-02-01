package com.tws.zeromq;

/**
 * Created by admin on 1/31/2016.
 */
public class Tuple {
    String a;
    String b;

    public Tuple(String a, String b) {
        this.a = a;
        this.b = b;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
