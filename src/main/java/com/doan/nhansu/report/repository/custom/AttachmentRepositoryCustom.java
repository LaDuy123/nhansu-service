package com.doan.nhansu.report.repository.custom;

import com.doan.nhansu.report.dto.AttachmentDTO;
import com.doan.nhansu.report.dto.ReportDTO;
import com.doan.nhansu.users.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class AttachmentRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    UserRepository userRepository;
    private static final String startRow = "startRow";
    private static final String endRow = "endRow";
    public List<AttachmentDTO> doSearch(AttachmentDTO dto) {

        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        params.put(startRow,(dto.getPage()-1) * dto.getSize());
        params.put(endRow,dto.getSize());
        sqlWhere += " and user_id = :userId ";
        params.put("userId",dto.getUserId());
        if (Objects.nonNull(dto.getFileName()) && !dto.getFileName().trim().isEmpty()) {
            sqlWhere += " AND lower(filename) LIKE :name";
            params.put("name", "%" + dto.getFileName().trim().toLowerCase() + "%");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" ATTACHMENT_ID as id, ");
        sb.append(" FILENAME as filename, ");
        sb.append(" PATH as path, ");
        sb.append(" TABLE_ID as tableId, ");
        sb.append(" USER_ID as userId, ");
        sb.append(" SIZE_FILE as sizeFile, ");
        sb.append(" CREATED as created, ");
        sb.append(" CREATEDBY as createdBY, ");
        sb.append(" UPDATED as updated, ");
        sb.append(" UPDATEDBY as updatedBy ");
        sb.append(" from Attachment ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by updated desc" );
        sb.append( " OFFSET :startRow ROWS FETCH NEXT :endRow ROWS ONLY" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("fileName", StandardBasicTypes.STRING)
                .addScalar("path", StandardBasicTypes.STRING)
                .addScalar("tableId", StandardBasicTypes.LONG)
                .addScalar("userId", StandardBasicTypes.LONG)
                .addScalar("sizeFile", StandardBasicTypes.LONG)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)

                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(AttachmentDTO.class));
        List<AttachmentDTO> lstData = querySelect.getResultList();

        return lstData;

    }
}
