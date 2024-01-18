package com.ckilb.booma.bookmark.repository;

import com.ckilb.booma.bookmark.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    @Query("SELECT t FROM Entry t WHERE t.passphrase = ?1 AND t.parent IS NULL")
    Collection<Entry> findAllByPassphraseWithoutParent(String passphrase);

    @Query("SELECT t FROM Entry t WHERE t.passphrase = ?1 AND t.parent.folder.path = ?2")
    Collection<Entry> findAllByPassphraseAndParentFolderPath(String passphrase, String folderPath);

    @Query("SELECT t FROM Entry t WHERE t.passphrase = ?1 AND t.folder.path = ?2")
    Optional<Entry> findOneByPassphraseAndFolderPath(String passphrase, String folderPath);

    @Modifying
    @Transactional
    @Query("DELETE FROM Entry t WHERE t.id = ?1 AND t.passphrase = ?2")
    void deleteByIdAndPassphrase(Long id, String passphrase);
}
