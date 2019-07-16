package com.wj5633.vesta.provider;

/**
 * Created at 2019/7/16 15:36.
 *
 * @author wangjie
 * @version 1.0.0
 */

public interface MachineIdsProvider extends MachineIdProvider {

    long getNextMachineId();
}
