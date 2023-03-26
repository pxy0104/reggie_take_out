package com.pxy.reggie.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author pxy
 * @software IntelliJ IDEA
 * @create 2023-03-26 18:34
 **/

@SpringBootTest
public class Upload {
    @Test
    public void testFileName() {
        String name = "nsi1.23.jpg";
        String substring = name.substring(name.lastIndexOf("."));
        System.out.println(substring);
    }

}
