package edu.gcsc.vrl.luaparser;

public final class GeneralUtil {
    public GeneralUtil(){ throw new AssertionError(); }

    public static void disableWithAllChildNodes(ValueData v){
        v.setDisabled(true);
        v.setSelection(false);
        if(v.getOptions() != null){
            for(ValueData vd : v.getOptions()){
                if(vd.isOption()){
                    vd.setDisabled(true);
                    vd.setSelection(false);
                    if(vd.getOptions() != null){
                        for(ValueData ve : vd.getOptions()){
                            disableWithAllChildNodes(ve);
                        }
                    }
                }
            }
        }
    }

    public static void enableWithAllChildNodes(ValueData v){
        v.setDisabled(false);

        if(v.getOptions() != null){
            for(ValueData vd : v.getOptions()){
                if(vd.isOption()){
                    vd.setDisabled(false);
                    if(vd.getOptions() != null){
                        for(ValueData ve : vd.getOptions()){
                            enableWithAllChildNodes(ve);
                        }
                    }
                }
            }
        }
    }

    public static void selectAllParentNodes(ValueData v) {
        if (v.getParentNode() != v.getRootNode()) {
            if (v.getParentNode() != null) {
                if (v.getParentNode().isOption()) {
                    v.getParentNode().setSelection(true);
                    if (v.getParentNode().getParentNode() != null) {
                        selectAllParentNodes(v.getParentNode().getParentNode());
                    }
                } else {
                    selectAllParentNodes(v.getParentNode());
                }
            }
        }
    }
}
