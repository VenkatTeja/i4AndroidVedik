package dev.niekirk.com.instagram4android.storymetadata;

public abstract class StoryMetadata {

    public abstract String key();

    public abstract String metadata();

    public abstract boolean check() throws IllegalArgumentException;

}