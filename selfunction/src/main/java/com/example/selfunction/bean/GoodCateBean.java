package com.example.selfunction.bean;

import java.util.List;

public class GoodCateBean {

    private int code;
    private String msg;
    private long time;
    private List<Data> data;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }


    public static class Data {

        private int id;
        private int pid;
        private String name;
        private String icon;
        private String image;
        private int level;
        private String color;
        private List<Children> children;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getPid() {
            return pid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

        public void setChildren(List<Children> children) {
            this.children = children;
        }

        public List<Children> getChildren() {
            return children;
        }

    }

    public static class Children {

        private int id;
        private int pid;
        private String name;
        private String icon;
        private String image;
        private int level;
        private String color;
        private List<String> children;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getPid() {
            return pid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

        public void setChildren(List<String> children) {
            this.children = children;
        }

        public List<String> getChildren() {
            return children;
        }

    }
}
