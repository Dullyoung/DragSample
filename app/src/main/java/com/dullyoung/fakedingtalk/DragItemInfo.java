package com.dullyoung.fakedingtalk;

/*
 *  Created  in  2021/6/7
 */
public class DragItemInfo {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DragItemInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
