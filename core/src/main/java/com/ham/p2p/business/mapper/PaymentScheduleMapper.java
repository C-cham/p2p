package com.ham.p2p.business.mapper;

import com.ham.p2p.business.domain.PaymentSchedule;
import com.ham.p2p.business.query.PaymentScheduleQueryObject;
import java.util.List;

public interface PaymentScheduleMapper {


    int insert(PaymentSchedule record);

    PaymentSchedule selectByPrimaryKey(Long id);



    int updateByPrimaryKey(PaymentSchedule record);

    List queryPage(PaymentScheduleQueryObject qo);

    List<PaymentSchedule> queryByBidRequestId(Long bidRequestId);
}