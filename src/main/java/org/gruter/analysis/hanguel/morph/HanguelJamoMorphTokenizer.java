package org.gruter.analysis.hanguel.morph;

/**
 * Created by hwjeong on 15. 11. 18..
 */
public class HanguelJamoMorphTokenizer {

  private volatile static HanguelJamoMorphTokenizer hanguelJamoMorphTokenizer;

  private HanguelJamoMorphTokenizer() {
  }

  public static HanguelJamoMorphTokenizer getInstance() {
    if ( hanguelJamoMorphTokenizer == null ) {
      synchronized ( HanguelJamoMorphTokenizer.class ) {
        if ( hanguelJamoMorphTokenizer == null ) {
          hanguelJamoMorphTokenizer = new HanguelJamoMorphTokenizer();
        }
      }
    }
    return hanguelJamoMorphTokenizer;
  }

  // {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'}
  private static final char[] CHOSUNG =
      {0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};

  // {'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'}
  private static final char[] JUNGSUNG =
      {0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163};

  // {' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'}
  private static final char[] JONGSUNG =
      {0x0000, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};

  private static final char CHOSUNG_BEGIN_UNICODE = 12593;
  private static final char CHOSUNG_END_UNICODE = 12622;
  private static final char HANGUEL_BEGIN_UNICODE = 44032;
  private static final char HANGUEL_END_UNICODE = 55203;
  private static final char NUMBER_BEGIN_UNICODE = 48;
  private static final char NUMBER_END_UNICODE = 57;
  private static final char ENGLISH_LOWER_BEGIN_UNICODE = 65;
  private static final char ENGLISH_LOWER_END_UNICODE = 90;
  private static final char ENGLISH_UPPER_BEGIN_UNICODE = 97;
  private static final char ENGLISH_UPPER_END_UNICODE = 122;

  private static boolean isPossibleCharacter(char c){
    if ((   (c >= NUMBER_BEGIN_UNICODE && c <= NUMBER_END_UNICODE)
        || (c >= ENGLISH_UPPER_BEGIN_UNICODE && c <= ENGLISH_UPPER_END_UNICODE)
        || (c >= ENGLISH_LOWER_BEGIN_UNICODE && c <= ENGLISH_LOWER_END_UNICODE)
        || (c >= HANGUEL_BEGIN_UNICODE && c <= HANGUEL_END_UNICODE)
        || (c >= CHOSUNG_BEGIN_UNICODE && c <= CHOSUNG_END_UNICODE))
        ){
      return true;
    }else{
      return false;
    }
  }

  public String tokenizer(String source, HanguelJamoType jamoType) {
    String jamo = "";

    /*
    [분리 기본 공식]
    초성 = ( ( (글자 - 0xAC00) - (글자 - 0xAC00) % 28 ) ) / 28 ) / 21
    중성 = ( ( (글자 - 0xAC00) - (글자 - 0xAC00) % 28 ) ) / 28 ) % 21
    종성 = (글자 - 0xAC00) % 28

    [합치기 기본 공식]
    원문 = 0xAC00 + 28 * 21 * (초성의 index) + 28 * (중성의 index) + (종성의 index)

    각 index 정보는 CHOSUNG, JUNGSUNG, JONGSUNG char[]에 정의한 index 입니다.
    하지만 아래 코드에서는 원문이 필요 없기 때문에 합치기 위한 로직은 포함 되어 있지 않습니다.
    */

    switch ( jamoType ) {
      case CHOSUNG:
        jamo = chosungTokenizer(source);
        break;
      case JUNGSUNG:
        jamo = jungsungTokenizer(source);
        break;
      case JONGSUNG:
        jamo = jongsungTokenizer(source);
        break;
      default:
        jamo = chosungTokenizer(source);
    }

    return jamo;
  }

  public String chosungTokenizer(String source) {
    String chosung = "";

    for(int i = 0 ; i < source.length(); i++) {
      char sourceChar = source.charAt(i);

      if(sourceChar >= 0xAC00) {
        int criteia = (sourceChar - 0xAC00);
        char choIdx = (char)(((criteia - (criteia%28))/28)/21);

        chosung = chosung + CHOSUNG[choIdx];
      } else {
        if ( isPossibleCharacter(sourceChar) ) {
          chosung = chosung + sourceChar;
        }
      }
    }

    return chosung;
  }

  public String jungsungTokenizer(String source) {
    String jungsung = "";

    for(int i = 0 ; i < source.length(); i++) {
      char sourceChar = source.charAt(i);

      if(sourceChar >= 0xAC00) {
        int criteia = (sourceChar - 0xAC00);
        char jungIdx = (char)(((criteia - (criteia%28))/28)%21);

        jungsung = jungsung + JUNGSUNG[jungIdx];
      } else {
        if ( isPossibleCharacter(sourceChar) ) {
          jungsung = jungsung + sourceChar;
        }
      }
    }

    return jungsung;
  }

  public String jongsungTokenizer(String source) {
    String jongsung = "";

    for(int i = 0 ; i < source.length(); i++) {
      char sourceChar = source.charAt(i);

      if(sourceChar >= 0xAC00) {
        char jongIdx = (char)((sourceChar - 0xAC00)%28);

        jongsung = jongsung + JONGSUNG[jongIdx];
      } else {
        if (isPossibleCharacter(sourceChar) ) {
          jongsung = jongsung + sourceChar;
        }
      }
    }

    return jongsung;
  }
}
