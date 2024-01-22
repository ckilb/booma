package com.ckilb.booma.bookmark.controller;

import com.ckilb.booma.bookmark.dto.FolderRequest;
import com.ckilb.booma.bookmark.saver.FolderSaver;
import com.ckilb.booma.bookmark.entity.Folder;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://booma-angular.s3-website.eu-north-1.amazonaws.com", "https://booma.io"})
@RateLimiter(name = "general")
public class FolderController {
    final FolderSaver folderSaver;

    public FolderController(FolderSaver folderSaver) {
        this.folderSaver = folderSaver;
    }

    @PostMapping("/folders")
    ResponseEntity<Folder> saveFolder(@Valid  @RequestBody FolderRequest folderRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String passphrase) {
        var isSaved = this.folderSaver.saveFolder(folderRequest, passphrase);

        if (!isSaved) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
