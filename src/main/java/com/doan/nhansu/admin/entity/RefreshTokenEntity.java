package com.doan.nhansu.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "refreshtoken")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_seq")
    @SequenceGenerator(name = "refresh_seq", sequenceName = "refresh_sequence", allocationSize = 1)
    Long id;
    @Column(name = "USER_ID")
    Long userId;
    @Column(name = "TOKEN")
    String token;
    @Column(name = "EXPIRYDATE")
    Date expiryDate;
}
