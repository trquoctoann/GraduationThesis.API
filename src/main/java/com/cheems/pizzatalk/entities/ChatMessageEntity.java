package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.entities.enumeration.ChatMessageStatus;
import com.cheems.pizzatalk.entities.enumeration.ChatMessageType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "chat_message")
public class ChatMessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "content")
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ChatMessageType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChatMessageStatus status;

    @NotNull
    @Column(name = "sent_time", nullable = false)
    private Instant sentTime;

    @Column(name = "read_time")
    private Instant readTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "participants", "chatMessages" }, allowSetters = true)
    private ConversationEntity conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "conversation", "chatMessages" }, allowSetters = true)
    private ParticipantEntity participant;

    @OneToMany(mappedBy = "chatMessage")
    @JsonIgnoreProperties(value = { "chatMessage" }, allowSetters = true)
    private Set<AttachmentEntity> attachments = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatMessageEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatMessageEntity content(String content) {
        this.content = content;
        return this;
    }

    public ChatMessageType getType() {
        return this.type;
    }

    public void setType(ChatMessageType type) {
        this.type = type;
    }

    public ChatMessageEntity type(ChatMessageType type) {
        this.type = type;
        return this;
    }

    public ChatMessageStatus getStatus() {
        return this.status;
    }

    public void setStatus(ChatMessageStatus status) {
        this.status = status;
    }

    public ChatMessageEntity status(ChatMessageStatus status) {
        this.status = status;
        return this;
    }

    public Instant getSentTime() {
        return this.sentTime;
    }

    public void setSentTime(Instant sentTime) {
        this.sentTime = sentTime;
    }

    public ChatMessageEntity sentTime(Instant sentTime) {
        this.sentTime = sentTime;
        return this;
    }

    public Instant getReadTime() {
        return this.readTime;
    }

    public void setReadTime(Instant readTime) {
        this.readTime = readTime;
    }

    public ChatMessageEntity readTime(Instant readTime) {
        this.readTime = readTime;
        return this;
    }

    public ConversationEntity getConversation() {
        return this.conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    public ChatMessageEntity conversation(ConversationEntity conversation) {
        this.conversation = conversation;
        return this;
    }

    public ParticipantEntity getParticipant() {
        return this.participant;
    }

    public void setParticipant(ParticipantEntity participant) {
        this.participant = participant;
    }

    public ChatMessageEntity participant(ParticipantEntity participant) {
        this.participant = participant;
        return this;
    }

    public Set<AttachmentEntity> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Set<AttachmentEntity> attachments) {
        if (this.attachments != null) {
            this.attachments.forEach(attachment -> attachment.setChatMessage(null));
        }
        if (attachments != null) {
            attachments.forEach(attachment -> attachment.setChatMessage(this));
        }
        this.attachments = attachments;
    }

    public ChatMessageEntity attachments(Set<AttachmentEntity> attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public ChatMessageEntity addAttachment(AttachmentEntity attachment) {
        attachment.setChatMessage(this);
        this.attachments.add(attachment);
        return this;
    }

    public ChatMessageEntity removeAttachment(AttachmentEntity attachment) {
        attachment.setChatMessage(null);
        this.attachments.remove(attachment);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatMessageEntity)) {
            return false;
        }
        return id != null && id.equals(((ChatMessageEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Message{" +
            "id=" +
            getId() +
            ", content='" +
            getContent() +
            "'" +
            ", type='" +
            getType() +
            "'" +
            ", getStatus='" +
            getStatus() +
            "'" +
            ", sentTime='" +
            getSentTime() +
            "'" +
            ", readTime='" +
            getReadTime() +
            "'" +
            "}"
        );
    }
}
