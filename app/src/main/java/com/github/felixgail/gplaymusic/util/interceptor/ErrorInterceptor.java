package com.github.felixgail.gplaymusic.util.interceptor;

import com.github.felixgail.gplaymusic.exceptions.NetworkException;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Logger;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ErrorInterceptor implements Interceptor {

  private static final Logger logger = Logger.getLogger(ErrorInterceptor.class.getName());
  private Gson gson;
  private InterceptorBehaviour behaviour;

  public ErrorInterceptor(InterceptorBehaviour b) {
    this.behaviour = b;
    gson = new Gson();
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    Response response = chain.proceed(request);
    if (response.code() >= 400) {
      NetworkException networkException = NetworkException.parse(response);
      networkException.setResponse(response);
      if (behaviour == InterceptorBehaviour.THROW_EXCEPTION) {
        throw networkException;
      } else {
        logger.warning(networkException.toString());
      }
    }
    return response;
  }

  public enum InterceptorBehaviour {
    LOG, THROW_EXCEPTION
  }
}
