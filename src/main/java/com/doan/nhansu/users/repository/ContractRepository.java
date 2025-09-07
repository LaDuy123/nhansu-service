package com.doan.nhansu.users.repository;

import com.doan.nhansu.users.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

    @Query(value = "Select count(*) from ContractEntity e where e.userId = :userId")
    Integer countByUser(Long userId);
}
