package com.doan.nhansu.users.repository;

import com.doan.nhansu.users.dto.ContractRenewDTO;
import com.doan.nhansu.users.entity.ContractRenewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRenewRepository extends JpaRepository<ContractRenewEntity, Long> {
    @Query(value = "select e from ContractRenewEntity e where 1=1 " +
            " and (:#{#dto.id} is null or e.id = :#{#dto.id}) " +
            " and (:#{#dto.keySearch} is null or lower(e.name) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            " or lower(e.value) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            ") " +
            " order by e.id desc"
    )
    Page<ContractRenewEntity> doSearchContractRenew(ContractRenewDTO dto, Pageable pageable);
    @Query(value = "select e from ContractRenewEntity e where 1=1 " +
            " and (:#{#dto.name} is null or lower(e.name) like lower(concat('%', trim(:#{#dto.name}), '%') ) )" +
            " and (:#{#dto.description} is null or lower(e.description) like lower(concat('%',trim(:#{#dto.description}) ,'%') ) )" +
            " and (:#{#dto.value} is null or lower(e.value) like lower(concat('%',trim(:#{#dto.value}) ,'%') ) )" +
            " and (:#{#dto.createdBy} is null or e.createdBy = :#{#dto.createdBy} )" +
            " and (:#{#dto.updatedBy} is null or e.updatedBy = :#{#dto.updatedBy} )" +
            " order by e.updatedBy desc"
    )
    Page<ContractRenewEntity> doSearch(@Param("dto") ContractRenewDTO dto, Pageable pageable);
    @Query(value = "Select count(*) from ContractRenewEntity e where e.value = :value and (:id is null or e.id != :id)")
    Integer countByValue(String value, Long id);
}
