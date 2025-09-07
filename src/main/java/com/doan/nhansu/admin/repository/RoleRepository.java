package com.doan.nhansu.admin.repository;

import com.doan.nhansu.admin.dto.RoleDTO;
import com.doan.nhansu.admin.entity.RoleEntity;
import com.doan.nhansu.users.dto.DepartmentDTO;
import com.doan.nhansu.users.entity.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query(value = "select e from RoleEntity e where 1=1 " +
            " and (:#{#dto.id} is null or e.id = :#{#dto.id}) " +
            " and (:#{#dto.keySearch} is null or lower(e.name) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            " or lower(e.value) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            ") " +
            " order by e.id desc"
    )
    Page<RoleEntity> doSearchRole(RoleDTO dto, Pageable pageable);
}
