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
@Table(name = "report")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_seq")
    @SequenceGenerator(name = "report_seq", sequenceName = "report_sequence", allocationSize = 1)
    @Column(name = "REPORT_ID")
    Long id;
    @Column(name = "NAME")
    String name;
    @Column(name = "TYPE_ID")
    Long typeId;
    @Column(name = "TABLE_ID")
    Long tableId;
    @Column(name = "MONTH")
    String month;
    @Column(name = "VALUE")
    String value;
    @Column(name = "URL")
    String url;
    @Column(name = "DESCRIPTION")
    String description;
    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;
}
