package com.ham.p2p.business.query;

import com.ham.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidRequestQueryObject extends QueryObject {
    private int bidRequestState = -1;
    private int[] states;
    private String orderByCondition;
}
