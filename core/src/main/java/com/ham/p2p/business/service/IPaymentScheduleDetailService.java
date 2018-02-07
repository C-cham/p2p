package com.ham.p2p.business.service;

import com.ham.p2p.business.domain.PaymentScheduleDetail;

import java.util.Date;

public interface IPaymentScheduleDetailService {
    int save(PaymentScheduleDetail record);

    PaymentScheduleDetail get(Long id);



    int update(PaymentScheduleDetail record);

    void updatePayDate(Long psId, Date payDate);
}
