package com.ckilb.booma.bookmark.controller;

import com.ckilb.booma.bookmark.dto.BookmarkRequest;
import com.ckilb.booma.bookmark.entity.Entry;
import com.ckilb.booma.bookmark.entity.Folder;
import com.ckilb.booma.bookmark.repository.EntryRepository;
import com.ckilb.booma.bookmark.repository.FolderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BookmarkControllerTest {
    @Autowired
    private BookmarkController subject;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Test
    void saveBookmarkWithoutFolder() {
        BookmarkRequest request = new BookmarkRequest();

        request.setTitle("my bookmark");
        request.setUrl("http://test");

        var passphrase = "passphrase123";

        this.subject.saveBookmark(request, passphrase);

        this.entryRepository.flush();
        var result = this.entryRepository.findAllByPassphraseWithoutParent(passphrase);

        assertThat(result).hasSize(1);

        var saved = result.iterator().next();

        assertThat(saved.getBookmark()).isNotNull();
        assertThat(saved.getFolder()).isNull();
        assertThat(saved.getBookmark().getTitle()).isEqualTo(request.getTitle());
        assertThat(saved.getBookmark().getUrl()).isEqualTo(request.getUrl());
    }

    @Test
    void saveBookmarkWithoutExistingFolder() {
        BookmarkRequest request = new BookmarkRequest();

        request.setTitle("my bookmark");
        request.setUrl("http://test");
        request.setFolder("not existing");

        var passphrase = "passphrase123";

        this.subject.saveBookmark(request, passphrase);

        var result = this.entryRepository.findAllByPassphraseWithoutParent(passphrase);

        assertThat(result).hasSize(1);

        var saved = result.iterator().next();

        assertThat(saved.getBookmark()).isNotNull();
        assertThat(saved.getFolder()).isNull();
    }

    @Test
    void saveBookmarkWithFolder() {
        var passphrase = "passphrase123";

        Entry folderEntry = new Entry();

        folderEntry.setPassphrase(passphrase);

        this.entryRepository.saveAndFlush(folderEntry);

        var folder = new Folder();

        folder.setTitle("My Folder");
        folder.setPath("my-folder");
        folder.setEntry(folderEntry);

        this.folderRepository.saveAndFlush(folder);

        BookmarkRequest request = new BookmarkRequest();

        request.setTitle("my bookmark");
        request.setUrl("http://test");
        request.setFolder(folder.getPath());

        this.subject.saveBookmark(request, passphrase);

        var result = this.entryRepository.findAllByPassphraseAndParentFolderPath(
            passphrase,
            folder.getPath()
        );

        assertThat(result).hasSize(1);

        var saved = result.iterator().next();

        assertThat(saved.getBookmark()).isNotNull();
        assertThat(saved.getParent()).isNotNull();
        assertThat(saved.getParent().getId()).isEqualTo(folderEntry.getId());
        assertThat(saved.getFolder()).isNull();
    }
}
