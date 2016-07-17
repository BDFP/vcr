package io.github.shakdwipeea.vcr.data;

/**
 * Created by akash on 7/3/16.
 */
public class Song {
    private boolean success;
    private String url;

    public Song(boolean success, String url) {
        this.success = success;
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getUrl() {
        return url;
    }
}
