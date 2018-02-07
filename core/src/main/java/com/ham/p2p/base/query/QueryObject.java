package com.ham.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueryObject {
    private Integer currentPage = 1;
    private Integer pageSize = 10;
}
