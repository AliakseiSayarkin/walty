package com.walty.transaction.service.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRecordModel {

    private String id;

    private String telegramChatId;

    private Date date;
    private String description;

    private double usd;
    private double eur;
    private double byn;
    private double rub;
}
