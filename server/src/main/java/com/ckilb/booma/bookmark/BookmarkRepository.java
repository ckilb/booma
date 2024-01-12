package com.ckilb.booma.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b WHERE b.passphrase = ?1")
    Collection<Bookmark> findAllByPassphrase(String passphrase);

    @Modifying
    @Transactional
    @Query("DELETE FROM Bookmark b WHERE b.id = ?1 AND b.passphrase = ?2")
    void deleteByIdAndPassphrase(Long id, String passphrase);
}
