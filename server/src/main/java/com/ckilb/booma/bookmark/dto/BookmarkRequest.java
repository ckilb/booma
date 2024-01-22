package com.ckilb.booma.bookmark.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import java.util.Optional;

public class BookmarkRequest {
    @NotEmpty
    private String title;

    @NotEmpty
    @URL
    private String url;

    private String folder;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Optional<String> getFolder() {
        return this.folder == null ? Optional.empty() : Optional.of(this.folder);
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
