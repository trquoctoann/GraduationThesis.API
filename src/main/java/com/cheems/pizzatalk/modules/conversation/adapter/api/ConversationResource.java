package com.cheems.pizzatalk.modules.conversation.adapter.api;

import com.cheems.pizzatalk.entities.mapper.ConversationMapper;
import com.cheems.pizzatalk.modules.conversation.application.port.in.command.CreateConversationCommand;
import com.cheems.pizzatalk.modules.conversation.application.port.in.command.UpdateConversationMetadataCommand;
import com.cheems.pizzatalk.modules.conversation.application.port.in.query.ConversationCriteria;
import com.cheems.pizzatalk.modules.conversation.application.port.in.share.ConversationLifecycleUseCase;
import com.cheems.pizzatalk.modules.conversation.application.port.in.share.QueryConversationUseCase;
import com.cheems.pizzatalk.modules.conversation.domain.Conversation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class ConversationResource {

    private final Logger log = LoggerFactory.getLogger(ConversationResource.class);

    private static final String ENTITY_NAME = "conversation";

    @Value("${spring.application.name}")
    private String applicationName;

    private final ConversationLifecycleUseCase conversationLifecycleUseCase;

    private final QueryConversationUseCase queryConversationUseCase;

    public ConversationResource(
        ConversationLifecycleUseCase conversationLifecycleUseCase,
        QueryConversationUseCase queryConversationUseCase
    ) {
        this.conversationLifecycleUseCase = conversationLifecycleUseCase;
        this.queryConversationUseCase = queryConversationUseCase;
    }

    @GetMapping("/conversations/all")
    public ResponseEntity<List<Conversation>> getAllConversations(ConversationCriteria criteria) {
        log.debug("REST request to get all conversations by criteria: {}", criteria);
        return ResponseEntity.ok().body(queryConversationUseCase.findListByCriteria(criteria));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<Conversation>> getPageConversations(ConversationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get conversations by criteria: {}", criteria);
        Page<Conversation> page = queryConversationUseCase.findPageByCriteria(criteria, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/conversations/{id}")
    public ResponseEntity<Conversation> getConversation(@PathVariable(value = "id", required = true) Long id) {
        log.debug("REST request to get conversation, ID: {}", id);
        Optional<Conversation> optionalConversation = queryConversationUseCase.findById(
            id,
            ConversationMapper.DOMAIN_USER,
            ConversationMapper.DOMAIN_CHAT_MESSAGES
        );
        return optionalConversation
            .map(conversation -> ResponseEntity.ok().body(conversation))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/conversations")
    public ResponseEntity<Void> createConversation(@Valid @RequestBody CreateConversationCommand command) throws URISyntaxException {
        log.debug("REST request to create conversation: {}", command);
        Conversation conversation = conversationLifecycleUseCase.create(command);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("/api/conversations/" + conversation.getId()));
        headers.add("X-applicationName-alert", "entity.creation.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + conversation.getId().toString());

        return ResponseEntity.created(new URI("/api/conversations/" + conversation.getId())).headers(headers).build();
    }

    @PutMapping("/conversations/{id}")
    public ResponseEntity<Void> updateConversationMetadata(
        @PathVariable(value = "id", required = true) Long id,
        @Valid @RequestBody UpdateConversationMetadataCommand command
    ) {
        log.debug("REST request to update metadata of conversation, ID: {}", id);
        Conversation conversation = conversationLifecycleUseCase.updateMetadata(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-applicationName-alert", "entity.update.success");
        headers.add("X-applicationName-params", ENTITY_NAME + ":" + conversation.getId().toString());

        return ResponseEntity.noContent().headers(headers).build();
    }
}
