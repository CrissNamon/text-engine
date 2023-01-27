package progressive.data;

import tech.hiddenproject.progressive.basic.lambda.StateMachineAction;
import tech.hiddenproject.progressive.data.StateMachineTransition;

/**
 * @author Danila Rassokhin
 */
public class TestAction implements StateMachineAction<TestState, TestEvent> {

  private int counter;

  @Override
  public void make(StateMachineTransition<TestState, TestEvent> transition) {
    counter++;
  }

  public int getCounter() {
    return counter;
  }
}
