package xyz.wcd.base;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.List;

public class LClass extends LCommon {
    private final Symbol.ClassSymbol classSymbol;
    private final JCTree.JCClassDecl classDecl;
    private Element element;

    public LClass(ProcessContext processContext, Symbol.ClassSymbol classSymbol) {
        super(processContext);
        this.classSymbol = classSymbol;
        classDecl = super.trees.getTree(classSymbol);
        element=classSymbol;
    }
    public Element getElement(){
        return this.element;
    }

    public LClass insertFieldFromStringList(List<LField>  fieldList) {
        ListBuffer<JCTree> statements = new ListBuffer<JCTree>();
        for (LField field: fieldList) {
            if (existsField(field.name())) {
                return this;
            }
            messager.printMessage(Diagnostic.Kind.NOTE,"进入insert");
            // 导入包
            importPackageFromString(this, field.getTypeFromString());

            // 添加 JCTree
            JCTree.JCVariableDecl variableDecl = treeMaker.VarDef(treeMaker.Modifiers(field.modifiers()), names.fromString(field.name()), buildClassIdentFromString(field.getTypeFromString()), field.value().expression());
            statements.append(variableDecl);
        }

        // 循环添加，重新赋值。
        for (JCTree jcTree : classDecl.defs) {
            statements.append(jcTree);
        }
        classDecl.defs = statements.toList();
        return this;
    }


    private JCTree.JCIdent buildClassIdentFromString(String  classPath) {
        Symbol.ClassSymbol classSymbol = new Symbol.ClassSymbol(Flags.ClassFlags,
                names.fromString(classPath.substring(classPath.lastIndexOf(".")+1)),null);
        return treeMaker.Ident(classSymbol);
    }

    private boolean existsField(String fieldName) {
        for (JCTree jcTree : classDecl.defs) {
            if (jcTree.getKind() == Tree.Kind.VARIABLE) {
                JCTree.JCVariableDecl var = (JCTree.JCVariableDecl) jcTree;
                System.out.println(var.sym.flags_field);
                if (fieldName.equals(var.name.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
    public Symbol.ClassSymbol classSymbol() { return classSymbol; }
}
