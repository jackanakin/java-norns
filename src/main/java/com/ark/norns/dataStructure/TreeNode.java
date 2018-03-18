package com.ark.norns.dataStructure;

import java.util.TreeSet;

public class TreeNode<K, P, D> implements Comparable<TreeNode<K, P, D>> {
    D data;
    K key;
    P path;
    int rank;
    boolean isLeaf = false;
    TreeNode<K, P, D> parent;
    TreeSet<TreeNode<K, P, D>> children;

    public TreeNode(K key, P path, D d, int r) {
        this.rank = rank;
        this.data = d;
        this.key = key;
        this.path = path;
        this.children = new TreeSet<TreeNode<K, P, D>>();
    }

    public TreeNode(K key, P path, D d, int r, boolean isLeaf) {
        this.rank = rank;
        this.data = d;
        this.key = key;
        this.path = path;
        this.isLeaf = isLeaf;
        this.children = new TreeSet<TreeNode<K, P, D>>();
    }

    public TreeNode<K, P, D> addChildren(K k, P p, D d) {
        TreeNode<K, P, D> tNode = children.stream().filter(
                node -> node.getKey() == k
        ).findFirst().orElse(null);

        if (tNode != null) {
            return tNode;
        } else {
            tNode = new TreeNode<K, P, D>(k, p, d, this.rank++);
            tNode.parent = this;
            this.children.add(tNode);
            return tNode;
        }
    }

    public TreeNode<K, P, D> addChildren(K k, P p, D d, boolean isLeaf) {
        TreeNode<K, P, D> tNode = children.stream().filter(
                node -> node.getKey() == k
        ).findFirst().orElse(null);

        if (tNode != null) {
            return tNode;
        } else {
            tNode = new TreeNode<K, P, D>(k, p, d, this.rank++, isLeaf);
            tNode.parent = this;
            this.children.add(tNode);
            return tNode;
        }
    }

    public TreeNode<K, P, D> findParentNode(String parentIdentifier) {
        for (TreeNode<K, P, D> node : this.children) {
            MibFileOid mibFileOid = (MibFileOid) node.getData();
            if (mibFileOid != null && mibFileOid.getIdentifier() != null &&
                    mibFileOid.getIdentifier().equals(parentIdentifier)) {
                return node;
            } else if (node.children.size() > 0) {
                TreeNode<K, P, D> childNode = node.findParentNode(parentIdentifier);
                if (childNode != null) {
                    return childNode;
                }
            }
        }
        return null;
    }

    public TreeNode<K, P, D> findParentNodeByOid(String oid) {
        for (TreeNode<K, P, D> node : this.children) {
            MibFileOid mibFileOid = (MibFileOid) node.getData();
            if (mibFileOid != null && mibFileOid.getOid() != null &&
                    mibFileOid.getOid().equals(oid)) {
                return node;
            } else if (node.children.size() > 0) {
                TreeNode<K, P, D> childNode = node.findParentNodeByOid(oid);
                if (childNode != null) {
                    return childNode;
                }
            }
        }
        return null;
    }

    public void replaceNodeByOid(String oid, TreeNode<K, P, D> toReplace) {
        for (TreeNode<K, P, D> node : this.children) {
            MibFileOid mibFileOid = (MibFileOid) node.getData();
            if (mibFileOid != null && mibFileOid.getOid() != null &&
                    mibFileOid.getOid().equals(oid)) {
                node = toReplace;
                break;
            } else if (node.children.size() > 0) {
                node.replaceNodeByOid(oid, toReplace);
            }
        }
    }

    public void printAll() {
        if (this.getParent() == null) {
            System.out.println(this.getData().toString());
        }
        for (TreeNode<K, P, D> tree : this.children) {
            System.out.println(tree.getData().toString());
            if (tree.children.size() > 0) {
                tree.printAll();
            }
        }
    }

    @Override
    public int compareTo(TreeNode<K, P, D> o) {
        if ((Integer) this.key > (Integer) o.getKey()) {
            return 1;
        }
        if ((Integer) this.key < (Integer) o.getKey()) {
            return -1;
        }
        return 0;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public P getPath() {
        return path;
    }

    public void setPath(P path) {
        this.path = path;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public TreeNode<K, P, D> getParent() {
        return parent;
    }

    public void setParent(TreeNode<K, P, D> parent) {
        this.parent = parent;
    }

    public TreeSet<TreeNode<K, P, D>> getChildren() {
        return children;
    }

    public void setChildren(TreeSet<TreeNode<K, P, D>> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}