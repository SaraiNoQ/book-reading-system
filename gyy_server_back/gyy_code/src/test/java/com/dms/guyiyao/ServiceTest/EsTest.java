package com.dms.guyiyao.ServiceTest;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
public class EsTest {
    @Autowired
    private RestHighLevelClient client;
    @Test
    void getInfo() throws IOException {
        GetRequest getRequest=new GetRequest("jd1","1");
//    getRequest.fetchSourceContext(new FetchSourceContext(false));
//    getRequest.storedFields("_none_");
        GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(documentFields.getSourceAsString());
}
@Test
    void get() throws IOException {

}

    public static void main(String[] args) {
        ArrayList arrayList= new ArrayList();

        String []s=new String[20];
        s[0]="1";
        List list= Arrays.asList(s);
        System.out.println(list);
    }
}
