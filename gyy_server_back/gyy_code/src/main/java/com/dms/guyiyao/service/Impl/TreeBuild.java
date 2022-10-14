package com.dms.guyiyao.service.Impl;

import com.dms.guyiyao.pojo.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TreeBuild {
    //    nodeLost是某一本树的所有章节节点
    public List<TreeNode>nodeList=new ArrayList<TreeNode>();
    //    通过构造方法注入数据
    public TreeBuild(List<TreeNode>nodeList){
        this.nodeList=nodeList;
    }

    //通过这个方法获取顶级节点
    public List<TreeNode>getRootNode(){
        List<TreeNode>rootNodeList=new ArrayList<>();
        for (TreeNode treeNode : nodeList) {
            if(treeNode.getGrade()==0){
//                System.out.println(nodeList.size());
//                System.out.println(treeNode.getChapter());
                rootNodeList.add(treeNode);
            }
        }
        return rootNodeList;
    }
    //使用顶级节点构建目录树
    public List<TreeNode>buildTree() {
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (TreeNode treeRootNode : getRootNode()) {
            treeRootNode=buildChildTree(treeRootNode);
            treeNodes.add(treeRootNode);
        }
        return  treeNodes;
    }
    //使用非递归算法构建父子关系，flage判断改节点是否找到父节点，如果为true表明已找到直接跳过
    Stack stack=new Stack<>();
    public TreeNode buildChildTree(TreeNode pNode){
        List<TreeNode>childTree=new ArrayList<TreeNode>();
        stack.push(pNode);

        while (stack.size()!=0){
            TreeNode parNode= (TreeNode) stack.pop();
            for (int i = 0; i < nodeList.size(); i++) {
                try{
                if(!nodeList.get(i).isFlage()&&nodeList.get(i).getParentid().equals(parNode.getId())){
                    nodeList.get(i).setFlage(true);//子节点已被收纳，不在被其他节点收纳
                    childTree.add(nodeList.get(i));
                    stack.push(nodeList.get(i));//子节点成为新的父节点
                }
                }catch (Exception e){
                    continue;
                }
            }
            parNode.setCataLog(childTree);
            childTree=new ArrayList<TreeNode>();
        }
//        返回顶级节点
        return pNode;
    }

}

