import re
import json
import sys

def containsHeader(string):
    if "Temperature preference" in string:
        return True
    if "Watering guidelines" in string:
        return True
    if "Fertilizer suggestions" in string:
        return True
    if "Mist requirements" in string:
        return True
    if "pH range" in string:
        return True
    if "Acidity preference" in string:
        return True
    if "Toxicity" in string:
        return True
    if "Climate" in string:
        return True
    if "Light preferences" in string:
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
            if "Temperature preference" in line:
                TempLine = re.sub("Temperature preference\s+", "", line)
                TempLine = re.sub("\n", "", TempLine)
                data["temperature"] = TempLine
                continue
            elif "Watering guidelines" in line:
                wGuideline = re.sub("Watering guidelines\s+", "", line)
                wGuideline = re.sub("\n", "", wGuideline)
                data["water"] = wGuideline
                continue
            elif "Fertilizer suggestions" in line:
                FerLine = re.sub("Fertilizer suggestions\s+	", "", line)
                FerLine = re.sub("\n", "", line)
                data["fertilizer"] = FerLine
                continue
            elif "Mist requirements" in line:
                MistLine = re.sub("Mist requirements\s+", "", line)
                MistLine = re.sub("\n", "", MistLine)
                data["mist"] = MistLine
                continue
            elif "pH range" in line:
                phLine = re.sub("pH range\s+", "", line)
                phLine = re.sub("\n", "", phLine)
                data["ph"] = phLine
                continue
            elif "Acidity preference" in line:
                AcidLine = re.sub("Acidity preference\s+", "", line)
                AcidLine = re.sub("\n", "", AcidLine)
                data["acid"] = AcidLine
                continue
            elif "Toxicity" in line:
                ToxicLine = re.sub("Toxicity\s+", "", AcidLine)
                ToxicLine = re.sub("\n", "", AcidLine)
                data["toxic"] = ToxicLine
                continue
            elif "Climate" in line:
                ClimateLine = re.sub("Climate\s+", "", line)
                ClimateLine = re.sub("\n", "", ClimateLine)
                data["climate"] = ClimateLine

                break
            elif "Light preferences" in line:
                LightLine = re.sub("Light preferences\s+", "", line)
                LightLine = re.sub("\n", "", LightLine)
                data["light"] = LightLine
                continue

    datalist.append(data)
print(datalist)
json.dump(datalist,json_result)