package com.capcredit.ms_notification.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@Entity
@Table(name = "tb_user")
public class User {
    @Id
    private UUID userId;
    private String name;
    private String email;
    private String phone;

    public User() {

    }
}
