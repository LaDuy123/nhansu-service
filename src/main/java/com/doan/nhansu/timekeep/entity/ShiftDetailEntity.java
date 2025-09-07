package com.doan.nhansu.timekeep.entity;

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
@Table(name = "shift_Detail")
public class ShiftDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shiftdetail_seq")
    @SequenceGenerator(name = "shiftdetail_seq", sequenceName = "shiftdetail_sequence", allocationSize = 1)

    @Column(name = "SHIFT_DETAIL_ID")
    Long id;
    @Column(name = "DAY")
    Date day;
    @Column(name= "SHIFT_ID")
    Long shiftId;
    @Column(name = "SHIFT_TYPE_ID")
    Long shiftTypeId;
    @Column(name = "NAME")
    String name;
    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;

}
