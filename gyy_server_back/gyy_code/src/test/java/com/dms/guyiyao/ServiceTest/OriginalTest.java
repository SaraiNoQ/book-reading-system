package com.dms.guyiyao.ServiceTest;

import com.dms.guyiyao.pojo.chapter.FullCataLog;
import com.dms.guyiyao.pojo.original.OriginalReturn;
import com.dms.guyiyao.service.Impl.TsBookOrginalServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Set;

@SpringBootTest
public class OriginalTest {
@Autowired
    TsBookOrginalServiceImpl orginalService;
    @Test
    public void  getFullcataLog(){


//        FullCataLog fullCataLog=orginalService.getFullcataLog("002009004");
//        FullCataLog fullCataLog_temp=fullCataLog;
//        while (fullCataLog!=null){
//           String chapterName=fullCataLog.getChaptername();
//           fullCataLog_temp=orginalService.getFullcataLog(fullCataLog.getParentid());
//           if(fullCataLog_temp==null)break;
//           chapterName=fullCataLog_temp.getChaptername()+"\\"+chapterName;
//           fullCataLog.setChaptername(chapterName);
//           fullCataLog.setParentid(fullCataLog_temp.getParentid());
//        }
//        System.out.println(fullCataLog);
        System.out.println(orginalService.getFullcataLog("123"));
}
/*测试数据库添加*/
@Test
    public void addDb(){
    System.out.println(orginalService.uploadImgDb("002002","testName1-1","/testurl-1"));
}
@Test
    public void addMatch(){
    System.out.println(orginalService.addMatch("002002","test3.23",new String[]{"6e8b196daa4a11ec98b700163e30620d",
                                                                                                "d5dfbf23aa4a11ec98b700163e30620d"},
                                                                                            1));
}
@Test
  public  void getImgInfo(){
    System.out.println(orginalService.getPicUrl("002002"));
}
@Test
    public void getMatch(){
 Map map= orginalService.getOriginal("002002");
    for (Object o : map.keySet()) {
        System.out.println(o+":"+map.get(o));
    }
}
    @Test
    public void deleteMatch(){
        System.out.println(orginalService.deleteMathch("002002",1));
    }
}
