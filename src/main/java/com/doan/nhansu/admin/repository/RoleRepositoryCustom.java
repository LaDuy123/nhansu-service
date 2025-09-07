package com.doan.nhansu.admin.repository;

import com.doan.nhansu.admin.dto.RoleDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class RoleRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";
    public List<RoleDTO> doSearch(RoleDTO dto) {
        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        params.put(startRow,(dto.getPage()-1) * dto.getSize());
        params.put(endRow,dto.getSize());
        if (Objects.nonNull(dto.getValue()) && !dto.getValue().trim().isEmpty()) {
            sqlWhere += " AND lower(r.value) LIKE :value";
            params.put("value", "%" + dto.getValue().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getName()) && !dto.getName().trim().isEmpty()) {
            sqlWhere += " AND lower(r.name) LIKE :name";
            params.put("name", "%" + dto.getName().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getDescription()) && !dto.getDescription().trim().isEmpty()) {
            sqlWhere += " AND lower(r.description) LIKE :description";
            params.put("description", "%" + dto.getDescription().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getCreatedBy())) {
            sqlWhere += " AND r.createdBy = :createdBy";
            params.put("createdBy", dto.getCreatedBy());
        }
        if (Objects.nonNull(dto.getUpdatedBy())) {
            sqlWhere += " AND r.updatedBy = :updatedBy";
            params.put("updatedBy", dto.getUpdatedBy());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" r.ROLE_ID as id, ");
        sb.append(" r.VALUE as value, ");
        sb.append(" r.NAME as name, ");
        sb.append(" r.DESCRIPTION as description, ");
        sb.append(" r.CREATED as created, ");
        sb.append(" r.CREATEDBY as createdBY, ");
        sb.append(" r.UPDATED as updated, ");
        sb.append(" r.UPDATEDBY as updatedBy ");
        sb.append(" from Role r ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by r.updated desc " );
        sb.append( " OFFSET :startRow ROWS FETCH NEXT :endRow ROWS ONLY" );

// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("value", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(RoleDTO.class));
        List<RoleDTO> RoleDTOS = querySelect.getResultList();
        return RoleDTOS;
    }
}
