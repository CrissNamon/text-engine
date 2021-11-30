package ru.hiddenproject.example.game;

import ru.hiddenproject.example.game.component.GameItem;
import ru.hiddenproject.progressive.annotation.Configuration;
import ru.hiddenproject.progressive.annotation.GameBean;
import ru.hiddenproject.progressive.annotation.Qualifier;
import ru.hiddenproject.progressive.basic.util.BasicObjectCaster;
import ru.hiddenproject.progressive.injection.GameBeanCreationPolicy;

/**
 * Basic DI configuration.
 */
//Configuration class must be annotated as @Configuration
@Configuration
//Will scan ru.hiddenproject.example.game.component
// package for @GameBean classes and @Configuration classes
//@ComponentScan("ru.hiddenproject.example.game.component")
public class BasicConfiguration {

  private long globalIdGenerator = -1;

  //Will create bean of type BasicObjectCaster and name "objCaster"
  @GameBean(name = "objCaster")
  public BasicObjectCaster objCaster() {
    return new BasicObjectCaster();
  }

  //Will add bean information of type Long and name "globalIdGenerator".
  //Will create new object every time when you call getBean
  //Will give new Long value every time you call getBean("globalIdGenerator", Long.class)
  @GameBean(name = "globalIdGenerator", policy = GameBeanCreationPolicy.OBJECT, order = 1)
  public Long generateId() {
    return ++globalIdGenerator;
  }

  //Will create bean with type GameItem and name "createitem"
  //Before method call will inject random bean of type Long as parameter if such bean exists
  @GameBean(policy = GameBeanCreationPolicy.OBJECT, order = 2)
  public GameItem createItem(@Qualifier("globalIdGenerator") Long id) {
    GameItem item = new GameItem();
    item.setId(id);
    return item;
  }

  //Use @Qualifier to specify bean name to inject
  @GameBean(name = "generate", order = 3)
  public Long generate(@Qualifier("globalIdGenerator") Long id) {
    return ++globalIdGenerator;
  }

  //Will create bean with type GameItem and name "gameItem"
  //Will inject bean with name "globalIdGenerator" as Long arg
  @GameBean(name = "gameItem", policy = GameBeanCreationPolicy.OBJECT, order = 4)
  public GameItem createItemWithIdGenerator(@Qualifier("generate") Long id) {
    return createItem(id);
  }

}
