package com.doan.nhansu.users.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.users.dto.ContractDTO;
import com.doan.nhansu.users.dto.ContractTypeDTO;
import com.doan.nhansu.users.service.ContractService;
import com.doan.nhansu.users.service.ContractTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/contract-type")
public class ContractTypeController {
    ContractTypeService contractTypeService;
    @PostMapping("/do-search")
    public ApiResponse<List<ContractTypeDTO>> doSearchContractType(@RequestBody ContractTypeDTO dto) {
        ApiResponse<List<ContractTypeDTO>> response  = contractTypeService.doSearch(dto);
        return response;
    }

    @PostMapping
    public ApiResponse<ContractTypeDTO> create(@RequestBody ContractTypeDTO dto){
        return contractTypeService.create(dto);
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = contractTypeService.delete(id);
        return res;
    }
}
