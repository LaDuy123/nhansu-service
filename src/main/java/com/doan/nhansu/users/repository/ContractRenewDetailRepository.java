package com.doan.nhansu.users.repository;

import com.doan.nhansu.users.entity.ContractRenewDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRenewDetailRepository extends JpaRepository<ContractRenewDetailEntity, Long> {
    @Query(value = "select e from ContractRenewDetailEntity e where e.contractRenewId = :contractRenewId order by e.updatedBy desc")
    List<ContractRenewDetailEntity> doSearchByContractRenew(Long contractRenewId);
}
