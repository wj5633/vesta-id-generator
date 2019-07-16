package com.wj5633.vesta.provider;

import com.wj5633.vesta.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created at 2019/7/16 15:46.
 *
 * @author wangjie
 * @version 1.0.0
 */


@Slf4j
public class IpConfigurableMachineIdProvider implements MachineIdProvider {

    private long machineId;

    private Map<String, Long> ipsMap = new HashMap<String, Long>();

    public IpConfigurableMachineIdProvider() {
        log.debug("IpConfigurableMachineIdProvider constructed.");
    }

    public IpConfigurableMachineIdProvider(String ips) {
        setIps(ips);
        init();
    }


    public void init() {
        String ip = IpUtils.getHostIp();

        if (StringUtils.isEmpty(ip)) {
            String msg = "Fail to get host IP address. Stop to initialize the IpConfigurableMachineIdProvider.";

            log.error(msg);
            throw new IllegalStateException(msg);
        }

        if (!ipsMap.containsKey(ip)) {
            String msg = String
                    .format("Fail to configure ID for host IP address %s. " +
                            "Stop to initialize the IpConfigurableMachineIdProvider.", ip);

            log.error(msg);
            throw new IllegalStateException(msg);
        }

        machineId = ipsMap.get(ip);

        log.info("IpConfigurableMachineIdProvider.init ip {} id {}", ip, machineId);
    }

    public void setIps(String ips) {
        log.debug("IpConfigurableMachineIdProvider ips {}", ips);
        if (!StringUtils.isEmpty(ips)) {
            String[] ipArray = ips.split(",");

            for (int i = 0; i < ipArray.length; i++) {
                ipsMap.put(ipArray[i], (long) i);
            }
        }
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

}
