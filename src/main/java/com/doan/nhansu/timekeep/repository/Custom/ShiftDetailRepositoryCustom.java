package com.doan.nhansu.timekeep.repository.Custom;

import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.timekeep.dto.ShiftDetailDTO;
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

import java.math.BigDecimal;
import java.util.*;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class ShiftDetailRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";

    public List<ShiftDetailDTO> doSearch(ShiftDTO dto) {
        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        if(Objects.nonNull(dto.getId())){
            sqlWhere += " and s.shift_id = :id";
            params.put("id", dto.getId());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" s.SHIFT_DETAIL_ID as id, ");
        sb.append(" s.DAY as day, ");
        sb.append(" s.SHIFT_ID as shiftId, ");
        sb.append(" s.SHIFT_TYPE_Id as shiftTypeId, ");
        sb.append(" st.START_TIME as startTime, ");
        sb.append(" st.END_TIME as endTime, ");
        sb.append(" s.NAME as name, ");
        sb.append(" s.CREATED as created, ");
        sb.append(" s.CREATEDBY as createdBY, ");
        sb.append(" s.UPDATED as updated, ");
        sb.append(" s.UPDATEDBY as updatedBy ");
        sb.append(" from shift_detail s ");
        sb.append(" Left join shift_type st on s.shift_type_id = st.shift_type_id ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by s.name asc " );

// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("day", StandardBasicTypes.DATE)
                .addScalar("shiftId", StandardBasicTypes.LONG)
                .addScalar("shiftTypeId", StandardBasicTypes.LONG)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("startTime", StandardBasicTypes.STRING)
                .addScalar("endTime", StandardBasicTypes.STRING)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(ShiftDetailDTO.class));
        List<ShiftDetailDTO> shiftDetailDTOS = querySelect.getResultList();
        return shiftDetailDTOS;
    }
    public boolean existedByDetail(ShiftDetailDTO dto){
        String selectSql = """
        SELECT COUNT(*) FROM shift_detail
        WHERE shift_id = :shiftId
        AND shift_type_id = :shiftTypeId
        AND name = :name
        """;
        Session session = entityManager.unwrap(Session.class);
        Query selectQuery = session.createNativeQuery(selectSql);
        selectQuery.setParameter("shiftId", dto.getShiftId());
        selectQuery.setParameter("shiftTypeId", dto.getShiftTypeId());
        selectQuery.setParameter("name", dto.getName());

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }

    public BigDecimal checkDayOffPaid(Date startDate, Date endDate, Long shiftId){
        String sql = "Select Count(*) from shift_detail where shift_id = :shiftId and day >= :startDate and day <= :endDate and TO_CHAR(day, 'DY', 'NLS_DATE_LANGUAGE=AMERICAN') NOT IN ('SAT', 'SUN')";
        Session session =entityManager.unwrap(Session.class);
        Query query = session.createNativeQuery(sql);
        query.setParameter("shiftId", shiftId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        BigDecimal dayOffPaid = (BigDecimal) query.getSingleResult();
        if (dayOffPaid == null) {
            dayOffPaid = BigDecimal.ZERO;
        }
        return dayOffPaid;
    }
}
