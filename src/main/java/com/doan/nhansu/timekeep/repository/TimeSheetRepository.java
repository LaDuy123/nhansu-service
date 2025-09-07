package com.doan.nhansu.timekeep.repository;

import com.doan.nhansu.timekeep.entity.TimeSheetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheetEntity, Long> {
    boolean existsByUserIdAndWorkDate(Long userId, Date workDate);

}
