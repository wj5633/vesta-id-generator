package com.wj5633.vesta.populater;

import com.wj5633.vesta.bean.Id;
import com.wj5633.vesta.bean.IdMeta;
import com.wj5633.vesta.bean.IdType;
import com.wj5633.vesta.util.TimeUtils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created at 2019/7/16 15:22.
 *
 * @author wangjie
 * @version 1.0.0
 */

public class AtomicIdPopulator implements IdPopulator, ResetPopulator {

    private class Variant {
        private long sequence = 0;
        private long lastTimestamp = -1;
    }

    private AtomicReference<Variant> variant = new AtomicReference<>(new Variant());

    public AtomicIdPopulator() {
        super();
    }

    @Override
    public void populateId(Id id, IdMeta idMeta) {
        Variant varOld, varNew;
        long timestamp, sequence;

        while (true) {
            varOld = variant.get();

            timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
            TimeUtils.validateTimestamp(varOld.lastTimestamp, timestamp);

            sequence = varOld.sequence;

            if (timestamp == varOld.lastTimestamp) {
                sequence++;
                sequence &= idMeta.getSeqBitsMask();
                if (sequence == 0) {
                    timestamp = TimeUtils.tillNextTimeUnit(varOld.lastTimestamp, IdType.parse(id.getType()));
                }
            } else {
                sequence = 0;
            }

            varNew = new Variant();
            varNew.sequence = sequence;
            varNew.lastTimestamp = timestamp;

            if (variant.compareAndSet(varOld, varNew)) {
                id.setSeq(sequence);
                id.setTime(timestamp);
                break;
            }
        }
    }

    @Override
    public void reset() {
        variant = new AtomicReference<Variant>(new Variant());
    }
}
