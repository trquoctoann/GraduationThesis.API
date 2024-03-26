package com.cheems.pizzatalk.entities;

import com.cheems.pizzatalk.common.entity.AbstractAuditingEntity;
import com.cheems.pizzatalk.entities.enumeration.ConversationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "conversation")
public class ConversationEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ConversationType type;

    @Column(name = "metadata")
    private String metadata;

    @OneToMany(mappedBy = "conversation")
    @JsonIgnoreProperties(value = { "user", "conversation", "chatMessages" }, allowSetters = true)
    private Set<ParticipantEntity> participants = new HashSet<>();

    @OneToMany(mappedBy = "conversation")
    @JsonIgnoreProperties(value = { "conversation", "participant", "attachments" }, allowSetters = true)
    private Set<ChatMessageEntity> chatMessages = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConversationEntity id(Long id) {
        this.id = id;
        return this;
    }

    public ConversationType getType() {
        return this.type;
    }

    public void setType(ConversationType type) {
        this.type = type;
    }

    public ConversationEntity type(ConversationType type) {
        this.type = type;
        return this;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public ConversationEntity metadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    public Set<ParticipantEntity> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<ParticipantEntity> participants) {
        if (this.participants != null) {
            this.participants.forEach(participant -> participant.setConversation(null));
        }
        if (participants != null) {
            participants.forEach(participant -> participant.setConversation(this));
        }
        this.participants = participants;
    }

    public ConversationEntity participants(Set<ParticipantEntity> participants) {
        this.setParticipants(participants);
        return this;
    }

    public ConversationEntity addParticipant(ParticipantEntity participant) {
        participant.setConversation(this);
        this.participants.add(participant);
        return this;
    }

    public ConversationEntity removeParticipant(ParticipantEntity participant) {
        participant.setConversation(null);
        this.participants.remove(participant);
        return this;
    }

    public Set<ChatMessageEntity> getChatMessages() {
        return this.chatMessages;
    }

    public void setChatMessages(Set<ChatMessageEntity> chatMessages) {
        if (this.chatMessages != null) {
            this.chatMessages.forEach(chatMessage -> chatMessage.setConversation(null));
        }
        if (chatMessages != null) {
            chatMessages.forEach(chatMessage -> chatMessage.setConversation(this));
        }
        this.chatMessages = chatMessages;
    }

    public ConversationEntity chatMessages(Set<ChatMessageEntity> chatMessages) {
        this.chatMessages(chatMessages);
        return this;
    }

    public ConversationEntity addChatMessage(ChatMessageEntity chatMessage) {
        chatMessage.setConversation(this);
        this.chatMessages.add(chatMessage);
        return this;
    }

    public ConversationEntity removeChatMessage(ChatMessageEntity chatMessage) {
        chatMessage.setConversation(null);
        this.chatMessages.remove(chatMessage);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConversationEntity)) {
            return false;
        }
        return id != null && id.equals(((ConversationEntity) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return ("Conversation{" + "id=" + getId() + ", type='" + getType() + "'" + ", metadata='" + getMetadata() + "'" + "}");
    }
}
