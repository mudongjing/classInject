package io.github.mudongjing.base;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;

public abstract class BaseProcessor extends AbstractProcessor {
    protected Messager messager;
    protected JavacTrees trees;
    protected TreeMaker treeMaker;
    protected Names names;
    public Elements elementUtils;
    protected ProcessContext processContext;
    // 文件生成器 类/资源，Filter用来创建新的源文件，class文件以及辅助文件
    protected Filer filer;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        elementUtils = processingEnv.getElementUtils();
        this.processContext = ProcessContext.newInstance().messager(messager).names(names).treeMaker(treeMaker).trees(trees);
        filer = processingEnv.getFiler();
    }
    public Filer getFiler(){ return this.filer; }
}
