package com.doan.nhansu.admin.repository;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.admin.dto.RefreshTokenDTO;
import com.doan.nhansu.admin.dto.UserResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class RefreshTokenRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    public void deleteRefreshToken(Long userId){
        String sql = "delete from refreshtoken where user_id = :userId";
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createNativeQuery(sql);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }
    public Optional<RefreshTokenDTO> getRefreshToken(String token){
        String sql = "select token as token, user_id as userId, expirydate as expiryDate from refreshtoken where token = :token";
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createNativeQuery(sql)
                .addScalar("token", StandardBasicTypes.STRING)
                .addScalar("userId", StandardBasicTypes.LONG)
                .addScalar("expiryDate", StandardBasicTypes.DATE);
        query.setParameter("token", token);
        query.setResultTransformer(Transformers.aliasToBean(RefreshTokenDTO.class));
        try{
            return Optional.of((RefreshTokenDTO) query.getSingleResult());
        }catch (Exception e){
            return Optional.empty();
        }

    }
}
