package com.dms.guyiyao.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class TreeNode implements Comparable{
/*节点ID*/
    private String id;
    private Integer grade;
    private  String chapter;
/*父节点ID,顶级id为0*/
    private  String parentId;
/*节点名称*/
/*子节点*/
    private List<TreeNode>cataLog;
    /*节点结构化状态*/
    private  boolean flage;
/*
* 排序
* */
    private  int sequence;


    @Override
    public int compareTo(Object o) {
        TreeNode treeNode=(TreeNode)o;
        if (this.getSequence()>treeNode.getSequence())return 1;
        else if (this.getSequence()<treeNode.getSequence()) return  -1;
        return this.chapter.compareTo(treeNode.chapter);
    }
}
