package io.github.mudongjing.base;

public class LField {
    private long modifiers;
    private String typeFromString;
    private String name;
    private LObject value;
    public static LField newInstance() {
        return new LField();
    }
    public long modifiers() {
        return modifiers;
    }
    public LField modifiers(long modifiers) {
        this.modifiers = modifiers;
        return this;
    }
    public String getTypeFromString(){
        return typeFromString;
    }
    public LField typeFromString(String typeFromString){
        this.typeFromString=typeFromString;
        return this;
    }
    public String name() {
        return name;
    }
    public LField name(String fieldName) {
        this.name = fieldName;
        return this;
    }
    public LObject value() {
        return value;
    }
    public LField value(LObject value) {
        this.value = value;
        return this;
    }
}
