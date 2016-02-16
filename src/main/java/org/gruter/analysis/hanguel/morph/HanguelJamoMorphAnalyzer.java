package org.gruter.analysis.hanguel.morph;

import org.apache.lucene.analysis.Analyzer;

/**
 * Created by hwjeong on 15. 11. 18..
 */
public class HanguelJamoMorphAnalyzer extends Analyzer {

  public HanguelJamoMorphAnalyzer() {
  }

  @Override
  protected TokenStreamComponents createComponents(String fieldName) {
    return new TokenStreamComponents(new HanguelJamoMorphTokenizer());
  }
}
