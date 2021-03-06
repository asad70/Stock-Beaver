package com.example.stockbeaver;


import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
public class JsonParsing {

    // Take url and convert it to String
    protected static String convertUrlToString(String urlofJson) throws Exception {
        BufferedReader bufferReaderObject = null;
        try {
            // create object of url and pass the url
            URL url = new URL(urlofJson);
            bufferReaderObject = new BufferedReader(new InputStreamReader(url.openStream()));
            // created object of String Buffer
            StringBuffer bufferObject = new StringBuffer();
            int counter;
            char[] charArray = new char[1024];
            while ((counter = bufferReaderObject.read(charArray)) != -1)
                bufferObject.append(charArray, 0, counter);

            Log.d("Tagbuffer", String.valueOf(bufferObject));
            // convert String buffer to String
            return bufferObject.toString();
        }
        finally {
            if (bufferReaderObject != null)
                bufferReaderObject.close();
        }
    }

}