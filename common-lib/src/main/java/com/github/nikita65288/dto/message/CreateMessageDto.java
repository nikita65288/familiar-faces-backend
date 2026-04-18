package com.github.nikita65288.dto.message;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateMessageDto {

    @Size(max = 2000, message = "Сообщение слишком длинное (макс. 2000 символов)")
    private String content;

    private String attachmentUrl;

    private Long replyToMessageId;
}
