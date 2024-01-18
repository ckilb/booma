package com.ckilb.booma.bookmark.dto;

import jakarta.validation.constraints.NotEmpty;

public class FolderRequest {
    @NotEmpty
    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
