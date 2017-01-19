package com.survivor.gui;

import com.plugin.framework.FrameWork;
import com.plugin.framework.Plugin;

/**
 * 
 * Created by sxf on 15-2-16.
 */
public class Main {
    public static final String GUI_PLUGIN = "GuiPlugin";
    public static FrameWork framework;
    public static GuiPlugin guiPlugin;
    public static void main(String[] args) {
        /** 初始化插件系统 */
        framework = new FrameWork();
        framework.getPluginInstances().put(GUI_PLUGIN,makeGuiPlugin());
        framework.StartAsyn("plugins/");
        guiPlugin.Main();
    }
    private static Plugin makeGuiPlugin() {
        guiPlugin = new GuiPlugin();
        Plugin p = new Plugin();
        p.setPlugin_class_name(GUI_PLUGIN);
        p.setPlugin_name(GUI_PLUGIN);
        p.setPlugin_version(1);
        p.setPlugin_interface(guiPlugin);
        return p;
    }

}
