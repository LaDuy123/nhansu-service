package com.doan.nhansu.admin.repository;

import com.doan.nhansu.admin.entity.RefreshTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    @Modifying
    @Query("delete from RefreshTokenEntity where userId = ?1 ")
    void deleteByUserId(Long userId);
}
