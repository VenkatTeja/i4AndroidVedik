package dev.niekirk.com.instagram4android.storymetadata;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * StoryPollItem
 * @author Justin Vo
 *
 */
@Getter
@Setter
public class StoryPollItem {
    private String x;
    private String y;
    private String z;
    private double width;
    private double height;
    private double rotation;
    private Map<String, Object> poll_sticker;
}