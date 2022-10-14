package com.dms.guyiyao.ServiceTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dms.guyiyao.pojo.word.JsonTrans;
import com.dms.guyiyao.utils.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.regex.Pattern;

@SpringBootTest
public class RegTest {
@Test
    public void test_1(){
    String reg_user="^[a-zA-Z0-9_-]{4,16}$";         //4-16位用户名(可以是数字，字母，下划线)
    String reg_pass="^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{6,15}$"; //6-15位密码（字母数字特殊字符至少包含两种）
//    String reg_email="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";//邮箱正则
    String reg_email="^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";//邮箱正则

    String input_user="aa23aa";
    String input_pass="aaaaaa";
    String input_email="1164175212@qq.com";

    boolean matches_user=Pattern.matches(reg_user,input_user);
    boolean matches_pass=Pattern.matches(reg_pass,input_pass);
    boolean matches_email = Pattern.matches(reg_email, input_email);

    System.out.println("用户名验证结果"+matches_user);
    System.out.println("密码验证结果"+matches_pass);
    System.out.println("邮箱检验结果"+matches_email);


}
@Test
    public void test_2(){
}

    public static void main(String[] args) {
        String str="[[0,\"备急千金要方(宋校版)\",\"咽门论第三\",\"咽门论第三\",\"咽门论第三\",\"新雕孙真人千金方\",\"咽门论第三\",null,null],[1,\"备急千金要方(宋校版)\",\"咽门论第三\",\"咽门论第三\",\"咽门论第三\",\"新雕孙真人千金方\",\"咽门论第三\",null,null]]";
        ArrayList arrayList=new ArrayList();
        ArrayList tempList_1=new ArrayList();
        tempList_1.add("1");
        tempList_1.add("备用急千金药方");
        ArrayList tempList_2=new ArrayList();
        tempList_2.add("2");
        tempList_2.add("孙真人千金方");
        arrayList.add(tempList_1);
        arrayList.add(tempList_2);
        Object myjson = JSONObject.toJSON(arrayList);
        System.out.println("myjson"+myjson);
        System.out.println("前端传来的string"+str);
        ArrayList<ArrayList<Object>> arrayList1 = JSON.parseObject(str, new ArrayList<ArrayList<Object>>().getClass());

        System.out.println("前端对象结果"+arrayList1);
        Object o = arrayList1.get(1);
        System.out.println(o);
//        JSONArray objects = JSON.parseArray(str);
//        ArrayList<ArrayList<Object>> arr= (ArrayList<ArrayList<Object>>) JSONObject.toJavaObject(str, new ArrayList<ArrayList<Object>>().getClass());
//        Object parse = JSON.parse(str);
//        ArrayList arrayList1 = JSON.parseObject(str, ArrayList.class);
//        System.out.println(arrayList1);

    }

}
