package com.ShareText.demo.dto;


import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.ShareText.demo.enums.*;


@Data
public class CreateRoomRequest {
    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Language is required")
    private Language language;

    @NotNull(message = "Expiry is required")
    private Expiry expiry;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Expiry getExpiry() {
        return expiry;
    }

    public void setExpiry(Expiry expiry) {
        this.expiry = expiry;
    }
}
