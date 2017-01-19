package com.survivor.gui;

import com.plugin.framework.Plugin;
import com.plugin.framework.PluginInterface;

/**
 * Created by sxf on 15-2-16.
 */
public class GuiPlugin implements PluginInterface {
    MainWindow mainWindow = new MainWindow();
    /**
     * 将所有你需要的依赖项注入进来
     *
     * @param plugins 所依赖的插件的列表
     */
    @Override
    public void setDI(Plugin[] plugins) {

    }

    /**
     * 获取插件的接口对象，需要强制转换成插件对应的接口
     *
     * @return 接口类对象的名字
     */
    @Override
    public String[] getInterfaces() {
        return new String[0];
    }

    /**
     * 接口构造器,用于构造新的接口对象,也同事用于获取插件持有的实例化对象使用
     *
     * @param class_name 实例化类名
     * @param args       参数表
     * @return 接口的实例化对象
     */
    @Override
    public Object Factory(String class_name, Object... args) {
        if ("MainWindow".equals(class_name)) {
            return mainWindow;
        }
        return null;
    }

    /**
     * 判断当前插件是否时拥有独立线程
     *
     * @return 拥有独立线程的插件返回true，否则返回false
     */
    @Override
    public boolean isIndependent() {
        return false;
    }

    /**
     * 插件的初始化方法，会在整个插件系统初始化时被调用
     *
     * @return 错误码，正常为0
     */
    @Override
    public int Initialize() {

        return 0;
    }

    /**
     * 插件的主方法，拥有独立线程的插件才会被调用
     *
     * @return 错误码，正常为0
     */
    @Override
    public int Main() {
        mainWindow.Start();
        return 0;
    }
}
