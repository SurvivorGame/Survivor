package com.survivor.view;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;


/**
 * Created by sxf on 15-5-4.
 */
public class SkyView {

    private AssetManager assetManager;
    private Node rootNode;

    private Node sky;

    public SkyView(Node rootNode, AssetManager assetManager) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }

    public void initSky() {
        Texture back = assetManager.loadTexture("assets/images/blue-sky/bluesky_back.jpg");
        Texture front = assetManager.loadTexture("assets/images/blue-sky/bluesky_front.jpg");
        Texture up = assetManager.loadTexture("assets/images/blue-sky/bluesky_top.jpg");
        Texture left = assetManager.loadTexture("assets/images/blue-sky/bluesky_left.jpg");
        Texture right = assetManager.loadTexture("assets/images/blue-sky/bluesky_right.jpg");
        rootNode.attachChild(SkyFactory.createSky(
                assetManager, right, left, back, front, up, up));
    }
}
