package tech.hiddenproject.example.variant;

import tech.hiddenproject.progressive.annotation.*;
import tech.hiddenproject.progressive.basic.*;

/**
 * This service will be loaded to BasicDIContainer if android variant was specified in
 * BasicDIContainer.
 */
@GameBean(variant = "android")
public class AndroidService implements MyService {
  @Override
  public void printVariant() {
    BasicComponentManager.getGameLogger().info("I'm Android service");
  }
}
