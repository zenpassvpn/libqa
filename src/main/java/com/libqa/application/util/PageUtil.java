package com.libqa.application.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Created by yion on 2015. 3. 20..
 */
public class PageUtil {

    public static Sort sortId(String direction, String columnId) {
        if (direction.equals("DESC"))
            return new Sort(Sort.Direction.DESC, columnId);
        else
            return new Sort(Sort.Direction.ASC, columnId);
    }

    public static PageRequest sortPageable(int page, int size, Sort sort) {
        return new PageRequest(page, size, sort);
    }

    public static PageRequest pageable(int page, int size) {
        return new PageRequest(page, size);
    }

}