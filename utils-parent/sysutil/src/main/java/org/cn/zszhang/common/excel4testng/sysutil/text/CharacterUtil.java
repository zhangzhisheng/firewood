package org.cn.zszhang.common.excel4testng.sysutil.text;

/**
 * Created by zszhang on 2015/5/28.
 */
public class CharacterUtil {
    public static final int CHAR_USELESS = 0;

    public static final int CHAR_ARABIC = 0X00000001;

    public static final int CHAR_ENGLISH = 0X00000002;

    public static final int CHAR_CHINESE = 0X00000004;

    public static final int CHAR_OTHER_CJK = 0X00000008;


    /**
     * 识别字符类型
     * @param input
     * @return int CharacterUtil定义的字符类型常量
     */
    public static int identifyCharType(char input){
        if(input >= '0' && input <= '9'){
            return CHAR_ARABIC;

        }else if((input >= 'a' && input <= 'z')
                || (input >= 'A' && input <= 'Z')){
            return CHAR_ENGLISH;

        }else {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(input);

            if(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A){
                //目前已知的中文字符UTF-8集合
                return CHAR_CHINESE;

            }else if(ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS //全角数字字符和日韩字符
                    //韩文字符集
                    || ub == Character.UnicodeBlock.HANGUL_SYLLABLES
                    || ub == Character.UnicodeBlock.HANGUL_JAMO
                    || ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
                    //日文字符集
                    || ub == Character.UnicodeBlock.HIRAGANA //平假名
                    || ub == Character.UnicodeBlock.KATAKANA //片假名
                    || ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS){
                return CHAR_OTHER_CJK;

            }
        }
        //其他的不做处理的字符
        return CHAR_USELESS;
    }

    /**
     * 进行字符规格化（全角转半角，大写转小写处理）
     * @param input
     * @return char
     */
    public static char regularize(char input){
        if (input == 0x003000) {
            input = 0x20; // space
        }else if (input > 0xff00 && input < 0xff5f) { // 全角字母、数字、标点
            input = (char) (input - 0xfee0); // 减值完成转换
        }else if (input >= 'A' && input <= 'Z') {
            input += 0x20;
        }

        return input;
    }

    /**
     * 进行字符规格化（全角转半角，大写转小写，带括号字母数字转换为小写字母、数字，希腊数字转英文数字(仅支持到12)处理）
     * @param input
     * @param resultBuffer 结果会追加到此处
     * @return void
     */
    public static void advanceRegularize(char input, StringBuffer resultBuffer){
        if(input >= 0x2460 && input <= 0x24fe) {
            regularize20Nums(input, (char) 0x2460, (char) 0x2473, resultBuffer);// ① - ⑳
            regularize20Nums(input, (char) 0x2474, (char) 0x2487, resultBuffer);// ⑴ - ⒇
            regularize20Nums(input, (char) 0x2488, (char) 0x249b, resultBuffer);// ⒈ - ⒛
            if(input >= 0x249c && input <= 0x24b5) { // (a) - (z)
                resultBuffer.append((char)(input - (0x249c-'a')));
            }else if(input >= 0x24b6 && input <= 0x24cf) { // 圈A - 圈Z
                resultBuffer.append((char)(input - (0x24b6-'a')));
            }else if(input >= 0x24d0 && input <= 0x24e9) { // 圈a - 圈z
                resultBuffer.append((char)(input - (0x24d0-'a')));
            }else if(input == 0x24ea) { // 圈0
                resultBuffer.append((char)('0'));
            }else if(input >= 0x24eb && input <=0x24f3) { // 11-19
                resultBuffer.append((char)('1'));
                resultBuffer.append((char)(input-(0x24eb-'1')));
            }else if(input == 0x24f4) { // 20
                resultBuffer.append("20");
            }else if(input >= 0x24f5 && input <= 0x24fe) { // 1-10
                regularize20Nums(input, (char) 0x24f5, (char) 0x24fe, resultBuffer);
            }
        } else if(input >= 0x2776 && input <= 0x2793) {
            regularize20Nums(input, (char) 0x2776, (char) (0x2776 + 9), resultBuffer);
            regularize20Nums(input, (char) (0x2776 + 10), (char) (0x2776 + 19), resultBuffer);
            regularize20Nums(input, (char) (0x2776 + 20), (char) (0x2776 + 29), resultBuffer);
        }else if(input >= 0x2160 && input <= 0x217b) { // 希腊数字1-12(ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅺⅻ)
            regularize20Nums(input, (char) 0x2160, (char) (0x2160 + 11), resultBuffer);
            regularize20Nums(input, (char) (0x2170), (char) (0x2170 + 11), resultBuffer);
        } else if(input >= 0x3220 && input <= 0x3229) { // ㈠ - ㈩
            regularize20Nums(input, (char) 0x3220, (char) 0x3229, resultBuffer);
        } else {
            resultBuffer.append((char)(regularize(input)));
        }
    }

    private static void regularize20Nums(char input, char begin, char end, StringBuffer resultBuffer) {
        if (input >= begin && input <= begin+8 && input <= end) { // 1-9
            resultBuffer.append((char)(input-(begin-'1')));
        }else if (input >= begin+9 && input <= begin+18 && input <= end) { // 上面格式的10-19
            resultBuffer.append((char)('1')); // 十位
            resultBuffer.append((char)(input-(begin+9-'0'))); // 个位
        }else if (input == begin + 19 && input <= end) { // 上面格式的20
            resultBuffer.append("20");
        }
    }

    public static String regularString(String input) {
        if( null == input || input.isEmpty() ) return input;

        StringBuffer stringBuffer = new StringBuffer(input.length());
        for(int i=0; i<input.length();i++)
            advanceRegularize(input.charAt(i), stringBuffer);

        return stringBuffer.toString();
    }
}
