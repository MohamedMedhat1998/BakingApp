package com.andalus.abomed7at55.bakingapp.Recipes;

/**
 * This class represents a single step of the recipe
 */

public class Step {

    private String id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;


    public Step(String id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public String getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
