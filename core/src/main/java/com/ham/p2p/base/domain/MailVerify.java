package com.ham.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter@Getter
public class MailVerify extends BaseDomain {
    private String email;
    private String uuid;
    private Date sendTime;
    private Long userId;
}
