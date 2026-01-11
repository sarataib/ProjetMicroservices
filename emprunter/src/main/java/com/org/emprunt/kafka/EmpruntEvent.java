package com.org.emprunt.kafka;

import java.time.LocalDateTime;

public class EmpruntEvent {

    private Long empruntId;
    private Long userId;
    private Long bookId;
    private String eventType;
    private LocalDateTime timestamp;

    public EmpruntEvent(Long empruntId, Long userId, Long bookId) {
        this.empruntId = empruntId;
        this.userId = userId;
        this.bookId = bookId;
        this.eventType = "EMPRUNT_CREATED";
        this.timestamp = LocalDateTime.now();
    }

    public Long getEmpruntId() { return empruntId; }
    public Long getUserId() { return userId; }
    public Long getBookId() { return bookId; }
    public String getEventType() { return eventType; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
