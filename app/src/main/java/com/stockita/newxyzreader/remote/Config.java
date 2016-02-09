package com.stockita.newxyzreader.remote;

import android.support.design.widget.Snackbar;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class will only have the URL address to fetch data from
 */
public class Config {

    // The URL object
    public static final URL BASE_URL;

    /**
     * Static block
     */
    static {
        URL url = null;
        try {
            url = new URL("https://dl.dropboxusercontent.com/u/231329/xyzreader_data/data.json" );
        } catch (MalformedURLException ignored) {


        }

        BASE_URL = url;
    }
}
