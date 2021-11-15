package io.github.mudongjing.base;

import com.sun.tools.javac.code.Symbol;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class BaseClassProcessor extends BaseProcessor {
    protected abstract Class<? extends Annotation> getAnnotationClass();
    protected void handleClass(final LClass lClass) {
        // do nothing;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<LClass> classList = getClassList(roundEnv, getAnnotationClass());
        for (LClass lClass : classList) { handleClass(lClass); }
        return true;
    }

    protected List<LClass> getClassList(final RoundEnvironment roundEnv, final Class<? extends Annotation> clazz) {
        List<LClass> classList = new ArrayList<LClass>();
        Set<? extends Element> serialSet = roundEnv.getElementsAnnotatedWith(clazz);
        // 对于每一个类可以分开，使用线程进行处理。
        for (Element element : serialSet) {
            if (element instanceof Symbol.ClassSymbol) {
                LClass lClass = new LClass(processContext, (Symbol.ClassSymbol) element);
                classList.add(lClass);
            }
        }
        return classList;
    }
}
