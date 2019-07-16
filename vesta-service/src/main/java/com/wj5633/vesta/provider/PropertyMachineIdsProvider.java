package com.wj5633.vesta.provider;

/**
 * Created at 2019/7/16 15:50.
 *
 * @author wangjie
 * @version 1.0.0
 */

public class PropertyMachineIdsProvider implements MachineIdsProvider {
    private long[] machineIds;
    private int currentIndex;

    public long getNextMachineId() {
        return getMachineId();
    }

    public long getMachineId() {
        return machineIds[currentIndex++ % machineIds.length];
    }

    public void setMachineIds(long[] machineIds) {
        this.machineIds = machineIds;
    }
}
