package com.ark.norns.dataStructure;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

public class TreeNode<K, P, D> implements Comparable<TreeNode<K, P, D>>, Serializable {
    @JsonIgnore
    D data;

    @JsonIgnore
    K key;

    P path;

    @JsonIgnore
    boolean isLeaf = false;

    @JsonBackReference
    TreeNode<K, P, D> parent;

    @JsonManagedReference
    TreeSet<TreeNode<K, P, D>> children = new TreeSet<>();

    @JsonIgnore
    Set<MibFile> mibFileComposition = new HashSet<>();

    public TreeNode() {
    }

    public TreeNode(D data, P path, boolean isLeaf) {
        this.data = data;
        this.path = path;
        this.isLeaf = isLeaf;
    }

    public TreeNode(K key, P path, D d) {
        this.data = d;
        this.key = key;
        this.path = path;
        this.children = new TreeSet<TreeNode<K, P, D>>();
    }

    public TreeNode(K key, P path, D d, boolean isLeaf, TreeNode<K, P, D> parent) {
        this.data = d;
        this.key = key;
        this.path = path;
        this.isLeaf = isLeaf;
        this.children = new TreeSet<TreeNode<K, P, D>>();
        this.parent = parent;
    }

    public void addLeaf(Queue<Integer> queue, K key, P path, D data) {
        Integer id = queue.poll();
        if (queue.isEmpty()) {
            this.children.add(new TreeNode<K, P, D>(key, path, data, true, this));
            return;
        }

        if (this.parent == null) {
            id = queue.poll();
        }

        for (TreeNode<K, P, D> node : this.children) {
            if ((Integer) node.key == id) {
                node.addLeaf(queue, key, path, data);
                return;
            }
        }

        this.children.add(new TreeNode<K, P, D>(key, path, data, true, this));
    }

    public void addMibFile(MibFile mibFile) {
        this.mibFileComposition.add(mibFile);
    }

    public void addMibFile(Set<MibFile> mibFile) {
        this.mibFileComposition.addAll(mibFile);
    }

    public TreeNode<K, P, D> addChildren(K k, P p, D d) {
        TreeNode<K, P, D> tNode = children.stream().filter(
                node -> node.getKey() == k
        ).findFirst().orElse(null);

        if (tNode != null) {
            return tNode;
        } else {
            tNode = new TreeNode<K, P, D>(k, p, d);
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
        if (this.key == null && o.getKey() == null) {
            return 0;
        } else if (this.key == null) {
            return 1;
        } else if (o.getKey() == null) {
            return -1;
        }

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

    public Set<MibFile> getMibFileComposition() {
        return mibFileComposition;
    }

    public void setMibFileComposition(Set<MibFile> mibFileComposition) {
        this.mibFileComposition = mibFileComposition;
    }
}