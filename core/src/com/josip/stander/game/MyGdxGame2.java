package com.josip.stander.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.Label;
import java.util.Iterator;

import static com.badlogic.gdx.Input.Keys.G;
import static java.awt.SystemColor.text;

public class MyGdxGame2 extends ApplicationAdapter {

    BitmapFont yourBitmapFontName;
    private Texture dropimage;
    private Texture bucketimage;
    private Sound dropsound;
    private Music rainmusic;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private Rectangle bucket;
    private Array<Rectangle> raindrops;
    private long dropTime;
    private int score;
    private String yourScoreName;



    @Override
    public void create() {

        dropimage = new Texture(Gdx.files.internal("droplet.png"));
        bucketimage = new Texture(Gdx.files.internal("bucket.png"));
        dropsound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainmusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        

        rainmusic.setLooping(true);
        rainmusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        spriteBatch = new SpriteBatch();

        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        raindrops = new Array<Rectangle>();
        spawnRaindrops();//metoda

        score = 0;
        yourScoreName = "SCORE:  0";
        yourBitmapFontName = new BitmapFont();


    }


    private void spawnRaindrops() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        dropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);//floati za boju o.67 itd  public long getScore() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        yourBitmapFontName.setColor(1, 1, 1, 1);
        yourBitmapFontName.draw(spriteBatch, yourScoreName, 20, 450);
        spriteBatch.draw(bucketimage, bucket.x, bucket.y);


        for (Rectangle raindrop : raindrops) {
            spriteBatch.draw(dropimage, raindrop.x, raindrop.y);
        }
        spriteBatch.end();

        if (Gdx.input.isTouched())  //za android verziju igre
            bucket.x = Gdx.input.getX() - 64 / 2;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) //za desktop verziju igre na kompjuteru
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) //za desktop verziju igre na kompjuteru
            bucket.x += 200 * Gdx.graphics.getDeltaTime();

        if (bucket.x < 0) bucket.x = 0;//da ne ode s ekrana
        if (bucket.x > 800 - 64) bucket.x = 800 - 64;//da ne ode s ekrana

        if (TimeUtils.nanoTime() - dropTime > 1000000000)//sekunda u nano  kako pada kisa brzo
            spawnRaindrops();

        Iterator<Rectangle> iterator = raindrops.iterator();//da pada kisa pokreni
        while (iterator.hasNext()) {
            Rectangle raindrop = iterator.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) iterator.remove();
            if (raindrop.overlaps(bucket)) {
                score++;
                yourScoreName = "SCORE: " + score;
                dropsound.play();
                iterator.remove();

            }

        }

    }

    @Override
    public void dispose() {

        dropimage.dispose();
        bucketimage.dispose();
        rainmusic.dispose();
        dropsound.dispose();
        spriteBatch.dispose();

    }


}
