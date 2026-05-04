package com.example.webtmdt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest {

    @NotNull(message = "ID người nhận không được để trống")
    private Long receiverId;

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    private String content;
}
