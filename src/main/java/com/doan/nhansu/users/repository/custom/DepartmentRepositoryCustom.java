package com.doan.nhansu.users.repository.custom;

import com.doan.nhansu.users.dto.DepartmentDTO;
import com.doan.nhansu.users.entity.DepartmentEntity;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.mapper.DepartmentMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.hibernate.query.Query;
import org.hibernate.Session;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class DepartmentRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    DepartmentMapper departmentMapper;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";

    
    public boolean existsByName(String departmentName) {
        String selectSql = " Select Count(*) from department where name = :department_name ";
        Session session = entityManager.unwrap(Session.class);
        Query selectQuery = session.createNativeQuery(selectSql);
        selectQuery.setParameter("department_name", departmentName);

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }


    public String getDepartmetnName(Long department_id) {
        String selectSql = " Select department_name from department where id = :id ";
        Session session = entityManager.unwrap(Session.class);
        Query selectQuery = session.createNativeQuery(selectSql);
        selectQuery.setParameter("id", department_id);

        return (String) selectQuery.getSingleResult();
    }


    public List<Long> getDepartmentIds() {
        String sql = "Select id from department";
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createNativeQuery(sql);
        List<Long> ids = query.getResultList();
        return  ids;
    }


    public boolean existsByNameUpdate(Long departmentId, String departmentName) {
        String selectSql = """
        SELECT COUNT(*) FROM department 
        WHERE name = :department_name 
        AND department_id <> :departmentId
    """;
        Session session = entityManager.unwrap(Session.class);
        Query selectQuery = session.createNativeQuery(selectSql);
        selectQuery.setParameter("department_name", departmentName);
        selectQuery.setParameter("departmentId", departmentId);

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }
    public Optional<String> getPath(Long idDepartment) {
        // Câu lệnh SQL sử dụng hàm UPPER để chuyển toàn bộ dữ liệu thành chữ hoa và so sánh
        String getPathSql = "SELECT path FROM department WHERE lower(department_id) = lower(:department_id)";
        Session session = entityManager.unwrap(Session.class);

        // Lấy path của phòng ban
        Query query = session.createNativeQuery(getPathSql);
        query.setParameter("department_id", idDepartment);

        try{
            return  Optional.of((String)query.getSingleResult());
        }catch (Exception e){
            return Optional.empty();
        }
    }


    public List<DepartmentDTO> doSearch(DepartmentDTO dto) {

        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        params.put(startRow,(dto.getPage()-1) * dto.getSize());
        params.put(endRow,dto.getSize());
        if (Objects.nonNull(dto.getValue()) && !dto.getValue().trim().isEmpty()) {
            sqlWhere += " AND lower(c.value) LIKE :value";
            params.put("value", "%" + dto.getValue().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getName()) && !dto.getName().trim().isEmpty()) {
            sqlWhere += " AND lower(d.name) LIKE :name";
            params.put("name", "%" + dto.getName().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getParentId())) {
            sqlWhere += " AND d.parent_id LIKE :id";
            params.put("id", "%" + dto.getParentId() + "%");
        }
        if (Objects.nonNull(dto.getCreatedBy())) {
            sqlWhere += " AND d.createdBy = :createdBy";
            params.put("createdBy",  dto.getCreatedBy());
        }
        if (Objects.nonNull(dto.getUpdatedBy())) {
            sqlWhere += " AND d.updatedBy = :updatedBy";
            params.put("updatedBy", dto.getUpdatedBy());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" d.DEPARTMENT_ID as id, ");
        sb.append(" d.NAME as name, ");
        sb.append(" d.PARENT_ID as parentId, ");
        sb.append(" d.VALUE as value, ");
        sb.append(" d.PATH as path, ");
        sb.append(" d.DEPARTMENT_LEVEL as departmentLevel, ");
        sb.append(" (Select d1.name from department d1 where d1.department_id = d.parent_id) as departmentParent, ");
        sb.append(" d.CREATED as created, ");
        sb.append(" d.CREATEDBY as createdBY, ");
        sb.append(" d.UPDATED as updated, ");
        sb.append(" d.UPDATEDBY as updatedBy ");
        sb.append(" from Department d ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by d.updated desc" );
        sb.append( " OFFSET :startRow ROWS FETCH NEXT :endRow ROWS ONLY" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("value", StandardBasicTypes.STRING)
                .addScalar("departmentParent", StandardBasicTypes.STRING)
                .addScalar("departmentLevel", StandardBasicTypes.LONG)
                .addScalar("parentId", StandardBasicTypes.LONG)
                .addScalar("path", StandardBasicTypes.STRING)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(DepartmentDTO.class));
        List<DepartmentDTO> departmentDTOS = querySelect.getResultList();

        return departmentDTOS;

    }
    

}
