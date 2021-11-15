package io.github.mudongjing.base;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;
import javax.annotation.processing.Messager;

public class ProcessContext {
    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;
    public static ProcessContext newInstance() {
        return new ProcessContext();
    }
    public Messager messager() {
        return messager;
    }
    public ProcessContext messager(Messager messager) {
        this.messager = messager;
        return this;
    }
    public JavacTrees trees() { return trees; }
    public ProcessContext trees(JavacTrees trees) {
        this.trees = trees;
        return this;
    }
    public TreeMaker treeMaker() {
        return treeMaker;
    }
    public ProcessContext treeMaker(TreeMaker treeMaker) {
        this.treeMaker = treeMaker;
        return this;
    }
    public Names names() {
        return names;
    }
    public ProcessContext names(Names names) {
        this.names = names;
        return this;
    }
}
