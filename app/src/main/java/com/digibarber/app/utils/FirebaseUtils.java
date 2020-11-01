package com.digibarber.app.utils;

import com.digibarber.app.apicalls.ApiClient;

public class FirebaseUtils {
    private static FirebaseUtils client;
    private FirebaseUtils(){
    }
    public static FirebaseUtils getInstance(){
        if(client == null){
            client = new FirebaseUtils();
        }
        return client;
    }
}