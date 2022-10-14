package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.service.Impl.TsBookOrginalServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OriginalTest {
    @Autowired
    TsBookOrginalServiceImpl orginalService;
    @Test
    public void test(){
        System.out.println(orginalService.getOriginal("002002"));
    }
}
