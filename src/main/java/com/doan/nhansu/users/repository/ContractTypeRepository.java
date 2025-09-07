package com.doan.nhansu.users.repository;

import com.doan.nhansu.users.dto.ContractTypeDTO;
import com.doan.nhansu.users.entity.ContractTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractTypeRepository extends JpaRepository<ContractTypeEntity, Long> {
    @Query(value = "select e from ContractTypeEntity e where 1=1 " +
            " and (:#{#dto.id} is null or e.id = :#{#dto.id}) " +
            " and (:#{#dto.keySearch} is null or lower(e.name) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            " or lower(e.value) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            ") " +
            " order by e.id desc"
    )
    Page<ContractTypeEntity> doSearchContractType(ContractTypeDTO dto, Pageable pageable);
    @Query(value = "Select count(*) from ContractTypeEntity e where e.value = :value and (:id is null or e.id != :id)")
    Integer countByValue(String value, Long id);
}
