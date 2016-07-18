package io.github.shakdwipeea.vcr.data;

/**
 * @author Akash Shakdwipeea
 *         Created on 7/18/16
 */
public class BillboardSong {
    private String name;
    private String url;
    private String artist;

    public BillboardSong() {
    }

    public BillboardSong(String name, String url, String artist) {
        this.name = name;
        this.url = url;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
