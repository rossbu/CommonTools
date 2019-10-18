package com.pojo;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class TreeNode {

    private int value;

    private Set<TreeNode> children;

    TreeNode(int value, TreeNode... children) {
        this.value = value;
        this.children = newHashSet(children);
    }

    public int getValue() {
        return value;
    }

    public Set<TreeNode> getChildren() {
        return children;
    }
}