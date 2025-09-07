package com.doan.nhansu.admin.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    public int status;
    public String message;
    public List<String> errors;
    public T result;
    public Integer page;
    public Integer size;
    public Long totalCount;

    public ApiResponse<T>  createResponse(T list){
        ModelMapper  modelMapper = new ModelMapper();
        ApiResponse<T> apiResponse = new ApiResponse();
        if(list instanceof List || list instanceof ArrayList){
            BaseDTO baseDTO = modelMapper.map(((List<?>) list).get(0), BaseDTO.class);
            apiResponse.totalCount = baseDTO.getTotalCount();
            apiResponse.result =list;
        }
        return apiResponse;
    }
    public static class ResponseBuilder<E> {
        ApiResponse<E> response;

        public ResponseBuilder() {
            this.response = new ApiResponse<>();
        }


        public ResponseBuilder<E> page(Integer page) {
            this.response.page = page;
            return this;
        }

        public ResponseBuilder<E> size(Integer size) {
            this.response.size = size;
            return this;
        }

        public ResponseBuilder<E> total(Long totalCount) {
            this.response.totalCount = totalCount;
            return this;
        }
        public ApiResponse<E> success(E result) {
            return success(result, "Thành công");
        }
        public ApiResponse<E> success(E result, String message) {
            if (result instanceof List || result instanceof ArrayList) {
                this.response.size = ((List) result).size();
                if (((List) result).size() > 0) {
                    ModelMapper mapper = new ModelMapper();
                    BaseDTO baseDTO = new BaseDTO();
                    mapper.map(((List<?>) result).get(0),baseDTO);
                    this.response.totalCount = baseDTO.getTotalCount();
                } else {
                    response.totalCount = 0L;
                }
            }
            this.response.result = result;
            this.response.status = StatusResponse.SUCCESS;
            this.response.message = message;
            this.response.errors = new ArrayList<>();
            return this.response;
        }
        public ApiResponse<E> failed(E result, String message, int status) {
            this.response.result = result;
            this.response.status = status;
            this.response.message = message;
            this.response.errors = new ArrayList<>();
            return this.response;
        }
        public ApiResponse<E> failed(List<String> errors, String message) {
            this.response.status = StatusResponse.FAILED;
            this.response.message = message;
            this.response.errors = errors;
            return this.response;
        }

    }

}
