# -*- coding: utf-8 -*-
import re
import json
from convert_kana_to_roma import convert_kana_to_roma

def convert_text_file(kana_path,kanji_path):
    with open(kana_path,encoding="utf-8_sig") as f:
        lines = f.readlines()
    line = lines[0]
    kana_splited = re.split("[、|(|)|。|「|」|！|!|?|？]",line)
    
    with open(kanji_path,encoding="utf-8_sig") as f:
        lines = f.readlines()
    line = lines[0]
    kanji_splited = re.split("[、|(|)|。|「|」|！|!|?|？]",line)
    assert(len(kana_splited)==len(kanji_splited))

    roma_list,kana_list,kanji_list = convert_kana_to_roma(kana_splited,kanji_splited)

    output_dict = {"roma":roma_list,"kana":kana_list,"kanji":kanji_list}
    file_name = re.split("[.|/|\\\]",kana_path)[-2]
    with open(f"./output/{file_name}.json","w") as f:
        json.dump(output_dict,f)

if __name__ == "__main__":
    kana_path="./data/merosu.txt"
    kanji_path="./data/merosu_kanji.txt"
    convert_text_file(kana_path,kanji_path)