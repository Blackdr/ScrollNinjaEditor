package org.genshin.scrollninjaeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class ScrollPaneStage{
	private int loopCnt = 0;							// ループカウンタ用
	private Skin skin;
	private ScrollPane scrollPane;
	private Table scrollTable;
	private MapObject mapobj;
	private Table table = new Table();
	
	/**
	 * Constructor
	 */
	public ScrollPaneStage(){
	}
	
	/**
	 * Create
	 * @param stage
	 * @param manager
	 * @param table
	 * @param spriteDrawble
	 * @param camera
	 */
	public void create(Stage stage,final MapObjectManager manager,SpriteDrawable spriteDrawble,final OrthographicCamera camera){
		for (loopCnt = 0 ; loopCnt < manager.getMapObjectList().size() ; loopCnt ++){
			if(loopCnt % 3 == 0)
				table.row();
			manager.getMapObjectList().get(loopCnt).getSp().setSize(64,64);
			spriteDrawble = new SpriteDrawable();												// 上書きが必要
			spriteDrawble.setSprite(manager.getMapObjectList().get(loopCnt).getSp());		// 上書きではないので注意
			final ObjectButton objB = new ObjectButton(spriteDrawble, manager.getMapObjectList().get(loopCnt));
			
			objB.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event ,float x,float y){
					mapobj = new MapObject(objB.mapObject);										 // クリックされたオブジェクト情報を読み込み
					mapobj.setPosition(camera.position.x,camera.position.y);
					manager.setFrontObject(mapobj);															 // オブジェクトをセット
				}
			});
			table.add(objB);
		}
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));	// スキンファイルを読み込み
		scrollPane = new ScrollPane(table,skin);							// スクロールに情報を読み込み
		scrollPane.setFlickScroll(false);									// フリックの有無
		scrollPane.setFadeScrollBars(true);								// ここでfalseなら常に。trueなら使用するとき。
		scrollPane.setScrollingDisabled(false, false);					// 一番目は縦、二番目は横。これによりスクロールをするかしないか
		scrollTable = new Table();
		scrollTable.debug();
		scrollTable.setLayoutEnabled(false);
		scrollPane.setWidth(200);
		scrollPane.setHeight(Gdx.graphics.getHeight());
		scrollTable.setX(Gdx.graphics.getWidth() - scrollPane.getWidth());
		scrollTable.add(scrollPane);
		scrollPane.removeListener(scrollPane.getListeners().get(0));
	}
	
	/**
	 * Add process
	 */
	public void add(Stage stage){
		stage.addActor(scrollTable);
	}
	
	/**
	 * Remove process
	 */
	public void remove(Stage stage){
		stage.getRoot().removeActor(scrollTable);
	}

	/**
	 * getScrollPaneWidth
	 * @return scrollPane.getWidth()
	 */
	public float getScrollPaneWidth(){
		return scrollPane.getWidth();
	}


}