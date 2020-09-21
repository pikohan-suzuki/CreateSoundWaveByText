# -*- coding: utf-8 -*-
import requests

def convert_kana_to_roma(kana_str_list:list,kanji_list:list):
    kana_roma_dict = { "ア" : "a","イ" : "i","ウ" : "u","エ" : "e","オ" : "o","カ" : "ka","キ" : "ki","ク" : "ku","ケ" : "ke","コ" : "ko","サ" : "sa","タ" : "ta","チ" : "ti","ツ" : "tu","テ" : "te","ト" : "to","シ" : "si","ス" : "su","セ" : "se","ソ" : "so","ナ" : "na","ニ" : "ni","ヌ" : "nu","ネ" : "ne","ノ" : "no","ハ" : "ha","ヒ" : "hi","フ" : "hu","ヘ" : "he","ホ" : "ho","マ" : "ma","ミ" : "mi","ム" : "mu","メ" : "me","モ" : "mo","ヤ" : "ya","ユ" : "yu","ヨ" : "yo","ラ" : "ra","リ" : "ri","ル" : "ru","レ" : "re","ロ" : "ro","ワ" : "wa","ヲ" : "wo","ン" : "nn","ガ" : "ga","ギ" : "gi","グ" : "gu","ゲ" : "ge","ゴ" : "go","ザ" : "za","ジ" : "zi","ズ" : "zu","ゼ" : "ze","ゾ" : "zo","ダ" : "da","ヂ" : "di","ヅ" : "du","デ" : "de","ド" : "do","バ" : "ba","ビ" : "bi","ブ" : "bu","ベ" : "be","ボ" : "bo","パ" : "pa","ピ" : "pi","プ" : "pu","ペ" : "pe","ポ" : "po","ッ" : "xtu","ャ" : "xya","ュ" : "xyu","ョ" : "xyo","ァ" : "xa","ィ" : "xi","ゥ" : "xu","ェ" : "xe","ォ" : "xo"}
    result_roma_list = []
    result_kana_list = []
    result_kanji_list = []
    for text,kanji in zip(kana_str_list,kanji_list):
        if len(text) <= 2: continue
        try:
            roma_text = ""
            for value in text:
                if value in ["-","ー","―","－"]:
                    roma_text += roma_text[-1]
                else:
                    roma_text += kana_roma_dict[value]
            result_roma_list.append(roma_text)
            result_kana_list.append(text)
            result_kanji_list.append(kanji)
        except Exception as e:
            # print(e)
            # print(text)
            pass

    return result_roma_list,result_kana_list,result_kanji_list

if __name__ == "__main__":
    kana_list = ["チェルーン","クロエ","ユニパイセン"]
    kanji_list =["亜","伊","宇"]
    print(convert_kana_to_roma(kana_list,kanji_list))