package com.doan.nhansu.users.repository.custom;

import com.doan.nhansu.users.dto.ContractRenewDTO;
import com.doan.nhansu.users.dto.ContractRenewDetailDTO;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
public class ContractRenewDetailRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    public List<ContractRenewDetailDTO> doSearchByContractRenew(Long id) {
        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(id)){
            sqlWhere += " and e.contract_renew_id = :id ";
            params.put("id",id);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" select ");
        sb.append(" Count(1) over() as totalCount, ");
        sb.append(" e.CONTRACT_RENEW_DETAIL_ID as id, ");
        sb.append(" e.USER_ID as userId, ");
        sb.append(" e.CONTRACT_RENEW_ID as contractRenewId, ");
        sb.append(" (Select fullname from users u where u.user_id = e.user_id) as fullName, ");
        sb.append(" (Select name from contract_renew r where r.contract_renew_id = e.contract_renew_id) as contractRenewName, ");
        sb.append(" e.CREATED as created, ");
        sb.append(" e.CREATEDBY as createdBY, ");
        sb.append(" e.UPDATED as updated, ");
        sb.append(" e.UPDATEDBY as updatedBy ");
        sb.append(" from Contract_Renew_Detail e ");
        sb.append(" WHERE 1=1 ");
        sb.append(sqlWhere);
        sb.append( " order by e.updated desc" );
// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.query.Query querySelect = session.createNativeQuery(sb.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("userId", StandardBasicTypes.LONG)
                .addScalar("contractRenewId", StandardBasicTypes.LONG)
                .addScalar("fullName", StandardBasicTypes.STRING)
                .addScalar("contractRenewName", StandardBasicTypes.STRING)
                .addScalar("totalCount", StandardBasicTypes.LONG)
                .addScalar("created", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.LONG)
                .addScalar("updated", StandardBasicTypes.DATE)
                .addScalar("updatedBy", StandardBasicTypes.LONG)
                ;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            querySelect.setParameter(entry.getKey(), entry.getValue());
        }
        querySelect.setResultTransformer(Transformers.aliasToBean(ContractRenewDetailDTO.class));
        List<ContractRenewDetailDTO> ContractRenewDetailDTOS = querySelect.getResultList();

        return ContractRenewDetailDTOS;

    }

    public void delete(ContractRenewDTO dto){
        String sql = "delete from contract_renew_detail where contract_renew_id = :id";
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createNativeQuery(sql);
        query.setParameter("id", dto.getId());
        query.executeUpdate();
    }
}
