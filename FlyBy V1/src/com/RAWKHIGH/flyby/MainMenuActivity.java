package com.RAWKHIGH.flyby;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.animator.SlideMenuAnimator;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainMenuActivity extends BaseGameActivity implements
		IOnMenuItemClickListener {

	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 480;

	protected static final int MENU_ABOUT = 0;
	protected static final int MENU_QUIT = MENU_ABOUT + 1;
	protected static final int MENU_PLAY = 100;
	protected static final int MENU_SCORES = MENU_PLAY + 1;
	protected static final int MENU_OPTIONS = MENU_SCORES + 1;

	protected Camera mCamera;
	protected Scene mMainScene;
	protected Handler mHandler;
	private Texture mMenuBackTexture;
	private TextureRegion mMenuBackTextureRegion;
	protected MenuScene mStaticMenuScene, mPopUpMenuScene;
	private Texture mPopUpTexture;
	private Texture mFontTexture;
	private Font mFont;
	protected TextureRegion mPopUpAboutTextureRegion;
	protected TextureRegion mPopUpQuitTextureRegion;
	protected TextureRegion mMenuPlayTextureRegion;
	protected TextureRegion mMenuScoresTextureRegion;
	protected TextureRegion mMenuOptionsTextureRegion;
	protected TextureRegion mMenuHelpTextureRegion;
	private boolean popupDisplayed;
	private Music myMusic;

	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mCamera).setNeedsSound(true));

	} // End onLoadEngine

	public void onLoadResources() {
		this.mFontTexture = new Texture(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.mFontTexture, this,
				"zolofse.ttf", 32, true, Color.WHITE);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);

		this.mMenuBackTexture = new Texture(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuBackTextureRegion = TextureRegionFactory.createFromAsset(
				this.mMenuBackTexture, this, "gfx/Menu/MainMenu.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mMenuBackTexture);

		this.mPopUpTexture = new Texture(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPopUpAboutTextureRegion = TextureRegionFactory.createFromAsset(
				this.mPopUpTexture, this, "gfx/Menu/About_button.png", 0, 0);
		this.mPopUpQuitTextureRegion = TextureRegionFactory.createFromAsset(
				this.mPopUpTexture, this, "gfx/Menu/Quit_button.png", 0, 50);
		this.mEngine.getTextureManager().loadTexture(this.mPopUpTexture);
		popupDisplayed = false;

		MusicFactory.setAssetBasePath("mfx/");
		try {
			this.myMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "menu.ogg");
			this.myMusic.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}
	
	} // End onLoadResources

	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.createStaticMenuScene();
		this.createPopUpMenuScene();

		final int centerX = (CAMERA_WIDTH - this.mMenuBackTextureRegion
				.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mMenuBackTextureRegion
				.getHeight()) / 2;

		this.mMainScene = new Scene(1);
		final Sprite menuBack = new Sprite(centerX, centerY,
				this.mMenuBackTextureRegion);
		mMainScene.getLastChild().attachChild(menuBack);
		mMainScene.setChildScene(mStaticMenuScene);
		


		return this.mMainScene;
	} // End onLoadScene

	public void onLoadComplete() {
		try {
			this.myMusic.play();
		} catch (IllegalStateException ex) {
		}
	} // End onLoadComplete

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pKeyCode == KeyEvent.KEYCODE_MENU
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (popupDisplayed) {

				this.mPopUpMenuScene.back();
				mMainScene.setChildScene(mStaticMenuScene);
				popupDisplayed = false;
			} else {

				this.mMainScene.setChildScene(this.mPopUpMenuScene, false,
						true, true);
				popupDisplayed = true;
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	} // End onKeyDown

	public boolean onMenuItemClicked(final MenuScene pMenuScene,
			final IMenuItem pMenuItem, final float pMenuItemLocalX,
			final float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_ABOUT:
			Toast.makeText(MainMenuActivity.this, "About selected",
					Toast.LENGTH_SHORT).show();
			return true;
		case MENU_QUIT:
			this.finish();
			return true;
		case MENU_PLAY:
			mHandler.postDelayed(mLaunchGameTask, 1000);
		case MENU_SCORES:
			mHandler.postDelayed(mLaunchScoreTask, 1000);
		case MENU_OPTIONS:
			Toast.makeText(MainMenuActivity.this, "Options selected",
					Toast.LENGTH_SHORT).show();
			return true;
		default:
			return false;
		}
	} // End onMenuItemClicked

	protected void createStaticMenuScene() {
		this.mStaticMenuScene = new MenuScene(this.mCamera);
		final IMenuItem playMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_PLAY, mFont, "Play Game"), 0.5f, 0.5f,
				0.5f, 1.0f, 0.0f, 0.0f);
		playMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(playMenuItem);
		final IMenuItem scoresMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_SCORES, mFont, "Scores"), 0.5f, 0.5f,
				0.5f, 1.0f, 0.0f, 0.0f);
		scoresMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(scoresMenuItem);
		final IMenuItem optionsMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_OPTIONS, mFont, "Options"), 0.5f, 0.5f,
				0.5f, 1.0f, 0.0f, 0.0f);
		optionsMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(optionsMenuItem);
		this.mStaticMenuScene.buildAnimations();
		this.mStaticMenuScene.setBackgroundEnabled(false);
		this.mStaticMenuScene.setOnMenuItemClickListener(this);
	} // End createStaticMenuScene

	protected void createPopUpMenuScene() {
		this.mPopUpMenuScene = new MenuScene(this.mCamera);
		final SpriteMenuItem aboutMenuItem = new SpriteMenuItem(MENU_ABOUT,
				this.mPopUpAboutTextureRegion);
		aboutMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mPopUpMenuScene.addMenuItem(aboutMenuItem);
		final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT,
				this.mPopUpQuitTextureRegion);
		quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mPopUpMenuScene.addMenuItem(quitMenuItem);
		this.mPopUpMenuScene.setMenuAnimator(new SlideMenuAnimator());
		this.mPopUpMenuScene.buildAnimations();
		this.mPopUpMenuScene.setBackgroundEnabled(false);
		this.mPopUpMenuScene.setOnMenuItemClickListener(this);
	} // End createPopUpMenuScene

	private Runnable mLaunchGameTask = new Runnable() {
		public void run() {
			Intent myIntent = new Intent(MainMenuActivity.this,
					GameActivity.class);
			MainMenuActivity.this.startActivity(myIntent);
		}
	}; // End mLaunchGameTask

	private Runnable mLaunchScoreTask = new Runnable() {
		public void run() {
			Intent myIntent = new Intent(MainMenuActivity.this,
					SQLiteScore.class);
			MainMenuActivity.this.startActivity(myIntent);
		}
	}; // End mLaunchScoreTask

} // End MainMenuActivity