package com.vaadin.base.devserver.editor;

import org.junit.Test;

public class HelloWorldTest extends AbstractClassBasedTest {

    @Test
    public void createHelloWorld() throws Exception {
        setupTestClass("EmptyView");
        // System.out.println(getTestFileContents());

        // Create is the class declaration when there is no constructor
        int create = getLineNumber(testFile, "public class EmptyView");
        int attach = create;
        editor.addComponentInside(testFile, create, attach,
                ComponentType.TEXTFIELD, "Your name");

        System.out.println(getTestFileContents());

        int yourNameCreate = getLineNumber(testFile, "new TextField");
        int yourNameAttach = getLineNumber(testFile, "add(");
        editor.addComponentAfter(testFile, yourNameCreate, yourNameAttach,
                ComponentType.BUTTON, "Say hello");

        System.out.println(getTestFileContents());

        int sayHelloCreate = getLineNumber(testFile, "new Button");
        int sayHelloAttach = getLineNumber(testFile, "add(");

        System.out.println(getTestFileContents());
        editor.setComponentAttribute(testFile, sayHelloCreate, sayHelloAttach,
                ComponentType.BUTTON, "setFoo", "bar");
        System.out.println(getTestFileContents());
        editor.addClickListener(testFile, sayHelloCreate, sayHelloAttach);

        System.out.println(getTestFileContents());

    }
}
