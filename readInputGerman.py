import re
import json
import sys

def containsHeader(string):
    if "Bewässerungs Richtlinien" in string:
        return True
    if "Dünger Vorschläge" in string:
        return True
    if "Nebel Anforderungen" in string:
        return True
    if "Licht Präferenzen" in string:
        return True
    if "Temperatur Präferenz" in string:
        return True
    if "pH-Bereich" in string:
        return True
    if "Säure Präferenz" in string:
        return True
    if "Toxizität" in string:
        return True
    if "Klima" in string:
        return True
    return False


file = open(sys.argv[1],"r+")

file_removed_blank = open("input_removed.txt","w+")

res = []

for line in file:
    if line.strip():
        line = re.sub("\t","  ",line)
        res.append(line)
file.close()
for resLine in res :
    file_removed_blank.write(resLine)
file_removed_blank.close()


##TODO : Getting next line without moving the iterator

file_removed = open("input_removed.txt")
json_result = open("output_json.json", "w+")
datalist = []

while True:
    print("loop")
    data = {}
    try:
        name = next(file_removed)
    except StopIteration:
        print("outer")
        break

    name = re.sub("\n", "", name)
    data["name"] = name

    familyName = next(file_removed)
    familyName = re.sub("\n", "", familyName)
    data["family"] = familyName
    while True:
        try :
            line = next(file_removed)
            print(line)
        except StopIteration:
            print("inner")
            break
        if containsHeader(line) == True:
            if "Temperatur Präferenz" in line:
                TempLine = re.sub("Temperatur Präferenz\s+", "", line)
                TempLine = re.sub("\n", "", TempLine)
                data["temperature"] = TempLine
                continue
            elif "Bewässerungs Richtlinien" in line:
                wGuideline = re.sub("Bewässerungs Richtlinien\s+", "", line)
                wGuideline = re.sub("\n", "", wGuideline)
                data["water"] = wGuideline
                continue
            elif "Dünger Vorschläge" in line:
                FerLine = re.sub("Dünger Vorschläge\s+	", "", line)
                FerLine = re.sub("\n", "", line)
                data["fertilizer"] = FerLine
                continue
            elif "Nebel Anforderungen" in line:
                MistLine = re.sub("Nebel Anforderungen\s+", "", line)
                MistLine = re.sub("\n", "", MistLine)
                data["mist"] = MistLine
                continue
            elif "pH-Bereich" in line:
                phLine = re.sub("pH-Bereich\s+", "", line)
                phLine = re.sub("\n", "", phLine)
                data["ph"] = phLine
                continue
            elif "Säure Präferenz" in line:
                AcidLine = re.sub("Säure Präferenz\s+", "", line)
                AcidLine = re.sub("\n", "", AcidLine)
                data["acid"] = AcidLine
                continue
            elif "Toxizität" in line:
                ToxicLine = re.sub("Toxizität\s+", "", line)
                ToxicLine = re.sub("\n", "", ToxicLine)
                data["toxic"] = ToxicLine
                continue
            elif "Klima" in line:
                ClimateLine = re.sub("Klima\s+", "", line)
                ClimateLine = re.sub("\n", "", ClimateLine)
                data["climate"] = ClimateLine
                break
            elif "Licht Präferenzen" in line:
                LightLine = re.sub("Licht Präferenzen\s+", "", line)
                LightLine = re.sub("\n", "", LightLine)
                data["light"] = LightLine
                continue

    datalist.append(data)
print(datalist)
json.dump(datalist,json_result)