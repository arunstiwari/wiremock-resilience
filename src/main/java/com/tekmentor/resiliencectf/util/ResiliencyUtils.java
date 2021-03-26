package com.tekmentor.resiliencectf.util;

import java.util.Arrays;
import java.util.StringTokenizer;

public class ResiliencyUtils {

    public static String[] parseDependentUrls(String urls){
        StringTokenizer tokens = new StringTokenizer( urls, ",", false );
        String[] result = new String[tokens.countTokens()];
        int i = 0;
        while ( tokens.hasMoreTokens() ) {
            result[i++] = tokens.nextToken();
        }
        System.out.println("result = " + Arrays.toString(result));
        return result;
    }

}
