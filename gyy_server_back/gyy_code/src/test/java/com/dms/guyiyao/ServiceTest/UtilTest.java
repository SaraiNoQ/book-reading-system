package com.dms.guyiyao.ServiceTest;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Random;

public class UtilTest {

    @Test
    public   void testPY(){
        String china="备急千金要方卷第五下 少小婴孺方下\\小儿杂病第九方一百二十一首 灸法十三首";
        HanyuPinyinOutputFormat formart = new HanyuPinyinOutputFormat();
        formart.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        formart.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        formart.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] arrays = china.trim().toCharArray();
        String result = "";
        try {
            for (int i=0;i<arrays.length;i++) {
                char ti = arrays[i];
                if(Character.toString(ti).matches("[\\u4e00-\\u9fa5]")){ //匹配是否是中文
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(ti,formart);
                    result += temp[0].substring(0,1);
                }else{
                    result += ti;
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

//        return result;
        System.out.println(result.replace(" ",""));
    }
    @Test
    public void dirTest(){
        String dir="C:\\Users\\11641\\Desktop\\古医药项目\\代码\\备份\\2.26\\gyy_code\\gyy_server_back\\gyy_code\\src\\main\\resources\\static\\bjqjyf\\bjqjyfjdwxsxyrfx\\xezbdjfybesysjfsss";
        File file=new File(dir);
        if (!file.exists())file.mkdirs();
    }
    @Test
    public  void  test(){
        int a= (int) (Math.random()*1000);
        System.out.println(a);
    }

}
