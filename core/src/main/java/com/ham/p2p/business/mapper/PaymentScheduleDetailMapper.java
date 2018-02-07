package com.ham.p2p.business.mapper;

import com.ham.p2p.business.domain.PaymentScheduleDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PaymentScheduleDetailMapper {

    int insert(PaymentScheduleDetail record);

    PaymentScheduleDetail selectByPrimaryKey(Long id);


    int updateByPrimaryKey(PaymentScheduleDetail record);

    List<PaymentScheduleDetail> selectByScheduleId(Long paymentScheduleId);

    void updatePayDate(@Param("psId") Long psId, @Param("payDate") Date payDate);
}