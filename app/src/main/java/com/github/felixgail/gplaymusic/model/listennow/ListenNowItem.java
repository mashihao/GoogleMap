package com.github.felixgail.gplaymusic.model.listennow;

import com.github.felixgail.gplaymusic.model.snippets.ArtRef;
import com.github.felixgail.gplaymusic.util.deserializer.ListenNowItemDeserializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * A ListenNowItem is a listen suggestion by the Server. It considers songs rated by you and recent
 * activity.
 */
@JsonAdapter(ListenNowItemDeserializer.class)
public abstract class ListenNowItem implements Serializable {

  @Expose
  private List<ArtRef> compositeArtRefs;
  @Expose
  private List<ArtRef> images;
  @Expose
  @SerializedName("suggestion_reason")
  private String suggestionReason;
  @Expose
  @SerializedName("suggestion_text")
  private String suggestionText;

  public Optional<List<ArtRef>> getCompositeArtRefs() {
    return Optional.ofNullable(compositeArtRefs);
  }

  public Optional<List<ArtRef>> getImages() {
    return Optional.ofNullable(images);
  }

  public String getSuggestionReason() {
    return suggestionReason;
  }

  public String getSuggestionText() {
    return suggestionText;
  }
}
