package com.cheems.pizzatalk.modules.attachment.domain;

import com.cheems.pizzatalk.entities.enumeration.AttachmentType;
import com.cheems.pizzatalk.modules.chatmessage.domain.ChatMessage;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Attachment implements Serializable {

    private Long id;

    @NotNull
    private Long size;

    @NotNull
    private AttachmentType type;

    @Size(max = 255)
    private String thumbUrl;

    @NotNull
    @Size(max = 255)
    private String fileUrl;

    @NotNull
    private Long chatMessageId;

    private ChatMessage chatMessage;
}
