import re
import json
import sys

def containsHeader(string):
    if "Kategorie:" in string:
        return True
    if "Verfügbare Farben:" in string:
        return True
    if "Blütezeit:" in string:
        return True
    if "Höhenbereich:" in string:
        return True
    if "Raumbereich:" in string:
        return True
    if "Niedrigste Temperatur:" in string:
        return True
    if "Pflanzen Licht:" in string:
        return True
    if "Pflanzenfutter" in string:
        return True
    if "Bewässerung" in string:
        return True
    if "Boden" in string:
        return True
    if "Grundversorgung Zusammenfassung" in string:
        return True
    print(string +"\n")
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

    ##print("loop")
    ##data = {}
    ##try:
    ##    name = next(file_removed)
    ##except StopIteration:
    ##    print("outer")
    ##    break

    ##name = re.sub("\n", "", name)

data = {}
while True:
    try :
        line = next(file_removed)
        ##print(line)
    except StopIteration:
        ##print("inner")
        break
    if containsHeader(line) == True:
        if "Kategorie:" in line:
            TempLine = re.sub("Kategorie:\s+", "", line)
            TempLine = re.sub("\n", "", TempLine)
            data["category"] = TempLine
            continue
        elif "Verfügbare Farben:" in line:
            wGuideline = re.sub("Verfügbare Farben:\s+", "", line)
            wGuideline = re.sub("\n", "", wGuideline)
            data["availableColors"] = wGuideline
            continue
        elif "Blütezeit:" in line:
            FerLine = re.sub("Blütezeit:\s+", "", line)
            FerLine = re.sub("\n", "", FerLine)
            data["bloomTime"] = FerLine
            continue
        elif "Raumbereich:" in line:
            MistLine = re.sub("Raumbereich:\s+", "", line)
            MistLine = re.sub("\n", "", MistLine)
            data["heightRange"] = MistLine
            continue
        elif "Raumbereich:" in line:
            phLine = re.sub("Raumbereich:\s+", "", line)
            phLine = re.sub("\n", "", phLine)
            data["spaceRange"] = phLine
            continue
        elif "Pflanzen Licht:" in line:
            AcidLine = re.sub("Pflanzen Licht:\s+", "", line)
            AcidLine = re.sub("\n", "", AcidLine)
            data["plantLight"] = AcidLine
            continue
        elif "Pflanzenfutter" in line:
            ToxicLine = re.sub("Pflanzenfutter\s+", "", line)
            ToxicLine = re.sub("\n", "", ToxicLine)
            data["feed"] = ToxicLine
            continue
        elif "Bewässerung" in line:
            ClimateLine = re.sub("Bewässerung\s+", "", line)
            ClimateLine = re.sub("\n", "", ClimateLine)
            data["watering"] = ClimateLine
            continue
        elif "Grundversorgung Zusammenfassung" in line:
            ClimateLine = re.sub("Grundversorgung Zusammenfassung\s+", "", line)
            ClimateLine = re.sub("\n", "", ClimateLine)
            data["basicCare"] = ClimateLine
            continue
        elif "Boden" in line:
            LightLine = re.sub("Boden\s+", "", line)
            LightLine = re.sub("\n", "", LightLine)
            data["soil"] = LightLine
            continue

        elif "Niedrigste Temperatur:" in line:
            LightLine = re.sub("Niedrigste Temperatur:\s+", "", line)
            LightLine = re.sub("\n", "", LightLine)
            data["lowTemp"] = LightLine
            continue
    else:
        datalist.append(data)
        data = {}
        latinName = line[line.find("(") + 1:line.find(")")]
        shortname = line[:line.find("(")]
        data["name"] = shortname

        familyName = latinName
        familyName = re.sub("\n", "", familyName)
        data["family"] = familyName


##print(datalist)
json.dump(datalist,json_result)