package com.ham.p2p.base.query;

import com.ham.p2p.base.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class SystemDictionaryQueryObject extends QueryObject {
    private String keyword;

}
