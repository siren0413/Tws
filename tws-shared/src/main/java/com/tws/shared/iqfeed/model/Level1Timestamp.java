package com.tws.shared.iqfeed.model;

import java.io.Serializable;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1Timestamp implements Serializable{
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
