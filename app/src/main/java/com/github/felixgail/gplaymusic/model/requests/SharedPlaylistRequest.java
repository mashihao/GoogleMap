package com.github.felixgail.gplaymusic.model.requests;

import com.github.felixgail.gplaymusic.model.Playlist;
import com.github.felixgail.gplaymusic.util.language.Language;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedPlaylistRequest implements Serializable {

  @Expose
  private List<Map<String, String>> entries;

  public SharedPlaylistRequest(Playlist playlist, int maxResults) {
    if (!playlist.getType().equals(Playlist.PlaylistType.SHARED)) {
      throw new IllegalArgumentException(Language.get("api.SharedPlaylistRequest"));
    }
    HashMap<String, String> map = new HashMap<>();
    map.put("shareToken", playlist.getShareToken());
    if (maxResults >= 0 && maxResults <= 1000) {
      map.put("max-results", String.valueOf(maxResults));
    }
    entries = Arrays.asList(map);
  }
}
