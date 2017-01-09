package com.runnergame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

public class MetaLevels {
    public static Array<MetaLevel> levels;
    private static String FILE = "";
    public int level;

    public MetaLevels(String fileName) {
        FILE = fileName;
        level = GameRunner.dm.load2("metaGameLevel");
    }

    public void load() {
        try {
            XmlReader xmlReader = new XmlReader();
            System.out.print(FILE+"\n");
            FileHandle file = Gdx.files.internal(FILE);
            XmlReader.Element root = xmlReader.parse(file);
            String name = root.getAttribute("name");

            int lvlCount = root.getChildCount();
            this.levels = new Array<MetaLevel>(lvlCount);

            for (int i = 0; i < lvlCount; ++i) {
                XmlReader.Element taskElement = root.getChild(i);
                int lvlName = taskElement.getInt("name");
                int p = taskElement.getInt("prize");
                String d = taskElement.getAttribute("discription");
                System.out.print(d+"\n");
                String tex = taskElement.getAttribute("tex");
                String param = taskElement.getAttribute("param");
                int price = taskElement.getInt("price");
                levels.add(new MetaLevel(d, lvlName, p, tex, price, param));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
