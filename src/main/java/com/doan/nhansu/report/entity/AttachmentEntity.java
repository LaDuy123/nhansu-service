package com.doan.nhansu.report.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "attachment")
public class AttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_seq")
    @SequenceGenerator(name = "attachment_seq", sequenceName = "attachment_sequence", allocationSize = 1)
    @Column(name = "ATTACHMENT_ID")
    Long id;
    @Column(name = "SIZE_FILE")
    Long sizeFile;
    @Column(name = "FILENAME")
    String fileName;
    @Column(name = "PATH")
    String path;
    @Column(name = "TABLE_ID")
    Long tableId;
    @Column(name = "USER_ID")
    Long userId;
    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;
}
