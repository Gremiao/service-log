package br.com.jatai.service_log.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "logs")
public class Log {

    @Id
    private String id;
    private LocalDateTime dateTime;
    private String key;
    private String partition;
    private String topic;
    private String offset;
    private String comments;
    private String payload;

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Log(LocalDateTime dateTime, String key, String partition, String topic, String offset, String comments, String payload) {
        this.dateTime = dateTime;
        this.key = key;
        this.partition = partition;
        this.topic = topic;
        this.offset = offset;
        this.comments = comments;
        this.payload = payload;
    }
}
