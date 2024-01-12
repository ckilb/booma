package com.ckilb.booma.bookmark;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://booma-angular.s3-website.eu-north-1.amazonaws.com", "https://booma.io"})
public class BookmarkController {
    final BookmarkRepository repository;

    public BookmarkController(BookmarkRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/bookmarks")
    @RateLimiter(name = "general")
    Collection<Bookmark> all(@RequestHeader(HttpHeaders.AUTHORIZATION) String passphrase) {
        return repository.findAllByPassphrase(passphrase);
    }

    @PostMapping("/bookmarks")
    ResponseEntity<Bookmark> saveBookmark(@Valid  @RequestBody Bookmark bookmark, @RequestHeader(HttpHeaders.AUTHORIZATION) String passphrase) {
        if (passphrase.length() < 8 || passphrase.length() > 50) {
            return ResponseEntity.badRequest().build();
        }

        bookmark.setPassphrase(passphrase);
        return new ResponseEntity<>(repository.save(bookmark), HttpStatus.CREATED);
    }

    @DeleteMapping("/bookmarks/{id}")
    ResponseEntity<Void> deleteBookmark(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String passphrase) {
        repository.deleteByIdAndPassphrase(id, passphrase);

        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
