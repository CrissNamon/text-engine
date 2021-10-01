package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.basic.*;
import ru.danilarassokhin.progressive.basic.component.GameItem;
import ru.danilarassokhin.progressive.basic.component.GameNode;
import ru.danilarassokhin.progressive.basic.component.NodeBundle;
import ru.danilarassokhin.progressive.basic.system.CharacterSystem;
import ru.danilarassokhin.progressive.basic.system.InventorySystem;
import ru.danilarassokhin.progressive.basic.system.ItemSystem;
import ru.danilarassokhin.progressive.basic.system.NodeSystem;
import ru.danilarassokhin.progressive.component.GameObject;

public class Main {

    public static void main(String[] args) {
        BasicGame game = BasicGame.getInstance();
        GameObject nodeFlow = game.addGameObject();
        NodeSystem nodeSystem = nodeFlow.getGameScript(NodeSystem.class);
        NodeBundle nodeBundle = new NodeBundle();

        nodeBundle.setText("Hello!");
        GameNode<NodeBundle> node1 = nodeSystem.createNode(GameNode.class);
        node1.setBundle(nodeBundle);
        node1.setBundle(nodeBundle);

        GameObject mainCharacter = game.addGameObject();
        CharacterSystem characterSystem = mainCharacter.getGameScript(CharacterSystem.class);
        characterSystem.setHealth(80);

        ItemSystem itemSystem = mainCharacter.getGameScript(ItemSystem.class);
        InventorySystem inventorySystem = mainCharacter.getGameScript(InventorySystem.class);
        GameItem item = new GameItem(1L);
        inventorySystem.addItem(item);
        inventorySystem.addItem(item);
    }
}
