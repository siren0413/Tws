package com.tws.shared;

/**
 * Created by chris on 1/18/16.
 */
public interface Marshaller {
    String marshal(Object obj);
    Object unmarshal(String text);
}
