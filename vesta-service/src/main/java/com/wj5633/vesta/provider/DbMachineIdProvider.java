package com.wj5633.vesta.provider;

import com.wj5633.vesta.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;


/**
 * Created at 2019/7/16 15:43.
 *
 * @author wangjie
 * @version 1.0.0
 */

@Slf4j
public class DbMachineIdProvider implements MachineIdProvider {

    private long machineId;

    private JdbcTemplate jdbcTemplate;

    public DbMachineIdProvider() {
        log.debug("DbMachineIdProvider constructed.");
    }

    public void init() {
        String ip = IpUtils.getHostIp();

        if (StringUtils.isEmpty(ip)) {
            String msg = "Fail to get host IP address. Stop to initialize the DbMachineIdProvider provider.";
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        Long id = null;
        try {
            id = jdbcTemplate.queryForObject("select ID from DB_MACHINE_ID_PROVIDER where IP = ?",
                    new Object[]{ip}, Long.class);

        } catch (EmptyResultDataAccessException e) {
            log.error("No allocation before for ip {}.", ip);
        }

        if (id != null) {
            machineId = id;
            return;
        }

        log.info(
                "Fail to get ID from DB for host IP address {}. Next step try to allocate one.",
                ip);

        int count = jdbcTemplate.update("update DB_MACHINE_ID_PROVIDER set IP = ? where IP is null limit 1", ip);

        if (count != 1) {
            String msg = String.format("Fail to allocte ID for host IP address %s. The %s records are updated. " +
                    "Stop to initialize the DbMachineIdProvider provider.", ip, count);

            log.error(msg);
            throw new IllegalStateException(msg);
        }

        try {
            id = jdbcTemplate.queryForObject("select ID from DB_MACHINE_ID_PROVIDER where IP = ?",
                    new Object[]{ip}, Long.class);

        } catch (EmptyResultDataAccessException e) {
            // Ignore the exception
            log.error("Fail to do allocation for ip {}.", ip);
        }

        if (id == null) {
            String msg = String.format("Fail to get ID from DB for host IP address %s after allocation.", ip);
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        machineId = id;
    }


    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
