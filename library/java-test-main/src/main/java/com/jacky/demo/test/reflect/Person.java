package com.jacky.demo.test.reflect;

/**
 * Created by Jacky on 2020/7/7
 */
class Person {
    private int age;
    private String name;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    private String sayHello() {
        return "Hello{" +
            "age=" + age +
            ", name='" + name + '\'' +
            '}';
    }

    @Override
    public String toString() {
        return "Person{" +
            "age=" + age +
            ", name='" + name + '\'' +
            '}';
    }
}
