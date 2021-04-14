package org.gruter.analysis.hanguel.morph;

/**
 * Created by hwjeong on 15. 11. 19..
 */
public enum HanguelJamoType {
  CHOSUNG("CHOSUNG"),
  JUNGSUNG("JUNGSUNG"),
  JONGSUNG("JONGSUNG"),
  KORTOENG("KORTOENG");

  private String name;

  HanguelJamoType(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
