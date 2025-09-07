package com.doan.nhansu.timekeep.repository.Custom;

import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.users.repository.DepartmentRepository;
import com.doan.nhansu.timekeep.repository.ShiftRepository;
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

import java.math.BigDecimal;
import java.util.*;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ShiftRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    DepartmentRepository departmentRepository;
    ShiftRepository shiftRepository;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";

    public boolean existsById(Long shiftId) {
        String selectSql = " Select Count(*) from shift where shift_id = :shiftId ";
        Query selectQuery = entityManager.createNativeQuery(selectSql);
        selectQuery.setParameter("shiftId", shiftId);

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }

    public boolean existsByNameUpdate(Long shiftId, String shiftName) {
        String selectSql = " Select Count(*) from shift where name = :shift_name and shift_id <> :id";
        Query selectQuery = entityManager.createNativeQuery(selectSql);
        selectQuery.setParameter("shift_name", shiftName);
        selectQuery.setParameter("id", shiftId);

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }

    public List<ShiftDTO> doSearch(ShiftDTO dto) {
        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        params.put(startRow,(dto.getPage()-1) * dto.getSize());
        params.put(endRow,dto.getSize());
        if (Objects.nonNull(dto.getValue()) && !dto.getValue().trim().isEmpty()) {
            sqlWhere += " AND lower(s.value) LIKE :value";
            params.put("value", "%" + dto.getValue().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getName()) && !dto.getName().trim().isEmpty()) {
            sqlWhere += " AND lower(s.name) LIKE :name";
            params.put("name", "%" + dto.getName().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getCreatedBy())) {
            sqlWhere += " AND s.createdBy = :createdBy";
            params.put("createdBy", dto.getCreatedBy());
        }
        if (Objects.nonNull(dto.getUpdatedBy())) {
            sqlWhere += " AND s.updatedBy = :updatedBy";
            params.put("updatedBy", dto.getUpdatedBy());
        }
        if (Objects.nonNull(dto.getStartDate())) {
            sqlWhere += " AND s.start_date <= :startDate ";
            params.put("startDate", dto.getStartDate());
        }
        if (Objects.nonNull(dto.getEndDate())) {
            sqlWhere += " AND s.end_date >= :endDate ";
            params.put("endDate", dto.getEndDate());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" s.SHIFT_ID as id, ");
        sb.append(" s.NAME as name, ");
        sb.append(" s.DEPARTMENT_ID as departmentId, ");
        sb.append(" s.END_DATE as endDate, ");
        sb.append(" s.START_DATE as startDate, ");
        sb.append(" d.NAME as departmentName, ");
        sb.append(" s.Value as value, ");
        sb.append(" s.CREATED as created, ");
        sb.append(" s.CREATEDBY as createdBY, ");
        sb.append(" s.UPDATED as updated, ");
        sb.append(" s.UPDATEDBY as updatedBy ");
        sb.append(" from Shift s ");
        sb.append(" Left join department d on s.department_id = d.department_id ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by s.updated desc " );
        sb.append( " OFFSET :startRow ROWS FETCH NEXT :endRow ROWS ONLY" );

// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("departmentId", StandardBasicTypes.LONG)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("value", StandardBasicTypes.STRING)
                .addScalar("departmentName", StandardBasicTypes.STRING)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("startDate", StandardBasicTypes.DATE)
                .addScalar("endDate", StandardBasicTypes.DATE)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(ShiftDTO.class));
        List<ShiftDTO> shiftDTOS = querySelect.getResultList();
        return shiftDTOS;
    }


}
