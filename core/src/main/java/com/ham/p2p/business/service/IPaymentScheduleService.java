package com.ham.p2p.business.service;

import com.github.pagehelper.PageInfo;
import com.ham.p2p.business.domain.PaymentSchedule;
import com.ham.p2p.business.query.PaymentScheduleQueryObject;

public interface IPaymentScheduleService {
    int save(PaymentSchedule record);

    PaymentSchedule get(Long id);



    int update(PaymentSchedule record);

    PageInfo queryPage(PaymentScheduleQueryObject qo);

    void returnMoney(Long id);
}
