package com.dms.guyiyao.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class TreeNode {
    /*节点ID*/
    private String id;
    /*父节点ID,顶级id为0*/
    private  String parentid;
    private  String bookId;
    private int grade;
    /*节点名称*/
    private  String sequence;
    private  String chapter;
    /*子节点*/
    private List<TreeNode>cataLog;
    /*节点结构化状态*/

    private  boolean flage;
    /*
     * 排序
     * */


}
