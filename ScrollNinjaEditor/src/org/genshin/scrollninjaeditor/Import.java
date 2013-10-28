package org.genshin.scrollninjaeditor;

import java.io.File;



import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class Import extends ImageButton{
	private LayerManager  layermanager;
	private MapObjectManager mapObjManager;

	
	
	public Import(SpriteDrawable sd) {
		super(sd);
		
		layermanager = LayerManager.getInstance();
		mapObjManager = MapObjectManager.getInstance();
	
		this.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event ,float x,float y) {
				importFile();
			}
		});
		
		setSize(32, 32);
		
    }
	
	public void setlayer(LayerManager layer) {
		this.layermanager = layer;
	}
	
	private void importFile() {
		File current = new File("./bin/data");
		JFileChooser FileChooser = new JFileChooser(current.getAbsolutePath());
		//ファイル選択フィルター宣言
				ExtendsFileFilter filter[] = {
					new ExtendsFileFilter(".json","JSON  ファイル(*.json)"),
				};
				
				//フィルター設定
				for(int i = 0; i < filter.length ; i ++)
					FileChooser.addChoosableFileFilter(filter[i]);
				
				int res = FileChooser.showOpenDialog(FileChooser);

				if(res == JFileChooser.APPROVE_OPTION) {
					File file = FileChooser.getSelectedFile();
					//開いたファイルの種類のチェック
					for(int i = 0 ;i < filter.length ; i++)	{	
						//開いたファイルが正しい場合
						if(filter[i].accept(file)) {
							JsonRead read = new JsonRead(Gdx.files.absolute(file.getAbsolutePath()).path());
							
							for(int node = 0;read.getRootNode(node) != null;node++)	{
								MapObject setObj = null;
								
								//スプライトの種類チェック
								int layer = read.getObjectInt("layer", node);
								int layerNo = read.getObjectInt("layerNo", node);
								
								for(MapObject obj:mapObjManager.getMapObjectList()) {
									String label = read.getObjectString("label", node);
																	
									if(label.matches(obj.getLabelName())) {	
										setObj = new MapObject(obj);
										break;
									}
								}
								setObj.setPosition(read.getObjectFloat("x", node), read.getObjectFloat("y", node));
								if(layer == Layer.FRONT){
									if(layerNo <= layermanager.getFrontLayers().size()) {
										for(int index = 0;index <= layerNo;index++) {
											if(layermanager.getFrontLayer(index) == null)
												layermanager.addFront(index);
										}
										Gdx.app.log("node", "" + node);
										layermanager.getFrontLayer(layerNo).getMapObjects().add(setObj);
										
									}
								}
								else if(layer == Layer.BACK)
									if(layerNo <= layermanager.getBackLayers().size()) {
										for(int index = 0;index <= layerNo;index++) {
											if(layermanager.getBackLayer(index) == null)
												layermanager.addBack(index);
										}
										Gdx.app.log("node", "" + node);
										layermanager.getBackLayer(layerNo).getMapObjects().add(setObj);
									}
							}
						}
					}
				}
	}
	

}