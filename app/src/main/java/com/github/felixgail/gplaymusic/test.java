package com.github.felixgail.gplaymusic;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.api.PlaylistApi;
import com.github.felixgail.gplaymusic.model.Playlist;
import com.github.felixgail.gplaymusic.model.PlaylistEntry;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.util.TokenProvider;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;
//AuthToken{token='owfYC_fys9FPUgWfqursNFVpia48lF-DgUIuV0TwQBGZIEmbz-oqvYlmBcQvuiS_2RdyVw.', expiry=-1}
public class test {
    private static String USERNAME = "x814850963@gmail.com";
    private static String PASSWORD = "yht1998520";
    private static String ANDROID_ID = "3d422fb1d1dc4a40";

    public static void main(String [] args)
    {
        AuthToken authToken;
        GPlayMusic api;
        ArrayList<URL> tracksUrl=new ArrayList<>();
        try {
             authToken = TokenProvider.provideToken(USERNAME,
                    PASSWORD, ANDROID_ID);
             api = new GPlayMusic.Builder().setAuthToken(authToken).build();
            ArrayList<Track> tracks = (ArrayList<Track>) api.getPromotedTracks();
            System.out.println(tracks.size());
            for (Track t: tracks) {
                if(null !=t)
                {
                    System.out.println(t.getStreamURL(StreamQuality.HIGH).toString());
                    tracksUrl.add(t.getStreamURL(StreamQuality.MEDIUM));
                }
            }
            PlaylistApi listApi = api.getPlaylistApi();
            List<Playlist> playlists = listApi.listPlaylists();
            ArrayList<List<PlaylistEntry>> listlistEntry = new ArrayList<>();
            for (Playlist pl: playlists) {
                if(null != pl)
                    listlistEntry.add(pl.getContents(100));
            }
            for (List<PlaylistEntry> entryList:listlistEntry) {
                for (PlaylistEntry pl:entryList) {
                    System.out.println(pl.getTrack().getTitle());
                    System.out.println(pl.getTrack().getStreamURL(StreamQuality.MEDIUM));
                }
            }

//            for (String url: tracksUrl) {
//                //System.out.println(url);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Gpsoauth.TokenRequestFailed tokenRequestFailed) {
            tokenRequestFailed.printStackTrace();
        }

        //AuthToken authToken = new AuthToken("aas_et/AKppINZm2jnntEgHZelwqSiwmqA3HM0uJDijY6kUcoj-RRrv6-bXm4Qb_U7of86t3z_H4DOqdeR0fKAA4zzwkKr0VvqCuhZYJPzy14CrnMsGZEz0qM0V_4iQltBrLzWhtA==",1000L);


    }
}
