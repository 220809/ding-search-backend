package com.dingzk.dingsearch.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingzk.dingsearch.common.ResponseEntity;
import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.facade.SearchResourceFacade;
import com.dingzk.dingsearch.model.request.SearchResourceRequest;
import com.dingzk.dingsearch.model.vo.SearchResourceVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchResourceController {

    @Resource
    private SearchResourceFacade searchResourceFacade;

    @PostMapping
    public ResponseEntity<SearchResourceVo<?>> searchResource(@RequestBody SearchResourceRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.BAD_PARAMS, "请求参数为空");
        }
        Page<?> page = searchResourceFacade.searchResource(request);

        return ResponseEntity.success(SearchResourceVo.fromPage(request.getType(), page));
    }
}
