package com.example.demo.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "messages")
public class Message {

    @Id
    @Generated
    private long id;

    @NonNull
    private String resource;
    @NonNull
    private String action;
    @NonNull
    private String request;
}
