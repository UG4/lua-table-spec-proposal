package edu.gcsc.vrl.luaparser;

public final class GenUtil {
    public GenUtil() {
        throw new AssertionError();
    }

    public static void disableWithAllChildNodes(ValueData v) {
        v.setDisabled(true);
        if (v.getOptions() != null) {
            for (ValueData vd : v.getOptions()) {
                if (vd.isOption()||vd.isOptValue()) {
                    vd.setDisabled(true);
                    if (vd.getOptions() != null) {
                        for (ValueData ve : vd.getOptions()) {
                            disableWithAllChildNodes(ve);
                        }
                    }
                } else {
                }
            }
        }
    }

    public static void enableWithAllChildNodes(ValueData v) {
        v.setDisabled(false);

        if (v.getOptions() != null) {
            for (ValueData vd : v.getOptions()) {
                if (vd.isOption()||vd.isOptValue()) {
                    vd.setDisabled(false);
                    if (vd.getOptions() != null) {
                        for (ValueData ve : vd.getOptions()) {
                            enableWithAllChildNodes(ve);
                        }
                    }
                }
            }
        }
    }

    public static void selectAllParentNodes(ValueData v) {
        if (v.getParentNode() != null) {
            if (v.getParentNode() != v.getRootNode()) {
                if (v.getParentNode().isOption()) {
                    v.getParentNode().setSelectedNew(true);
                    if (v.getParentNode().getParentNode() != null) {
                        selectAllParentNodes(v.getParentNode());
                    }
                } else {
                    selectAllParentNodes(v.getParentNode());
                }
            }
        }
    }

    public static boolean haveOptValue(ValueData v){
        if(!v.isOption()) {
            if (v.getOptions() != null) {
                for (ValueData vd : v.getOptions()) {
                    if(vd.isOptValue()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean haveOptValSelected(ValueData v){
        if(!v.isOption()) {
            if (v.getOptions() != null) {
                for (ValueData vd : v.getOptions()) {
                    if(vd.isOptValue() && vd.isSelected()){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
