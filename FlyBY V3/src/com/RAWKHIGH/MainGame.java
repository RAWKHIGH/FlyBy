
package com.RAWKHIGH;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import java.util.Random;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class MainGame extends BaseGameActivity implements IOnSceneTouchListener {   
	public static MainGame main;	
    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 800;    
    private static Camera _camera;
	private TextureRegion playerTextureRegion;
	private TextureRegion enemyTextureRegion;	
	private Sprite playerSprite;
	private ArrayList<Sprite> enemyArray = new ArrayList<Sprite>();	
	private int _score = 0;
	
    public Engine onLoadEngine() {
    	main = this;
    	MainGame._camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);        
        Engine _engine = new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),MainGame._camera));
        return _engine;
    }
    

    public void onLoadResources() {		
    	Texture texture = new Texture(256, 128);    	
    	playerTextureRegion   = TextureRegionFactory.createFromAsset(texture, this, "gfx/Game/Player.png", 0, 0);
    	enemyTextureRegion = TextureRegionFactory.createFromAsset(texture, this, "gfx/Game/Obstical.png", 125, 0);    	
    	this.mEngine.getTextureManager().loadTexture(texture);
    	this.onLoadSprites();
    }

    public void onLoadSprites() {    	
    	playerSprite = new Sprite(getScreenCenterX(playerTextureRegion.getWidth()), CAMERA_HEIGHT - playerTextureRegion.getHeight(), playerTextureRegion);
    }


	@Override
    public Scene onLoadScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());
        final Scene scene = new Scene(1);               
        scene.getTopLayer().addEntity(playerSprite);        
        scene.registerUpdateHandler(new IUpdateHandler() {
        	private long lastRaindropAdd = 0;
			@Override
        	public void onUpdate(final float pSecondsElapsed) {        		
				upDateEnemyPoistion(scene);
        		if (System.currentTimeMillis() - lastRaindropAdd  > 3000) {
        			lastRaindropAdd = System.currentTimeMillis();        			
        			addEnemy(scene);
        		}
            }

			@Override
			public void reset() {

			}
        });
        
        scene.setOnSceneTouchListener(this);        
        return scene;
    }

	private void upDateEnemyPoistion(Scene scene){
		int size = enemyArray.size();
		for (int i = 0; i < size; i++) 
		{
			Sprite raindrop = enemyArray.get(i);        			
			raindrop.setPosition(raindrop.getX(), raindrop.getY() + 5); 		
			if (raindrop.collidesWith(playerSprite)) {				
				this.finish();
				synchronized(raindrop) {
					size--;
					enemyArray.remove(i);
					scene.getTopLayer().removeEntity(raindrop);
				}
			}
			
			else if (raindrop.getY() > CAMERA_HEIGHT) {			
				_score++;
				System.out.println("Score: " + _score);
				synchronized(raindrop) {
					size--;
					enemyArray.remove(i);
					scene.getTopLayer().removeEntity(raindrop);
				}
			}
		}
	}
	

	public void addEnemy(Scene scene){
		 int START = 1;
		 int END = 300;
		 Random random = new Random();        			    
		 int randomInt = showRandomInteger(START, END, random);
		Sprite raindrop = getMango(randomInt, 0);
		enemyArray.add(raindrop);
		scene.getTopLayer().addEntity(raindrop);
	}
	
	
	public int  showRandomInteger(int aStart, int aEnd, Random aRandom){
	    if ( aStart > aEnd ) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }

	    long range = (long)aEnd - (long)aStart + 1;

	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);    
	     return randomNumber;
	  }

	
	public Sprite getMango(float x, float y) {
		return (new Sprite(x, y, enemyTextureRegion.clone()));
	}
	
	@Override
	public void onLoadComplete() {
		
	}
      
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		this.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				float touchX = pSceneTouchEvent.getX();
				playerSprite.setPosition(touchX - playerSprite.getWidth()/2, playerSprite.getY());
			}
		});
		return true;
	}

	public float getScreenCenterX(float width) {
		return (CAMERA_WIDTH / 2) - width / 2;
	}

	public float getScreenCenterY(float height) {
		return (CAMERA_HEIGHT / 2) - height / 2;
	}
}