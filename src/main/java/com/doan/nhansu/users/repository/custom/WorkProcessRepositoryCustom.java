package com.doan.nhansu.users.repository.custom;

import com.doan.nhansu.users.dto.WorkProcessDTO;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class WorkProcessRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";
    
    public boolean existsById(Long WorkProcessId) {
        String selectSql = " Select Count(*) from WorkProcess where id = :WorkProcessId ";
        Query selectQuery = entityManager.createNativeQuery(selectSql);
        selectQuery.setParameter("WorkProcessId", WorkProcessId);

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }

    public List<WorkProcessDTO> doSearchByUser(Long id) {
        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(id)){
            sqlWhere += " and e.USER_ID = :id ";
            params.put("id",id);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" e.WORKPROCESS_ID as id, ");
        sb.append(" e.USER_ID as userId, ");
        sb.append(" e.DEPARTMENT_ID as departmentId, ");
        sb.append(" e.POSITION_ID as positionId, e.START_DATE as startDate, ");
        sb.append(" e.END_DATE as endDate, ");
        sb.append(" e.VALUE as value, ");
        sb.append(" (Select fullname from users u where u.user_id = e.user_id) as fullName, ");
        sb.append(" (Select name from department d where d.department_id = e.department_id) as departmentName, ");
        sb.append(" (Select name from position p where p.position_id = e.position_id) as positionName, ");
        sb.append(" e.CREATED as created, ");
        sb.append(" e.CREATEDBY as createdBY, ");
        sb.append(" e.UPDATED as updated, ");
        sb.append(" e.UPDATEDBY as updatedBy ");
        sb.append(" from WorkProcess e ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by e.updated desc" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("userId", StandardBasicTypes.LONG)
                .addScalar("departmentId", StandardBasicTypes.LONG)
                .addScalar("positionId", StandardBasicTypes.LONG)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("value", StandardBasicTypes.STRING)
                .addScalar("departmentName", StandardBasicTypes.STRING)
                .addScalar("positionName", StandardBasicTypes.STRING)
                .addScalar("startDate", StandardBasicTypes.DATE)
                .addScalar("endDate", StandardBasicTypes.DATE)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)
                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(WorkProcessDTO.class));
        List<WorkProcessDTO> WorkProcessDTOS = querySelect.getResultList();

        return WorkProcessDTOS;

    }
}
