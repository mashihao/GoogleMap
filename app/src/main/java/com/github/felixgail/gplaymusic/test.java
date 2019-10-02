package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.util.TokenProvider;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;
//AuthToken{token='owfYC_fys9FPUgWfqursNFVpia48lF-DgUIuV0TwQBGZIEmbz-oqvYlmBcQvuiS_2RdyVw.', expiry=-1}
public class test {
    private static String USERNAME = "x814850963@gmail.com";
    private static String PASSWORD = "pwls6666";
    private static String ANDROID_ID = "3d422fb1d1dc4a40";
    public static GPlayMusic api;
    public static AuthToken authToken = null;
    public static GPlayMusic connect(){
        ArrayList<URL> tracksUrl=new ArrayList<>();
        try {
            authToken = TokenProvider.provideToken(USERNAME,
                   PASSWORD, ANDROID_ID);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Gpsoauth.TokenRequestFailed tokenRequestFailed) {
            tokenRequestFailed.printStackTrace();
        }
        api = new GPlayMusic.Builder().setAuthToken(authToken).build();
        return api;


        //AuthToken authToken = new AuthToken("aas_et/AKppINZm2jnntEgHZelwqSiwmqA3HM0uJDijY6kUcoj-RRrv6-bXm4Qb_U7of86t3z_H4DOqdeR0fKAA4zzwkKr0VvqCuhZYJPzy14CrnMsGZEz0qM0V_4iQltBrLzWhtA==",1000L);

    }

    public static void main(String[]args) throws InterruptedException {
        api = test.connect();
       int i=0;
        while(true)
        {
            Thread.sleep(1000);
            System.out.println(api.toString());
        }
    }
}
