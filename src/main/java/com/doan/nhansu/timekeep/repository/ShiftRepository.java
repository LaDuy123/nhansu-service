package com.doan.nhansu.timekeep.repository;

import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.entity.ShiftEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShiftRepository extends JpaRepository<ShiftEntity, Long> {
    @Query(value = "select e from ShiftEntity e where 1=1 " +
            " and (:#{#dto.id} is null or e.id = :#{#dto.id}) " +
            " and (:#{#dto.keySearch} is null or lower(e.name) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            " or lower(e.value) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            ") " +
            " order by e.id desc"
    )
    Page<ShiftEntity> doSearchShift(ShiftDTO dto, Pageable pageable);
    @Query("SELECT s.id FROM ShiftEntity s WHERE s.departmentId = :departmentId")
    Long findFirstIdByDepartmentId(Long departmentId);
    @Query(value = "Select count(*) from ShiftEntity e where e.value = :value and (:id is null or e.id != :id)")
    Integer countByValue(String value, Long id);
}
