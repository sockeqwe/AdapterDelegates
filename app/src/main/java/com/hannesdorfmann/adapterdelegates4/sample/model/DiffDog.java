package com.hannesdorfmann.adapterdelegates4.sample.model;

/**
 * @author Sergey Opivalov
 */

public class DiffDog implements DiffItem {

    private int id;

    private String name;

    private int age;

    public DiffDog(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public long getItemId() {
        return id;
    }

    @Override
    public int getItemHash() {
        return hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiffDog diffDog = (DiffDog) o;

        if (id != diffDog.id) return false;
        if (age != diffDog.age) return false;
        return name != null ? name.equals(diffDog.name) : diffDog.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }
}
