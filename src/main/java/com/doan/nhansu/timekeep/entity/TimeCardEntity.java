package com.doan.nhansu.timekeep.entity;

import com.doan.nhansu.users.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "timecard")
public class TimeCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timecard_seq")
    @SequenceGenerator(name = "timecard_seq", sequenceName = "timecard_sequence", allocationSize = 1)
    @Column(name = "TIMECARD_ID")
    Long id;
    @Column(name = "NAME")
    String name;
    @Column(name = "VALUE")
    String value;
    @Column(name = "MONTH")
    Long month;
    @Column(name = "YEAR")
    Long year;
    @Column(name = "TOTALWORK_DAY")
    BigDecimal totalWorkDay;
    @Column(name = "TOTALWORK_SUN")
    BigDecimal totalWorkSun;
    @Column(name = "TOTALWORKHOURS_DAY")
    BigDecimal totalWorkHoursDay;
    @Column(name = "TOTALWORKHOURS_SUN")
    BigDecimal totalWorkHoursSun;
    @Column(name = "TOTALOVERTIMEHOURS")
    BigDecimal totalOverTimeHours;
    @Column(name = "DAYS_OFF_PAID")
    BigDecimal daysOffPaid;
    @Column(name = "DAYS_OFF_UNPAID")
    BigDecimal daysOffUnpaid;
    @Column(name = "START_DATE")
    Date startDate;
    @Column(name = "END_DATE")
    Date endDate;
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
