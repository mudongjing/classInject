package xyz.wcd.base;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class LCommon {
    protected Messager messager;
    protected JavacTrees trees;
    protected TreeMaker treeMaker;
    protected Names names;

    public LCommon(ProcessContext processContext) {
        this.treeMaker = processContext.treeMaker();
        this.trees = processContext.trees();
        this.names = processContext.names();
        this.messager = processContext.messager();
    }

    public void importPackageFromString(LClass lClass, String classPath) {
        JCTree.JCCompilationUnit compilationUnit = (JCTree.JCCompilationUnit) trees.getPath(lClass.classSymbol()).getCompilationUnit();
        ListBuffer<JCTree> imports = new ListBuffer<JCTree>();
        JCTree.JCIdent packageIdent = treeMaker.Ident(names.fromString(classPath.substring(0,classPath.lastIndexOf("."))));
        messager.printMessage(Diagnostic.Kind.NOTE,"导入包String"+classPath.substring(0,classPath.lastIndexOf(".")));
        JCTree.JCFieldAccess fieldAccess = treeMaker.Select(packageIdent, names.fromString(classPath.substring(classPath.lastIndexOf(".")+1)));
        messager.printMessage(Diagnostic.Kind.NOTE,"String简单名 "+classPath.substring(classPath.lastIndexOf(".")+1));
        JCTree.JCImport jcImport = treeMaker.Import(fieldAccess, false);
        imports.append(jcImport);
        for (int i = 0; i < compilationUnit.defs.size(); i++) { imports.append(compilationUnit.defs.get(i)); }
        compilationUnit.defs = imports.toList();
    }
}
