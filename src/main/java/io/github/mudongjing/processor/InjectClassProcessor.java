package io.github.mudongjing.processor;

import io.github.mudongjing.annotation.InjectClass;
import io.github.mudongjing.base.BaseClassProcessor;
import io.github.mudongjing.base.LObject;
import com.google.auto.service.AutoService;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import io.github.mudongjing.base.LClass;
import io.github.mudongjing.base.LField;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("io.github.mudongjing.annotation.InjectClass")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class InjectClassProcessor extends BaseClassProcessor {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return InjectClass.class;
    }
    @Override
    protected void handleClass(LClass lClass)  {
        createSerialVersionUID(lClass);
    }
    private void createSerialVersionUID(LClass lClass)  {
        FileObject test = null;
        try {
            test = getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "test");
        } catch (IOException e) { e.printStackTrace(); }
        InjectClass proInject=lClass.classSymbol().getAnnotation(InjectClass.class);
        String path=test.toUri().getPath();
        File[] files = new File(test.toUri().getPath().substring(1,path.indexOf("target"))+ "src/main/java/"+proInject.path().replace(".","/")).listFiles();
        java.util.List<LField> fieldList=new ArrayList<>();
        Set<String> fileNamesSet=new HashSet();
        for (File file1:files){
            String s=file1.getName();
            String var=s.substring(0,s.indexOf("."));
            String classS=proInject.path()+"."+var;
            messager.printMessage(Diagnostic.Kind.NOTE,"classS "+classS);
            LObject value = new LObject(processContext).expression(null);
            String varLow;
            if (var.charAt(0)>='a' && var.charAt(0)<='z') varLow=var+"var";
            else varLow=var.substring(0,1).toLowerCase()+var.substring(1);
            messager.printMessage(Diagnostic.Kind.NOTE,"varLow "+varLow);
            LField lField = LField.newInstance().modifiers(Flags.PRIVATE).typeFromString(classS).name(varLow).value(value);
            fieldList.add(lField);
            fileNamesSet.add(lField.name());
        }
        JCTree tree = trees.getTree(lClass.getElement());
        lClass.insertFieldFromStringList(fieldList);
        tree.accept(new TreeTranslator(){
            @Override
            public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();

                for (JCTree jcTree : jcClassDecl.defs){
                    if (jcTree.getKind().equals(Tree.Kind.VARIABLE)){
                        JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) jcTree;
                        jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
                    }
                }
                jcVariableDeclList.forEach(jcVariableDecl -> {
                    if (fileNamesSet.contains(jcVariableDecl.getName().toString())){
                        jcClassDecl.defs = jcClassDecl.defs.prepend(makeSetterMethod(jcVariableDecl));
                        jcClassDecl.defs = jcClassDecl.defs.prepend(makeGetterMethod(jcVariableDecl));
                    }
                });
                super.visitClassDef(jcClassDecl);
            }
        });
    }
    private JCTree.JCMethodDecl makeSetterMethod(JCTree.JCVariableDecl jcVariableDecl){

        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        JCTree.JCExpressionStatement aThis = makeAssignment(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName()), treeMaker.Ident(jcVariableDecl.getName()));
        statements.append(aThis);
        JCTree.JCBlock block = treeMaker.Block(0, statements.toList());
        JCTree.JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), jcVariableDecl.getName(), jcVariableDecl.vartype, null);
        List<JCTree.JCVariableDecl> parameters = List.of(param);
        JCTree.JCExpression methodType = treeMaker.Type(new Type.JCVoidType());
        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC),getNewMethodName(jcVariableDecl.getName()),methodType, List.nil(),parameters,List.nil(),block,null);
    }

    private JCTree.JCExpressionStatement makeAssignment(JCTree.JCExpression lhs, JCTree.JCExpression rhs) {
        return treeMaker.Exec(treeMaker.Assign(lhs, rhs));
    }
    private Name getNewMethodName(Name name){
        String s = name.toString();
        return names.fromString("set"+s.substring(0,1).toUpperCase()+s.substring(1,name.length()));
    }
    private JCTree.JCMethodDecl makeGetterMethod(JCTree.JCVariableDecl jcVariableDecl){
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        JCTree.JCReturn aThis = treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName()));
        statements.append(aThis);
        JCTree.JCBlock block = treeMaker.Block(0, statements.toList());
        JCTree.JCExpression methodType = treeMaker.Ident(names.fromString(jcVariableDecl.vartype.toString()));
        messager.printMessage(Diagnostic.Kind.NOTE,"返回type"+methodType.toString());
        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC),getGetMethodName(jcVariableDecl.getName()),methodType, List.nil(),List.nil(),List.nil(),block,null);
    }

    private Name getGetMethodName(Name name){
        String s = name.toString();
        return names.fromString("get"+s.substring(0,1).toUpperCase()+s.substring(1,name.length()));
    }
}
