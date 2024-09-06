package com.company.goodlock;

public class ListViewItem {
    private int id;
    private String name;

    public ListViewItem(){
        super();
    }
    public ListViewItem(String name){
        super();
        this.name = name;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

}