package com.survivor.have_a_try;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by sxf on 15-2-17.
 */

public class gpuNoise extends SimpleApplication {

    static int[] permutation;
    private Material mat;
    static float[][] grads = new float[][]{
            {1,1,0},
            {-1,1,0},
            {1,-1,0},
            {-1,-1,0},
            {1,0,1},
            {-1,0,1},
            {1,0,-1},
            {-1,0,-1},
            {0,1,1},
            {0,-1,1},
            {0,1,-1},
            {0,-1,-1},
            {1,1,0},
            {0,-1,1},
            {-1,1,0},
            {0,-1,-1}
    };
    public static void main(String[] args){
        gpuNoise app = new gpuNoise();
        app.setDisplayStatView(false);
        app.setDisplayFps(false);
        app.start();
    }
    public void simpleInitApp(){
        ByteBuffer data = BufferUtils.createByteBuffer((int) Math.ceil(Image.Format.RGBA16F.getBitsPerPixel() / 8.0) * 256 * 256);
        Image permImage = new Image();
        permImage.setWidth(256);
        permImage.setHeight(256);
        permImage.setFormat(Image.Format.RGBA16F);
        permImage.setData(data);

        ByteBuffer data2 = BufferUtils.createByteBuffer( (int)Math.ceil(Image.Format.RGBA16F.getBitsPerPixel() / 8.0) * 256 * 1);
        Image gradImage = new Image();
        gradImage.setWidth(256);
        gradImage.setHeight(1);
        gradImage.setFormat(Image.Format.RGBA16F);
        gradImage.setData(data2);

        long seed = new Random().nextLong();

        Random r = new Random(seed);

        // Compute permutation table
        permutation = new int[256];
        for(int i = 0; i<permutation.length; i++){
            permutation[i] = -1;
        }
        // Random Numbers 0-255
        for(int i = 0; i< permutation.length; i++){
            while(true){
                int per = Math.abs(r.nextInt()) % permutation.length;
                if(permutation[per] == -1){
                    permutation[per] = i;
                    break;
                }
            }
        }
        // fills permutation texture...generates 4 hash values here to save GPU processing power, originally this was a 1D table
        ImageRaster rP = ImageRaster.create(permImage);
        for(int x = 0; x < 256; x++){
            for(int y = 0; y < 256; y++){
                int A = perm2d(x) + y;
                int AA = perm2d(A);
                int AB = perm2d(A + 1);
                int B = perm2d(x + 1) + y;
                int BA = perm2d(B);
                int BB = perm2d(B + 1);
                ColorRGBA c = new ColorRGBA((float)AA/255f, (float)AB/255f, (float)BA/255f, (float)BB/255f);
                rP.setPixel(x, y, c);
            }
        }

        //Creates table of gradients
        //The gradients are rearranged based on the permutation table here instead of on the GPU
        ImageRaster rG = ImageRaster.create(gradImage);
        for(int x = 0; x<256; x++){
            for(int y = 0; y < 1; y++){
            }
        }

        Texture2D permutationTable = new Texture2D(permImage);
        permutationTable.setWrap(Texture.WrapMode.Repeat);
        permutationTable.setMagFilter(Texture.MagFilter.Nearest);
        permutationTable.setMinFilter(Texture.MinFilter.NearestNoMipMaps);

        Texture2D gradientTable = new Texture2D(gradImage);
        gradientTable.setWrap(Texture.WrapAxis.S, Texture.WrapMode.Repeat);
        gradientTable.setWrap(Texture.WrapAxis.T, Texture.WrapMode.Clamp);
        gradientTable.setMagFilter(Texture.MagFilter.Nearest);
        gradientTable.setMinFilter(Texture.MinFilter.NearestNoMipMaps);

        Quad q = new Quad(2,2);
        cam.resize(256, 256, true);
        mat = new Material(assetManager, "noiseMaterial.j3md");
        Geometry ge = new Geometry("Mesh",q);
        ge.setMaterial(mat);
        mat.setTexture("permutationTexture", permutationTable);
        mat.setTexture("gradientTexture", gradientTable);
        rootNode.attachChild(ge);
    }

    public int perm2d(int i){
        return permutation[i % 256];
    }

}
