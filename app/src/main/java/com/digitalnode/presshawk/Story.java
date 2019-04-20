package com.digitalnode.presshawk;

public class Story {

    private String source;
    private String pubAt;
    private String author;
    private String description;
    private String contentUrl;
    private String imageUrl;
    private String title;
    private String textContent;

    public Story(String source, String pubAt, String author, String description, String contentUrl, String imageUrl, String title, String textContent)
    {
        this.source = source;
        this.pubAt = pubAt;
        this.author = author;
        this.description = description;
        this.contentUrl = contentUrl;
        this.imageUrl = imageUrl;
        this.title = title;
        this.textContent = textContent;
    }

    public String getSource() {
        return source;
    }

    public String getPubAt() {
        return pubAt;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getTextContent() {
        return textContent;
    }
}
