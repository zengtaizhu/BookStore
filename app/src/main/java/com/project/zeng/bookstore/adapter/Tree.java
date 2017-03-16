package com.project.zeng.bookstore.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/16.
 */

public class Tree {

    public int num = 0;
    public Tree leftTree = null;
    public Tree rightTree = null;

    public Tree getLeftTree() {
        return leftTree;
    }

    public Tree getRightTree() {
        return rightTree;
    }

    //判断是否有左子数
    public boolean hasLeft(){
        return leftTree == null;
    }

    //判断是否有右子数
    public boolean hasRight(){
        return rightTree == null;
    }

    //打印路径
    public void printRoute(List<Tree> trees){
        for(Tree tree:trees){
            System.out.println(tree.num);
        }
    }

    public void getRoute(int target, Tree twoTree){
        List<Tree> trees = new ArrayList<>();
        int temp = target;
        Tree tempTree = twoTree;//遍历中的当前节点
        Tree tempParentTree = twoTree;//遍历中当前节点的父节点
        while(!tempTree.hasLeft()){
            temp = temp - tempTree.num;
            if(temp < 0){
                break;//结束循环，该节点路径以下不必再遍历
            }else if(temp == 0){
                printRoute(trees);//找到路劲后向右遍历
                tempTree = tempParentTree.rightTree;
                getRoute(temp, tempTree);
            }else{
                trees.add(tempTree);
                tempParentTree = tempTree;
                tempTree = tempTree.leftTree;
            }
        }
    }
}
