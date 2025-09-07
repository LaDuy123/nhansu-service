package com.doan.nhansu.timekeep.entity;

import com.doan.nhansu.admin.util.JsonDateDeserializer;
import com.doan.nhansu.admin.util.JsonDateSerializerDate;
import com.doan.nhansu.users.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "timesheet")
public class TimeSheetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheet_seq")
    @SequenceGenerator(name = "timesheet_seq", sequenceName = "timesheet_sequence", allocationSize = 1)
    @Column(name = "TIMESHEET_ID")
    Long id;
    @Column(name = "CHECKINTIME")
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    Date checkInTime;
    @JsonSerialize(using = JsonDateSerializerDate.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "CHECKOUTTIME")
    Date checkoutTime;
    @Column(name = "TOTAL_HOURS")
    Long totalHours;
    @Column(name = "WORKDATE")
    Date workDate;
    @Column(name = "DAY_OF_WEEK")
    String dayOfWeek;
    @Column(name = "SHIFT_ID")
    Long shiftId;
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
