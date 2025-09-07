package com.doan.nhansu.users.repository.custom;

import com.doan.nhansu.users.dto.PositionDTO;

import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.entity.PositionEntity;

import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class PositionRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";
    
    public boolean existsById(Long positionId) {
        String selectSql = " Select Count(*) from position where id = :positionId ";
        Query selectQuery = entityManager.createNativeQuery(selectSql);
        selectQuery.setParameter("positionId", positionId);

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }

    
    public boolean existsByName(String positionName) {
        String selectSql = " Select Count(*) from position where name = :position_name";
        Query selectQuery = entityManager.createNativeQuery(selectSql);
        selectQuery.setParameter("position_name", positionName);

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }

    
    public boolean existsByNameUpdate(Long positionId, String positionName) {
        String selectSql = " Select Count(*) from position where name = :position_name and position_id <> :id";
        Query selectQuery = entityManager.createNativeQuery(selectSql);
        selectQuery.setParameter("position_name", positionName);
        selectQuery.setParameter("id", positionId);

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }

    


    public List<PositionDTO> doSearch(PositionDTO dto) {

        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        params.put(startRow,(dto.getPage()-1) * dto.getSize());
        params.put(endRow,dto.getSize());
        if (Objects.nonNull(dto.getValue()) && !dto.getValue().trim().isEmpty()) {
            sqlWhere += " AND lower(value) LIKE :value";
            params.put("value", "%" + dto.getValue().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getName()) && !dto.getName().trim().isEmpty()) {
            sqlWhere += " AND lower(name) LIKE :name";
            params.put("name", "%" + dto.getName().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getCreatedBy())) {
            sqlWhere += " AND createdBy = :createdBy";
            params.put("createdBy", dto.getCreatedBy());
        }
        if (Objects.nonNull(dto.getUpdatedBy())) {
            sqlWhere += " AND updatedBy = :updatedBy";
            params.put("updatedBy", dto.getUpdatedBy());
        }
        if (Objects.nonNull(dto.getId())) {
            sqlWhere += " AND position_id = :id";
            params.put("id", "%" + dto.getId() + "%");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" POSITION_ID as id, ");
        sb.append(" NAME as name, ");
        sb.append(" VALUE as value, ");
        sb.append(" CREATED as created, ");
        sb.append(" CREATEDBY as createdBY, ");
        sb.append(" UPDATED as updated, ");
        sb.append(" UPDATEDBY as updatedBy ");
        sb.append(" from position ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by updated desc" );
        sb.append( " OFFSET :startRow ROWS FETCH NEXT :endRow ROWS ONLY" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("value", StandardBasicTypes.STRING)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

               ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(PositionDTO.class));
        List<PositionDTO> positionDTOS = querySelect.getResultList();

        return positionDTOS;

    }
}
