package com.ham.p2p.base.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AjaxResult {
    private String msg;
    private boolean success = false;

    public AjaxResult(String msg, boolean success) {
        this.msg = msg;
        this.success = success;
    }

    public AjaxResult(String msg) {
        this.msg = msg;
    }
}
