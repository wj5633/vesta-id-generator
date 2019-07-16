package com.wj5633.vesta.factory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wj5633.vesta.provider.DbMachineIdProvider;
import com.wj5633.vesta.provider.IpConfigurableMachineIdProvider;
import com.wj5633.vesta.provider.MachineIdProvider;
import com.wj5633.vesta.provider.PropertyMachineIdProvider;
import com.wj5633.vesta.service.IdService;
import com.wj5633.vesta.service.impl.IdServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import java.beans.PropertyVetoException;

/**
 * Created at 2019/7/16 15:53.
 *
 * @author wangjie
 * @version 1.0.0
 */

@Slf4j
@Data
public class IdServiceFactoryBean implements FactoryBean<IdService> {

    public enum Type {
        PROPERTY, IP_CONFIGURABLE, DB
    }

    private Type providerType;

    private long machineId;

    private String ips;

    private String dbUrl;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    private long genMethod = -1;
    private long type = -1;
    private long version = -1;

    private IdService idService;

    public void init() {
        Assert.notNull(providerType, "The type of Id service is mandatory.");
        MachineIdProvider machineIdProvider = null;
        switch (providerType) {
            case PROPERTY:
                machineIdProvider = constructPropertyMachineIdProvider(machineId);
                break;
            case IP_CONFIGURABLE:
                machineIdProvider = constructIpConfigurableMachineIdProvider(ips);
                break;
            case DB:
                machineIdProvider = constructDbMachineIdProvider(dbUrl, dbName, dbUser, dbPassword);
                break;
        }
        Assert.notNull(machineIdProvider, "Construct machineIdProvider failed.");
        IdServiceImpl idServiceImpl = new IdServiceImpl();
        idServiceImpl.setMachineIdProvider(machineIdProvider);
        if (genMethod != -1)
            idServiceImpl.setGenMethod(genMethod);
        if (type != -1)
            idServiceImpl.setType(type);
        if (version != -1)
            idServiceImpl.setVersion(version);
        idServiceImpl.init();

        this.idService = idServiceImpl;
    }

    public IdService getObject() {
        return idService;
    }

    private MachineIdProvider constructPropertyMachineIdProvider(long machineId) {
        log.info("Construct Property IdService machineId {}", machineId);
        PropertyMachineIdProvider propertyMachineIdProvider = new PropertyMachineIdProvider();
        propertyMachineIdProvider.setMachineId(machineId);
        return propertyMachineIdProvider;
    }

    private MachineIdProvider constructIpConfigurableMachineIdProvider(String ips) {
        log.info("Construct Ip Configurable IdService ips {}", ips);
        return new IpConfigurableMachineIdProvider(ips);
    }

    private MachineIdProvider constructDbMachineIdProvider(String dbUrl, String dbName,
                                                           String dbUser, String dbPassword) {
        log.info("Construct Db IdService dbUrl {} dbName {} dbUser {} dbPassword {}",
                dbUrl, dbName, dbUser, dbPassword);

        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();

        String jdbcDriver = "com.mysql.jdbc.Driver";
        try {
            comboPooledDataSource.setDriverClass(jdbcDriver);
        } catch (PropertyVetoException e) {
            log.error("Wrong JDBC driver {}, error: {}", jdbcDriver, e);
            throw new IllegalStateException("Wrong JDBC driver ", e);
        }

        comboPooledDataSource.setMinPoolSize(5);
        comboPooledDataSource.setMaxPoolSize(30);
        comboPooledDataSource.setIdleConnectionTestPeriod(20);
        comboPooledDataSource.setMaxIdleTime(25);
        comboPooledDataSource.setBreakAfterAcquireFailure(false);
        comboPooledDataSource.setCheckoutTimeout(3000);
        comboPooledDataSource.setAcquireRetryAttempts(50);
        comboPooledDataSource.setAcquireRetryDelay(1000);

        String url = String.format(
                "jdbc:mysql://%s/%s?useUnicode=true&amp;characterEncoding=UTF-8&amp;autoReconnect=true", dbUrl, dbName);

        comboPooledDataSource.setJdbcUrl(url);
        comboPooledDataSource.setUser(dbUser);
        comboPooledDataSource.setPassword(dbPassword);

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setLazyInit(false);
        jdbcTemplate.setDataSource(comboPooledDataSource);

        DbMachineIdProvider dbMachineIdProvider = new DbMachineIdProvider();
        dbMachineIdProvider.setJdbcTemplate(jdbcTemplate);
        dbMachineIdProvider.init();

        return dbMachineIdProvider;
    }

    public Class<?> getObjectType() {
        return IdService.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
