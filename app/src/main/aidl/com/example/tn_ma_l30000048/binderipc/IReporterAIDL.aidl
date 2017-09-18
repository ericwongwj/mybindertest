// IReporterAIDL.aidl
package com.example.tn_ma_l30000048.binderipc;

// Declare any non-default types here with import statements

interface IReporterAIDL {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    int report(String values, int type);

}
