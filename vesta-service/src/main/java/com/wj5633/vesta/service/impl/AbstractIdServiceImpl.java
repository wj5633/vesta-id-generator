package com.wj5633.vesta.service.impl;

import com.wj5633.vesta.bean.Id;
import com.wj5633.vesta.bean.IdMeta;
import com.wj5633.vesta.bean.IdMetaFactory;
import com.wj5633.vesta.bean.IdType;
import com.wj5633.vesta.converter.IdConverter;
import com.wj5633.vesta.converter.IdConverterImpl;
import com.wj5633.vesta.provider.MachineIdProvider;
import com.wj5633.vesta.service.IdService;
import com.wj5633.vesta.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created at 2019/7/16 13:57.
 *
 * @author wangjie
 * @version 1.0.0
 */

@Slf4j
public abstract class AbstractIdServiceImpl implements IdService {

    protected long machineId = -1;
    protected long genMethod = 0;
    protected long type = 0;
    protected long version = 0;

    protected IdType idType;
    protected IdMeta idMeta;

    protected IdConverter idConverter;
    protected MachineIdProvider machineIdProvider;

    public AbstractIdServiceImpl() {
        idType = IdType.MAX_PEAK;
    }

    public AbstractIdServiceImpl(String idType) {
        this.idType = IdType.parse(idType);
    }

    public AbstractIdServiceImpl(IdType idType) {
        this.idType = idType;
    }

    public void init() {
        this.machineId = machineIdProvider.getMachineId();

        if (machineId < 0) {
            log.error("The machine ID id not configured properly.");

            throw new IllegalStateException("The machine ID id not configured properly.");
        }
        if (this.idMeta == null) {
            setIdMeta(IdMetaFactory.getIdMeta(idType));
            setType(idType.value());
        } else {
            if (this.idMeta.getTimeBits() == 30) {
                setType(0);
            } else if (this.idMeta.getTimeBits() == 40) {
                setType(1);
            } else {
                throw new RuntimeException("Init Error. The time bits in IdMeta should be set to 30 or 40");
            }
        }
        setIdConverter(new IdConverterImpl(this.idMeta));
    }

    public long genId() {
        Id id = Id.builder().machine(machineId)
                .genMethod(genMethod)
                .type(type)
                .version(version).build();

        populateId(id);

        long ret = idConverter.convert(id);

        if (log.isTraceEnabled()) {
            log.trace(String.format("Id: %s => %d", id, ret));
        }
        return ret;
    }

    protected abstract void populateId(Id id);

    public Date transTime(final long time) {
        if (idType == IdType.MAX_PEAK) {
            return new Date(time * 1000 + TimeUtils.EPOCH);
        } else if (idType == IdType.MIN_GRANULARITY) {
            return new Date(time + TimeUtils.EPOCH);
        }

        return null;
    }

    @Override
    public Id expId(long id) {
        return idConverter.convert(id);
    }

    @Override
    public long makeId(long time, long seq) {
        return makeId(time, seq, machineId);
    }

    @Override
    public long makeId(long time, long seq, long machine) {
        return makeId(genMethod, time, seq, machine);
    }

    @Override
    public long makeId(long genMethod, long time, long seq, long machine) {
        return makeId(type, genMethod, time, seq, machine);
    }

    @Override
    public long makeId(long type, long genMethod, long time, long seq, long machine) {
        return makeId(version, type, genMethod, time, seq, machine);
    }

    @Override
    public long makeId(long version, long type, long genMethod, long time, long seq, long machine) {
        IdType idType = IdType.parse(type);
        Id id = Id.builder()
                .machine(machine)
                .seq(seq)
                .time(time)
                .genMethod(genMethod)
                .type(type)
                .version(version)
                .build();
        IdConverter idConverter = new IdConverterImpl(idType);
        return idConverter.convert(id);
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public long getGenMethod() {
        return genMethod;
    }

    public void setGenMethod(long genMethod) {
        this.genMethod = genMethod;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public IdMeta getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(IdMeta idMeta) {
        this.idMeta = idMeta;
    }

    public IdConverter getIdConverter() {
        return idConverter;
    }

    public void setIdConverter(IdConverter idConverter) {
        this.idConverter = idConverter;
    }

    public MachineIdProvider getMachineIdProvider() {
        return machineIdProvider;
    }

    public void setMachineIdProvider(MachineIdProvider machineIdProvider) {
        this.machineIdProvider = machineIdProvider;
    }
}
