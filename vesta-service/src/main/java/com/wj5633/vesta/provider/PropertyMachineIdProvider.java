package com.wj5633.vesta.provider;

/**
 * Created at 2019/7/16 15:49.
 *
 * @author wangjie
 * @version 1.0.0
 */

public class PropertyMachineIdProvider implements MachineIdProvider {
    private long machineId;

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }
}