package com.doan.nhansu.users.repository;

import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "select e from UserEntity e where 1=1 " +
            " and (:#{#dto.id} is null or e.id = :#{#dto.id}) " +
            " and (:#{#dto.keySearch} is null or lower(e.fullName) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            " or lower(e.value) like lower(concat('%',trim(:#{#dto.keySearch}) ,'%') ) " +
            ") " +
            " order by e.id desc"
    )
    Page<UserEntity> doSearchUser(UserDTO dto, Pageable pageable);
    @Query("SELECT COUNT(e) FROM UserEntity e WHERE e.created BETWEEN :startDate AND :endDate")
    Long countEmployeesByCreatedDateBetween(Date startDate, Date endDate);

    @Query("Select id from UserEntity e where e.departmentId = :departmentId")
    List<Long> UserIdsByDepartmentId(Long departmentId);
    @Query("Select departmentId from UserEntity where id = :userId")
    Long departmentByUser(Long userId);
    @Query(value = "Select count(*) from UserEntity e where e.value = :value and (:id is null or e.id != :id)")
    Integer countByValue(String value, Long id);
}
