package com.ckilb.booma.bookmark.saver;

import com.ckilb.booma.bookmark.dto.BookmarkRequest;
import com.ckilb.booma.bookmark.entity.Bookmark;
import com.ckilb.booma.bookmark.entity.Entry;
import com.ckilb.booma.bookmark.repository.BookmarkRepository;
import com.ckilb.booma.bookmark.repository.EntryRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class BookmarkSaver {

    private final EntryRepository entryRepository;
    private final BookmarkRepository bookmarkRepository;

    public BookmarkSaver(EntryRepository entryRepository, BookmarkRepository bookmarkRepository) {
        this.entryRepository = entryRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    public boolean saveBookmark(BookmarkRequest request, String passphrase) {
        if (passphrase.length() < 8 || passphrase.length() > 50) {
            return false;
        }

        var entry = this.createEntry(request, passphrase);;
        var bookmark = new Bookmark();

        bookmark.setTitle(request.getTitle());
        bookmark.setUrl(request.getUrl());
        bookmark.setEntry(entry);

        this.bookmarkRepository.saveAndFlush(bookmark);

        entry.setBookmark(bookmark);

        return true;
    }

    private Optional<Entry> getParentEntry(@NonNull String folderPath, String passphrase) {
         if (folderPath.isBlank()) {
             return Optional.empty();
         }

         return this.entryRepository.findOneByPassphraseAndFolderPath(
            passphrase,
            folderPath
        );
    }

    private Entry createEntry(BookmarkRequest request, String passphrase) {
        var entry = new Entry();

        if (request.getFolder().isPresent()) {
            var folderPath = request.getFolder().get();
            var parent = this.getParentEntry(folderPath, passphrase);

            parent.ifPresent(entry::setParent);
        }

        entry.setPassphrase(passphrase);

        this.entryRepository.saveAndFlush(entry);

        return entry;
    }
}
