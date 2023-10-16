package com.example.serviceclient.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class FileResponse{
    private String format;
    private String resourceType;
    private String secureUrl;
    private String createdAt;
    private String url;
    private Long bytes;
    private Long width;
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
