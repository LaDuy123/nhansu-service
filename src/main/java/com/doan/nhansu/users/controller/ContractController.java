package com.doan.nhansu.users.controller;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import com.doan.nhansu.timekeep.dto.ShiftDTO;
import com.doan.nhansu.users.dto.*;
import com.doan.nhansu.users.dto.ContractDTO;
import com.doan.nhansu.users.entity.ContractEntity;
import com.doan.nhansu.users.service.ContractService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/contract")
public class ContractController {
    ContractService contractService;
    @PostMapping("/do-search")
    public ApiResponse<List<ContractDTO>> doSearchContract(@RequestBody ContractDTO dto) {
        ApiResponse<List<ContractDTO>> response  = contractService.doSearch(dto);
        return response;
    }

    @PostMapping
    public ApiResponse<ContractDTO> create(@RequestBody ContractDTO dto){
        return contractService.create(dto);
    }

    @PostMapping("/{id}/delete")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        ApiResponse<Boolean> res = contractService.delete(id);
        return res;
    }
    @PostMapping("/delete-line")
    public ApiResponse<UserDTO> deleteLine(@RequestBody UserDTO dto) {
        ApiResponse<UserDTO> res = contractService.deleteLine(dto);
        return res;
    }

    @PostMapping("/get-details")
    public ApiResponse<List<ContractDTO>> getDetails(@RequestBody UserDTO dto) {
        ApiResponse<List<ContractDTO>> result = contractService.getDetails(dto.getId());
        return result;
    }

    @PostMapping("/generate-contract-number")
    public ApiResponse<String> generateContractNumber(@RequestBody ContractDTO dto){
        return contractService.generateContractNumber(dto);
    }

    @PostMapping("/onSigned")
    public ApiResponse<ContractDTO> onSigned(@RequestBody ContractDTO dto){
        return contractService.onSigned(dto);
    }
    @PostMapping("/offSigned")
    public ApiResponse<ContractDTO> offSigned(@RequestBody ContractDTO dto){
        return contractService.offSigned(dto);
    }
    @PostMapping("/export-word")
    public ApiResponse<FileDTO> exportWord(@RequestBody ContractDTO dto) throws IOException {
        ApiResponse<FileDTO> result = contractService.exportDataToWord(dto);
        return result;
    }
}
