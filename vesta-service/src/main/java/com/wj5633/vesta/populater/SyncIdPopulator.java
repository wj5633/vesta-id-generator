package com.wj5633.vesta.populater;

import com.wj5633.vesta.bean.Id;
import com.wj5633.vesta.bean.IdMeta;

/**
 * Created at 2019/7/16 15:22.
 *
 * @author wangjie
 * @version 1.0.0
 */

public class SyncIdPopulator extends BasePopulator {

    public SyncIdPopulator() {
        super();
    }

    public synchronized void populateId(Id id, IdMeta idMeta) {
        super.populateId(id, idMeta);
    }

}