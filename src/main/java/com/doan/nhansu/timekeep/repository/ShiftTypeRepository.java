package com.doan.nhansu.timekeep.repository;

import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftTypeDTO;
import com.doan.nhansu.timekeep.entity.ShiftEntity;
import com.doan.nhansu.timekeep.entity.ShiftTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShiftTypeRepository extends JpaRepository<ShiftTypeEntity, Long>{
    @Query(value = "select e from ShiftTypeEntity e where 1=1 " +
            " and (:#{#dto.id} is null or e.id = :#{#dto.id}) " +
            " and (:#{#dto.keySearch} is null or lower(e.name) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            " or lower(e.value) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            ") " +
            " order by e.id desc"
    )
    Page<ShiftTypeEntity> doSearchShiftType(ShiftTypeDTO dto, Pageable pageable);
    @Query(value = "Select count(*) from ShiftTypeEntity e where e.value = :value and (:id is null or e.id != :id)")
    Integer countByValue(String value, Long id);
}
