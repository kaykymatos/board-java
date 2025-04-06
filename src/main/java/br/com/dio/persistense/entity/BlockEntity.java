package br.com.dio.persistense.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.security.Timestamp;
import java.time.OffsetDateTime;

@Data
public class BlockEntity {
    private Long id;
    private String description;
    private String blockReason;
    private OffsetDateTime blockedAt;
    private OffsetDateTime unblockedAt;


}
