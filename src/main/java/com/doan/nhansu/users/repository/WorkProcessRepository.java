package com.doan.nhansu.users.repository;

import com.doan.nhansu.users.entity.WorkProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkProcessRepository extends JpaRepository<WorkProcessEntity, Long>{
    @Modifying
    @Query("delete from WorkProcessEntity where userId = ?1 ")
    void deleteByUserId(Long userId);
}
