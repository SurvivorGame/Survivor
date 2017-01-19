package com.survivor.gui;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.survivor.view.MapView;
import com.survivor.view.SkyView;

/**
 * Created by sxf on 15-5-4.
 */
public class GameContext implements IGameContext {
    private SkyView sky;
    private MapView map;

    private Node root;
    private Node gui;

    private AssetManager assetManager;


    private BulletAppState bulletAppState;
    private BetterCharacterControl playerControl;

    Texture mainTexture;
}
