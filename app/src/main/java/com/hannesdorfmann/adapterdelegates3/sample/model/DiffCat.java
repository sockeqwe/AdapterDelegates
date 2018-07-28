package com.hannesdorfmann.adapterdelegates3.sample.model;

public class DiffCat implements DiffItem {

    private int id;

    private String name;

    public DiffCat(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

        DiffCat diffCat = (DiffCat) o;

        if (id != diffCat.id) return false;
        return name != null ? name.equals(diffCat.name) : diffCat.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
