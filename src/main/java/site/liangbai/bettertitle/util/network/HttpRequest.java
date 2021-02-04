package site.liangbai.bettertitle.util.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public final class HttpRequest {
    private final URLConnection urlConnection;

    public HttpRequest(String url) throws IOException {
        this.urlConnection = (new URL(url)).openConnection();
    }

    public URLConnection getUrlConnection() {
        return urlConnection;
    }

    public void putRequestProperty(String key, String value) {
        this.urlConnection.setRequestProperty(key, value);
    }

    public String getCharset() {
        return "utf-8";
    }

    public String sendRequestAndReturn() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.urlConnection.getInputStream(), Charset.forName(this.getCharset())));
        StringBuilder sb = new StringBuilder();

        String line;
        while((line = reader.readLine()) != null) {
            sb.append(line);
        }

        reader.close();
        return sb.toString();
    }
}
