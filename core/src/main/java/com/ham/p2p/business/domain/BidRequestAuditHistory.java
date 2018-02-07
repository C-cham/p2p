package com.ham.p2p.business.domain;

import com.ham.p2p.base.domain.BaseAuthDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.FileHandler;

@Setter@Getter
public class BidRequestAuditHistory extends BaseAuthDomain {
    public static final  int PUBLISH_AUDIT=0;
    public static final  int AUDIT1=1;
    public static final  int AUDIT2=2;
    private Long bidRequestId;
    private  int auditType;

    public String getAuditTypeDisplay(){
        switch (this.auditType){
            case PUBLISH_AUDIT:return "发表前审核";
            case AUDIT1:return "满标一审";
            case AUDIT2:return "满标二审";
            default:return "";
        }
    }


}
