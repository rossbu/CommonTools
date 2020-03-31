package com.jdk.jdk5.annotation;

@Component(identifier = "upper")
public class UpperCaseComponent {

    public String doWork(String input) {
        if (input != null) {
            return input.toUpperCase();
        } else {
            return null;
        }
    }
}

class Client {
    public static void main(String[] args) {
        try {
            Class componentClass = Class.forName("com.jdk.jdk5.annotation.UpperCaseComponent");
            if (componentClass.isAnnotationPresent(Component.class)) {
                Component component = (Component) componentClass.getAnnotation(Component.class);
                String identifier = component.identifier();
                System.out.println(String.format("Identifier for com.jdk.jdk5.annotation.UpperCaseComponent is ' %s '", identifier));
            } else {
                System.out.println("com.jdk.jdk5.annotation.UpperCaseComponent is not annotated by com.jdk.jdk5.annotation.Component");
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}