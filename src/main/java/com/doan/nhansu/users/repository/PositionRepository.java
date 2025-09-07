package com.doan.nhansu.users.repository;

import com.doan.nhansu.users.dto.PositionDTO;
import com.doan.nhansu.users.entity.PositionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Long>{

    @Query(value = "select e from PositionEntity e where 1=1 " +
            " and (:#{#dto.id} is null or e.id = :#{#dto.id}) " +
            " and (:#{#dto.keySearch} is null or lower(e.name) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            " or lower(e.value) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            ") " +
            " order by e.id desc"
    )
    Page<PositionEntity> doSearchPosition(PositionDTO dto, Pageable pageable);
    @Query(value = "Select count(*) from PositionEntity e where e.value = :value and (:id is null or e.id != :id)")
    Integer countByValue(String value, Long id);
}
