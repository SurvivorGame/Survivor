package com.survivor.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.survivor.plugin_interface.MainWindowInterface;
import java.util.prefs.BackingStoreException;

/**
 * 这个类是用来显示主窗口并加载游戏逻辑的
 * Created by sxf on 15-1-27.
 */
public class MainWindow extends SimpleApplication implements MainWindowInterface, ActionListener {
    public static final String MATDEFS_UNSHADED = "Common/MatDefs/Misc/Unshaded.j3md";
    public static final String MATDEFS_LIGHTING = "Common/MatDefs/Light/Lighting.j3md";

    private BulletAppState bulletAppState;
    private BetterCharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    Node ground;
    Texture mainTexture;
    //Temporary vectors used on each frame.
    //They here to avoid instanciating new vectors on each frame
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private float airTime = 0;
    Node playerNode = new Node();
    private ChaseCamera chaseCam;


    public void Start(){
        /** 初始化App及应用窗口 */
        showSettings = true;
        AppSettings settings = new AppSettings(true);
        settings.setSettingsDialogImage("Interface/logo.jpg");
        try {
            settings.load("Survivor.prop");
        } catch (BackingStoreException e) {
            System.err.println("未能正确加载用户配置，载入默认配置");
        }
        setSettings(settings);
        start(); // start the game
    }

    @Override
    public void simpleInitApp() {

        setUpKeys();

        makeGround();
        makeSky();
        makeLight();

        setUpCollision();
        initCam();
        guiInit();
    }

    private void setUpCollision() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        ground.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().addAll(ground);
    }

    private void initCam() {
        flyCam.setEnabled(false);

        player = new BetterCharacterControl(0.5f, 1f, 1f);
        player.setWalkDirection(new Vector3f(0, 0, 0));
        player.setJumpForce(new Vector3f(0,5f,0));
        player.setGravity(new Vector3f(0,1f,0));
//        player.warp(new Vector3f(0.5f,1f,0.5f));
        bulletAppState.getPhysicsSpace().add(player);

        Box box = new Box(0.5f,1f,0.5f);
        Geometry playerModel = new Geometry("Box", box);
        Material mat = new Material(assetManager,
                MATDEFS_LIGHTING);
        mat.setColor("Specular",ColorRGBA.White);
        mat.setColor("Diffuse", ColorRGBA.White);
        playerModel.setMaterial(mat);
        playerNode.move(0,3.5f,0);
        playerNode.attachChild(playerModel);
        playerNode.addControl(player);
        bulletAppState.getPhysicsSpace().addAll(playerNode);

        chaseCam = new ChaseCamera(cam, playerNode, inputManager);

        rootNode.attachChild(playerNode);
    }

    private void makeSky() {
        Texture back = assetManager.loadTexture("Textures/blue-sky/bluesky_back.jpg");
        Texture front = assetManager.loadTexture("Textures/blue-sky/bluesky_front.jpg");
        Texture up = assetManager.loadTexture("Textures/blue-sky/bluesky_top.jpg");
        Texture left = assetManager.loadTexture("Textures/blue-sky/bluesky_left.jpg");
        Texture right = assetManager.loadTexture("Textures/blue-sky/bluesky_right.jpg");
        rootNode.attachChild(SkyFactory.createSky(
                assetManager,right, left, back,  front, up, up));
    }

    private void guiInit() {
        // Display a line of text with a default font
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText("Hello World");
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);
    }

    private void makeGround() {
        mainTexture = assetManager.loadTexture(
                "Textures/blocks/gravel.png");
        Material mat = new Material(assetManager,
                MATDEFS_LIGHTING);
        mat.setFloat("Shininess", 105f);
        mat.setColor("Specular",ColorRGBA.White);
        mat.setTexture("DiffuseMap", mainTexture);
        Box box = new Box(0.5f,0.5f,0.5f);
        ground = new Node();
        box.scaleTextureCoordinates(new Vector2f(0.05f,0.05f));


        for (int i = -25; i<= 25; ++i) {
            for (int j = -25; j<= 25; ++j) {
                Geometry red = new Geometry("Box", box);
                red.setMaterial(mat);
                red.setLocalTranslation(new Vector3f(i, -5, j));
                ground.attachChild(red);
            }
        }
        rootNode.attachChild(ground);
    }

    private void makeLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1,-2,-2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);
    }

    /** We over-write some navigational key mappings here, so we can
     * add physics-controlled walking and jumping: */
    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
    }

    /** These are our custom actions triggered by key presses.
     * We do not walk yet, we just keep track of the direction the user pressed. */
    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("Left")) {
            left = isPressed;
        } else if (binding.equals("Right")) {
            right= isPressed;
        } else if (binding.equals("Up")) {
            up = isPressed;
        } else if (binding.equals("Down")) {
            down = isPressed;
        } else if (binding.equals("Jump")) {
            if (isPressed) { player.jump(); }
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left)  walkDirection.addLocal(camLeft);
        if (right) walkDirection.addLocal(camLeft.negate());
        if (up) walkDirection.addLocal(camDir);
        if (down) walkDirection.addLocal(camDir.negate());

        if (!player.isOnGround()) { // use !character.isOnGround() if the character is a BetterCharacterControl type.
            airTime += tpf;
        } else {
            airTime = 0;
        }
        walkDirection.multLocal(1000f).multLocal(tpf);
        player.setWalkDirection(walkDirection);
        cam.setLocation(playerNode.getWorldTranslation());
    }

}
