package com.doan.nhansu.users.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.users.dto.ContractRenewDTO;
import com.doan.nhansu.users.dto.ContractRenewDetailDTO;
import com.doan.nhansu.users.service.ContractRenewService;
import com.doan.nhansu.users.service.ContractService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/contract-renew")
public class ContractRenewController {
    ContractRenewService contractRenewService;
    @PostMapping("/do-search")
    public ApiResponse<List<ContractRenewDTO>> doSearchContractRenew(@RequestBody ContractRenewDTO dto) {
        ApiResponse<List<ContractRenewDTO>> response  = contractRenewService.doSearch(dto);
        return response;
    }

    @PostMapping
    public ApiResponse<ContractRenewDTO> create(@RequestBody ContractRenewDTO dto){
        return contractRenewService.create(dto);
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = contractRenewService.delete(id);
        return res;
    }
    @PostMapping("/get-details")
    public ApiResponse<List<ContractRenewDetailDTO>> getDetails(@RequestBody ContractRenewDetailDTO dto) {
        ApiResponse<List<ContractRenewDetailDTO>> result = contractRenewService.getDetails(dto.getId());
        return result;
    }
    @PostMapping("/create-line")
    public ApiResponse<ContractRenewDTO> createLine(@RequestBody ContractRenewDTO dto){
        return contractRenewService.createLine(dto);
    }
    @PostMapping("/delete-line")
    public ApiResponse<ContractRenewDTO> deleteLine(@RequestBody ContractRenewDTO dto) {
        ApiResponse<ContractRenewDTO> res = contractRenewService.deleteLine(dto);
        return res;
    }


}
