package com.ckilb.booma.bookmark;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "title", "url", "passphrase" }) })
public class Bookmark {


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    @URL
    private String url;

    @JsonIgnore
    private String passphrase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
