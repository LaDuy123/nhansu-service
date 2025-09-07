package com.doan.nhansu.timekeep.repository;

import com.doan.nhansu.timekeep.entity.ShiftDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public interface ShiftDetailRepository extends JpaRepository<ShiftDetailEntity, Long> {
    @Modifying
    @Query("delete from ShiftDetailEntity where shiftId = ?1 ")
    void deleteByShiftId(Long shiftId);

    Long countByDayBetween(Date start, Date end);
}
