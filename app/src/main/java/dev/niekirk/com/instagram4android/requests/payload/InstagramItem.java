package dev.niekirk.com.instagram4android.requests.payload;

import java.util.List;

import dev.niekirk.com.instagram4android.storymetadata.StoryPollItem;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * InstagramItem
 *
 * @author Ozan Karaali
 *
 */

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramItem extends StatusResult{
    private final int PHOTO = 1;
    private final int VIDEO = 2;
    private final int ALBUM = 8;
    private String pk;
    private String id;
    private String media_type;
    private String code;
    private String visibility;
    private String taken_at;
    private String device_timestamp;
    private String client_cache_key;
    private String filter_type;
    private InstagramUser user;
    //private InstagramFeedUserTag usertags;
    //private InstagramMedia media;
    //private InstagramStory stories;
    private List<Integer> media_ids;
    private List<StoryPollItem> story_polls;
    private int media_id;
    private String thumbnail_urls;
    private String large_urls;
    private String media_infos;
    private String value;
    private String collapse_comments;
}