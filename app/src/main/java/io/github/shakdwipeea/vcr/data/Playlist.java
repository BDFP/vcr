package io.github.shakdwipeea.vcr.data;

/**
 * @author Akash Shakdwipeea
 *         Created on 7/18/16
 */
public class Playlist {
    private String name;
    private String author;
    private String url;

    public Playlist() {
    }

    public Playlist(String name, String author, String url) {
        this.name = name;
        this.author = author;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }
}
