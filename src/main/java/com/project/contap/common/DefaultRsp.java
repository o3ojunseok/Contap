package com.project.contap.common;

import com.project.contap.common.enumlist.DefaultRspEnum;
import lombok.Data;

@Data
public class DefaultRsp {
    private String msg = "";
    public DefaultRsp (DefaultRspEnum msg)
    {
        this.msg = msg.getValue();
    }
}
