package com.example.selfunction.bean;

import java.util.List;

public class AddressDataBean {

    private List<Cityjson> cityjson;

    public void setCityjson(List<Cityjson> cityjson) {
        this.cityjson = cityjson;
    }

    public List<Cityjson> getCityjson() {
        return cityjson;
    }

    public static class Children {

        private String label;
        private String value;
        private List<Childrens> children;

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setChildren(List<Childrens> children) {
            this.children = children;
        }

        public List<Childrens> getChildren() {
            return children;
        }

    }

    public static class Childrens {

        private String label;
        private String value;

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }


    public static class Cityjson {

        private String label;
        private String value;
        private List<Children> children;

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setChildren(List<Children> children) {
            this.children = children;
        }

        public List<Children> getChildren() {
            return children;
        }

    }
}
