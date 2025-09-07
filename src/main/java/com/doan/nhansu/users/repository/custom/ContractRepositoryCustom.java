package com.doan.nhansu.users.repository.custom;

import com.doan.nhansu.users.dto.ContractDTO;
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

import java.math.BigDecimal;
import java.util.*;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class ContractRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;
    UserRepository userRepository;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";


    public String getMaxNumberSequence(String value) {
        String sql = "SELECT MAX(TO_NUMBER(SUBSTR(contract_number, 1, 4))) as maxNumberSequence " +
                "FROM contract " +
                " WHERE contract_number LIKE :contractnumber";

        Session session = entityManager.unwrap(Session.class);
        Query query = session.createNativeQuery(sql);

        query.setParameter("contractnumber", "%" + value +"%");

        List<Object> list = query.getResultList();
        BigDecimal a = (BigDecimal) list.get(0);
        if(a == null){
            a = BigDecimal.ZERO;
        }
        a = a.add(new BigDecimal(1));
        String xx = a.toString();
        while (xx.length() < 4) {
            xx = "0" + xx;
        }
        return xx;
    }
    public String doSearchValue(Long id){
        String sql = "Select value from contract_type where contract_type_id = :id";
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createNativeQuery(sql);
        query.setParameter("id", id);
        String value = (String) query.getSingleResult();
        return value;
    }

    public List<ContractDTO> doSearch(ContractDTO dto) {

        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        params.put(startRow,(dto.getPage()-1) * dto.getSize());
        params.put(endRow,dto.getSize());
        if (Objects.nonNull(dto.getValue()) && !dto.getValue().trim().isEmpty()) {
            sqlWhere += " AND lower(c.value) LIKE :value";
            params.put("value", "%" + dto.getValue().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getContractNumber())) {
        sqlWhere += " AND lower(c.contract_number) LIKE :number";
            params.put("number", "%" + dto.getContractNumber().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getCreatedBy())) {
            sqlWhere += " AND c.createdBy = :createdBy";
            params.put("createdBy", dto.getCreatedBy());
        }
        if (Objects.nonNull(dto.getUpdatedBy())) {
            sqlWhere += " AND c.updatedBy = :updatedBy";
            params.put("updatedBy", dto.getUpdatedBy());
        }
        if (Objects.nonNull(dto.getUserId())) {
            sqlWhere += " AND c.user_id = :id";
            params.put("id", dto.getUserId());
        }
        if (Objects.nonNull(dto.getEffectiveDate()) && Objects.nonNull(dto.getExpirationDate())) {
            sqlWhere += " AND c.expirationdate >= :effectivedate and c.expirationdate <= :expirationdate ";
            params.put("effectivedate", dto.getEffectiveDate());
            params.put("expirationdate", dto.getExpirationDate());
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" c.CONTRACT_ID as id, ");
        sb.append(" c.CONTRACT_NUMBER as contractNumber, ");
        sb.append(" c.COEFFICIENT as coefficient, ");
        sb.append(" c.SALARY as salary, ");
        sb.append(" c.ALLOWANCE as allowance, ");
        sb.append(" c.SIGNDATE as signDate, ");
        sb.append(" c.EFFECTIVEDATE as effectiveDate, ");
        sb.append(" c.EXPIRATIONDATE as expirationDate, ");
        sb.append(" c.CONTRACT_TYPE_ID as contractTypeId, ");
        sb.append(" c.DURATION as duration, ");
        sb.append(" c.CURRENT_POSITION as currentPosition, ");
        sb.append(" c.NEW_POSITION as newPosition, ");
        sb.append(" c.CURRENT_DEPARTMENT as currentDepartment, ");
        sb.append(" c.NEW_DEPARTMENT as newDepartment, ");
        sb.append(" c.SIGNING_TIME as signingTime, ");
        sb.append(" c.WORK_PLACE as workPlace, ");
        sb.append(" c.WORKING_TIME as workingTime, ");
        sb.append(" c.WORKING_TIME_MORNING as workingTimeMorning, ");
        sb.append(" c.WORKING_TIME_AFTERNOON as workingTimeAfternoon, ");
        sb.append(" c.USER_ID as userId, ");
        sb.append(" c.USER_MANAGER_ID as userManagerId, ");
        sb.append(" c.ISSIGNED as isSigned, ");
        sb.append(" (Select name from position a where a.position_id = c.current_position) as currentPositionName, ");
        sb.append(" (Select name from position a where a.position_id = c.new_position) as newPositionName, ");
        sb.append(" (Select name from department a where a.department_id = c.current_department) as currentDepartmentName, ");
        sb.append(" (Select name from department a where a.department_id = c.new_department) as newDepartmentName, ");
        sb.append(" (Select name from contract_type ct where c.contract_type_id = ct.contract_type_id) as name, ");
        sb.append(" (Select value from contract_type ct where c.contract_type_id = ct.contract_type_id) as value, ");
        sb.append(" (Select fullname from users u where c.user_id = u.user_id) as fullName, ");
        sb.append(" u.FULLNAME as userManagerName, ");
        sb.append(" u.PHONE as userManagerPhone, ");
        sb.append(" d.NAME as userManagerDepartmentName, ");
        sb.append(" p.NAME as userManagerPositionName, ");
        sb.append(" c.CREATED as created, ");
        sb.append(" c.CREATEDBY as createdBY, ");
        sb.append(" c.UPDATED as updated, ");
        sb.append(" c.UPDATEDBY as updatedBy ");
        sb.append("  from Contract c ");
        sb.append(" left join users u on c.user_manager_id = u.user_id ");
        sb.append(" left join department d on u.department_id = d.department_id ");
        sb.append(" left join position p on u.position_id = p.position_id ");
        sb.append("  WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by c.updated desc" );
        sb.append( " OFFSET :startRow ROWS FETCH NEXT :endRow ROWS ONLY" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("contractNumber", StandardBasicTypes.STRING)
                .addScalar("coefficient", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("salary", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("allowance", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("signDate", StandardBasicTypes.DATE)
                .addScalar("effectiveDate", StandardBasicTypes.DATE)
                .addScalar("expirationDate", StandardBasicTypes.DATE)
                .addScalar("contractTypeId", StandardBasicTypes.LONG)
                .addScalar("duration", StandardBasicTypes.STRING)
                .addScalar("isSigned", StandardBasicTypes.STRING)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("currentPosition", StandardBasicTypes.LONG)
                .addScalar("currentPositionName", StandardBasicTypes.STRING)
                .addScalar("newPosition", StandardBasicTypes.LONG)
                .addScalar("newPositionName", StandardBasicTypes.STRING)
                .addScalar("currentDepartment", StandardBasicTypes.LONG)
                .addScalar("currentDepartmentName", StandardBasicTypes.STRING)
                .addScalar("newDepartment", StandardBasicTypes.LONG)
                .addScalar("newDepartmentName", StandardBasicTypes.STRING)
                .addScalar("signingTime", StandardBasicTypes.LONG)
                .addScalar("workPlace", StandardBasicTypes.STRING)
                .addScalar("workingTime", StandardBasicTypes.STRING)
                .addScalar("workingTimeMorning", StandardBasicTypes.STRING)
                .addScalar("workingTimeAfternoon", StandardBasicTypes.STRING)
                .addScalar("userId", StandardBasicTypes.LONG)
                .addScalar("userManagerId", StandardBasicTypes.LONG)
                .addScalar("userManagerName", StandardBasicTypes.STRING)
                .addScalar("userManagerPositionName", StandardBasicTypes.STRING)
                .addScalar("userManagerPhone", StandardBasicTypes.STRING)
                .addScalar("userManagerDepartmentName", StandardBasicTypes.STRING)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(ContractDTO.class));
        List<ContractDTO> ContractDTOS = querySelect.getResultList();

        return ContractDTOS;

    }

    public List<ContractDTO> doSearchByRenew(Long month) {

        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        int monthValue = month.intValue();
        // Lấy năm hiện tại
        Calendar currentCal = Calendar.getInstance();
        int currentYear = currentCal.get(Calendar.YEAR);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, currentYear);
        cal.set(Calendar.MONTH, monthValue - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfCurrentMonth = cal.getTime();


        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date endOfCurrentMonth = cal.getTime();
        sqlWhere += " AND c.expirationdate >= :effectivedate and c.expirationdate <= :expirationdate ";
        params.put("effectivedate", startOfCurrentMonth);
        params.put("expirationdate", endOfCurrentMonth);

        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" c.CONTRACT_NUMBER as contractNumber, ");
        sb.append(" c.USER_ID as userId, ");
        sb.append(" u.FULLNAME as fullName, ");
        sb.append(" u.DEPARTMENT_ID as departmentId, ");
        sb.append(" d.VALUE as departmentValue ");
        sb.append("  from contract c ");
        sb.append(" join users u on c.user_id = u.user_id ");
        sb.append(" join department d on c.current_department = d.department_id ");
        sb.append("  WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by c.updated desc" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("contractNumber", StandardBasicTypes.STRING)
                .addScalar("userId", StandardBasicTypes.LONG)
                .addScalar("departmentId", StandardBasicTypes.LONG)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("departmentValue", StandardBasicTypes.STRING)
                .addScalar("totalCount", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(ContractDTO.class));
        List<ContractDTO> ContractDTOS = querySelect.getResultList();

        return ContractDTOS;

    }

    public List<ContractDTO> doSearchByUser(Long id) {

        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(id)){
            sqlWhere += " and c.USER_ID = :id ";
            params.put("id",id);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" c.CONTRACT_ID as id, ");
        sb.append(" c.CONTRACT_NUMBER as contractNumber, ");
        sb.append(" c.COEFFICIENT as coefficient, ");
        sb.append(" c.SALARY as salary, ");
        sb.append(" c.ALLOWANCE as allowance, ");
        sb.append(" c.SIGNDATE as signDate, ");
        sb.append(" c.EFFECTIVEDATE as effectiveDate, ");
        sb.append(" c.EXPIRATIONDATE as expirationDate, ");
        sb.append(" c.CONTRACT_TYPE_ID as contractTypeId, ");
        sb.append(" c.DURATION as duration, ");
        sb.append(" c.CURRENT_POSITION as currentPosition, ");
        sb.append(" c.NEW_POSITION as newPosition, ");
        sb.append(" c.CURRENT_DEPARTMENT as currentDepartment, ");
        sb.append(" c.NEW_DEPARTMENT as newDepartment, ");
        sb.append(" c.SIGNING_TIME as signingTime, ");
        sb.append(" c.WORK_PLACE as workPlace, ");
        sb.append(" c.WORKING_TIME as workingTime, ");
        sb.append(" c.WORKING_TIME_MORNING as workingTimeMorning, ");
        sb.append(" c.WORKING_TIME_AFTERNOON as workingTimeAfternoon, ");
        sb.append(" c.USER_ID as userId, ");
        sb.append(" c.USER_MANAGER_ID as userManagerId, ");
        sb.append(" c.ISSIGNED as isSigned, ");
        sb.append(" (Select name from contract_type ct where c.contract_type_id = ct.contract_type_id) as name, ");
        sb.append(" (Select value from contract_type ct where c.contract_type_id = ct.contract_type_id) as value, ");
        sb.append(" (Select fullname from users u where c.user_id = u.user_id) as fullName, ");
        sb.append(" c.CREATED as created, ");
        sb.append(" c.CREATEDBY as createdBY, ");
        sb.append(" c.UPDATED as updated, ");
        sb.append(" c.UPDATEDBY as updatedBy ");
        sb.append("  from Contract c ");
        sb.append("  WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by c.updated desc" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("contractNumber", StandardBasicTypes.STRING)
                .addScalar("coefficient", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("salary", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("allowance", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("signDate", StandardBasicTypes.DATE)
                .addScalar("effectiveDate", StandardBasicTypes.DATE)
                .addScalar("expirationDate", StandardBasicTypes.DATE)
                .addScalar("contractTypeId", StandardBasicTypes.LONG)
                .addScalar("duration", StandardBasicTypes.STRING)
                .addScalar("isSigned", StandardBasicTypes.STRING)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("currentPosition", StandardBasicTypes.LONG)
                .addScalar("newPosition", StandardBasicTypes.LONG)
                .addScalar("currentDepartment", StandardBasicTypes.LONG)
                .addScalar("newDepartment", StandardBasicTypes.LONG)
                .addScalar("signingTime", StandardBasicTypes.LONG)
                .addScalar("workPlace", StandardBasicTypes.STRING)
                .addScalar("workingTime", StandardBasicTypes.STRING)
                .addScalar("workingTimeMorning", StandardBasicTypes.STRING)
                .addScalar("workingTimeAfternoon", StandardBasicTypes.STRING)
                .addScalar("userId", StandardBasicTypes.LONG)
                .addScalar("userManagerId", StandardBasicTypes.LONG)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(ContractDTO.class));
        List<ContractDTO> ContractDTOS = querySelect.getResultList();

        return ContractDTOS;

    }
    
}
