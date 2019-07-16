package com.wj5633.vesta.converter;

import com.wj5633.vesta.bean.Id;
import com.wj5633.vesta.bean.IdMeta;
import com.wj5633.vesta.bean.IdMetaFactory;
import com.wj5633.vesta.bean.IdType;

/**
 * Created at 2019/7/16 14:43.
 *
 * @author wangjie
 * @version 1.0.0
 */

public class IdConverterImpl implements IdConverter {

    private IdMeta idMeta;

    public IdConverterImpl() {
    }

    public IdConverterImpl(IdMeta idMeta) {
        this.idMeta = idMeta;
    }

    public IdConverterImpl(IdType idType) {
        this(IdMetaFactory.getIdMeta(idType));
    }

    public long convert(Id id) {
        return doConvert(id, idMeta);
    }

    protected long doConvert(Id id, IdMeta idMeta) {
        long ret = 0;
        ret |= id.getMachine();
        ret |= id.getSeq() << idMeta.getSeqBitsStartPos();
        ret |= id.getTime() << idMeta.getTimeBitsStartPos();
        ret |= id.getGenMethod() << idMeta.getGenMethodBitsStartPos();
        ret |= id.getType() << idMeta.getTypeBitsStartPos();
        ret |= id.getVersion() << idMeta.getVersionBitsStartPos();
        return ret;
    }

    public Id convert(long id) {
        return doConvert(id, idMeta);
    }

    protected Id doConvert(long id, IdMeta idMeta) {
        Id.IdBuilder builder = Id.builder();
        builder.machine(id & idMeta.getMachineBitsMask());
        builder.seq(id >>> idMeta.getSeqBitsStartPos() & idMeta.getSeqBitsMask());
        builder.time(id >>> idMeta.getTimeBitsStartPos() & idMeta.getTimeBitsMask());
        builder.genMethod(id >>> idMeta.getGenMethodBitsStartPos() & idMeta.getGenMethodBitsMask());
        builder.type(id >>> idMeta.getTypeBitsStartPos() & idMeta.getTypeBitsMask());
        builder.version(id >>> idMeta.getVersionBitsStartPos() & idMeta.getVersionBitsMask());
        return builder.build();
    }

    public IdMeta getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(IdMeta idMeta) {
        this.idMeta = idMeta;
    }
}
