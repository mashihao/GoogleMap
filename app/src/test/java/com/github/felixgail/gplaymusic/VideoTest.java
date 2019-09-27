package com.github.felixgail.gplaymusic;

import android.util.Log;

import com.github.felixgail.gplaymusic.model.Video;
import com.github.felixgail.gplaymusic.model.requests.SearchTypes;
import com.github.felixgail.gplaymusic.util.TestUtil;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import svarzee.gps.gpsoauth.Gpsoauth;

import static android.content.ContentValues.TAG;

public class VideoTest extends TestWithLogin {

  @BeforeClass
  public static void before() throws IOException, Gpsoauth.TokenRequestFailed {
    loginToService(TestUtil.USERNAME, TestUtil.PASSWORD, TestUtil.ANDROID_ID, TestUtil.TOKEN);
  }

  //Feature seems to be desabled at the moment.
  @Test
  public void testVideo() throws IOException {
    List<Video> videos = getApi()
        .search("", new SearchTypes(Video.RESULT_TYPE)).getVideos();
    TestUtil.assume(videos);
    Log.e(TAG, "List of videos is empty." );
    Assume.assumeTrue( videos.size() > 0);
    System.out.printf("Empty search for videos yielded %d results\n", videos.size());
    videos.forEach(v -> {
      Assert.assertNotNull("Video is null", v);
      Assert.assertNotNull("ID is null", v.getId());
      Assert.assertTrue("ID is empty", !v.getId().isEmpty());
    });
  }
}
