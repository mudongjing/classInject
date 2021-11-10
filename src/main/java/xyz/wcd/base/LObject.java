package xyz.wcd.base;

import com.sun.tools.javac.tree.JCTree;

public class LObject extends LCommon{
    private JCTree.JCExpression expression;
    public LObject(ProcessContext processContext) { super(processContext); }
    public JCTree.JCExpression expression() { return expression; }
    public LObject expression(JCTree.JCExpression expression) {
        this.expression = expression;
        return this;
    }
}