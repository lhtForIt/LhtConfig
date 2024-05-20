package com.lht.lhtconfig.server;

import com.lht.lhtconfig.server.dao.LocksMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Leo
 * @date 2024/05/19
 */
@Slf4j
@Component
public class DistributedLocks {

    @Autowired
    private LocksMapper locksMapper;

    @Autowired
    private DataSource dataSource;

    private Connection connection;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    TransactionStatus transaction = null;

    @Getter
    private AtomicBoolean locked = new AtomicBoolean(false);

    ScheduledExecutorService executor= Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("get connection failed;");
        }
        executor.scheduleWithFixedDelay(this::tryLock, 1, 5, TimeUnit.SECONDS);
    }



    @SneakyThrows
    public boolean lock() {
//        DefaultTransactionDefinition df = new DefaultTransactionDefinition();
//        df.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//        transaction = dataSourceTransactionManager.getTransaction(df);
//
//
//        locksMapper.waitTime();
//        return locksMapper.selectForUpdate().equals("app1");

        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.createStatement().execute("set innodb_lock_wait_timeout=5");
        connection.createStatement().execute("select * from locks where id=1 for update");

        if (locked.get()) {
            log.info(" ====>> reenter a dist lock.");
        } else {
            log.info(" ====>> get a dist lock.");
        }
//
        return true;
    }

    private void tryLock(){
        try {
            lock();
            locked.set(true);
        } catch (Exception e) {
            log.info(" ===>>>>  lock failed...");
            locked.set(false);
        }
//        try {
//            if (lock()) {
//                locked.set(true);
//            } else {
//                locked.set(false);
//            }
//        } catch (Exception e) {
//            log.info(" ===>>>>  lock failed...");
//            locked.set(false);
//        }
    }


    @PreDestroy
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                connection.close();
            }
        } catch (Exception e) {
            log.info("ignore this close exception");
        }
    }

}
