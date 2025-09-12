package com.senmol.mes;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Administrator
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
@MapperScan("com.senmol.mes.*.mapper")
public class SenmolMesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenmolMesApplication.class, args);
        System.err.println("\nMES系统启动成功 ......\n");
    }

}
