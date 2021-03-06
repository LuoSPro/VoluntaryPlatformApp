package com.ls.voluntaryplatformapp.model;

import java.util.List;

public class ActionTab {


    /**
     * activeSize : 16
     * normalSize : 14
     * activeColor : #ED7282
     * normalColor : #666666
     * select : 0
     * tabGravity : 0
     * tabs : [{"title":"待发布","index":0,"tag":"unpublished","enable":true},{"title":"正在进行","index":1,"tag":"underway","enable":true},{"title":"已结束","index":2,"tag":"finished","enable":true},{"title":"已失效","index":3,"tag":"disable","enable":true}]
     */

    private int activeSize;
    private int normalSize;
    private String activeColor;
    private String normalColor;
    private int select;
    private int tabGravity;
    private List<Tabs> tabs;

    public int getActiveSize() {
        return activeSize;
    }

    public void setActiveSize(int activeSize) {
        this.activeSize = activeSize;
    }

    public int getNormalSize() {
        return normalSize;
    }

    public void setNormalSize(int normalSize) {
        this.normalSize = normalSize;
    }

    public String getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(String activeColor) {
        this.activeColor = activeColor;
    }

    public String getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(String normalColor) {
        this.normalColor = normalColor;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public int getTabGravity() {
        return tabGravity;
    }

    public void setTabGravity(int tabGravity) {
        this.tabGravity = tabGravity;
    }

    public List<Tabs> getTabs() {
        return tabs;
    }

    public void setTabs(List<Tabs> tabs) {
        this.tabs = tabs;
    }

    public static class Tabs {
        /**
         * title : 待发布
         * index : 0
         * tag : unpublished
         * enable : true
         */

        private String title;
        private int index;
        private String tag;
        private boolean enable;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }
}
