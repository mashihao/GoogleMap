package com.github.felixgail.gplaymusic;

import android.util.Log;

import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.util.TestUtil;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import svarzee.gps.gpsoauth.Gpsoauth;

import static android.content.ContentValues.TAG;

public class FreeUserTest extends TestWithLogin {

  @BeforeClass
  public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
    loginToService(TestUtil.FREE_USERNAME, TestUtil.FREE_PASSWORD,
        TestUtil.FREE_ANDROID_ID, TestUtil.FREE_TOKEN);
  }

  @Test
  public void testLibrarySongDownload() throws IOException {
    List<Track> tracks = getApi().getTrackApi().getLibraryTracks();
    Log.e(TAG, "testLibrarySongDownload: " );
    Assume.assumeTrue( tracks.size() > 0);
    Track track = tracks.get(0);
    TestUtil.testDownload("FreeUser_LibraryTrackDownloadTest.mp3", track);
  }

}
