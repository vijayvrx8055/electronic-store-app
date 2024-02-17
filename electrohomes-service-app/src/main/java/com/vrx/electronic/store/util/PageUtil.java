package com.vrx.electronic.store.util;

import com.vrx.electronic.store.dto.response.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.stream.Collectors;

public class PageUtil {

    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> classType) {
        List<U> entities = page.getContent(); // U: Entity type data
        PageableResponse<V> response = new PageableResponse<>(); // V: DTO type data
        response.setContent(entities.stream().map(ob -> new ModelMapper().map(ob, classType)).collect(Collectors.toList()));
        response.setPageSize(page.getSize());
        response.setPageNumber(page.getNumber());
        response.setLastPage(page.isLast());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        return response;
    }
}
