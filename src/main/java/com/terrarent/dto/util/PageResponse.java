package com.terrarent.dto.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Generic paginated response structure")
public class PageResponse<T> {
    @Schema(description = "List of content for the current page")
    private List<T> content;
    @Schema(description = "Total number of pages available")
    private int totalPages;
    @Schema(description = "Total number of elements across all pages")
    private long totalElements;
    @Schema(description = "Number of elements in the current page")
    private int size;
    @Schema(description = "Current page number (0-indexed)")
    private int number;
    @Schema(description = "True if this is the first page")
    private boolean first;
    @Schema(description = "True if this is the last page")
    private boolean last;
    @Schema(description = "True if the current page is empty")
    private boolean empty;
}
