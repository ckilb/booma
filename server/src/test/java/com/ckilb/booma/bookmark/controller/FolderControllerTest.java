package com.ckilb.booma.bookmark.controller;

import com.ckilb.booma.bookmark.dto.FolderRequest;
import com.ckilb.booma.bookmark.repository.EntryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FolderControllerTest {
    @Autowired
    private FolderController subject;

    @Autowired
    private EntryRepository entryRepository;

    @Test
    void saveFolder() {
        FolderRequest request = new FolderRequest();

        request.setTitle("My Folder ä ß");

        var passphrase = "passphrase123";

        this.subject.saveFolder(request, passphrase);

        var result = this.entryRepository.findAllByPassphraseWithoutParent(passphrase);
        assertThat(result).hasSize(1);

        var saved = result.iterator().next();

        assertThat(saved.getBookmark()).isNull();
        assertThat(saved.getFolder()).isNotNull();
        assertThat(saved.getFolder().getTitle()).isEqualTo(request.getTitle());
        assertThat(saved.getFolder().getPath()).isEqualTo("my-folder-ae-ss");
    }
}
