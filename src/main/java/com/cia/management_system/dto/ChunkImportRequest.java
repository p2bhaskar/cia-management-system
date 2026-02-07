package com.cia.management_system.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ChunkImportRequest {
    private List<Map<String, Object>> chunk;
    private Integer chunkNumber;
    private Integer totalChunks;
    private String type;
}
