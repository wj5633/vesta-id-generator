package com.wj5633.vesta.converter;

import com.wj5633.vesta.bean.Id;

/**
 * Created at 2019/7/16 14:12.
 *
 * @author wangjie
 * @version 1.0.0
 */

public interface IdConverter {
    long convert(Id id);

    Id convert(long id);
}
