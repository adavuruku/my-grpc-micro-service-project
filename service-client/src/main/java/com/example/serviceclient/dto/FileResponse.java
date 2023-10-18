package com.example.serviceclient.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class FileResponse{
    @NotEmpty
    @NotBlank
    private String format;
    @NotEmpty
    @NotBlank
    private String resourceType;
    @NotEmpty
    @NotBlank
    private String secureUrl;
    @NotEmpty
    @NotBlank
    private String createdAt;
    @NotEmpty
    @NotBlank
    private String url;
    @Positive
    private Long bytes;
    @Positive
    private Long width;
    @Positive
    private Long height;
    public FileResponse(){}

    public static FileResponse build(Map uploadResult){

        return FileResponse.builder()
                .format(uploadResult.get("format").toString())
                .resourceType(uploadResult.get("resource_type").toString())
                .secureUrl(uploadResult.get("secure_url").toString())
                .createdAt(uploadResult.get("created_at").toString())
                .url(uploadResult.get("url").toString())
                .bytes(Long.parseLong(uploadResult.get("bytes").toString()))
                .width(Long.parseLong(uploadResult.get("width").toString()))
                .height(Long.parseLong(uploadResult.get("height").toString()))
                .build();
    }

    public static FileResponse fromJsonNode(JsonNode jsonNode){

        return FileResponse.builder()
                .format(jsonNode.get("format").toString())
                .resourceType(jsonNode.get("resourceType").toString())
                .secureUrl(jsonNode.get("secureUrl").toString())
                .createdAt(jsonNode.get("createdAt").toString())
                .url(jsonNode.get("url").toString())
                .bytes(Long.parseLong(jsonNode.get("bytes").toString()))
                .width(Long.parseLong(jsonNode.get("width").toString()))
                .height(Long.parseLong(jsonNode.get("height").toString()))
                .build();
    }

}
