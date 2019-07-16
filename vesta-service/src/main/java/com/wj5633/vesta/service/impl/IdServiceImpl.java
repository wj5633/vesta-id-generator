package com.wj5633.vesta.service.impl;

import com.wj5633.vesta.bean.Id;
import com.wj5633.vesta.bean.IdType;
import com.wj5633.vesta.populater.AtomicIdPopulator;
import com.wj5633.vesta.populater.IdPopulator;
import com.wj5633.vesta.populater.LockIdPopulator;
import com.wj5633.vesta.populater.SyncIdPopulator;
import com.wj5633.vesta.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Created at 2019/7/16 15:12.
 *
 * @author wangjie
 * @version 1.0.0
 */

@Slf4j
public class IdServiceImpl extends AbstractIdServiceImpl {
    private static final String SYNC_LOCK_IMPL_KEY = "vesta.sync.lock.impl.key";

    private static final String ATOMIC_IMPL_KEY = "vesta.atomic.impl.key";

    protected IdPopulator idPopulator;

    public IdServiceImpl() {
        super();
        initPopulator();
    }

    public IdServiceImpl(String type) {
        super(type);

        initPopulator();
    }

    public IdServiceImpl(IdType type) {
        super(type);

        initPopulator();
    }


    private void initPopulator() {
        if (idPopulator != null) {
            log.info("The " + idPopulator.getClass().getCanonicalName() + " is used.");
        } else if (CommonUtils.isPropKeyOn(SYNC_LOCK_IMPL_KEY)) {
            log.info("The SyncIdPopulator is used.");
            idPopulator = new SyncIdPopulator();
        } else if (CommonUtils.isPropKeyOn(ATOMIC_IMPL_KEY)) {
            log.info("The AtomicIdPopulator is used.");
            idPopulator = new AtomicIdPopulator();
        } else {
            log.info("The default LockIdPopulator is used.");
            idPopulator = new LockIdPopulator();
        }
    }

    @Override
    protected void populateId(Id id) {
        idPopulator.populateId(id, this.idMeta);
    }

    public void setIdPopulator(IdPopulator idPopulator) {
        this.idPopulator = idPopulator;
    }
}
