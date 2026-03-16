package com.github.nikita65288.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateMessageDto {

    @NotBlank(message = "Сообщение не может быть пустым")
    @Size(max = 2000, message = "Сообщение слишком длинное (макс. 2000 символов)")
    private String content;
}
