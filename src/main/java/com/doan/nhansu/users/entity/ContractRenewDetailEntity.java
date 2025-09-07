package com.doan.nhansu.users.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "contract_renew_detail")
public class ContractRenewDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_renew_detail_seq")
    @SequenceGenerator(name = "contract_renew_detail_seq", sequenceName = "contract_renew_detail_sequence", allocationSize = 1)
    @Column(name = "CONTRACT_RENEW_DETAIL_ID")
    Long id;
    @Column(name = "USER_ID")
    Long userId;
    @Column(name = "CONTRACT_RENEW_ID")
    Long contractRenewId;
    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;
}
