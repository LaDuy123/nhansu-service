package com.doan.nhansu.timekeep.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "shift_type")
public class ShiftTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shifttype_seq")
    @SequenceGenerator(name = "shifttype_seq", sequenceName = "shifttype_sequence", allocationSize = 1)

    @Column(name = "SHIFT_TYPE_ID")
    Long id;
    @Column(name = "NAME")
    String name;
    @Column(name = "VALUE")
    String value;
    @Column(name = "START_TIME")
    String startTime;
    @Column(name = "END_TIME")
    String endTime;
    @Column(name = "BREAK_TIME")
    String breakTime;
    @Column(name = "END_BREAK_TIME")
    String endBreakTime;
    @Column(name = "TOTAL_WORK_HOURS")
    String totalWorkHours;
    @Column(name = "COEFFICIENT")
    BigDecimal coefficient;

    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;

}
