package com.tws.shared.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Created by chris on 2/25/16.
 */
public class Utils {

    public static Gson getGson(){
        return new GsonBuilder().create();
    }



}
