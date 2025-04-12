package br.com.dio.dto;

import java.time.OffsetDateTime;

public record CardDetailsDTO(Long id, boolean blokcked, OffsetDateTime blockedAt, String blockReason, int blockMount, Long columnId, String columnName) {
}
