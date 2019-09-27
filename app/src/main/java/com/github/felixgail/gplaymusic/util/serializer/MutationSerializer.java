package com.github.felixgail.gplaymusic.util.serializer;

import com.github.felixgail.gplaymusic.model.requests.mutations.Mutation;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class MutationSerializer implements JsonSerializer<Mutation> {

  private final static Gson gson = new Gson();

  @Override
  public JsonElement serialize(Mutation mutation, Type type,
      JsonSerializationContext jsonSerializationContext) {
    String elementName = mutation.getSerializedAttributeName();
    JsonObject mutationJSON = new JsonObject();
    mutationJSON.add(elementName, gson.toJsonTree(mutation.getMutation()));
    return mutationJSON;
  }
}
