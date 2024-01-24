package com.ckilb.booma.bookmark.controller;

import com.ckilb.booma.bookmark.entity.Entry;
import com.ckilb.booma.bookmark.repository.EntryRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://booma-angular.s3-website.eu-north-1.amazonaws.com", "https://booma.io"})
@RateLimiter(name = "general")
@Transactional
public class EntryController {
    final EntryRepository repository;

    public EntryController(EntryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/entries")
    Collection<Entry> getEntries(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String passphrase,
            @RequestParam(defaultValue = "") String folder
    ) {
        if (!folder.isBlank()) {
            return repository.findAllByPassphraseAndParentFolderPath(passphrase, folder);
        }

        return repository.findAllByPassphraseWithoutParent(passphrase);
    }

    @DeleteMapping("/entries/{id}")
    ResponseEntity<Void> deleteEntry(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String passphrase) {
        this.repository.deleteByIdAndPassphrase(id, passphrase);

        return ResponseEntity.noContent().build();
    }
}
