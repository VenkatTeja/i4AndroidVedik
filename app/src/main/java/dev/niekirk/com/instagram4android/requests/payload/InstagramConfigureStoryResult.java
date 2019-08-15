package dev.niekirk.com.instagram4android.requests.payload;

import java.util.List;

import dev.niekirk.com.instagram4android.requests.payload.InstagramItem;
import dev.niekirk.com.instagram4android.requests.payload.StatusResult;

import lombok.Getter;
import lombok.Setter;

/**
 * InstagramConfigureStoryResult
 * @author Justin Vo
 *
 */
@Getter
@Setter
public class InstagramConfigureStoryResult extends StatusResult {
    private InstagramItem media;
    private List<Object> message_metadata;
}