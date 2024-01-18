package com.ckilb.booma.bookmark.saver;

import com.ckilb.booma.bookmark.dto.FolderRequest;
import com.ckilb.booma.bookmark.entity.Entry;
import com.ckilb.booma.bookmark.entity.Folder;
import com.ckilb.booma.bookmark.repository.EntryRepository;
import com.ckilb.booma.bookmark.repository.FolderRepository;
import com.github.slugify.Slugify;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
public class FolderSaver {

    private final EntryRepository entryRepository;
    private final FolderRepository folderRepository;

    private final Slugify slugger;

    public FolderSaver(EntryRepository entryRepository, FolderRepository folderRepository) {
        this.entryRepository = entryRepository;
        this.folderRepository = folderRepository;
        this.slugger = Slugify.builder().locale(Locale.GERMAN).build();
    }

    public boolean saveFolder(FolderRequest request, String passphrase) {
        if (passphrase.length() < 8 || passphrase.length() > 50) {
            return false;
        }

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            return false;
        }

        var entry = this.createEntry(passphrase);
        var folder = new Folder();

        folder.setTitle(request.getTitle());
        folder.setPath(this.createPath(request, passphrase));
        folder.setEntry(entry);

        this.folderRepository.saveAndFlush(folder);

        return true;
    }

    private String createPath(FolderRequest request, String passphrase)
    {
        var path = this.slugger.slugify(request.getTitle());

        while (this.folderRepository.existsWithPassphraseAndPath(path, passphrase)) {
            var uuidChunks = UUID.randomUUID().toString().split("-");
            path = path + "-" + uuidChunks[uuidChunks.length - 1];
        }

        return path;
    }

    private Entry createEntry(String passphrase) {
        var entry = new Entry();

        entry.setPassphrase(passphrase);

        this.entryRepository.saveAndFlush(entry);

        return entry;
    }
}
