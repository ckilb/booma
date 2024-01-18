package com.ckilb.booma.bookmark.repository;

import com.ckilb.booma.bookmark.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("SELECT CASE WHEN (SELECT COUNT(f.id) FROM Folder f WHERE f.path = ?1 AND f.entry.passphrase = ?2) > 0 THEN true ELSE false END")
    public boolean existsWithPassphraseAndPath(String path, String passphrase);
}
