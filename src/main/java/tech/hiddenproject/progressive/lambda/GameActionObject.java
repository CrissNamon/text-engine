package tech.hiddenproject.progressive.lambda;

/**
 * Represents some story action.
 *
 * @param <O> Action parameter type
 */
public interface GameActionObject<O> {

  /**
   * Makes action.
   *
   * @param obj Action parameter
   */
  void make(O obj);
}
