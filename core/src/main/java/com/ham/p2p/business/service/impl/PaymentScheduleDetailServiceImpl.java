package com.ham.p2p.business.service.impl;

import com.ham.p2p.business.domain.PaymentScheduleDetail;
import com.ham.p2p.business.mapper.PaymentScheduleDetailMapper;
import com.ham.p2p.business.service.IPaymentScheduleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class PaymentScheduleDetailServiceImpl implements IPaymentScheduleDetailService {
   @Autowired
    private PaymentScheduleDetailMapper paymentScheduleDetailMapper;

    @Override
    public int save(PaymentScheduleDetail record) {
        return paymentScheduleDetailMapper.insert(record);
    }

    @Override
    public PaymentScheduleDetail get(Long id) {
        return paymentScheduleDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(PaymentScheduleDetail record) {
        return paymentScheduleDetailMapper.updateByPrimaryKey(record);
    }

    @Override
    public void updatePayDate(Long psId, Date payDate) {
     paymentScheduleDetailMapper.updatePayDate(psId,payDate);
    }
}
