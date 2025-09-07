package com.doan.nhansu.report.dto;

import com.doan.nhansu.users.dto.FileDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportDTO {
    @JsonProperty("file") private FileDTO file;
    @JsonProperty("adTableId")private Long adTableId;
    @JsonProperty("templateKey") private String templateKey;
    @JsonProperty("cDocTypeId") private Long cDocTypeId;
    @JsonProperty("adOrgId") private Long adOrgId;
    @JsonProperty("adTableName") private String adTableName;
    @JsonProperty("ids") private List<Long> ids;
}