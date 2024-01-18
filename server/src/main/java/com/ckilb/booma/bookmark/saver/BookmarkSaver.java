package com.ckilb.booma.bookmark.saver;

import com.ckilb.booma.bookmark.dto.BookmarkRequest;
import com.ckilb.booma.bookmark.entity.Bookmark;
import com.ckilb.booma.bookmark.entity.Entry;
import com.ckilb.booma.bookmark.repository.BookmarkRepository;
import com.ckilb.booma.bookmark.repository.EntryRepository;
import org.springframework.stereotype.Component;

@Component
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

        var parent = this.getParentEntry(request.getFolder(), passphrase);
        var entry = this.createEntry(parent, passphrase);;
        var bookmark = new Bookmark();

        bookmark.setTitle(request.getTitle());
        bookmark.setUrl(request.getUrl());
        bookmark.setEntry(entry);

        this.bookmarkRepository.saveAndFlush(bookmark);

        return true;
    }

    private Entry getParentEntry(String folderPath, String passphrase) {
         if (folderPath == null || folderPath.isBlank()) {
             return null;
         }

         var parent = this.entryRepository.findOneByPassphraseAndFolderPath(
            passphrase,
            folderPath
        );

        return parent.orElse(null);

    }

    private Entry createEntry(Entry parent, String passphrase) {
        var entry = new Entry();

        entry.setPassphrase(passphrase);
        entry.setParent(parent);

        this.entryRepository.saveAndFlush(entry);

        return entry;
    }
}
