package com.doan.nhansu.timekeep.repository.Custom;

import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftTypeDTO;
import com.doan.nhansu.timekeep.entity.ShiftTypeEntity;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ShiftTypeRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";
    
    public boolean existsById(Long id) {
        String selectSql = " Select Count(*) from shifttype where id = :id ";
        Query selectQuery = entityManager.createNativeQuery(selectSql);
        selectQuery.setParameter("id", id);

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }
    
    public boolean existsByNameUpdate(Long id, String name) {
        String selectSql = " Select Count(*) from shift_type where name = :name AND shift_type_id <> :id";
        Query selectQuery = entityManager.createNativeQuery(selectSql);
        selectQuery.setParameter("name", name);
        selectQuery.setParameter("id", id);

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }

    public List<ShiftTypeDTO> doSearch(ShiftTypeDTO dto) {

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
        if (Objects.nonNull(dto.getId())) {
            sqlWhere += " AND s.shift_type_id = :id";
            params.put("id", dto.getId());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" s.SHIFT_TYPE_ID as id, ");
        sb.append(" s.NAME as name, ");
        sb.append(" s.COEFFICIENT as coefficient, ");
        sb.append(" s.END_TIME as endTime, ");
        sb.append(" s.START_TIME as startTime, ");
        sb.append(" s.BREAK_TIME as breakTime, ");
        sb.append(" s.END_BREAK_TIME as endBreakTime, ");
        sb.append(" s.TOTAL_WORK_HOURS as totalWorkHours, ");
        sb.append(" s.VALUE as value, ");
        sb.append(" s.CREATED as created, ");
        sb.append(" s.CREATEDBY as createdBY, ");
        sb.append(" s.UPDATED as updated, ");
        sb.append(" s.UPDATEDBY as updatedBy ");
        sb.append(" from shift_type s ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by s.updated desc " );
        sb.append( " OFFSET :startRow ROWS FETCH NEXT :endRow ROWS ONLY" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("value", StandardBasicTypes.STRING)
                .addScalar("coefficient", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("breakTime", StandardBasicTypes.STRING)
                .addScalar("endBreakTime", StandardBasicTypes.STRING)
                .addScalar("startTime", StandardBasicTypes.STRING)
                .addScalar("endTime", StandardBasicTypes.STRING)
                .addScalar("totalWorkHours", StandardBasicTypes.STRING)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(ShiftTypeDTO.class));
        List<ShiftTypeDTO> shiftTypeDTOS = querySelect.getResultList();

        return shiftTypeDTOS;

    }

}
