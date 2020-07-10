package com.incarcloud.helper;

import com.incarcloud.boar.datapack.DataParserManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * APP启动类
 *
 * @author Aaric, created on 2020-07-10T14:59.
 * @version 0.1.0-SNAPSHOT
 */
@SpringBootApplication
public class App {

    /**
     * 挂载`com.incarcloud.boar.datapack`包下面的解析器
     */
    static {
        DataParserManager.loadClassOfSamePackage();
    }

    /**
     * Main
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
