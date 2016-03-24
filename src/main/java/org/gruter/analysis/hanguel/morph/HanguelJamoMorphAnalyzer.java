package org.gruter.analysis.hanguel.morph;

/**
 * Created by hwjeong on 15. 11. 18..
 */
public class HanguelJamoMorphAnalyzer {

  public HanguelJamoMorphAnalyzer() {
  }

  public static void main(String args[]) {
    String query = "Nike 청바지 사주세요.";
    HanguelJamoMorphTokenizer tokenizer = HanguelJamoMorphTokenizer.getInstance();

    String chosung = tokenizer.tokenizer(query, HanguelJamoType.CHOSUNG);
    String jungsung = tokenizer.tokenizer(query, HanguelJamoType.JUNGSUNG);
    String jongsung = tokenizer.tokenizer(query, HanguelJamoType.JONGSUNG);
    String english = tokenizer.tokenizer(query, HanguelJamoType.KORTOENG);

    System.out.println("원문 : [" + query + "]");
    System.out.println("영문 : [" + english + "]");
    System.out.println("초성 : [" + chosung + "]");
    System.out.println("중성 : [" + jungsung + "]");
    System.out.println("종성 : [" + jongsung + "]");
  }
}
