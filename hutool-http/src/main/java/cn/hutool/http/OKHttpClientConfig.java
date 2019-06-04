package cn.hutool.http;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * public 直接访问，方便直接重定义
 */
public class OKHttpClientConfig {

  public static OkHttpClient client;
  public static Map<String, List<Cookie>> cookieStore = new HashMap<>();

  static {
    init();
  }

  private static void init() {
    OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
    mBuilder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory());
    mBuilder.hostnameVerifier(new TrustAllHostnameVerifier())
        .cookieJar(new CookieJar() {
          @Override
          public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
            cookieStore.put(httpUrl.host(), list);
          }

          @Override
          public List<Cookie> loadForRequest(HttpUrl httpUrl) {
            List<Cookie> cookies = cookieStore.get(httpUrl.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
          }
        });
    client = mBuilder.build();
  }

  public static void main(String[] args) throws Exception {

  }

  static class TrustAllCerts implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[0];
    }

    static SSLSocketFactory createSSLSocketFactory() {
      SSLSocketFactory factory = null;
      try {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
        factory = context.getSocketFactory();
      } catch (Exception e) {
      }
      return factory;
    }


  }

  static class TrustAllHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }
}
