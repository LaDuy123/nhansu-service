package com.doan.nhansu.users.repository.custom;

import com.doan.nhansu.users.dto.ContractTypeDTO;
import com.doan.nhansu.users.repository.UserRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class ContractTypeRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    UserRepository userRepository;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";
    public List<ContractTypeDTO> doSearch(ContractTypeDTO dto) {


        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        params.put(startRow,(dto.getPage()-1) * dto.getSize());
        params.put(endRow,dto.getSize());
        if (Objects.nonNull(dto.getValue()) && !dto.getValue().trim().isEmpty()) {
            sqlWhere += " AND lower(c.value) LIKE :value";
            params.put("value", "%" + dto.getValue().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getName())) {
            sqlWhere += " AND lower(c.name) LIKE :name";
            params.put("name", "%" + dto.getName().trim().toLowerCase() + "%");
        } if (Objects.nonNull(dto.getDescription())) {
            sqlWhere += " AND lower(c.description) LIKE :description";
            params.put("description", "%" + dto.getDescription().trim().toLowerCase() + "%");
        }

        if (Objects.nonNull(dto.getCreatedBy())) {
            sqlWhere += " AND c.createdBy = :createdBy";
            params.put("createdBy", dto.getCreatedBy());
        }
        if (Objects.nonNull(dto.getUpdatedBy())) {
            sqlWhere += " AND c.updatedBy = :updatedBy";
            params.put("updatedBy", dto.getUpdatedBy());
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" c.CONTRACT_TYPE_ID as id, ");
        sb.append(" c.NAME as name, ");
        sb.append(" c.VALUE as value, ");
        sb.append(" c.DESCRIPTION as description, ");
        sb.append(" c.CREATED as created, ");
        sb.append(" c.CREATEDBY as createdBY, ");
        sb.append(" c.UPDATED as updated, ");
        sb.append(" c.UPDATEDBY as updatedBy ");
        sb.append("  from Contract_Type c ");
        sb.append("  WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by c.updated desc" );
        sb.append( " OFFSET :startRow ROWS FETCH NEXT :endRow ROWS ONLY" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        Query querySelect = session.createNativeQuery(sb.toString())
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
        querySelect.setResultTransformer(Transformers.aliasToBean(ContractTypeDTO.class));
        List<ContractTypeDTO> lstData = querySelect.getResultList();

        return lstData;

    }
}
