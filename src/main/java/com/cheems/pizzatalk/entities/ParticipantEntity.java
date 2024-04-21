package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.entities.enumeration.ParticipantStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "participant")
public class ParticipantEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ParticipantStatus status;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @Column(name = "left_at")
    private Instant leftAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userRoles", "userKeys", "participants", "carts" }, allowSetters = true)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "participants", "chatMessages" }, allowSetters = true)
    private ConversationEntity conversation;

    @OneToMany(mappedBy = "participant")
    @JsonIgnoreProperties(value = { "conversation", "participant", "attachments" }, allowSetters = true)
    private Set<ChatMessageEntity> chatMessages = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParticipantEntity id(Long id) {
        this.id = id;
        return this;
    }

    public ParticipantStatus getStatus() {
        return this.status;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }

    public ParticipantEntity status(ParticipantStatus status) {
        this.status = status;
        return this;
    }

    public Instant getJoinedAt() {
        return this.joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public ParticipantEntity joinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
        return this;
    }

    public Instant getLeftAt() {
        return this.leftAt;
    }

    public void setLeftAt(Instant leftAt) {
        this.leftAt = leftAt;
    }

    public ParticipantEntity leftAt(Instant leftAt) {
        this.leftAt = leftAt;
        return this;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public ParticipantEntity deletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ParticipantEntity user(UserEntity user) {
        this.user = user;
        return this;
    }

    public ConversationEntity getConversation() {
        return this.conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    public ParticipantEntity conversation(ConversationEntity conversation) {
        this.conversation = conversation;
        return this;
    }

    public Set<ChatMessageEntity> getChatMessages() {
        return this.chatMessages;
    }

    public void setChatMessages(Set<ChatMessageEntity> chatMessages) {
        if (this.chatMessages != null) {
            this.chatMessages.forEach(chatMessage -> chatMessage.setParticipant(null));
        }
        if (chatMessages != null) {
            chatMessages.forEach(chatMessage -> chatMessage.setParticipant(this));
        }
        this.chatMessages = chatMessages;
    }

    public ParticipantEntity chatMessages(Set<ChatMessageEntity> chatMessages) {
        this.setChatMessages(chatMessages);
        return this;
    }

    public ParticipantEntity addChatMessage(ChatMessageEntity chatMessage) {
        chatMessage.setParticipant(this);
        this.chatMessages.add(chatMessage);
        return this;
    }

    public ParticipantEntity removeChatMessage(ChatMessageEntity chatMessage) {
        chatMessage.setParticipant(null);
        this.chatMessages.remove(chatMessage);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticipantEntity)) {
            return false;
        }
        return id != null && id.equals(((ParticipantEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Participant{" +
            "id=" +
            getId() +
            ", status='" +
            getStatus() +
            "'" +
            ", joinedAt='" +
            getJoinedAt() +
            "'" +
            ", leftAt='" +
            getLeftAt() +
            "'" +
            ", deletedAt='" +
            getDeletedAt() +
            "'" +
            "}"
        );
    }
}
