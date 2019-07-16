package com.wj5633.vesta.populater;

import com.wj5633.vesta.bean.Id;
import com.wj5633.vesta.bean.IdMeta;

/**
 * Created at 2019/7/16 15:13.
 *
 * @author wangjie
 * @version 1.0.0
 */

public interface IdPopulator {
     void populateId(Id id, IdMeta idMeta);
}
