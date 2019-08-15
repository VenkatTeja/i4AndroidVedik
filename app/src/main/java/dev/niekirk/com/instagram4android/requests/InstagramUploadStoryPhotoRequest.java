package dev.niekirk.com.instagram4android.requests;


import java.io.File;
import java.io.IOException;
import java.util.Collection;

import android.util.Log;
import okhttp3.*;
import dev.niekirk.com.instagram4android.InstagramConstants;
import dev.niekirk.com.instagram4android.requests.internal.InstagramConfigureStoryRequest;
import dev.niekirk.com.instagram4android.requests.internal.InstagramExposeRequest;
import dev.niekirk.com.instagram4android.requests.payload.InstagramConfigureStoryResult;
import dev.niekirk.com.instagram4android.requests.payload.StatusResult;
import dev.niekirk.com.instagram4android.storymetadata.StoryMetadata;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * InstagramStoryPhotoUploadRequest
 * @author Justin Vo
 *
 */
@AllArgsConstructor
@RequiredArgsConstructor
public class InstagramUploadStoryPhotoRequest extends InstagramPostRequest<InstagramConfigureStoryResult> {

    @NonNull
    private File imageFile;

    private Collection<StoryMetadata> metadata = null;

    private String threadId = null;

    public InstagramUploadStoryPhotoRequest(File img, Collection<StoryMetadata> meta) {
        this.imageFile = img;
        this.metadata = meta;

        System.out.println(img);
    }

    @Override
    public String getUrl() {
        return "upload/photo/";
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public InstagramConfigureStoryResult execute() throws IOException {

        String uploadId = null;

        if (uploadId == null) {
            uploadId = String.valueOf(System.currentTimeMillis());
        }

        System.out.println(uploadId);
        Request post = createHttpRequest(createMultipartBody(uploadId));

        try (Response response = api.getClient().newCall(post).execute()) {
            api.setLastResponse(response);

            int resultCode = response.code();
            String content = response.body().string();
            System.out.println("First phase result " + resultCode + ": " + content);
            // Log.d("UPLOAD", "First phase result " + resultCode + ": " + content);

//            post.releaseConnection();

            StatusResult result = parseResult(resultCode, content);

            if (!result.getStatus().equalsIgnoreCase("ok")) {
                throw new RuntimeException("Error happened in photo upload: " + result.getMessage());
            }

            InstagramConfigureStoryResult configurePhotoResult = api.sendRequest(new InstagramConfigureStoryRequest(imageFile, uploadId, threadId, metadata));

            // Log.i("UPLOAD", "Configure photo result: " + configurePhotoResult);
            if (!configurePhotoResult.getStatus().equalsIgnoreCase("ok")) {
                throw new IllegalArgumentException("Failed to configure image: " + configurePhotoResult.getMessage());
            }

            StatusResult exposeResult = api.sendRequest(new InstagramExposeRequest());
            // Log.i("UPLOAD", "Expose result: " + exposeResult);
            if (!exposeResult.getStatus().equalsIgnoreCase("ok")) {
                throw new IllegalArgumentException("Failed to expose image: " + exposeResult.getMessage());
            }

            return configurePhotoResult;
        }
    }

    protected MultipartBody createMultipartBody(String uploadId) throws IOException {
        /*
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("upload_id", uploadId);
        builder.addTextBody("_uuid", api.getUuid());
        builder.addTextBody("_csrftoken", api.getOrFetchCsrf());
        builder.addTextBody("media_type", "2");
        builder.setBoundary(api.getUuid());
        HttpEntity entity = builder.build();
        */
        MediaType mediaType = MediaType.parse("image/jpeg");

        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photo", "filename.jpeg", RequestBody.create(mediaType, this.imageFile))
                .addFormDataPart("image_compression", "{ \"lib_name\": \"jt\", \"lib_version\": \"1.3.0\", \"quality\": \"92\" }")
                .addFormDataPart("upload_id", uploadId)
                .addFormDataPart("_uuid", this.api.getUuid())
                .addFormDataPart("_csfrtoken", this.api.getOrFetchCsrf(null))
                .addFormDataPart("device_id", this.api.getDeviceId())
                .build();
    }

    /**
     * @return http request
     */
    protected Request createHttpRequest(MultipartBody body) {
        String url = InstagramConstants.API_URL + getUrl();
        // Log.d("UPLAOD", "URL Upload: " + url);

        /*
        HttpPost post = new HttpPost(url);
        post.addHeader("X-IG-Capabilities", "3Q4=");
        post.addHeader("X-IG-Connection-Type", "WIFI");
        post.addHeader("Host", "i.instagram.com");
        post.addHeader("Cookie2", "$Version=1");
        post.addHeader("Accept-Language", "en-US");
        post.addHeader("Connection", "close");
        post.addHeader("Content-Type", "multipart/form-data; boundary=" + api.getUuid());
        post.addHeader("User-Agent", InstagramConstants.USER_AGENT);
        */

        return new Request.Builder()
                .url(url)
                .addHeader("X-IG-Capabilities", "3Q4=")
                .addHeader("X-IG-Connection-Type", "WIFI")
                .addHeader("Cookie2", "$Version=1")
                .addHeader("Accept-Language", "en-US")
                .addHeader("Host", "i.instagram.com")
                .addHeader("Connection", "close")
                .addHeader("User-Agent", InstagramConstants.USER_AGENT)
                .post(body)
                .build();
    }

    @Override
    public InstagramConfigureStoryResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramConfigureStoryResult.class);
    }

}