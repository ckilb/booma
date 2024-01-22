package com.ckilb.booma.bookmark.controller;

import com.ckilb.booma.bookmark.saver.BookmarkSaver;
import com.ckilb.booma.bookmark.entity.Bookmark;
import com.ckilb.booma.bookmark.dto.BookmarkRequest;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://booma-angular.s3-website.eu-north-1.amazonaws.com", "https://booma.io"})
@RateLimiter(name = "general")
public class BookmarkController {
    final BookmarkSaver saver;

    public BookmarkController(BookmarkSaver saver) {
        this.saver = saver;
    }

    @PostMapping("/bookmarks")
    ResponseEntity<Bookmark> saveBookmark(@Valid @RequestBody BookmarkRequest bookmarkRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String passphrase) {
        var isSaved = this.saver.saveBookmark(bookmarkRequest, passphrase);

        if (!isSaved) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
