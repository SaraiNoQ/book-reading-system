package com.dms.guyiyao.pojo.page;

import com.dms.guyiyao.pojo.book.TsBook_0;
import lombok.Data;

import java.util.List;

@Data
public class PageV1 {
    private List<TsBook_0> data;
    private Long total;
}
