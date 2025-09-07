package com.doan.nhansu.timekeep.repository.Custom;

import com.doan.nhansu.timekeep.dto.TimeCardDTO;
import com.doan.nhansu.timekeep.dto.TimeSheetDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.query.Query;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class TimeSheetRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";
    
    public boolean existsById(Long id) {
        String selectSql = " Select Count(*) from timesheet where id = :id ";
        Session session = entityManager.unwrap(Session.class);
        Query selectQuery = session.createNativeQuery(selectSql);
        selectQuery.setParameter("id", id);

        Number count = (Number) selectQuery.getSingleResult();
        return count.intValue() > 0;
    }
    public BigDecimal totalWorkHoursDay(Long userId, Date startDate, Date endDate) {
        String sql = "Select Sum(total_hours) from timesheet where user_id = :userId and workdate >= :startDate and workdate <= :endDate and day_of_week NOT IN ('thứ 7', 'chủ nhật') ";
        Session session =entityManager.unwrap(Session.class);
        Query query = session.createNativeQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        BigDecimal totalWorkHours = (BigDecimal) query.getSingleResult();
        if (totalWorkHours == null) {
            totalWorkHours = BigDecimal.ZERO;
        }
        return totalWorkHours;
    }
    public BigDecimal totalWorkHoursSun(Long userId, Date startDate, Date endDate) {
        String sql = "Select Sum(total_hours) from timesheet where user_id = :userId and workdate >= :startDate and workdate <= :endDate and day_of_week IN ('Thứ 7', 'Chủ nhật') ";
        Session session =entityManager.unwrap(Session.class);
        Query query = session.createNativeQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        BigDecimal totalWorkHours = (BigDecimal) query.getSingleResult();
        if (totalWorkHours == null) {
            totalWorkHours = BigDecimal.ZERO;
        }
        return totalWorkHours;
    }

    public Long calculateWorkingDays(LocalDate start, LocalDate end){
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }

        long workingDays = 0L;
        LocalDate current = start;
        while (!current.isAfter(end)) {
            DayOfWeek day = current.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            current = current.plusDays(1);
        }
        return workingDays;
    }

    public BigDecimal getWorkingDays(BigDecimal totalWorkHours) {
        BigDecimal workingDay = totalWorkHours.divide(BigDecimal.valueOf(8), 2, RoundingMode.HALF_UP);
        return workingDay;
    }

    public List<TimeSheetDTO> doSearch(TimeSheetDTO dto) {
        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        params.put(startRow,(dto.getPage()-1) * dto.getSize());
        params.put(endRow,dto.getSize());
        if (Objects.nonNull(dto.getCreatedBy())) {
            sqlWhere += " AND t.createdBy = :createdBy";
            params.put("createdBy", dto.getCreatedBy());
        }
        if (Objects.nonNull(dto.getUpdatedBy())) {
            sqlWhere += " AND t.updatedBy = :updatedBy";
            params.put("updatedBy", dto.getUpdatedBy());
        }
        if (Objects.nonNull(dto.getUserId())) {
            sqlWhere += " AND t.user_id = :userId";
            params.put("userId", dto.getUserId() );
        }
        if (Objects.nonNull(dto.getShiftId())) {
            sqlWhere += " AND t.shift_id = :shiftId";
            params.put("shiftId", dto.getShiftId() );
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" t.TimeSheet_ID as id, ");
        sb.append(" t.CHECKINTIME as checkInTime, ");
        sb.append(" t.CHECKOUTTIME as checkoutTime, ");
        sb.append(" t.DAY_OF_WEEK as dayOfWeek, ");
        sb.append(" t.TOTAL_HOURS as totalHours, ");
        sb.append(" t.WORKDATE as workDate, ");
        sb.append(" t.SHIFT_ID as shiftId, ");
        sb.append(" t.USER_ID as userId, ");
        sb.append(" u.fullName as fullName, ");
        sb.append(" d.NAME as departmentName, ");
        sb.append(" t.CREATED as created, ");
        sb.append(" t.CREATEDBY as createdBY, ");
        sb.append(" t.UPDATED as updated, ");
        sb.append(" t.UPDATEDBY as updatedBy ");
        sb.append(" from TimeSheet t ");
        sb.append(" left join users u on t.user_id = u.user_id ");
        sb.append(" left join department d on u.department_id = d.department_id ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by timesheet_id " );
        sb.append( " OFFSET :startRow ROWS FETCH NEXT :endRow ROWS ONLY" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("shiftId", StandardBasicTypes.LONG)
                .addScalar("userId", StandardBasicTypes.LONG)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("departmentName", StandardBasicTypes.STRING)
                .addScalar("checkInTime", StandardBasicTypes.DATE)
                .addScalar("checkoutTime", StandardBasicTypes.DATE)
                .addScalar("workDate", StandardBasicTypes.DATE)
                .addScalar("totalHours", StandardBasicTypes.LONG)
                .addScalar("dayOfWeek", StandardBasicTypes.STRING)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(TimeSheetDTO.class));
        List<TimeSheetDTO> timeSheetDTOS = querySelect.getResultList();
        return timeSheetDTOS;
    }

    public List<TimeSheetDTO> doSearchByTimeCard(TimeCardDTO dto) {
        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(dto.getStartDate()) && Objects.nonNull(dto.getStartDate())){
            sqlWhere += " and t.workdate >= :startDate and t.workdate <= :endDate ";
            params.put("startDate", dto.getStartDate());
            params.put("endDate", dto.getEndDate());
        }
        if(Objects.nonNull(dto.getUserId())){
            sqlWhere += " and t.user_id = :id ";
            params.put("id", dto.getUserId());
        }
        if(Objects.nonNull(dto.getDepartmentId())){
            sqlWhere += " and d.department_id = :id ";
            params.put("id", dto.getDepartmentId());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" t.TimeSheet_ID as id, ");
        sb.append(" t.CHECKINTIME as checkInTime, ");
        sb.append(" t.CHECKOUTTIME as checkoutTime, ");
        sb.append(" t.DAY_OF_WEEK as dayOfWeek, ");
        sb.append(" t.TOTAL_HOURS as totalHours, ");
        sb.append(" t.WORKDATE as workDate, ");
        sb.append(" t.SHIFT_ID as shiftId, ");
        sb.append(" t.USER_ID as userId, ");
        sb.append(" u.fullName as fullName, ");
        sb.append(" d.NAME as departmentName, ");
        sb.append(" t.CREATED as created, ");
        sb.append(" t.CREATEDBY as createdBY, ");
        sb.append(" t.UPDATED as updated, ");
        sb.append(" t.UPDATEDBY as updatedBy ");
        sb.append(" from TimeSheet t ");
        sb.append(" left join users u on t.user_id = u.user_id ");
        sb.append(" left join department d on u.department_id = d.department_id ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by t.updated desc " );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("shiftId", StandardBasicTypes.LONG)
                .addScalar("userId", StandardBasicTypes.LONG)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("departmentName", StandardBasicTypes.STRING)
                .addScalar("checkInTime", StandardBasicTypes.DATE)
                .addScalar("checkoutTime", StandardBasicTypes.DATE)
                .addScalar("workDate", StandardBasicTypes.DATE)
                .addScalar("totalHours", StandardBasicTypes.LONG)
                .addScalar("dayOfWeek", StandardBasicTypes.STRING)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(TimeSheetDTO.class));
        List<TimeSheetDTO> timeSheetDTOS = querySelect.getResultList();
        return timeSheetDTOS;
    }
}

