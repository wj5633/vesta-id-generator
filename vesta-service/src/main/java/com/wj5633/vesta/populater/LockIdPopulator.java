package com.wj5633.vesta.populater;

import com.wj5633.vesta.bean.Id;
import com.wj5633.vesta.bean.IdMeta;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created at 2019/7/16 15:23.
 *
 * @author wangjie
 * @version 1.0.0
 */

public class LockIdPopulator extends BasePopulator {

    private Lock lock = new ReentrantLock();

    public LockIdPopulator() {
        super();
    }

    public void populateId(Id id, IdMeta idMeta) {
        lock.lock();
        try {
            super.populateId(id, idMeta);
        } finally {
            lock.unlock();
        }
    }

}
