package com.doan.nhansu.timekeep.repository;

import com.doan.nhansu.timekeep.entity.TimeCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeCardRepository extends JpaRepository<TimeCardEntity, Long> {

}
