package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.entities.enumeration.AttachmentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "attachment")
public class AttachmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Column(name = "size", nullable = false)
    private Long size;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AttachmentType type;

    @Size(max = 255)
    @Column(name = "thumb_url", length = 255)
    private String thumbUrl;

    @NotNull
    @Size(max = 255)
    @Column(name = "file_url", length = 255, nullable = false)
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "conversation", "participant", "attachments" }, allowSetters = true)
    private ChatMessageEntity chatMessage;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttachmentEntity id(Long id) {
        this.id = id;
        return this;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public AttachmentEntity size(Long size) {
        this.size = size;
        return this;
    }

    public AttachmentType getType() {
        return this.type;
    }

    public void setType(AttachmentType type) {
        this.type = type;
    }

    public AttachmentEntity type(AttachmentType type) {
        this.type = type;
        return this;
    }

    public String getThumbUrl() {
        return this.thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public AttachmentEntity thumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
        return this;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public AttachmentEntity fileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public ChatMessageEntity getChatMessage() {
        return this.chatMessage;
    }

    public void setChatMessage(ChatMessageEntity chatMessage) {
        this.chatMessage = chatMessage;
    }

    public AttachmentEntity chatMessage(ChatMessageEntity chatMessage) {
        this.chatMessage = chatMessage;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachmentEntity)) {
            return false;
        }
        return id != null && id.equals(((AttachmentEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Attachment{" +
            "id=" +
            getId() +
            ", size='" +
            getSize() +
            "'" +
            ", type='" +
            getType() +
            "'" +
            ", thumbUrl='" +
            getThumbUrl() +
            "'" +
            ", fileUrl='" +
            getFileUrl() +
            "'" +
            "}"
        );
    }
}
