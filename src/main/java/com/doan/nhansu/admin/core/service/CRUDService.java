package com.doan.nhansu.admin.core.service;

import com.doan.nhansu.admin.core.dto.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface CRUDService<REQ, RES, ID> {

    default List<RES> findAll() {
        return new ArrayList<>();
    }

    default List<RES> findAll(ID parentId) {
        return new ArrayList<>();
    }

//    default Page<RES> findPage(SearchDTO searchDTO) {return null;}
    default List<RES> findPage2(REQ req) {return null;}
    RES findOneById(ID id);

    ApiResponse<Boolean> delete(ID id);

    default Boolean deleteAll(List<ID> listID) {return null;}


    default ApiResponse<RES> create(REQ request) {
        return null;
    }

    default RES update(ID id, REQ request) {
        return null;
    }

}