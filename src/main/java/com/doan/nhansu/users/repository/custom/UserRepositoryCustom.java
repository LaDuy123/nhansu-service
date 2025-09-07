package com.doan.nhansu.users.repository.custom;

import com.doan.nhansu.admin.dto.UserResponse;
import com.doan.nhansu.users.dto.UserDTO;
import com.doan.nhansu.users.entity.UserEntity;
import com.doan.nhansu.admin.exception.AppException;
import com.doan.nhansu.admin.exception.MessageError;
import com.doan.nhansu.users.repository.EducationRepository;
import com.doan.nhansu.users.repository.PositionRepository;
import com.doan.nhansu.timekeep.repository.ShiftRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.query.Query;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class UserRepositoryCustom{
    @PersistenceContext
    EntityManager entityManager;
    DepartmentRepositoryCustom departmentRepository;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";

    public List<UserDTO> doSearch(UserDTO dto) {

        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        params.put(startRow,(dto.getPage()-1) * dto.getSize());
        params.put(endRow,dto.getSize());
        if (Objects.nonNull(dto.getFullName())  && !dto.getFullName().trim().isEmpty()) {
            sqlWhere += " AND lower(u.fullName) LIKE :fullName";
            params.put("fullName", "%" + dto.getFullName().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getEmail())) {
            sqlWhere += " AND lower(u.email) LIKE :email";
            params.put("email", "%" + dto.getEmail().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getPhone())) {
            sqlWhere +=" AND lower(u.phone) LIKE :phone";
            params.put("phone", "%" + dto.getPhone().trim().toLowerCase() + "%");
        }

        if ((Objects.nonNull(dto.getCurrentAddress()) && !dto.getCurrentAddress().trim().isEmpty())) {
            sqlWhere +=" AND lower(u.current_address) LIKE :address";
            params.put("address", "%" + dto.getCurrentAddress().trim().toLowerCase() + "%");
        }
        if (Objects.nonNull(dto.getValue()) && !dto.getValue().trim().isEmpty()) {
            sqlWhere += " AND lower(u.value) LIKE :value";
            params.put("value", "%" + dto.getValue().trim().toLowerCase() + "%");
        }
//        if (Objects.nonNull(dto.getDepartmentId())) {
//            String path = departmentRepository.getPath(dto.getDepartmentId()).orElseThrow(()->
//                    new AppException(MessageError.ID_EXISTED));
//            sqlWhere +=" AND lower(d.path) LIKE :path";
//            params.put("path", "%" + path.trim().toLowerCase() + "%");
//        }
        if (Objects.nonNull(dto.getDepartmentId())) {
            sqlWhere += " AND lower(u.department_id) = :department_id";
            params.put("department_id", dto.getDepartmentId() );
        }
        if (Objects.nonNull(dto.getPositionId())) {
            sqlWhere +=" AND u.position_id = :positionId";
            params.put("positionId", dto.getPositionId());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" u.USER_ID as id, ");
        sb.append(" u.FIRSTNAME as firstname, ");
        sb.append(" u.LASTNAME as lastname, ");
        sb.append(" u.USERNAME as username, ");
        sb.append(" u.PASSWORD as password, ");
        sb.append(" u.FULLNAME as fullName, ");
        sb.append(" u.PHONE as phone, ");
        sb.append(" u.EMAIL as email, ");
        sb.append(" u.CURRENT_ADDRESS as currentAddress, ");
        sb.append(" u.DOB as dob, ");
        sb.append(" u.GENDER as gender, ");
        sb.append(" u.ID_CARD_NUMBER as idCardNumber, ");
        sb.append(" u.ISSUED_PLACE as issuedPlace, ");
        sb.append(" u.BIRTH_PLACE as birthPlace, ");
        sb.append(" u.ISSUED_DATE as issuedDate, ");
        sb.append(" u.PERMANENT_ADDRESS as permanentAddress, ");
        sb.append(" u.WORKING_STATUS as workingStatus, ");
        sb.append(" u.RELIGION as religion, ");
        sb.append(" u.ETHNIC as ethnic, ");
        sb.append(" u.NATIONALITY as nationality, ");
        sb.append(" u.BANK as bank, ");
        sb.append(" u.BANK_TYPE as bankType, ");
        sb.append(" u.BANK_NUMBER as bankNumber, ");
        sb.append(" u.TAX_CODE as taxCode,");
        sb.append(" u.MARITAL_STATUS as maritalStatus, ");
        sb.append(" u.DEPARTMENT_ID as departmentId, ");
        sb.append(" u.EDUCATION_ID as educationId, ");
        sb.append(" u.POSITION_ID as positionId, ");
        sb.append(" u.ROLE_ID as roleId, ");
        sb.append(" u.ISACTIVE as isactive, ");
        sb.append(" u.VALUE as value, ");
        sb.append(" (Select p.name from position p where p.position_id = u.position_id) as positionName, ");
        sb.append(" (Select e.name from education e where e.education_id = u.education_id) as educationName, ");
        sb.append(" (Select d.name from department d where d.department_id = u.department_id) as departmentName, ");
        sb.append(" (Select r.name from role r where r.role_id = u.role_id) as roleName, ");
        sb.append(" u.CREATED as created, ");
        sb.append(" u.CREATEDBY as createdBY, ");
        sb.append(" u.UPDATED as updated, ");
        sb.append(" u.UPDATEDBY as updatedBy ");
        sb.append(" FROM users u ");
        sb.append("  WHERE u.ISACTIVE = 'Y' ");
        sb.append(sqlWhere);
        sb.append( " order by u.updated desc" );
        sb.append( " OFFSET :startRow ROWS FETCH NEXT :endRow ROWS ONLY" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("value", StandardBasicTypes.STRING)
                .addScalar("username", StandardBasicTypes.STRING)
                .addScalar("password", StandardBasicTypes.STRING)
                .addScalar("firstname", StandardBasicTypes.STRING)
                .addScalar("lastname", StandardBasicTypes.STRING)
                .addScalar("phone", StandardBasicTypes.STRING)
                .addScalar("email", StandardBasicTypes.STRING)
                .addScalar("currentAddress", StandardBasicTypes.STRING)
                .addScalar("dob", StandardBasicTypes.DATE)
                .addScalar("idCardNumber", StandardBasicTypes.STRING)
                .addScalar("issuedPlace", StandardBasicTypes.STRING)
                .addScalar("birthPlace", StandardBasicTypes.STRING)
                .addScalar("issuedDate", StandardBasicTypes.DATE)
                .addScalar("permanentAddress", StandardBasicTypes.STRING)
                .addScalar("religion", StandardBasicTypes.LONG)
                .addScalar("ethnic", StandardBasicTypes.STRING)
                .addScalar("nationality", StandardBasicTypes.STRING)
                .addScalar("bank", StandardBasicTypes.STRING)
                .addScalar("bankType", StandardBasicTypes.STRING)
                .addScalar("bankNumber", StandardBasicTypes.STRING)
                .addScalar("taxCode", StandardBasicTypes.STRING)
                .addScalar("maritalStatus", StandardBasicTypes.LONG)
                .addScalar("departmentName", StandardBasicTypes.STRING)
                .addScalar("departmentId", StandardBasicTypes.LONG)
                .addScalar("positionId", StandardBasicTypes.LONG)
                .addScalar("positionName", StandardBasicTypes.STRING)
                .addScalar("educationId", StandardBasicTypes.LONG)
                .addScalar("educationName", StandardBasicTypes.STRING)
                .addScalar("roleId", StandardBasicTypes.LONG)
                .addScalar("roleName", StandardBasicTypes.STRING)
                .addScalar("workingStatus", StandardBasicTypes.LONG)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)
                .addScalar("isActive", StandardBasicTypes.STRING)
                .addScalar("gender", StandardBasicTypes.LONG);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(UserDTO.class));
        List<UserDTO> users = querySelect.getResultList();

        return users;

    }

    public Optional<UserDTO> finByIdResponse(Long userId) {

        String selectSql = """
                SELECT u.id, u.username, u.fullName, u.firstname, u.lastname, u.phone, u.email, u.current_address as currentAddress,u.password,
                u.marital_status as maritalStatus, u.dob, u.nationality, u.religion, u.ethnic, u.id_card_number as idCardNumber,
                u.issued_date as issuedDate, u.issued_place as issuedPlace, u.birth_place as birthPlace, u.permanent_address as permanentAddress, u.tax_code as taxCode,
                 u.bank,u.department_id as departmentId, d.department_name AS departmentName, u.working_status as workingStatus, u.gender,
           u.position_id as positionId, p.position_name as positionName, u.education_id as educationId,  e.education_name as educationName,
           u.bank_type as bankType, u.bank_number as bankNumber
    FROM users u
    LEFT JOIN department d ON u.department_id = d.id
    LEFT JOIN position p ON u.position_id = p.id
    LEFT JOIN education e ON u.education_id = e.id
    where u.id = :userId""";
        Session session = entityManager.unwrap(Session.class);
        Query selectQuery = session.createNativeQuery(selectSql)
                .addScalar("id", StandardBasicTypes.STRING)
                .addScalar("username", StandardBasicTypes.STRING)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("firstname", StandardBasicTypes.STRING)
                .addScalar("lastname", StandardBasicTypes.STRING)
                .addScalar("phone", StandardBasicTypes.STRING)
                .addScalar("email", StandardBasicTypes.STRING)
                .addScalar("currentAddress", StandardBasicTypes.STRING)
                .addScalar("password", StandardBasicTypes.STRING)
                .addScalar("maritalStatus", StandardBasicTypes.LONG)
                .addScalar("dob", StandardBasicTypes.DATE)
                .addScalar("nationality", StandardBasicTypes.STRING)
                .addScalar("religion", StandardBasicTypes.LONG)
                .addScalar("ethnic", StandardBasicTypes.STRING)
                .addScalar("idCardNumber", StandardBasicTypes.STRING)
                .addScalar("issuedDate", StandardBasicTypes.DATE)
                .addScalar("issuedPlace", StandardBasicTypes.STRING)
                .addScalar("birthPlace", StandardBasicTypes.STRING)
                .addScalar("permanentAddress", StandardBasicTypes.STRING)
                .addScalar("taxCode", StandardBasicTypes.STRING)
                .addScalar("bank", StandardBasicTypes.STRING)
                .addScalar("departmentName", StandardBasicTypes.STRING)
                .addScalar("positionName", StandardBasicTypes.STRING)
                .addScalar("educationName", StandardBasicTypes.STRING)
                .addScalar("educationId", StandardBasicTypes.LONG)
                .addScalar("departmentId", StandardBasicTypes.STRING)
                .addScalar("positionId", StandardBasicTypes.LONG)
                .addScalar("workingStatus", StandardBasicTypes.LONG)
                .addScalar("gender", StandardBasicTypes.LONG)
                .addScalar("bankType", StandardBasicTypes.STRING)
                .addScalar("bankNumber", StandardBasicTypes.STRING)
                ;

        // Truyền giá trị departmentPath vào truy vấn
        selectQuery.setParameter("userId", userId);
        selectQuery.setResultTransformer(Transformers.aliasToBean(UserDTO.class));

        try{
            UserDTO userResponse = (UserDTO) selectQuery.getSingleResult();
            if (userResponse != null){
                return Optional.of(userResponse);
            } else {
                return Optional.empty();
            }
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public List<UserDTO> finByUserName(String fullName) {
        String selectSql = " SELECT id, fullName FROM (SELECT id, fullName FROM users WHERE UPPER(fullName) LIKE UPPER(:fullName)) WHERE ROWNUM <= 5";
        Session session = entityManager.unwrap(Session.class);
        Query selectQuery = session.createNativeQuery(selectSql)
                .addScalar("id", StandardBasicTypes.STRING)
                .addScalar("fullName", StandardBasicTypes.STRING);
        selectQuery.setParameter("fullName", "%" + fullName + "%");
        selectQuery.setResultTransformer(Transformers.aliasToBean(UserDTO.class));
       return selectQuery.getResultList();
    }

    public Optional<UserResponse> finByUserNameResponse(String username) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT USER_ID as id, ");
        sb.append("USERNAME as username, ");
        sb.append("PASSWORD as password, ");
        sb.append("EMAIl as email, ");
        sb.append("PHONE as phone, ");
        sb.append("CURRENT_ADDRESS as currentAddress, ");
        sb.append("FULLNAME as fullName, ");
        sb.append(" u.ROLE_ID as roleId, ");
        sb.append(" (Select r.name from role r where r.role_id = u.role_id) as roleName, ");
        sb.append(" (Select r.value from role r where r.role_id = u.role_id) as roleValue, ");
        sb.append("VALUE as value ");
        sb.append("FROM users u ");
        sb.append("where u.username = :username ");
        Session session = entityManager.unwrap(Session.class);
        Query selectQuery = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("username", StandardBasicTypes.STRING)
                .addScalar("phone", StandardBasicTypes.STRING)
                .addScalar("email", StandardBasicTypes.STRING)
                .addScalar("currentAddress", StandardBasicTypes.STRING)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("password", StandardBasicTypes.STRING)
                .addScalar("roleId", StandardBasicTypes.LONG)
                .addScalar("roleName", StandardBasicTypes.STRING)
                .addScalar("roleValue", StandardBasicTypes.STRING)
                .addScalar("value", StandardBasicTypes.STRING)
                ;

        // Truyền giá trị departmentPath vào truy vấn
        selectQuery.setParameter("username", username);
        selectQuery.setResultTransformer(Transformers.aliasToBean(UserResponse.class));

        try{
            return Optional.of((UserResponse) selectQuery.getSingleResult());

        }catch (Exception e){
            return Optional.empty();
        }
    }

    public List<UserDTO> getUserByContract(Long signtime) {
        String sql = """
           SELECT u.id, u.username, u.phone, u.email, u.current_ddress,
           d.department_name AS departmentName, u.working_status, u.gender,
           p.position_name as positionName, e.education_name as educationName,
           c.signingtime as signTime
    FROM users u
    LEFT JOIN department d ON u.department_id = d.id
    LEFT JOIN position p ON u.position_id = p.id
    LEFT JOIN education e ON u.education_id = e.id
    JOIN contract c ON u.id = c.User_id
    WHERE c.signingtime >= :signtime""";
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createNativeQuery(sql)
                .addScalar("id", StandardBasicTypes.STRING)
                .addScalar("username", StandardBasicTypes.STRING)
                .addScalar("phone", StandardBasicTypes.STRING)
                .addScalar("email", StandardBasicTypes.STRING)
                .addScalar("current_address", StandardBasicTypes.STRING)
                .addScalar("departmentName", StandardBasicTypes.STRING)
                .addScalar("positionName", StandardBasicTypes.STRING)
                .addScalar("educationName", StandardBasicTypes.STRING)
                .addScalar("working_status", StandardBasicTypes.LONG)
                .addScalar("gender", StandardBasicTypes.LONG)
                .addScalar("signTime", StandardBasicTypes.LONG);
        query.setResultTransformer(Transformers.aliasToBean(com.doan.nhansu.users.dto.UserDTO.class));
        query.setParameter("signtime", signtime);

        return query.getResultList();
    }


    public UserResponse getMyInfo(String name) {
//        UserResponse userResponse = new UserResponse();
        var context = SecurityContextHolder.getContext();
        UserResponse userResponse = finByUserNameResponse(name).orElseThrow(() -> new AppException(MessageError.USER_NOT_EXISTED));
        return userResponse;
    }
}
