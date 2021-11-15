package io.github.mudongjing.base;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;

public class LMethod extends LCommon{
    private Symbol.MethodSymbol methodSymbol;
    private JCTree.JCMethodDecl methodDecl;
    private LClass refClass;
    public LMethod(ProcessContext processContext, Symbol.MethodSymbol methodSymbol) {
        super(processContext);
        this.methodSymbol = methodSymbol;
        this.refClass = new LClass(processContext, (Symbol.ClassSymbol) methodSymbol.owner);
        methodDecl = trees.getTree(methodSymbol);
    }
}
