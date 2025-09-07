package com.doan.nhansu.admin.repository;

import com.doan.nhansu.admin.dto.RoleDepartmentDTO;
import com.doan.nhansu.admin.entity.RoleDepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleDepartmentRepository extends JpaRepository<RoleDepartmentEntity, Long> {

    @Query(value = "Select e from RoleDepartmentEntity e where e.roleId = :roleId")
    List<RoleDepartmentEntity> doSearchByRole(Long roleId);


}
