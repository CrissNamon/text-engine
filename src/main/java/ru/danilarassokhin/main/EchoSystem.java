package ru.danilarassokhin.main;

import ru.danilarassokhin.progressive.annotation.IsGameScript;
import ru.danilarassokhin.progressive.basic.BasicGameObject;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;
import ru.danilarassokhin.progressive.basic.system.AbstractGameScript;

@IsGameScript
public class EchoSystem extends AbstractGameScript {

    private EchoSystem(BasicGameObject parent) {
        super(parent);
    }

    private void start() {
        System.out.println("ECHO START");
        BasicGamePublisher.getInstance().subscribeOn("EchoInput", (input) -> System.out.println("ECHO OUTPUT: " + input));
    }

    private void update() {

    }

}
