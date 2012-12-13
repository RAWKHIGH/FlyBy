package com.RAWKHIGH.flyby;

import java.util.Arrays;
import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.MoveYModifier;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.ease.EaseQuadOut;
import android.os.Handler;
import android.widget.Toast;

public class GameActivity extends BaseGameActivity{
	
	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 480;
    
	private Handler mHandler;
	private Texture mScrumTexture;
	protected Camera mCamera;
	protected Scene mMainScene;
	private Texture mGameBackTexture;
	private TextureRegion mGameBackTextureRegion;
	private TextureRegion mPlayerTextureRegion;
	private TextureRegion mLeftTextureRegion;
	private TextureRegion mRightTextureRegion;
	private TiledTextureRegion mScrumTextureRegion;

	private AnimatedSprite[] asprEnemy = new AnimatedSprite[10];
	private int nEnemy;
	Random gen;


	public Engine onLoadEngine() {
		mHandler = new Handler();
		gen = new Random();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
	} // End onLoadEngine


	public void onLoadResources() {
		TextureRegionFactory.setAssetBasePath("gfx/Game/");
		mGameBackTexture = new Texture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mGameBackTextureRegion = TextureRegionFactory.createFromAsset(this.mGameBackTexture, this, "Gamebk.png", 0, 0);
		mEngine.getTextureManager().loadTexture(this.mGameBackTexture);
		mScrumTexture = new Texture(512, 256, TextureOptions.DEFAULT);
		mScrumTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mScrumTexture, this, "Obstical.png", 0, 0, 8, 4);
		mEngine.getTextureManager().loadTexture(this.mScrumTexture);		
	} // End onLoadResources
	

	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		final Scene scene = new Scene(1);
		final int centerX = (CAMERA_WIDTH - mGameBackTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - mGameBackTextureRegion.getHeight()) / 2;

		final Sprite background = new Sprite(centerX, centerY, mGameBackTextureRegion);
		scene.getLastChild().attachChild(background);
		
		final Sprite Player = new Sprite(20.0f, CAMERA_HEIGHT - 40.0f, mPlayerTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pAreaTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					Toast.makeText(GameActivity.this, "Sprite touch DOWN", Toast.LENGTH_SHORT).show();
					break;
				case TouchEvent.ACTION_UP:
					Toast.makeText(GameActivity.this, "Sprite touch UP", Toast.LENGTH_SHORT).show();
					break;
				case TouchEvent.ACTION_MOVE:
					this.setPosition(pAreaTouchEvent.getX() - this.getWidth() / 2, pAreaTouchEvent.getY() - this.getHeight() / 2);
					break;
				}
				return true;
			}
		};
		Player.registerEntityModifier(				
				new SequenceEntityModifier(
						new ParallelEntityModifier(
								new MoveYModifier(3, 0.0f, CAMERA_HEIGHT - 40.0f, EaseQuadOut.getInstance() ),
								new AlphaModifier(3, 0.0f, 1.0f),
								new ScaleModifier(3, 0.5f, 1.0f)
						),
				new RotationModifier(3, 0, 360)
				)
		);
		
		final Sprite Left = new Sprite(20.0f, CAMERA_HEIGHT - 40.0f, mLeftTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pAreaTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					Toast.makeText(GameActivity.this, "Sprite touch DOWN", Toast.LENGTH_SHORT).show();
					break;
				case TouchEvent.ACTION_UP:
					Toast.makeText(GameActivity.this, "Sprite touch UP", Toast.LENGTH_SHORT).show();
					break;
				case TouchEvent.ACTION_MOVE:
					this.setPosition(pAreaTouchEvent.getX() - this.getWidth() / 2, pAreaTouchEvent.getY() - this.getHeight() / 2);
					break;
				}
				return true;
			}
		};
		Player.registerEntityModifier(				
				new SequenceEntityModifier(
						new ParallelEntityModifier(
								new MoveYModifier(3, 0.0f, CAMERA_HEIGHT - 40.0f, EaseQuadOut.getInstance() ),
								new AlphaModifier(3, 0.0f, 1.0f),
								new ScaleModifier(3, 0.5f, 1.0f)
						),
				new RotationModifier(3, 0, 360)
				)
		);
		
		final Sprite Right = new Sprite(20.0f, CAMERA_HEIGHT - 40.0f, mRightTextureRegion){
			@Override
			public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch(pAreaTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					Toast.makeText(GameActivity.this, "Sprite touch DOWN", Toast.LENGTH_SHORT).show();
					break;
				case TouchEvent.ACTION_UP:
					Toast.makeText(GameActivity.this, "Sprite touch UP", Toast.LENGTH_SHORT).show();
					break;
				case TouchEvent.ACTION_MOVE:
					this.setPosition(pAreaTouchEvent.getX() - this.getWidth() / 2, pAreaTouchEvent.getY() - this.getHeight() / 2);
					break;
				}
				return true;
			}
		};
		Player.registerEntityModifier(				
				new SequenceEntityModifier(
						new ParallelEntityModifier(
								new MoveYModifier(3, 0.0f, CAMERA_HEIGHT - 40.0f, EaseQuadOut.getInstance() ),
								new AlphaModifier(3, 0.0f, 1.0f),
								new ScaleModifier(3, 0.5f, 1.0f)
						),
				new RotationModifier(3, 0, 360)
				)
		);
		
		scene.registerTouchArea(Player);
		scene.registerTouchArea(Left);
		scene.registerTouchArea(Right);
		scene.setTouchAreaBindingEnabled(true);
		scene.getLastChild().attachChild(Player);

		scene.registerEntityModifier(new AlphaModifier(10, 0.0f, 1.0f));
		
       	nEnemy = 0;
		mHandler.postDelayed(mStartEnemy,5000);
		return scene;	
	} // End onLoadScene

	public void onLoadComplete() {
	} // End onLoadComplete
	
    private Runnable mStartEnemy = new Runnable() {
        public void run() {
        	int i = nEnemy++;
        	Scene scene = GameActivity.this.mEngine.getScene();
           	float startY = gen.nextFloat()*(CAMERA_HEIGHT - 50.0f);
           	asprEnemy[i] = new AnimatedSprite(CAMERA_WIDTH - 30.0f, startY, mScrumTextureRegion.clone());
        	final long[] frameDurations = new long[26];
        	Arrays.fill(frameDurations, 500);
            asprEnemy[i].animate(frameDurations, 0, 25, true);
           	asprEnemy[i].registerEntityModifier(
           			new SequenceEntityModifier (
           						new AlphaModifier(5.0f, 0.0f, 1.0f),
          						new MoveModifier(60.0f, asprEnemy[i].getX(), 30.0f, 
         							asprEnemy[i].getY(), (float)CAMERA_HEIGHT/2)));
           	scene.getLastChild().attachChild(asprEnemy[i]);
        	if (nEnemy < 10){
        		mHandler.postDelayed(mStartEnemy,5000);
        	}
        }
     }; // End mStartEnemy
} // End GameActivity