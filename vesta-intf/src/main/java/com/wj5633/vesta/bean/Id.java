package com.wj5633.vesta.bean;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Created at 2019/7/16 13:47.
 *
 * @author wangjie
 * @version 1.0.0
 */

@Data
@Builder
public class Id implements Serializable {

    private long machine;
    private long seq;
    private long time;
    private long genMethod;
    private long type;
    private long version;

}
