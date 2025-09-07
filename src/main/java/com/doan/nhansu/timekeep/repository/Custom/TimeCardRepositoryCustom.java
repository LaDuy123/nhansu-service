package com.doan.nhansu.timekeep.repository.Custom;

import com.doan.nhansu.timekeep.dto.TimeCardDTO;
import com.doan.nhansu.timekeep.dto.TimeSheetDTO;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.repository.custom.DepartmentRepositoryCustom;
import com.doan.nhansu.users.repository.custom.UserRepositoryCustom;
import com.doan.nhansu.timekeep.entity.TimeCardEntity;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.admin.mapper.TimeCardMapper;
import com.doan.nhansu.timekeep.repository.TimeSheetRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class TimeCardRepositoryCustom  {
    @PersistenceContext
    EntityManager entityManager;
    DepartmentRepositoryCustom departmentRepository;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";

   
    public boolean checkTimeCard(Long userId, Long month, Long year) {
        String sql = """
        Select Count(*) from timecard
        where user_id = :user_id
        and month = :month
        and year = :year""";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("user_id", userId);
        query.setParameter("month", month);
        query.setParameter("year", year);

        Number check = (Number) query.getSingleResult();
        return check.intValue() > 0;
    }
    public void deleteTimeCard(Long userId, Long month, Long year) {
        String deleteSql = "DELETE FROM timecard WHERE user_id = :user_id AND month = :month AND year = :year";
        Query deleteQuery = entityManager.createNativeQuery(deleteSql);
        deleteQuery.setParameter("user_id", userId);
        deleteQuery.setParameter("month", month);
        deleteQuery.setParameter("year", year);
        deleteQuery.executeUpdate();
    }

   
    public List<UserDTO> workMost(Long month, Long year) {
        List<Long> departmentIds = departmentRepository.getDepartmentIds();
        List<UserDTO> combinedResults = new ArrayList<>();

        for (Long departmentId : departmentIds) {
            List<UserDTO> resultByDept = workMostByDepartment(month, year, departmentId);
            combinedResults.addAll(resultByDept);
        }
        return combinedResults;
    }


    public List<UserDTO> workMostByDepartment(Long month, Long year, Long department_id) {
        String parentPath = departmentRepository.getPath(department_id).orElseThrow(()
                -> new AppException(MessageError.ID_EXISTED));
        String currentDepartmentName = departmentRepository.getDepartmetnName(department_id);
        String sql = """
    SELECT u.username, d.department_name AS resultDepartmentName, p.position_name AS positionName, t.totalwork AS totalWork,
           :searchingDepartmentName AS searchingDepartmentName
    FROM users u
    JOIN department d ON u.department_id = d.id
    JOIN position p ON u.position_id = p.id
    JOIN timecard t ON u.id = t.user_id
    WHERE t.month = :month AND t.year = :year AND d.path LIKE :parentPath || '%'
    AND t.totalwork = (
        SELECT MAX(t2.totalwork)
        FROM timecard t2
        JOIN users u2 ON t2.user_id = u2.id
        JOIN department d2 ON u2.department_id = d2.id
        WHERE d2.path LIKE :parentPath || '%'
        AND t2.month = :month AND t2.year = :year
    )
    """;

        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query query = session.createNativeQuery(sql)
                .addScalar("username", StandardBasicTypes.STRING)
                .addScalar("resultDepartmentName", StandardBasicTypes.STRING)
                .addScalar("positionName", StandardBasicTypes.STRING)
                .addScalar("totalWork", StandardBasicTypes.FLOAT)
                .addScalar("searchingDepartmentName", StandardBasicTypes.STRING);

        query.setParameter("month", month);
        query.setParameter("year", year);
        query.setParameter("parentPath", parentPath);
        query.setParameter("searchingDepartmentName", currentDepartmentName);
        query.setResultTransformer(Transformers.aliasToBean(UserDTO.class));
        return query.getResultList();
    }

   
    public List<UserDTO> restMost(Long month, Long year) {
        List<Long> departmentIds = departmentRepository.getDepartmentIds();
        List<UserDTO> combinedResults = new ArrayList<>();

        for (Long departmentId : departmentIds) {
            List<UserDTO> resultByDept = restMostByDepartment(month, year, departmentId);
            combinedResults.addAll(resultByDept);
        }
        return combinedResults;
    }

   
    public List<UserDTO> restMostByDepartment(Long month, Long year, Long department_id) {
        String parentPath = departmentRepository.getPath(department_id).orElseThrow(()
                -> new AppException(MessageError.ID_EXISTED));
        String currentDepartmentName = departmentRepository.getDepartmetnName(department_id);
        String sql = """
    SELECT u.username, d.department_name AS resultDepartmentName, p.position_name AS positionName, t.totalwork AS totalWork,
           :searchingDepartmentName AS searchingDepartmentName
    FROM users u
    JOIN department d ON u.department_id = d.id
    JOIN position p ON u.position_id = p.id
    JOIN timecard t ON u.id = t.user_id
    WHERE t.month = :month AND t.year = :year AND d.path LIKE :parentPath || '%'
    AND t.totalwork = (
        SELECT MIN(t2.totalwork)
        FROM timecard t2
        JOIN users u2 ON t2.user_id = u2.id
        JOIN department d2 ON u2.department_id = d2.id
        WHERE d2.path LIKE :parentPath || '%'
        AND t2.month = :month AND t2.year = :year
    )
    """;

        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query query = session.createNativeQuery(sql)
                .addScalar("username", StandardBasicTypes.STRING)
                .addScalar("resultDepartmentName", StandardBasicTypes.STRING)
                .addScalar("positionName", StandardBasicTypes.STRING)
                .addScalar("totalWork", StandardBasicTypes.FLOAT)
                .addScalar("searchingDepartmentName", StandardBasicTypes.STRING);

        query.setParameter("month", month);
        query.setParameter("year", year);
        query.setParameter("parentPath", parentPath);
        query.setParameter("searchingDepartmentName", currentDepartmentName);
        query.setResultTransformer(Transformers.aliasToBean(UserDTO.class));
        return query.getResultList();
    }
   
    public List<TimeCardEntity> findTimeCardByUser(Long userId, Long month, Long year) {
        String sql = "Select * from timecard where user_id = :userId";
        if(month != null && month >= 1 && month <= 12 && year != null){
            sql = sql + " and month = :month and year = :year";
        }
        Query query = entityManager.createNativeQuery(sql, TimeCardEntity.class);
        query.setParameter("userId", userId);
        if(month != null && month >= 1 && month <= 12 && year != null){
            query.setParameter("month", month);
            query.setParameter("year", year);
        }
        return query.getResultList();
    }

    public List<TimeCardDTO> doSearch(TimeCardDTO dto){
        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        params.put(startRow,(dto.getPage()-1) * dto.getSize());
        params.put(endRow,dto.getSize());
        if (Objects.nonNull(dto.getValue()) && !dto.getValue().trim().isEmpty()) {
            sqlWhere += " AND lower(t.value) LIKE :value";
            params.put("value", "%" + dto.getValue().trim().toLowerCase() + "%");
        }
        if(Objects.nonNull(dto.getUserId())){
            sqlWhere += " and t.user_id = :userId ";
            params.put("userId", dto.getUserId());
        }
        if(Objects.nonNull(dto.getDepartmentId())){
            sqlWhere += " and d.department_id = :departmentId ";
            params.put("departmentId", dto.getDepartmentId());
        }
        if(Objects.nonNull(dto.getMonth())){
            sqlWhere += " and t.month = :month ";
            params.put("month", dto.getMonth());
        }
        if(Objects.nonNull(dto.getYear())){
            sqlWhere += " and t.year = :year ";
            params.put("year", dto.getYear());
        }
        if (Objects.nonNull(dto.getCreatedBy())) {
            sqlWhere += " AND t.createdBy = :createdBy";
            params.put("createdBy", dto.getCreatedBy());
        }
        if (Objects.nonNull(dto.getUpdatedBy())) {
            sqlWhere += " AND t.updatedBy = :updatedBy";
            params.put("updatedBy", dto.getUpdatedBy());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" t.TIMECARD_ID as id, ");
        sb.append(" t.MONTH as month, ");
        sb.append(" t.YEAR as year, ");
        sb.append(" t.VALUE as value, ");
        sb.append(" t.DAYS_OFF_PAID as daysOffPaid, ");
        sb.append(" t.DAYS_OFF_UNPAID as daysOffUnpaid, ");
        sb.append(" t.TOTALWORKHOURS_DAY as totalWorkHoursDay, ");
        sb.append(" t.TOTALWORKHOURS_SUN as totalWorkHoursSun, ");
        sb.append(" t.TOTALOVERTIMEHOURS as totalOverTimeHours, ");
        sb.append(" t.START_DATE as startDate, ");
        sb.append(" t.END_DATE as endDate, ");
        sb.append(" t.TOTALWORK_DAY as totalWorkDay, ");
        sb.append(" t.TOTALWORK_Sun as totalWorkSun, ");
        sb.append(" t.NAME as name, ");
        sb.append(" t.USER_ID as userId, ");
        sb.append(" u.fullName as fullName, ");
        sb.append(" d.NAME as departmentName, ");
        sb.append(" t.CREATED as created, ");
        sb.append(" t.CREATEDBY as createdBY, ");
        sb.append(" t.UPDATED as updated, ");
        sb.append(" t.UPDATEDBY as updatedBy ");
        sb.append(" from TimeCard t ");
        sb.append(" left join users u on t.user_id = u.user_id ");
        sb.append(" left join department d on u.department_id = d.department_id ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by t.updated desc " );
        sb.append( " OFFSET :startRow ROWS FETCH NEXT :endRow ROWS ONLY" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("userId", StandardBasicTypes.LONG)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("value", StandardBasicTypes.STRING)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("departmentName", StandardBasicTypes.STRING)
                .addScalar("month", StandardBasicTypes.LONG)
                .addScalar("year", StandardBasicTypes.LONG)
                .addScalar("startDate", StandardBasicTypes.DATE)
                .addScalar("endDate", StandardBasicTypes.DATE)
                .addScalar("totalWorkHoursDay", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("totalWorkHoursSun", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("totalOverTimeHours", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("daysOffPaid", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("daysOffUnpaid", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("totalWorkDay", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("totalWorkSun", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(TimeCardDTO.class));
        List<TimeCardDTO> timeCardDTOS = querySelect.getResultList();
        return timeCardDTOS;
    }
}
