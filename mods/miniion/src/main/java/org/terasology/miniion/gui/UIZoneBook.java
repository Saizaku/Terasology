/*
 * Copyright 2012 Benjamin Glatzel <benjamin.glatzel@me.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.miniion.gui;

import java.util.Set;

import javax.vecmath.Vector2f;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.terasology.asset.Assets;
import org.terasology.entitySystem.EntityManager;
import org.terasology.entitySystem.EntityRef;
import org.terasology.game.CoreRegistry;
import org.terasology.math.Vector3i;
import org.terasology.miniion.components.ZoneSelectionComponent;
import org.terasology.miniion.componentsystem.controllers.MinionSystem;
import org.terasology.miniion.gui.UIModButton.ButtonType;
import org.terasology.miniion.minionenum.ZoneType;
import org.terasology.miniion.utilities.Zone;
import org.terasology.rendering.gui.framework.UIDisplayElement;
import org.terasology.rendering.gui.framework.events.ClickListener;
import org.terasology.rendering.gui.widgets.*;
import org.terasology.rendering.world.BlockGrid.GridPosition;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.family.BlockFamily;

public class UIZoneBook extends UIWindow {
	/*
	 * @In private LocalPlayer localPlayer;
	 * 
	 * @In private EntityManager entityManager;
	 */
	private final UIImage background;
	private final UILabel lblzonename, lblheight, lbldepth, lblwidth, lblzonetype;
	private final UIText txtzonename, txtheight, txtdepth, txtwidth;
	private final UIComboBox cmbType;
	private UIList uizonelistgroup, uizonelist;
	private UIModButton btnSave, btnDelete, btnBack;
	private EntityRef zoneselection;
	private boolean newzonefound;
	
	private ClickListener zonelistener = new ClickListener() {		
		@Override
		public void click(UIDisplayElement element, int button) {			
			UIListItem listitem = (UIListItem) element;
			if(listitem.getValue().getClass().equals(ZoneType.class)){
				switch(((ZoneType)listitem.getValue())){
				case Gather: {
					uizonelist.removeAll();
					for (Zone zone : MinionSystem.getGatherZoneList()) {
						UIListItem newlistitem = new UIListItem(zone.Name, zone);
						newlistitem.setTextColor(Color.black);
						newlistitem.addClickListener(zonelistener);
						uizonelist.addItem(newlistitem);
					}
					uizonelistgroup.setVisible(false);
					uizonelist.setVisible(true);
					btnBack.setVisible(true);
					break;
				}
				case Terraform: {
					uizonelist.removeAll();
					for (Zone zone : MinionSystem.getTerraformZoneList()) {
						UIListItem newlistitem = new UIListItem(zone.Name, zone);
						newlistitem.setTextColor(Color.black);
						newlistitem.addClickListener(zonelistener);
						uizonelist.addItem(newlistitem);
					}
					uizonelistgroup.setVisible(false);
					uizonelist.setVisible(true);
					btnBack.setVisible(true);
					break;
				}
				case Work : {
					uizonelist.removeAll();
					for (Zone zone : MinionSystem.getWorkZoneList()) {
						UIListItem newlistitem = new UIListItem(zone.Name, zone);
						newlistitem.setTextColor(Color.black);
						newlistitem.addClickListener(zonelistener);
						uizonelist.addItem(newlistitem);
					}
					uizonelistgroup.setVisible(false);
					uizonelist.setVisible(true);
					btnBack.setVisible(true);
					break;
				}
				default : {					
					break;
				}
				}
			}
			else{
				if(cmbType.isVisible()){
					cmbType.setVisible(false);
				}
				Zone zone = (Zone)listitem.getValue();
				txtzonename.setText(zone.Name);
				txtheight.setText("" + zone.zoneheight);
				txtwidth.setText("" + zone.zonewidth);
				txtdepth.setText("" + zone.zonedepth);
				switch(zone.zonetype){
					case Gather: {
						lblzonetype.setText("Zonetype : Gather");
						break;
					}
					case Terraform: {
						lblzonetype.setText("Zonetype : Terraform");
						break;
					}
					case Work : {
						lblzonetype.setText("Zonetype : Work");
						break;
					}
					default : {
						lblzonetype.setText("label wasn't set");
						break;
					}
				}
				
				btnSave.setVisible(false);
				btnDelete.setVisible(true);
			}
		}
	};
	
	public UIZoneBook() {

		setId("zonebook");
		setModal(true);
		maximize();
		setCloseKeys(new int[] { Keyboard.KEY_ESCAPE });

		background = new UIImage();
		background.setTexture(Assets.getTexture("miniion:openbook"));
		background.setHorizontalAlign(EHorizontalAlign.CENTER);
		background.setVerticalAlign(EVerticalAlign.CENTER);
		background.setSize(new Vector2f(500, 300));
		background.setVisible(true);
		addDisplayElement(background);
		
		uizonelist = new UIList();
		uizonelist.setSize(new Vector2f(200, 220));
		uizonelist.setPosition(new Vector2f(40, 20));
		uizonelist.setVisible(true);
		background.addDisplayElement(uizonelist);
		
		uizonelistgroup = new UIList();
		uizonelistgroup.setSize(new Vector2f(200, 250));
		uizonelistgroup.setPosition(new Vector2f(40, 20));
		uizonelistgroup.setVisible(true);
		background.addDisplayElement(uizonelistgroup);

		lblzonename = new UILabel("Zone name :");
		lblzonename.setPosition(new Vector2f(260, 20));
		lblzonename.setColor(Color.black);
		lblzonename.setVisible(true);
		background.addDisplayElement(lblzonename);

		txtzonename = new UIText();
		txtzonename.setPosition(new Vector2f(350, 20));
		txtzonename.setColor(Color.black);
		txtzonename.setSize(new Vector2f(80, 20));
		txtzonename.setVisible(true);
		background.addDisplayElement(txtzonename);

		lblheight = new UILabel("Height :");
		lblheight.setPosition(new Vector2f(260, 40));
		lblheight.setColor(Color.black);
		lblheight.setVisible(true);
		background.addDisplayElement(lblheight);

		txtheight = new UIText();
		txtheight.setPosition(new Vector2f(350, 40));
		txtheight.setColor(Color.black);
		txtheight.setSize(new Vector2f(80, 20));
		txtheight.setVisible(true);
		background.addDisplayElement(txtheight);

		lblwidth = new UILabel("Width :");
		lblwidth.setPosition(new Vector2f(260, 60));
		lblwidth.setColor(Color.black);
		lblwidth.setVisible(true);
		background.addDisplayElement(lblwidth);

		txtwidth = new UIText();
		txtwidth.setPosition(new Vector2f(350, 60));
		txtwidth.setColor(Color.black);
		txtwidth.setSize(new Vector2f(80, 20));
		txtwidth.setVisible(true);
		background.addDisplayElement(txtwidth);

		lbldepth = new UILabel("Depth :");
		lbldepth.setPosition(new Vector2f(260, 80));
		lbldepth.setColor(Color.black);
		lbldepth.setVisible(true);
		background.addDisplayElement(lbldepth);

		txtdepth = new UIText();
		txtdepth.setPosition(new Vector2f(350, 80));
		txtdepth.setColor(Color.black);
		txtdepth.setSize(new Vector2f(80, 20));
		txtdepth.setVisible(true);
		background.addDisplayElement(txtdepth);
		
		lblzonetype = new UILabel("");
		lblzonetype.setPosition(new Vector2f(260, 100));
		lblzonetype.setColor(Color.black);
		lblzonetype.setVisible(true);
		background.addDisplayElement(lblzonetype);
		
		cmbType = new UIComboBox(new Vector2f(80, 20), new Vector2f(80,200));
		cmbType.setPosition(new Vector2f(350, 100));
		cmbType.setVisible(false);		
		background.addDisplayElement(cmbType);
		initTypes();
		
		btnSave = new UIModButton(new Vector2f(50, 20), ButtonType.NORMAL);
		btnSave.setPosition(new Vector2f(260, 230));
		btnSave.setLabel("Save");
		btnSave.setId("btnSave");
		btnSave.setVisible(true);
		btnSave.addClickListener(new ClickListener() {
			@Override
			public void click(UIDisplayElement element, int button) {
				executeClick(element, button);
			}
		});
		background.addDisplayElement(btnSave);
		
		btnDelete = new UIModButton(new Vector2f(50, 20), ButtonType.NORMAL);
		btnDelete.setPosition(new Vector2f(260, 230));
		btnDelete.setLabel("Delete");
		btnDelete.setId("btnDelZone");
		btnDelete.setVisible(false);
		btnDelete.addClickListener(new ClickListener() {
			@Override
			public void click(UIDisplayElement element, int button) {
				executeDelClick(element, button, (Zone)uizonelist.getSelection().getValue());
			}
		});
		background.addDisplayElement(btnDelete);
		
		btnBack  = new UIModButton(new Vector2f(50, 20), ButtonType.NORMAL);
		btnBack.setPosition(new Vector2f(40, 240));
		btnBack.setLabel("Back");
		btnBack.setId("btnBack");
		btnBack.setVisible(false);
		btnBack.addClickListener(new ClickListener() {
			@Override
			public void click(UIDisplayElement element, int button) {
				initList();
				btnBack.setVisible(false);
			}
		});
		background.addDisplayElement(btnBack);

	}

	private void executeClick(UIDisplayElement element, int id) {
		if( (!cmbType.isVisible()) && zoneselection.getComponent(ZoneSelectionComponent.class).zonetype == null){
			newzonefound = false;
			this.close();
		}
		if(cmbType.isVisible() && cmbType.getSelection() == null){
			return;
		}
		UIModButton clickedbutton = (UIModButton) element;
		if (txtzonename.getText().length() < 2) {
			return;
		}		
		int tmp;
		for (Zone zone : MinionSystem.getGatherZoneList()) {
			if (zone.Name.matches(txtzonename.getText())) {
				return;
			}
		}
		try {
			tmp = Integer.parseInt(txtheight.getText());
			tmp = Integer.parseInt(txtwidth.getText());
			tmp = Integer.parseInt(txtdepth.getText());
		} catch (NumberFormatException e) {
			return;
		}
		ZoneSelectionComponent zoneselectioncomp = zoneselection
				.getComponent(ZoneSelectionComponent.class);
		Zone newzone = new Zone(zoneselectioncomp.startpos,
				zoneselectioncomp.endpos);
		newzone.Name = txtzonename.getText();
		newzone.zoneheight = Integer.parseInt(txtheight.getText());
		newzone.zonewidth = Integer.parseInt(txtwidth.getText());
		newzone.zonedepth = Integer.parseInt(txtdepth.getText());
		if(cmbType.isVisible()){
			newzone.zonetype = ZoneType.valueOf(cmbType.getSelection().getText());
		}else{
			newzone.zonetype = zoneselection.getComponent(ZoneSelectionComponent.class).zonetype;
		}
		MinionSystem.addZone(newzone);
		newzonefound = false;
		lblzonetype.setText("ZoneType : Gatherzone");
		this.close();
	}
	
	private void executeDelClick(UIDisplayElement element, int id, Zone deletezone) {
		switch(deletezone.zonetype){
			case Gather : {
				MinionSystem.getGatherZoneList().remove(deletezone);
				break;
			}
			case Work : {
				MinionSystem.getWorkZoneList().remove(deletezone);
				break;
			}
		}		
		fillUI();
	}

	@Override
	public void open() {
		super.open();
		fillUI();
	}
	
	private void fillUI(){
		initList();
		//clear the textbowes
		txtzonename.setText("");
		txtheight.setText("");
		txtwidth.setText("");
		txtdepth.setText("");
		lblzonetype.setText("");
		newzonefound = false;
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		// hopefully people won't run around with 50 zoning tools, I take the
		// first one that has data in it
		EntityManager entityManager = CoreRegistry.get(EntityManager.class);
		for (EntityRef entity : entityManager.iteratorEntities(ZoneSelectionComponent.class)) {
			ZoneSelectionComponent selection = entity.getComponent(ZoneSelectionComponent.class);
			if (selection.blockGrid != null	&& selection.blockGrid.getGridPositions().size() == 1) {
				Set<GridPosition> gridpositions =  selection.blockGrid.getGridPositions();
				for(GridPosition gridpos : gridpositions){
					WorldProvider worldprovider = CoreRegistry.get(WorldProvider.class);
					Block block = worldprovider.getBlock(gridpos.position);
					if(block.getURI().getFamily().matches("minionbench")){
						newzonefound = true;
						selection.zonetype = ZoneType.Work;
						entity.saveComponent(selection);
						zoneselection = entity;
						Vector3i minbounds = selection.blockGrid.getMinBounds();
						Vector3i maxbounds = selection.blockGrid.getMaxBounds();
						if (MinionSystem.getWorkZoneList() == null) {
							txtzonename.setText("Workzone0");
						} else {
							txtzonename.setText("Workzone" + MinionSystem.getWorkZoneList().size());
						}
						lblzonetype.setText("ZoneType : Workzone");
						txtwidth.setText("1");
						txtdepth.setText("1");
						txtheight.setText("1");
					}
				}
				
			}else
			if (selection.blockGrid != null
					&& selection.blockGrid.getGridPositions().size() > 1) {
				newzonefound = true;
				selection.zonetype = ZoneType.Gather;
				entity.saveComponent(selection);
				zoneselection = entity;
				Vector3i minbounds = selection.blockGrid.getMinBounds();
				Vector3i maxbounds = selection.blockGrid.getMaxBounds();
				if (MinionSystem.getGatherZoneList() == null) {
					txtzonename.setText("Zone0");
				} else {
					txtzonename.setText("Zone"
							+ MinionSystem.getGatherZoneList().size());
				}
				lblzonetype.setText("ZoneType :");
				cmbType.setVisible(true);
				txtwidth.setText(""
						+ (getAbsoluteDiff(maxbounds.x, minbounds.x)));
				txtdepth.setText(""
						+ (getAbsoluteDiff(maxbounds.z, minbounds.z)));
				txtheight.setText(""
						+ (getAbsoluteDiff(maxbounds.y, minbounds.y)));
			}
			if (newzonefound) {
				btnSave.setVisible(true);
				btnDelete.setVisible(false);
				break;
			}
		}
	}
	
	private void initList(){
		//clear and init the list
		uizonelistgroup.setVisible(true);
		uizonelist.setVisible(false);
		uizonelistgroup.removeAll();
		for (ZoneType zonetype : ZoneType.values()) {
			UIListItem listitem = new UIListItem(zonetype.toString(), zonetype);
			listitem.setTextColor(Color.black);
			listitem.addClickListener(zonelistener);
			uizonelistgroup.addItem(listitem);
		}		
	}
	
	private void initTypes(){
		UIListItem listitem = new UIListItem(ZoneType.Gather.toString(), ZoneType.Gather);
		listitem.setTextColor(Color.black);
		cmbType.addItem(listitem);
		listitem = new UIListItem(ZoneType.Terraform.toString(), ZoneType.Terraform);
		listitem.setTextColor(Color.black);
		cmbType.addItem(listitem);
	}

	private int getAbsoluteDiff(int val1, int val2) {
		int width;
		if (val1 == val2) {
			width = 1;
		} else if (val1 < 0) {
			if (val2 < 0 && val2 < val1) {
				width = Math.abs(val2) - Math.abs(val1);
			} else if (val2 < 0 && val2 > val1) {
				width = Math.abs(val1) - Math.abs(val2);
			} else {
				width = Math.abs(val1) + val2;
			}
			width++;
		} else {
			if (val2 > -1 && val2 < val1) {
				width = val1 - val2;
			} else if (val2 > -1 && val2 > val1) {
				width = val2 - val1;
			} else {
				width = Math.abs(val2) + val1;
			}
			width++;
		}
		return width;
	}
}
