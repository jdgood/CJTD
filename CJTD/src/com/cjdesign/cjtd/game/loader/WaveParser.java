package com.cjdesign.cjtd.game.loader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import com.cjdesign.cjtd.R;
import com.cjdesign.cjtd.game.gameobjects.creeps.Creep;
import com.cjdesign.cjtd.globals.G;

public class WaveParser {
	public static void parseWaves(int levelNum)
	{
		try {
			ArrayList<Creep> wave = new ArrayList<Creep>();
			InputStream is = G.gameContext.getResources().openRawResource(R.raw.levels);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(is);
			doc.getDocumentElement().normalize();
	 
			NodeList lwList = doc.getElementsByTagName("level");//level/wave list
			Node levelNode = null;
			boolean found = false;
			
			for(int i = 0; i < lwList.getLength(); i++){
				levelNode = lwList.item(i);
				if(new Integer(levelNode.getAttributes().getNamedItem("levelNum").getNodeValue()) == levelNum){
					found = true;
					break;
				}
			}
			
			if(!found){
				System.out.println("Level does not exist");
				return;
			}
			
			if (levelNode.getNodeType() == Node.ELEMENT_NODE){//should always be true, but put here just to be sure
				lwList = ((Element)levelNode).getElementsByTagName("wave");
				for(int i = 0; i < lwList.getLength(); i++){
					Node waveNode = lwList.item(i);
					if (levelNode.getNodeType() == Node.ELEMENT_NODE){//same as above
						NodeList creepList = ((Element)waveNode).getElementsByTagName("creep");
						for(int j = 0; j < creepList.getLength(); j++){
							Node creepNode = creepList.item(j);
							//String type = creepNode.getAttributes().getNamedItem("type").getNodeValue();
							float start = new Float(creepNode.getAttributes().getNamedItem("start").getNodeValue());
							float wait = new Float(creepNode.getAttributes().getNamedItem("wait").getNodeValue());
							int num = new Integer(creepNode.getAttributes().getNamedItem("num").getNodeValue());
							for(int k = 0; k < num; k++){
								//add check for type here
								wave.add(new Creep(start + wait * k));
							}
						}
					}
					G.Waves.add(wave);
					G.timeBetweenWaves.add(new Float(waveNode.getAttributes().getNamedItem("delay").getNodeValue()));
					wave = new ArrayList<Creep>();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*public static void parseWaves(int levelNum)
	{
		ArrayList<Creep> temp;
		try
		{
			InputStream is = G.gameContext.getResources().openRawResource(R.raw.levels);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(is);
			
			NodeList levels = doc.getElementsByTagName("level");
			System.out.println(levels.getLength() + " levels");
			Node currentWave = levels.item(levelNum-1).getFirstChild();
			
			do
			{
				temp = new ArrayList<Creep>();
				NodeList currentCreepSet = currentWave.getChildNodes();
				System.out.println(currentCreepSet.getLength() + " sets");*/
				/*
				do
				{
					NamedNodeMap attributes = currentCreepSet.getAttributes();
					System.out.println("Found num: " + attributes.getNamedItem("num").getNodeValue());
					System.out.println("Found start: " + attributes.getNamedItem("start").getNodeValue());
					System.out.println("Found wait: " + attributes.getNamedItem("wait").getNodeValue());
					System.out.println("Found type: " + attributes.getNamedItem("type").getNodeValue());
					for (int num = 0; num < new Integer(attributes.getNamedItem("num").getNodeValue()); num++)
					{
						temp.add(new Creep(new Integer(attributes.getNamedItem("start").getNodeValue())
							+ (num * new Integer(attributes.getNamedItem("wait").getNodeValue()))));
						System.out.println("Added creep at " + new Integer(attributes.getNamedItem("start").getNodeValue())
							+ (num * new Integer(attributes.getNamedItem("wait").getNodeValue())));
					}
				} while ((currentCreepSet = currentCreepSet.getNextSibling()) != null);*/
				/*G.Waves.add(temp);
				currentWave = currentWave.getNextSibling();
				while (currentWave.getNodeType() == Node.TEXT_NODE)
				{
					currentWave = currentWave.getNextSibling();
				}
			} while (currentWave != null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/
}
