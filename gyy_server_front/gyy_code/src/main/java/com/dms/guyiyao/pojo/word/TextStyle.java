package com.dms.guyiyao.pojo.word;

import lombok.Data;

/**
 * @Description 文本样式
 * 2018年12月1日  下午4:09:30
 * @Author Huangxiaocong
 */
@Data
public class TextStyle {
    private String url;    // 超链接
    private String text;    //文本内容
    private String fontFamily;    //字体
    private String fontSize;    //字体大小
    private String colorVal;    //字体颜色
    private String shdColor;    //底纹颜色
    private int position;    //文本位置
    private int spacingValue;    //间距
    private int indent;    //缩进
    private boolean isBlod;    //加粗
    private boolean isUnderLine;    //下划线
    private boolean underLineColor;    //
    private boolean isItalic;    //倾斜
    private boolean isStrike;    //删除线
    private boolean isDStrike;    //双删除线
    private boolean isShadow;    //阴影
    private boolean isVanish;    //隐藏
    private boolean isEmboss;    //阳文
    private boolean isImprint;    //阴文
    private boolean isOutline;    //空心
    private boolean isHightLight;    //突出显示文本
    private boolean isShd;    //底纹

    //此处省略get/set方法...

}