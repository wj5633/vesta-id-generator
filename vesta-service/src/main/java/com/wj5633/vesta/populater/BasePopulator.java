package com.wj5633.vesta.populater;

import com.wj5633.vesta.bean.Id;
import com.wj5633.vesta.bean.IdMeta;
import com.wj5633.vesta.bean.IdType;
import com.wj5633.vesta.util.TimeUtils;

/**
 * Created at 2019/7/16 15:23.
 *
 * @author wangjie
 * @version 1.0.0
 */

public abstract class BasePopulator implements IdPopulator, ResetPopulator {
    protected long sequence = 0;
    protected long lastTimestamp = -1;

    public BasePopulator() {
        super();
    }

    @Override
    public void populateId(Id id, IdMeta idMeta) {
        long timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
        TimeUtils.validateTimestamp(lastTimestamp, timestamp);

        if (timestamp == lastTimestamp) {
            sequence++;
            sequence &= idMeta.getSeqBitsMask();
            if (sequence == 0) {
                timestamp = TimeUtils.tillNextTimeUnit(lastTimestamp, IdType.parse(id.getType()));
            } else {
                lastTimestamp = timestamp;
                sequence = 0;
            }
        }
        id.setSeq(sequence);
        id.setTime(timestamp);
    }

    @Override
    public void reset() {
        this.sequence = 0;
        this.lastTimestamp = -1;
    }
}
