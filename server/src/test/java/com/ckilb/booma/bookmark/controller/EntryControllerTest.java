package com.ckilb.booma.bookmark.controller;

import com.ckilb.booma.bookmark.dto.BookmarkRequest;
import com.ckilb.booma.bookmark.dto.FolderRequest;
import com.ckilb.booma.bookmark.entity.Entry;
import com.ckilb.booma.bookmark.saver.BookmarkSaver;
import com.ckilb.booma.bookmark.saver.FolderSaver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class EntryControllerTest {
    @Autowired
    private EntryController subject;

    @Autowired
    private BookmarkSaver bookmarkSaver;

    @Autowired
    private FolderSaver folderSaver;

    @Test
    void getEntriesWithoutFolder() {
        BookmarkRequest request = new BookmarkRequest();

        request.setTitle("bookmark without folder");
        request.setUrl("http://without-folder");

        var passphrase = "passphrase123";

        this.bookmarkSaver.saveBookmark(request, passphrase);

        var result = this.subject.getEntries(passphrase, "");

        assertThat(result).hasSize(1);

        var saved = result.iterator().next();

        assertThat(saved.getBookmark()).isNotNull();
        assertThat(saved.getFolder()).isNull();
        assertThat(saved.getBookmark().getTitle()).isEqualTo(request.getTitle());
        assertThat(saved.getBookmark().getUrl()).isEqualTo(request.getUrl());
    }

    @Test
    void getEntriesWithFolder() {
        String passphrase = "passphrase123";
        FolderRequest folderRequest = new FolderRequest();

        folderRequest.setTitle("some folder");

        this.folderSaver.saveFolder(folderRequest, passphrase);

        BookmarkRequest bookmarkRequest = new BookmarkRequest();

        bookmarkRequest.setTitle("my bookmark");
        bookmarkRequest.setUrl("http://test");
        bookmarkRequest.setFolder("some-folder");

        this.bookmarkSaver.saveBookmark(bookmarkRequest, passphrase);

        Collection<Entry> resultWithoutFolder = this.subject.getEntries(passphrase, "");

        var savedFolderEntry = resultWithoutFolder.iterator().next();

        assertThat(savedFolderEntry.getBookmark()).isNull();
        assertThat(savedFolderEntry.getFolder()).isNotNull();
        assertThat(savedFolderEntry.getFolder().getTitle()).isEqualTo(folderRequest.getTitle());
        assertThat(savedFolderEntry.getFolder().getPath()).isEqualTo("some-folder");

        Collection<Entry> resultWithFolder = this.subject.getEntries(passphrase, "some-folder");

        assertThat(resultWithFolder).hasSize(1);

        Entry savedBookmarkEntry = resultWithFolder.iterator().next();

        assertThat(savedBookmarkEntry.getBookmark()).isNotNull();
        assertThat(savedBookmarkEntry.getFolder()).isNull();
        assertThat(savedBookmarkEntry.getBookmark().getTitle()).isEqualTo(bookmarkRequest.getTitle());
        assertThat(savedBookmarkEntry.getBookmark().getUrl()).isEqualTo(bookmarkRequest.getUrl());
    }
}
