package org.raapp.messenger.components.emoji;

import org.raapp.messenger.R;
import org.whispersystems.libsignal.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class EmojiPages {

  static final List<EmojiPageModel> PAGES = Arrays.asList(
    new StaticEmojiPageModel(R.attr.emoji_category_people, new String[] {
        "\ud83d\ude00","\ud83d\ude01","\ud83d\ude02","\ud83e\udd23","\ud83d\ude03","\ud83d\ude04","\ud83d\ude05","\ud83d\ude06","\ud83d\ude09","\ud83d\ude0a","\ud83d\ude0b","\ud83d\ude0e","\ud83d\ude0d","\ud83d\ude18","\ud83d\ude17","\ud83d\ude19","\ud83d\ude1a","\u263a\ufe0f","\ud83d\ude42","\ud83e\udd17","\ud83e\udd29","\ud83e\udd14","\ud83e\udd28","\ud83d\ude10","\ud83d\ude11","\ud83d\ude36","\ud83d\ude44","\ud83d\ude0f","\ud83d\ude23","\ud83d\ude25","\ud83d\ude2e","\ud83e\udd10","\ud83d\ude2f","\ud83d\ude2a","\ud83d\ude2b","\ud83d\ude34","\ud83d\ude0c","\ud83d\ude1b","\ud83d\ude1c","\ud83d\ude1d","\ud83e\udd24","\ud83d\ude12","\ud83d\ude13","\ud83d\ude14","\ud83d\ude15","\ud83d\ude43","\ud83e\udd11","\ud83d\ude32","\u2639\ufe0f","\ud83d\ude41","\ud83d\ude16","\ud83d\ude1e","\ud83d\ude1f","\ud83d\ude24","\ud83d\ude22","\ud83d\ude2d","\ud83d\ude26","\ud83d\ude27","\ud83d\ude28","\ud83d\ude29","\ud83e\udd2f","\ud83d\ude2c","\ud83d\ude30","\ud83d\ude31","\ud83d\ude33","\ud83e\udd2a","\ud83d\ude35","\ud83d\ude21","\ud83d\ude20","\ud83e\udd2c","\ud83d\ude37","\ud83e\udd12","\ud83e\udd15","\ud83e\udd22","\ud83e\udd2e","\ud83e\udd27","\ud83d\ude07","\ud83e\udd20","\ud83e\udd21","\ud83e\udd25","\ud83e\udd2b","\ud83e\udd2d","\ud83e\uddd0","\ud83e\udd13","\ud83d\ude08","\ud83d\udc7f","\ud83d\udc79","\ud83d\udc7a","\ud83d\udc80","\u2620\ufe0f","\ud83d\udc7b","\ud83d\udc7d","\ud83d\udc7e","\ud83e\udd16","\ud83d\udca9","\ud83d\ude3a","\ud83d\ude38","\ud83d\ude39","\ud83d\ude3b","\ud83d\ude3c","\ud83d\ude3d","\ud83d\ude40","\ud83d\ude3f","\ud83d\ude3e","\ud83d\ude48","\ud83d\ude49","\ud83d\ude4a","\ud83d\udc76","\ud83e\uddd2","\ud83d\udc66","\ud83d\udc67","\ud83e\uddd1","\ud83d\udc68","\ud83d\udc69","\ud83e\uddd3","\ud83d\udc74","\ud83d\udc75","\ud83d\udc68\u200d\u2695\ufe0f","\ud83d\udc69\u200d\u2695\ufe0f","\ud83d\udc68\u200d\ud83c\udf93","\ud83d\udc69\u200d\ud83c\udf93","\ud83d\udc68\u200d\ud83c\udfeb","\ud83d\udc69\u200d\ud83c\udfeb","\ud83d\udc68\u200d\u2696\ufe0f","\ud83d\udc69\u200d\u2696\ufe0f","\ud83d\udc68\u200d\ud83c\udf3e","\ud83d\udc69\u200d\ud83c\udf3e","\ud83d\udc68\u200d\ud83c\udf73","\ud83d\udc69\u200d\ud83c\udf73","\ud83d\udc68\u200d\ud83d\udd27","\ud83d\udc69\u200d\ud83d\udd27","\ud83d\udc68\u200d\ud83c\udfed","\ud83d\udc69\u200d\ud83c\udfed","\ud83d\udc68\u200d\ud83d\udcbc","\ud83d\udc69\u200d\ud83d\udcbc","\ud83d\udc68\u200d\ud83d\udd2c","\ud83d\udc69\u200d\ud83d\udd2c","\ud83d\udc68\u200d\ud83d\udcbb","\ud83d\udc69\u200d\ud83d\udcbb","\ud83d\udc68\u200d\ud83c\udfa4","\ud83d\udc69\u200d\ud83c\udfa4","\ud83d\udc68\u200d\ud83c\udfa8","\ud83d\udc69\u200d\ud83c\udfa8","\ud83d\udc68\u200d\u2708\ufe0f","\ud83d\udc69\u200d\u2708\ufe0f","\ud83d\udc68\u200d\ud83d\ude80","\ud83d\udc69\u200d\ud83d\ude80","\ud83d\udc68\u200d\ud83d\ude92","\ud83d\udc69\u200d\ud83d\ude92","\ud83d\udc6e\u200d\u2642\ufe0f","\ud83d\udc6e\u200d\u2640\ufe0f","\ud83d\udd75\ufe0f\u200d\u2642\ufe0f","\ud83d\udd75\ufe0f\u200d\u2640\ufe0f","\ud83d\udc82\u200d\u2642\ufe0f","\ud83d\udc82\u200d\u2640\ufe0f","\ud83d\udc77\u200d\u2642\ufe0f","\ud83d\udc77\u200d\u2640\ufe0f","\ud83e\udd34","\ud83d\udc78","\ud83d\udc73\u200d\u2642\ufe0f","\ud83d\udc73\u200d\u2640\ufe0f","\ud83d\udc72","\ud83e\uddd5","\ud83e\uddd4","\ud83d\udc71\u200d\u2642\ufe0f","\ud83d\udc71\u200d\u2640\ufe0f","\ud83e\udd35","\ud83d\udc70","\ud83e\udd30","\ud83e\udd31","\ud83d\udc7c","\ud83c\udf85","\ud83e\udd36","\ud83e\uddd9\u200d\u2640\ufe0f","\ud83e\uddd9\u200d\u2642\ufe0f","\ud83e\uddda\u200d\u2640\ufe0f","\ud83e\uddda\u200d\u2642\ufe0f","\ud83e\udddb\u200d\u2640\ufe0f","\ud83e\udddb\u200d\u2642\ufe0f","\ud83e\udddc\u200d\u2640\ufe0f","\ud83e\udddc\u200d\u2642\ufe0f","\ud83e\udddd\u200d\u2640\ufe0f","\ud83e\udddd\u200d\u2642\ufe0f","\ud83e\uddde\u200d\u2640\ufe0f","\ud83e\uddde\u200d\u2642\ufe0f","\ud83e\udddf\u200d\u2640\ufe0f","\ud83e\udddf\u200d\u2642\ufe0f","\ud83d\ude4d\u200d\u2642\ufe0f","\ud83d\ude4d\u200d\u2640\ufe0f","\ud83d\ude4e\u200d\u2642\ufe0f","\ud83d\ude4e\u200d\u2640\ufe0f","\ud83d\ude45\u200d\u2642\ufe0f","\ud83d\ude45\u200d\u2640\ufe0f","\ud83d\ude46\u200d\u2642\ufe0f","\ud83d\ude46\u200d\u2640\ufe0f","\ud83d\udc81\u200d\u2642\ufe0f","\ud83d\udc81\u200d\u2640\ufe0f","\ud83d\ude4b\u200d\u2642\ufe0f","\ud83d\ude4b\u200d\u2640\ufe0f","\ud83d\ude47\u200d\u2642\ufe0f","\ud83d\ude47\u200d\u2640\ufe0f","\ud83e\udd26","\ud83e\udd26\u200d\u2642\ufe0f","\ud83e\udd26\u200d\u2640\ufe0f","\ud83e\udd37","\ud83e\udd37\u200d\u2642\ufe0f","\ud83e\udd37\u200d\u2640\ufe0f","\ud83d\udc86\u200d\u2642\ufe0f","\ud83d\udc86\u200d\u2640\ufe0f","\ud83d\udc87\u200d\u2642\ufe0f","\ud83d\udc87\u200d\u2640\ufe0f","\ud83d\udeb6\u200d\u2642\ufe0f","\ud83d\udeb6\u200d\u2640\ufe0f","\ud83c\udfc3\u200d\u2642\ufe0f","\ud83c\udfc3\u200d\u2640\ufe0f","\ud83d\udc83","\ud83d\udd7a","\ud83d\udc6f\u200d\u2642\ufe0f","\ud83d\udc6f\u200d\u2640\ufe0f","\ud83e\uddd6\u200d\u2640\ufe0f","\ud83e\uddd6\u200d\u2642\ufe0f","\ud83e\uddd7\u200d\u2640\ufe0f","\ud83e\uddd7\u200d\u2642\ufe0f","\ud83e\uddd8\u200d\u2640\ufe0f","\ud83e\uddd8\u200d\u2642\ufe0f","\ud83d\udec0","\ud83d\udecc","\ud83d\udd74\ufe0f","\ud83d\udde3\ufe0f","\ud83d\udc64","\ud83d\udc65","\ud83e\udd3a","\ud83c\udfc7","\u26f7\ufe0f","\ud83c\udfc2","\ud83c\udfcc\ufe0f\u200d\u2642\ufe0f","\ud83c\udfcc\ufe0f\u200d\u2640\ufe0f","\ud83c\udfc4\u200d\u2642\ufe0f","\ud83c\udfc4\u200d\u2640\ufe0f","\ud83d\udea3\u200d\u2642\ufe0f","\ud83d\udea3\u200d\u2640\ufe0f","\ud83c\udfca\u200d\u2642\ufe0f","\ud83c\udfca\u200d\u2640\ufe0f","\u26f9\ufe0f\u200d\u2642\ufe0f","\u26f9\ufe0f\u200d\u2640\ufe0f","\ud83c\udfcb\ufe0f\u200d\u2642\ufe0f","\ud83c\udfcb\ufe0f\u200d\u2640\ufe0f","\ud83d\udeb4\u200d\u2642\ufe0f","\ud83d\udeb4\u200d\u2640\ufe0f","\ud83d\udeb5\u200d\u2642\ufe0f","\ud83d\udeb5\u200d\u2640\ufe0f","\ud83c\udfce\ufe0f","\ud83c\udfcd\ufe0f","\ud83e\udd38","\ud83e\udd38\u200d\u2642\ufe0f","\ud83e\udd38\u200d\u2640\ufe0f","\ud83e\udd3c","\ud83e\udd3c\u200d\u2642\ufe0f","\ud83e\udd3c\u200d\u2640\ufe0f","\ud83e\udd3d","\ud83e\udd3d\u200d\u2642\ufe0f","\ud83e\udd3d\u200d\u2640\ufe0f","\ud83e\udd3e","\ud83e\udd3e\u200d\u2642\ufe0f","\ud83e\udd3e\u200d\u2640\ufe0f","\ud83e\udd39","\ud83e\udd39\u200d\u2642\ufe0f","\ud83e\udd39\u200d\u2640\ufe0f","\ud83d\udc6b","\ud83d\udc6c","\ud83d\udc6d","\ud83d\udc69\u200d\u2764\ufe0f\u200d\ud83d\udc8b\u200d\ud83d\udc68","\ud83d\udc68\u200d\u2764\ufe0f\u200d\ud83d\udc8b\u200d\ud83d\udc68","\ud83d\udc69\u200d\u2764\ufe0f\u200d\ud83d\udc8b\u200d\ud83d\udc69","\ud83d\udc69\u200d\u2764\ufe0f\u200d\ud83d\udc68","\ud83d\udc68\u200d\u2764\ufe0f\u200d\ud83d\udc68","\ud83d\udc69\u200d\u2764\ufe0f\u200d\ud83d\udc69","\ud83d\udc68\u200d\ud83d\udc69\u200d\ud83d\udc66","\ud83d\udc68\u200d\ud83d\udc69\u200d\ud83d\udc67","\ud83d\udc68\u200d\ud83d\udc69\u200d\ud83d\udc67\u200d\ud83d\udc66","\ud83d\udc68\u200d\ud83d\udc69\u200d\ud83d\udc66\u200d\ud83d\udc66","\ud83d\udc68\u200d\ud83d\udc69\u200d\ud83d\udc67\u200d\ud83d\udc67","\ud83d\udc68\u200d\ud83d\udc68\u200d\ud83d\udc66","\ud83d\udc68\u200d\ud83d\udc68\u200d\ud83d\udc67","\ud83d\udc68\u200d\ud83d\udc68\u200d\ud83d\udc67\u200d\ud83d\udc66","\ud83d\udc68\u200d\ud83d\udc68\u200d\ud83d\udc66\u200d\ud83d\udc66","\ud83d\udc68\u200d\ud83d\udc68\u200d\ud83d\udc67\u200d\ud83d\udc67","\ud83d\udc69\u200d\ud83d\udc69\u200d\ud83d\udc66","\ud83d\udc69\u200d\ud83d\udc69\u200d\ud83d\udc67","\ud83d\udc69\u200d\ud83d\udc69\u200d\ud83d\udc67\u200d\ud83d\udc66","\ud83d\udc69\u200d\ud83d\udc69\u200d\ud83d\udc66\u200d\ud83d\udc66","\ud83d\udc69\u200d\ud83d\udc69\u200d\ud83d\udc67\u200d\ud83d\udc67","\ud83d\udc68\u200d\ud83d\udc66","\ud83d\udc68\u200d\ud83d\udc66\u200d\ud83d\udc66","\ud83d\udc68\u200d\ud83d\udc67","\ud83d\udc68\u200d\ud83d\udc67\u200d\ud83d\udc66","\ud83d\udc68\u200d\ud83d\udc67\u200d\ud83d\udc67","\ud83d\udc69\u200d\ud83d\udc66","\ud83d\udc69\u200d\ud83d\udc66\u200d\ud83d\udc66","\ud83d\udc69\u200d\ud83d\udc67","\ud83d\udc69\u200d\ud83d\udc67\u200d\ud83d\udc66","\ud83d\udc69\u200d\ud83d\udc67\u200d\ud83d\udc67","\ud83e\udd33","\ud83d\udcaa","\ud83d\udc48","\ud83d\udc49","\u261d\ufe0f","\ud83d\udc46","\ud83d\udd95","\ud83d\udc47","\u270c\ufe0f","\ud83e\udd1e","\ud83d\udd96","\ud83e\udd18","\ud83e\udd19","\ud83d\udd90\ufe0f","\u270b","\ud83d\udc4c","\ud83d\udc4d","\ud83d\udc4e","\u270a","\ud83d\udc4a","\ud83e\udd1b","\ud83e\udd1c","\ud83e\udd1a","\ud83d\udc4b","\ud83e\udd1f","\u270d\ufe0f","\ud83d\udc4f","\ud83d\udc50","\ud83d\ude4c","\ud83e\udd32","\ud83d\ude4f","\ud83e\udd1d","\ud83d\udc85","\ud83d\udc42","\ud83d\udc43","\ud83d\udc63","\ud83d\udc40","\ud83d\udc41\ufe0f","\ud83d\udc41\ufe0f\u200d\ud83d\udde8\ufe0f","\ud83e\udde0","\ud83d\udc45","\ud83d\udc44","\ud83d\udc8b","\ud83d\udc98","\u2764\ufe0f","\ud83d\udc93","\ud83d\udc94","\ud83d\udc95","\ud83d\udc96","\ud83d\udc97","\ud83d\udc99","\ud83d\udc9a","\ud83d\udc9b","\ud83e\udde1","\ud83d\udc9c","\ud83d\udda4","\ud83d\udc9d","\ud83d\udc9e","\ud83d\udc9f","\u2763\ufe0f","\ud83d\udc8c","\ud83d\udca4","\ud83d\udca2","\ud83d\udca3","\ud83d\udca5","\ud83d\udca6","\ud83d\udca8","\ud83d\udcab","\ud83d\udcac","\ud83d\udde8\ufe0f","\ud83d\uddef\ufe0f","\ud83d\udcad","\ud83d\udd73\ufe0f","\ud83d\udc53","\ud83d\udd76\ufe0f","\ud83d\udc54","\ud83d\udc55","\ud83d\udc56","\ud83e\udde3","\ud83e\udde4","\ud83e\udde5","\ud83e\udde6","\ud83d\udc57","\ud83d\udc58","\ud83d\udc59","\ud83d\udc5a","\ud83d\udc5b","\ud83d\udc5c","\ud83d\udc5d","\ud83d\udecd\ufe0f","\ud83c\udf92","\ud83d\udc5e","\ud83d\udc5f","\ud83d\udc60","\ud83d\udc61","\ud83d\udc62","\ud83d\udc51","\ud83d\udc52","\ud83c\udfa9","\ud83c\udf93","\ud83e\udde2","\u26d1\ufe0f","\ud83d\udcff","\ud83d\udc84","\ud83d\udc8d","\ud83d\udc8e",
    }, "emoji/People.png"),
    new StaticEmojiPageModel(R.attr.emoji_category_nature, new String[] {
        "\ud83d\udc35","\ud83d\udc12","\ud83e\udd8d","\ud83d\udc36","\ud83d\udc15","\ud83d\udc29","\ud83d\udc3a","\ud83e\udd8a","\ud83d\udc31","\ud83d\udc08","\ud83e\udd81","\ud83d\udc2f","\ud83d\udc05","\ud83d\udc06","\ud83d\udc34","\ud83d\udc0e","\ud83e\udd84","\ud83e\udd93","\ud83e\udd8c","\ud83d\udc2e","\ud83d\udc02","\ud83d\udc03","\ud83d\udc04","\ud83d\udc37","\ud83d\udc16","\ud83d\udc17","\ud83d\udc3d","\ud83d\udc0f","\ud83d\udc11","\ud83d\udc10","\ud83d\udc2a","\ud83d\udc2b","\ud83e\udd92","\ud83d\udc18","\ud83e\udd8f","\ud83d\udc2d","\ud83d\udc01","\ud83d\udc00","\ud83d\udc39","\ud83d\udc30","\ud83d\udc07","\ud83d\udc3f\ufe0f","\ud83e\udd94","\ud83e\udd87","\ud83d\udc3b","\ud83d\udc28","\ud83d\udc3c","\ud83d\udc3e","\ud83e\udd83","\ud83d\udc14","\ud83d\udc13","\ud83d\udc23","\ud83d\udc24","\ud83d\udc25","\ud83d\udc26","\ud83d\udc27","\ud83d\udd4a\ufe0f","\ud83e\udd85","\ud83e\udd86","\ud83e\udd89","\ud83d\udc38","\ud83d\udc0a","\ud83d\udc22","\ud83e\udd8e","\ud83d\udc0d","\ud83d\udc32","\ud83d\udc09","\ud83e\udd95","\ud83e\udd96","\ud83d\udc33","\ud83d\udc0b","\ud83d\udc2c","\ud83d\udc1f","\ud83d\udc20","\ud83d\udc21","\ud83e\udd88","\ud83d\udc19","\ud83d\udc1a","\ud83e\udd80","\ud83e\udd90","\ud83e\udd91","\ud83d\udc0c","\ud83e\udd8b","\ud83d\udc1b","\ud83d\udc1c","\ud83d\udc1d","\ud83d\udc1e","\ud83e\udd97","\ud83d\udd77\ufe0f","\ud83d\udd78\ufe0f","\ud83e\udd82","\ud83d\udc90","\ud83c\udf38","\ud83d\udcae","\ud83c\udff5\ufe0f","\ud83c\udf39","\ud83e\udd40","\ud83c\udf3a","\ud83c\udf3b","\ud83c\udf3c","\ud83c\udf37","\ud83c\udf31","\ud83c\udf32","\ud83c\udf33","\ud83c\udf34","\ud83c\udf35","\ud83c\udf3e","\ud83c\udf3f","\u2618\ufe0f","\ud83c\udf40","\ud83c\udf41","\ud83c\udf42","\ud83c\udf43",
    }, "emoji/Nature.png"),
    new StaticEmojiPageModel(R.attr.emoji_category_foods, new String[] {
        "\ud83c\udf47","\ud83c\udf48","\ud83c\udf49","\ud83c\udf4a","\ud83c\udf4b","\ud83c\udf4c","\ud83c\udf4d","\ud83c\udf4e","\ud83c\udf4f","\ud83c\udf50","\ud83c\udf51","\ud83c\udf52","\ud83c\udf53","\ud83e\udd5d","\ud83c\udf45","\ud83e\udd65","\ud83e\udd51","\ud83c\udf46","\ud83e\udd54","\ud83e\udd55","\ud83c\udf3d","\ud83c\udf36\ufe0f","\ud83e\udd52","\ud83e\udd66","\ud83c\udf44","\ud83e\udd5c","\ud83c\udf30","\ud83c\udf5e","\ud83e\udd50","\ud83e\udd56","\ud83e\udd68","\ud83e\udd5e","\ud83e\uddc0","\ud83c\udf56","\ud83c\udf57","\ud83e\udd69","\ud83e\udd53","\ud83c\udf54","\ud83c\udf5f","\ud83c\udf55","\ud83c\udf2d","\ud83e\udd6a","\ud83c\udf2e","\ud83c\udf2f","\ud83e\udd59","\ud83e\udd5a","\ud83c\udf73","\ud83e\udd58","\ud83c\udf72","\ud83e\udd63","\ud83e\udd57","\ud83c\udf7f","\ud83e\udd6b","\ud83c\udf71","\ud83c\udf58","\ud83c\udf59","\ud83c\udf5a","\ud83c\udf5b","\ud83c\udf5c","\ud83c\udf5d","\ud83c\udf60","\ud83c\udf62","\ud83c\udf63","\ud83c\udf64","\ud83c\udf65","\ud83c\udf61","\ud83e\udd5f","\ud83e\udd60","\ud83e\udd61","\ud83c\udf66","\ud83c\udf67","\ud83c\udf68","\ud83c\udf69","\ud83c\udf6a","\ud83c\udf82","\ud83c\udf70","\ud83e\udd67","\ud83c\udf6b","\ud83c\udf6c","\ud83c\udf6d","\ud83c\udf6e","\ud83c\udf6f","\ud83c\udf7c","\ud83e\udd5b","\u2615","\ud83c\udf75","\ud83c\udf76","\ud83c\udf7e","\ud83c\udf77","\ud83c\udf78","\ud83c\udf79","\ud83c\udf7a","\ud83c\udf7b","\ud83e\udd42","\ud83e\udd43","\ud83e\udd64","\ud83e\udd62","\ud83c\udf7d\ufe0f","\ud83c\udf74","\ud83e\udd44","\ud83d\udd2a","\ud83c\udffa",
    }, "emoji/Foods.png"),
    new StaticEmojiPageModel(R.attr.emoji_category_activity, new String[] {
        "\ud83c\udf83","\ud83c\udf84","\ud83c\udf86","\ud83c\udf87","\u2728","\ud83c\udf88","\ud83c\udf89","\ud83c\udf8a","\ud83c\udf8b","\ud83c\udf8d","\ud83c\udf8e","\ud83c\udf8f","\ud83c\udf90","\ud83c\udf91","\ud83c\udf80","\ud83c\udf81","\ud83c\udf97\ufe0f","\ud83c\udf9f\ufe0f","\ud83c\udfab","\ud83c\udf96\ufe0f","\ud83c\udfc6","\ud83c\udfc5","\ud83e\udd47","\ud83e\udd48","\ud83e\udd49","\u26bd","\u26be","\ud83c\udfc0","\ud83c\udfd0","\ud83c\udfc8","\ud83c\udfc9","\ud83c\udfbe","\ud83c\udfb1","\ud83c\udfb3","\ud83c\udfcf","\ud83c\udfd1","\ud83c\udfd2","\ud83c\udfd3","\ud83c\udff8","\ud83e\udd4a","\ud83e\udd4b","\ud83e\udd45","\ud83c\udfaf","\u26f3","\u26f8\ufe0f","\ud83c\udfa3","\ud83c\udfbd","\ud83c\udfbf","\ud83d\udef7","\ud83e\udd4c","\ud83c\udfae","\ud83d\udd79\ufe0f","\ud83c\udfb2","\u2660\ufe0f","\u2665\ufe0f","\u2666\ufe0f","\u2663\ufe0f","\ud83c\udccf","\ud83c\udc04","\ud83c\udfb4",
    }, "emoji/Activity.png"),
    new StaticEmojiPageModel(R.attr.emoji_category_places, new String[] {
        "\ud83c\udf0d","\ud83c\udf0e","\ud83c\udf0f","\ud83c\udf10","\ud83d\uddfa\ufe0f","\ud83d\uddfe","\ud83c\udfd4\ufe0f","\u26f0\ufe0f","\ud83c\udf0b","\ud83d\uddfb","\ud83c\udfd5\ufe0f","\ud83c\udfd6\ufe0f","\ud83c\udfdc\ufe0f","\ud83c\udfdd\ufe0f","\ud83c\udfde\ufe0f","\ud83c\udfdf\ufe0f","\ud83c\udfdb\ufe0f","\ud83c\udfd7\ufe0f","\ud83c\udfd8\ufe0f","\ud83c\udfd9\ufe0f","\ud83c\udfda\ufe0f","\ud83c\udfe0","\ud83c\udfe1","\ud83c\udfe2","\ud83c\udfe3","\ud83c\udfe4","\ud83c\udfe5","\ud83c\udfe6","\ud83c\udfe8","\ud83c\udfe9","\ud83c\udfea","\ud83c\udfeb","\ud83c\udfec","\ud83c\udfed","\ud83c\udfef","\ud83c\udff0","\ud83d\udc92","\ud83d\uddfc","\ud83d\uddfd","\u26ea","\ud83d\udd4c","\ud83d\udd4d","\u26e9\ufe0f","\ud83d\udd4b","\u26f2","\u26fa","\ud83c\udf01","\ud83c\udf03","\ud83c\udf04","\ud83c\udf05","\ud83c\udf06","\ud83c\udf07","\ud83c\udf09","\u2668\ufe0f","\ud83c\udf0c","\ud83c\udfa0","\ud83c\udfa1","\ud83c\udfa2","\ud83d\udc88","\ud83c\udfaa","\ud83c\udfad","\ud83d\uddbc\ufe0f","\ud83c\udfa8","\ud83c\udfb0","\ud83d\ude82","\ud83d\ude83","\ud83d\ude84","\ud83d\ude85","\ud83d\ude86","\ud83d\ude87","\ud83d\ude88","\ud83d\ude89","\ud83d\ude8a","\ud83d\ude9d","\ud83d\ude9e","\ud83d\ude8b","\ud83d\ude8c","\ud83d\ude8d","\ud83d\ude8e","\ud83d\ude90","\ud83d\ude91","\ud83d\ude92","\ud83d\ude93","\ud83d\ude94","\ud83d\ude95","\ud83d\ude96","\ud83d\ude97","\ud83d\ude98","\ud83d\ude99","\ud83d\ude9a","\ud83d\ude9b","\ud83d\ude9c","\ud83d\udeb2","\ud83d\udef4","\ud83d\udef5","\ud83d\ude8f","\ud83d\udee3\ufe0f","\ud83d\udee4\ufe0f","\u26fd","\ud83d\udea8","\ud83d\udea5","\ud83d\udea6","\ud83d\udea7","\ud83d\uded1","\u2693","\u26f5","\ud83d\udef6","\ud83d\udea4","\ud83d\udef3\ufe0f","\u26f4\ufe0f","\ud83d\udee5\ufe0f","\ud83d\udea2","\u2708\ufe0f","\ud83d\udee9\ufe0f","\ud83d\udeeb","\ud83d\udeec","\ud83d\udcba","\ud83d\ude81","\ud83d\ude9f","\ud83d\udea0","\ud83d\udea1","\ud83d\udef0\ufe0f","\ud83d\ude80","\ud83d\udef8","\ud83d\udece\ufe0f","\ud83d\udeaa","\ud83d\udecf\ufe0f","\ud83d\udecb\ufe0f","\ud83d\udebd","\ud83d\udebf","\ud83d\udec1","\u231b","\u23f3","\u231a","\u23f0","\u23f1\ufe0f","\u23f2\ufe0f","\ud83d\udd70\ufe0f","\ud83d\udd5b","\ud83d\udd67","\ud83d\udd50","\ud83d\udd5c","\ud83d\udd51","\ud83d\udd5d","\ud83d\udd52","\ud83d\udd5e","\ud83d\udd53","\ud83d\udd5f","\ud83d\udd54","\ud83d\udd60","\ud83d\udd55","\ud83d\udd61","\ud83d\udd56","\ud83d\udd62","\ud83d\udd57","\ud83d\udd63","\ud83d\udd58","\ud83d\udd64","\ud83d\udd59","\ud83d\udd65","\ud83d\udd5a","\ud83d\udd66","\ud83c\udf11","\ud83c\udf12","\ud83c\udf13","\ud83c\udf14","\ud83c\udf15","\ud83c\udf16","\ud83c\udf17","\ud83c\udf18","\ud83c\udf19","\ud83c\udf1a","\ud83c\udf1b","\ud83c\udf1c","\ud83c\udf21\ufe0f","\u2600\ufe0f","\ud83c\udf1d","\ud83c\udf1e","\u2b50","\ud83c\udf1f","\ud83c\udf20","\u2601\ufe0f","\u26c5","\u26c8\ufe0f","\ud83c\udf24\ufe0f","\ud83c\udf25\ufe0f","\ud83c\udf26\ufe0f","\ud83c\udf27\ufe0f","\ud83c\udf28\ufe0f","\ud83c\udf29\ufe0f","\ud83c\udf2a\ufe0f","\ud83c\udf2b\ufe0f","\ud83c\udf2c\ufe0f","\ud83c\udf00","\ud83c\udf08","\ud83c\udf02","\u2602\ufe0f","\u2614","\u26f1\ufe0f","\u26a1","\u2744\ufe0f","\u2603\ufe0f","\u26c4","\u2604\ufe0f","\ud83d\udd25","\ud83d\udca7","\ud83c\udf0a",
    }, "emoji/Places.png"),
    new StaticEmojiPageModel(R.attr.emoji_category_objects, new String[] {
        "\ud83d\udd07","\ud83d\udd08","\ud83d\udd09","\ud83d\udd0a","\ud83d\udce2","\ud83d\udce3","\ud83d\udcef","\ud83d\udd14","\ud83d\udd15","\ud83c\udfbc","\ud83c\udfb5","\ud83c\udfb6","\ud83c\udf99\ufe0f","\ud83c\udf9a\ufe0f","\ud83c\udf9b\ufe0f","\ud83c\udfa4","\ud83c\udfa7","\ud83d\udcfb","\ud83c\udfb7","\ud83c\udfb8","\ud83c\udfb9","\ud83c\udfba","\ud83c\udfbb","\ud83e\udd41","\ud83d\udcf1","\ud83d\udcf2","\u260e\ufe0f","\ud83d\udcde","\ud83d\udcdf","\ud83d\udce0","\ud83d\udd0b","\ud83d\udd0c","\ud83d\udcbb","\ud83d\udda5\ufe0f","\ud83d\udda8\ufe0f","\u2328\ufe0f","\ud83d\uddb1\ufe0f","\ud83d\uddb2\ufe0f","\ud83d\udcbd","\ud83d\udcbe","\ud83d\udcbf","\ud83d\udcc0","\ud83c\udfa5","\ud83c\udf9e\ufe0f","\ud83d\udcfd\ufe0f","\ud83c\udfac","\ud83d\udcfa","\ud83d\udcf7","\ud83d\udcf8","\ud83d\udcf9","\ud83d\udcfc","\ud83d\udd0d","\ud83d\udd0e","\ud83d\udd2c","\ud83d\udd2d","\ud83d\udce1","\ud83d\udd6f\ufe0f","\ud83d\udca1","\ud83d\udd26","\ud83c\udfee","\ud83d\udcd4","\ud83d\udcd5","\ud83d\udcd6","\ud83d\udcd7","\ud83d\udcd8","\ud83d\udcd9","\ud83d\udcda","\ud83d\udcd3","\ud83d\udcd2","\ud83d\udcc3","\ud83d\udcdc","\ud83d\udcc4","\ud83d\udcf0","\ud83d\uddde\ufe0f","\ud83d\udcd1","\ud83d\udd16","\ud83c\udff7\ufe0f","\ud83d\udcb0","\ud83d\udcb4","\ud83d\udcb5","\ud83d\udcb6","\ud83d\udcb7","\ud83d\udcb8","\ud83d\udcb3","\ud83d\udcb9","\ud83d\udcb1","\ud83d\udcb2","\u2709\ufe0f","\ud83d\udce7","\ud83d\udce8","\ud83d\udce9","\ud83d\udce4","\ud83d\udce5","\ud83d\udce6","\ud83d\udceb","\ud83d\udcea","\ud83d\udcec","\ud83d\udced","\ud83d\udcee","\ud83d\uddf3\ufe0f","\u270f\ufe0f","\u2712\ufe0f","\ud83d\udd8b\ufe0f","\ud83d\udd8a\ufe0f","\ud83d\udd8c\ufe0f","\ud83d\udd8d\ufe0f","\ud83d\udcdd","\ud83d\udcbc","\ud83d\udcc1","\ud83d\udcc2","\ud83d\uddc2\ufe0f","\ud83d\udcc5","\ud83d\udcc6","\ud83d\uddd2\ufe0f","\ud83d\uddd3\ufe0f","\ud83d\udcc7","\ud83d\udcc8","\ud83d\udcc9","\ud83d\udcca","\ud83d\udccb","\ud83d\udccc","\ud83d\udccd","\ud83d\udcce","\ud83d\udd87\ufe0f","\ud83d\udccf","\ud83d\udcd0","\u2702\ufe0f","\ud83d\uddc3\ufe0f","\ud83d\uddc4\ufe0f","\ud83d\uddd1\ufe0f","\ud83d\udd12","\ud83d\udd13","\ud83d\udd0f","\ud83d\udd10","\ud83d\udd11","\ud83d\udddd\ufe0f","\ud83d\udd28","\u26cf\ufe0f","\u2692\ufe0f","\ud83d\udee0\ufe0f","\ud83d\udde1\ufe0f","\u2694\ufe0f","\ud83d\udd2b","\ud83c\udff9","\ud83d\udee1\ufe0f","\ud83d\udd27","\ud83d\udd29","\u2699\ufe0f","\ud83d\udddc\ufe0f","\u2697\ufe0f","\u2696\ufe0f","\ud83d\udd17","\u26d3\ufe0f","\ud83d\udc89","\ud83d\udc8a","\ud83d\udeac","\u26b0\ufe0f","\u26b1\ufe0f","\ud83d\uddff","\ud83d\udee2\ufe0f","\ud83d\udd2e","\ud83d\uded2",
    }, "emoji/Objects.png"),
    new StaticEmojiPageModel(R.attr.emoji_category_symbol, new String[] {
        "\ud83c\udfe7","\ud83d\udeae","\ud83d\udeb0","\u267f","\ud83d\udeb9","\ud83d\udeba","\ud83d\udebb","\ud83d\udebc","\ud83d\udebe","\ud83d\udec2","\ud83d\udec3","\ud83d\udec4","\ud83d\udec5","\u26a0\ufe0f","\ud83d\udeb8","\u26d4","\ud83d\udeab","\ud83d\udeb3","\ud83d\udead","\ud83d\udeaf","\ud83d\udeb1","\ud83d\udeb7","\ud83d\udcf5","\ud83d\udd1e","\u2622\ufe0f","\u2623\ufe0f","\u2b06\ufe0f","\u2197\ufe0f","\u27a1\ufe0f","\u2198\ufe0f","\u2b07\ufe0f","\u2199\ufe0f","\u2b05\ufe0f","\u2196\ufe0f","\u2195\ufe0f","\u2194\ufe0f","\u21a9\ufe0f","\u21aa\ufe0f","\u2934\ufe0f","\u2935\ufe0f","\ud83d\udd03","\ud83d\udd04","\ud83d\udd19","\ud83d\udd1a","\ud83d\udd1b","\ud83d\udd1c","\ud83d\udd1d","\ud83d\uded0","\u269b\ufe0f","\ud83d\udd49\ufe0f","\u2721\ufe0f","\u2638\ufe0f","\u262f\ufe0f","\u271d\ufe0f","\u2626\ufe0f","\u262a\ufe0f","\u262e\ufe0f","\ud83d\udd4e","\ud83d\udd2f","\u2648","\u2649","\u264a","\u264b","\u264c","\u264d","\u264e","\u264f","\u2650","\u2651","\u2652","\u2653","\u26ce","\ud83d\udd00","\ud83d\udd01","\ud83d\udd02","\u25b6\ufe0f","\u23e9","\u23ed\ufe0f","\u23ef\ufe0f","\u25c0\ufe0f","\u23ea","\u23ee\ufe0f","\ud83d\udd3c","\u23eb","\ud83d\udd3d","\u23ec","\u23f8\ufe0f","\u23f9\ufe0f","\u23fa\ufe0f","\u23cf\ufe0f","\ud83c\udfa6","\ud83d\udd05","\ud83d\udd06","\ud83d\udcf6","\ud83d\udcf3","\ud83d\udcf4","\u267b\ufe0f","\u269c\ufe0f","\ud83d\udd31","\ud83d\udcdb","\ud83d\udd30","\u2b55","\u2705","\u2611\ufe0f","\u2714\ufe0f","\u2716\ufe0f","\u274c","\u274e","\u2795","\u2796","\u2797","\u27b0","\u27bf","\u303d\ufe0f","\u2733\ufe0f","\u2734\ufe0f","\u2747\ufe0f","\u203c\ufe0f","\u2049\ufe0f","\u2753","\u2754","\u2755","\u2757","\u3030\ufe0f","\u00a9\ufe0f","\u00ae\ufe0f","\u2122\ufe0f","\u0023\ufe0f\u20e3","\u002a\ufe0f\u20e3","\u0030\ufe0f\u20e3","\u0031\ufe0f\u20e3","\u0032\ufe0f\u20e3","\u0033\ufe0f\u20e3","\u0034\ufe0f\u20e3","\u0035\ufe0f\u20e3","\u0036\ufe0f\u20e3","\u0037\ufe0f\u20e3","\u0038\ufe0f\u20e3","\u0039\ufe0f\u20e3","\ud83d\udd1f","\ud83d\udcaf","\ud83d\udd20","\ud83d\udd21","\ud83d\udd22","\ud83d\udd23","\ud83d\udd24","\ud83c\udd70\ufe0f","\ud83c\udd8e","\ud83c\udd71\ufe0f","\ud83c\udd91","\ud83c\udd92","\ud83c\udd93","\u2139\ufe0f","\ud83c\udd94","\u24c2\ufe0f","\ud83c\udd95","\ud83c\udd96","\ud83c\udd7e\ufe0f","\ud83c\udd97","\ud83c\udd7f\ufe0f","\ud83c\udd98","\ud83c\udd99","\ud83c\udd9a","\ud83c\ude01","\ud83c\ude02\ufe0f","\ud83c\ude37\ufe0f","\ud83c\ude36","\ud83c\ude2f","\ud83c\ude50","\ud83c\ude39","\ud83c\ude1a","\ud83c\ude32","\ud83c\ude51","\ud83c\ude38","\ud83c\ude34","\ud83c\ude33","\u3297\ufe0f","\u3299\ufe0f","\ud83c\ude3a","\ud83c\ude35","\u25aa\ufe0f","\u25ab\ufe0f","\u25fb\ufe0f","\u25fc\ufe0f","\u25fd","\u25fe","\u2b1b","\u2b1c","\ud83d\udd36","\ud83d\udd37","\ud83d\udd38","\ud83d\udd39","\ud83d\udd3a","\ud83d\udd3b","\ud83d\udca0","\ud83d\udd18","\ud83d\udd32","\ud83d\udd33","\u26aa","\u26ab","\ud83d\udd34","\ud83d\udd35",
    }, "emoji/Symbols.png"),
    new StaticEmojiPageModel(R.attr.emoji_category_flags, new String[] {
        "\ud83c\udfc1","\ud83d\udea9","\ud83c\udf8c","\ud83c\udff4","\ud83c\udff3\ufe0f","\ud83c\udff3\ufe0f\u200d\ud83c\udf08","\ud83c\udde6\ud83c\udde8","\ud83c\udde6\ud83c\udde9","\ud83c\udde6\ud83c\uddea","\ud83c\udde6\ud83c\uddeb","\ud83c\udde6\ud83c\uddec","\ud83c\udde6\ud83c\uddee","\ud83c\udde6\ud83c\uddf1","\ud83c\udde6\ud83c\uddf2","\ud83c\udde6\ud83c\uddf4","\ud83c\udde6\ud83c\uddf6","\ud83c\udde6\ud83c\uddf7","\ud83c\udde6\ud83c\uddf8","\ud83c\udde6\ud83c\uddf9","\ud83c\udde6\ud83c\uddfa","\ud83c\udde6\ud83c\uddfc","\ud83c\udde6\ud83c\uddfd","\ud83c\udde6\ud83c\uddff","\ud83c\udde7\ud83c\udde6","\ud83c\udde7\ud83c\udde7","\ud83c\udde7\ud83c\udde9","\ud83c\udde7\ud83c\uddea","\ud83c\udde7\ud83c\uddeb","\ud83c\udde7\ud83c\uddec","\ud83c\udde7\ud83c\udded","\ud83c\udde7\ud83c\uddee","\ud83c\udde7\ud83c\uddef","\ud83c\udde7\ud83c\uddf1","\ud83c\udde7\ud83c\uddf2","\ud83c\udde7\ud83c\uddf3","\ud83c\udde7\ud83c\uddf4","\ud83c\udde7\ud83c\uddf6","\ud83c\udde7\ud83c\uddf7","\ud83c\udde7\ud83c\uddf8","\ud83c\udde7\ud83c\uddf9","\ud83c\udde7\ud83c\uddfb","\ud83c\udde7\ud83c\uddfc","\ud83c\udde7\ud83c\uddfe","\ud83c\udde7\ud83c\uddff","\ud83c\udde8\ud83c\udde6","\ud83c\udde8\ud83c\udde8","\ud83c\udde8\ud83c\udde9","\ud83c\udde8\ud83c\uddeb","\ud83c\udde8\ud83c\uddec","\ud83c\udde8\ud83c\udded","\ud83c\udde8\ud83c\uddee","\ud83c\udde8\ud83c\uddf0","\ud83c\udde8\ud83c\uddf1","\ud83c\udde8\ud83c\uddf2","\ud83c\udde8\ud83c\uddf3","\ud83c\udde8\ud83c\uddf4","\ud83c\udde8\ud83c\uddf5","\ud83c\udde8\ud83c\uddf7","\ud83c\udde8\ud83c\uddfa","\ud83c\udde8\ud83c\uddfb","\ud83c\udde8\ud83c\uddfc","\ud83c\udde8\ud83c\uddfd","\ud83c\udde8\ud83c\uddfe","\ud83c\udde8\ud83c\uddff","\ud83c\udde9\ud83c\uddea","\ud83c\udde9\ud83c\uddec","\ud83c\udde9\ud83c\uddef","\ud83c\udde9\ud83c\uddf0","\ud83c\udde9\ud83c\uddf2","\ud83c\udde9\ud83c\uddf4","\ud83c\udde9\ud83c\uddff","\ud83c\uddea\ud83c\udde6","\ud83c\uddea\ud83c\udde8","\ud83c\uddea\ud83c\uddea","\ud83c\uddea\ud83c\uddec","\ud83c\uddea\ud83c\udded","\ud83c\uddea\ud83c\uddf7","\ud83c\uddea\ud83c\uddf8","\ud83c\uddea\ud83c\uddf9","\ud83c\uddea\ud83c\uddfa","\ud83c\uddeb\ud83c\uddee","\ud83c\uddeb\ud83c\uddef","\ud83c\uddeb\ud83c\uddf0","\ud83c\uddeb\ud83c\uddf2","\ud83c\uddeb\ud83c\uddf4","\ud83c\uddeb\ud83c\uddf7","\ud83c\uddec\ud83c\udde6","\ud83c\uddec\ud83c\udde7","\ud83c\uddec\ud83c\udde9","\ud83c\uddec\ud83c\uddea","\ud83c\uddec\ud83c\uddeb","\ud83c\uddec\ud83c\uddec","\ud83c\uddec\ud83c\udded","\ud83c\uddec\ud83c\uddee","\ud83c\uddec\ud83c\uddf1","\ud83c\uddec\ud83c\uddf2","\ud83c\uddec\ud83c\uddf3","\ud83c\uddec\ud83c\uddf5","\ud83c\uddec\ud83c\uddf6","\ud83c\uddec\ud83c\uddf7","\ud83c\uddec\ud83c\uddf8","\ud83c\uddec\ud83c\uddf9","\ud83c\uddec\ud83c\uddfa","\ud83c\uddec\ud83c\uddfc","\ud83c\uddec\ud83c\uddfe","\ud83c\udded\ud83c\uddf0","\ud83c\udded\ud83c\uddf2","\ud83c\udded\ud83c\uddf3","\ud83c\udded\ud83c\uddf7","\ud83c\udded\ud83c\uddf9","\ud83c\udded\ud83c\uddfa","\ud83c\uddee\ud83c\udde8","\ud83c\uddee\ud83c\udde9","\ud83c\uddee\ud83c\uddea","\ud83c\uddee\ud83c\uddf1","\ud83c\uddee\ud83c\uddf2","\ud83c\uddee\ud83c\uddf3","\ud83c\uddee\ud83c\uddf4","\ud83c\uddee\ud83c\uddf6","\ud83c\uddee\ud83c\uddf7","\ud83c\uddee\ud83c\uddf8","\ud83c\uddee\ud83c\uddf9","\ud83c\uddef\ud83c\uddea","\ud83c\uddef\ud83c\uddf2","\ud83c\uddef\ud83c\uddf4","\ud83c\uddef\ud83c\uddf5","\ud83c\uddf0\ud83c\uddea","\ud83c\uddf0\ud83c\uddec","\ud83c\uddf0\ud83c\udded","\ud83c\uddf0\ud83c\uddee","\ud83c\uddf0\ud83c\uddf2","\ud83c\uddf0\ud83c\uddf3","\ud83c\uddf0\ud83c\uddf5","\ud83c\uddf0\ud83c\uddf7","\ud83c\uddf0\ud83c\uddfc","\ud83c\uddf0\ud83c\uddfe","\ud83c\uddf0\ud83c\uddff","\ud83c\uddf1\ud83c\udde6","\ud83c\uddf1\ud83c\udde7","\ud83c\uddf1\ud83c\udde8","\ud83c\uddf1\ud83c\uddee","\ud83c\uddf1\ud83c\uddf0","\ud83c\uddf1\ud83c\uddf7","\ud83c\uddf1\ud83c\uddf8","\ud83c\uddf1\ud83c\uddf9","\ud83c\uddf1\ud83c\uddfa","\ud83c\uddf1\ud83c\uddfb","\ud83c\uddf1\ud83c\uddfe","\ud83c\uddf2\ud83c\udde6","\ud83c\uddf2\ud83c\udde8","\ud83c\uddf2\ud83c\udde9","\ud83c\uddf2\ud83c\uddea","\ud83c\uddf2\ud83c\uddeb","\ud83c\uddf2\ud83c\uddec","\ud83c\uddf2\ud83c\udded","\ud83c\uddf2\ud83c\uddf0","\ud83c\uddf2\ud83c\uddf1","\ud83c\uddf2\ud83c\uddf2","\ud83c\uddf2\ud83c\uddf3","\ud83c\uddf2\ud83c\uddf4","\ud83c\uddf2\ud83c\uddf5","\ud83c\uddf2\ud83c\uddf6","\ud83c\uddf2\ud83c\uddf7","\ud83c\uddf2\ud83c\uddf8","\ud83c\uddf2\ud83c\uddf9","\ud83c\uddf2\ud83c\uddfa","\ud83c\uddf2\ud83c\uddfb","\ud83c\uddf2\ud83c\uddfc","\ud83c\uddf2\ud83c\uddfd","\ud83c\uddf2\ud83c\uddfe","\ud83c\uddf2\ud83c\uddff","\ud83c\uddf3\ud83c\udde6","\ud83c\uddf3\ud83c\udde8","\ud83c\uddf3\ud83c\uddea","\ud83c\uddf3\ud83c\uddeb","\ud83c\uddf3\ud83c\uddec","\ud83c\uddf3\ud83c\uddee","\ud83c\uddf3\ud83c\uddf1","\ud83c\uddf3\ud83c\uddf4","\ud83c\uddf3\ud83c\uddf5","\ud83c\uddf3\ud83c\uddf7","\ud83c\uddf3\ud83c\uddfa","\ud83c\uddf3\ud83c\uddff","\ud83c\uddf4\ud83c\uddf2","\ud83c\uddf5\ud83c\udde6","\ud83c\uddf5\ud83c\uddea","\ud83c\uddf5\ud83c\uddeb","\ud83c\uddf5\ud83c\uddec","\ud83c\uddf5\ud83c\udded","\ud83c\uddf5\ud83c\uddf0","\ud83c\uddf5\ud83c\uddf1","\ud83c\uddf5\ud83c\uddf2","\ud83c\uddf5\ud83c\uddf3","\ud83c\uddf5\ud83c\uddf7","\ud83c\uddf5\ud83c\uddf8","\ud83c\uddf5\ud83c\uddf9","\ud83c\uddf5\ud83c\uddfc","\ud83c\uddf5\ud83c\uddfe","\ud83c\uddf6\ud83c\udde6","\ud83c\uddf7\ud83c\uddea","\ud83c\uddf7\ud83c\uddf4","\ud83c\uddf7\ud83c\uddf8","\ud83c\uddf7\ud83c\uddfa","\ud83c\uddf7\ud83c\uddfc","\ud83c\uddf8\ud83c\udde6","\ud83c\uddf8\ud83c\udde7","\ud83c\uddf8\ud83c\udde8","\ud83c\uddf8\ud83c\udde9","\ud83c\uddf8\ud83c\uddea","\ud83c\uddf8\ud83c\uddec","\ud83c\uddf8\ud83c\udded","\ud83c\uddf8\ud83c\uddee","\ud83c\uddf8\ud83c\uddef","\ud83c\uddf8\ud83c\uddf0","\ud83c\uddf8\ud83c\uddf1","\ud83c\uddf8\ud83c\uddf2","\ud83c\uddf8\ud83c\uddf3","\ud83c\uddf8\ud83c\uddf4","\ud83c\uddf8\ud83c\uddf7","\ud83c\uddf8\ud83c\uddf8","\ud83c\uddf8\ud83c\uddf9","\ud83c\uddf8\ud83c\uddfb","\ud83c\uddf8\ud83c\uddfd","\ud83c\uddf8\ud83c\uddfe","\ud83c\uddf8\ud83c\uddff","\ud83c\uddf9\ud83c\udde6","\ud83c\uddf9\ud83c\udde8","\ud83c\uddf9\ud83c\udde9","\ud83c\uddf9\ud83c\uddeb","\ud83c\uddf9\ud83c\uddec","\ud83c\uddf9\ud83c\udded","\ud83c\uddf9\ud83c\uddef","\ud83c\uddf9\ud83c\uddf0","\ud83c\uddf9\ud83c\uddf1","\ud83c\uddf9\ud83c\uddf2","\ud83c\uddf9\ud83c\uddf3","\ud83c\uddf9\ud83c\uddf4","\ud83c\uddf9\ud83c\uddf7","\ud83c\uddf9\ud83c\uddf9","\ud83c\uddf9\ud83c\uddfb","\ud83c\uddf9\ud83c\uddfc","\ud83c\uddf9\ud83c\uddff","\ud83c\uddfa\ud83c\udde6","\ud83c\uddfa\ud83c\uddec","\ud83c\uddfa\ud83c\uddf2","\ud83c\uddfa\ud83c\uddf8","\ud83c\uddfa\ud83c\uddfe","\ud83c\uddfa\ud83c\uddff","\ud83c\uddfb\ud83c\udde6","\ud83c\uddfb\ud83c\udde8","\ud83c\uddfb\ud83c\uddea","\ud83c\uddfb\ud83c\uddec","\ud83c\uddfb\ud83c\uddee","\ud83c\uddfb\ud83c\uddf3","\ud83c\uddfb\ud83c\uddfa","\ud83c\uddfc\ud83c\uddeb","\ud83c\uddfc\ud83c\uddf8","\ud83c\uddfd\ud83c\uddf0","\ud83c\uddfe\ud83c\uddea","\ud83c\uddfe\ud83c\uddf9","\ud83c\uddff\ud83c\udde6","\ud83c\uddff\ud83c\uddf2","\ud83c\uddff\ud83c\uddfc","\ud83c\udff4\udb40\udc67\udb40\udc62\udb40\udc65\udb40\udc6e\udb40\udc67\udb40\udc7f","\ud83c\udff4\udb40\udc67\udb40\udc62\udb40\udc73\udb40\udc63\udb40\udc74\udb40\udc7f","\ud83c\udff4\udb40\udc67\udb40\udc62\udb40\udc77\udb40\udc6c\udb40\udc73\udb40\udc7f",
    }, "emoji/Flags.png"),
    new StaticEmojiPageModel(R.attr.emoji_category_emoticons, new String[] {
      ":-)",          ";-)",          "(-:",          ":->",          ":-D",          "\\o/",
      ":-P",          "B-)",          ":-$",          ":-*",          "O:-)",         "=-O",
      "O_O",          "O_o",          "o_O",          ":O",           ":-!",          ":-x",
      ":-|",          ":-\\",         ":-(",          ":'(",          ":-[",          ">:-(",
      "^.^",          "^_^",                          "\\(\u02c6\u02da\u02c6)/",
      "\u30fd(\u00b0\u25c7\u00b0 )\u30ce",            "\u00af\\(\u00b0_o)/\u00af",
      "\u00af\\_(\u30c4)_/\u00af",                    "(\u00ac_\u00ac)",
      "(>_<)",        "(\u2565\ufe4f\u2565)",         "(\u261e\uff9f\u30ee\uff9f)\u261e",
      "\u261c(\uff9f\u30ee\uff9f\u261c)",             "\u261c(\u2312\u25bd\u2312)\u261e",
      "(\u256f\u00b0\u25a1\u00b0)\u256f\ufe35",       "\u253b\u2501\u253b",
      "\u252c\u2500\u252c",                           "\u30ce(\u00b0\u2013\u00b0\u30ce)",
      "(^._.^)\uff89",                                "\u0e05^\u2022\ufecc\u2022^\u0e05",
      "(\u2022_\u2022)",                              " \u25a0-\u25a0\u00ac <(\u2022_\u2022) ",
      "(\u25a0_\u25a0\u00ac)"
    }, null));

  static final List<Pair<String, String>> OBSOLETE = new LinkedList<Pair<String, String>>() {{
    add(new Pair<>("\ud83c\udfc3", "\ud83c\udfc3\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83c\udfc4", "\ud83c\udfc4\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83c\udfca", "\ud83c\udfca\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83c\udfcb\ufe0f", "\ud83c\udfcb\ufe0f\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83c\udfcc\ufe0f", "\ud83c\udfcc\ufe0f\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83d\udc6a", "\ud83d\udc68\u200d\ud83d\udc69\u200d\ud83d\udc66"));
    add(new Pair<>("\ud83d\udc6e", "\ud83d\udc6e\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83d\udc6f", "\ud83d\udc6f\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83d\udc71", "\ud83d\udc71\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83d\udc73", "\ud83d\udc73\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83d\udc77", "\ud83d\udc77\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83d\udc81", "\ud83d\udc81\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83d\udc82", "\ud83d\udc82\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83d\udc86", "\ud83d\udc86\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83d\udc87", "\ud83d\udc87\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83d\udc8f", "\ud83d\udc69\u200d\u2764\ufe0f\u200d\ud83d\udc8b\u200d\ud83d\udc68"));
    add(new Pair<>("\ud83d\udc91", "\ud83d\udc69\u200d\u2764\ufe0f\u200d\ud83d\udc68"));
    add(new Pair<>("\ud83d\udd75\ufe0f", "\ud83d\udd75\ufe0f\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83d\ude45", "\ud83d\ude45\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83d\ude46", "\ud83d\ude46\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83d\ude47", "\ud83d\ude47\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83d\ude4b", "\ud83d\ude4b\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83d\ude4d", "\ud83d\ude4d\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83d\ude4e", "\ud83d\ude4e\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83d\udea3", "\ud83d\udea3\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83d\udeb4", "\ud83d\udeb4\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83d\udeb5", "\ud83d\udeb5\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83d\udeb6", "\ud83d\udeb6\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83e\uddd6", "\ud83e\uddd6\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83e\uddd7", "\ud83e\uddd7\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83e\uddd8", "\ud83e\uddd8\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83e\uddd9", "\ud83e\uddd9\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83e\uddda", "\ud83e\uddda\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83e\udddb", "\ud83e\udddb\u200d\u2640\ufe0f"));
    add(new Pair<>("\ud83e\udddc", "\ud83e\udddc\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83e\udddd", "\ud83e\udddd\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83e\uddde", "\ud83e\uddde\u200d\u2642\ufe0f"));
    add(new Pair<>("\ud83e\udddf", "\ud83e\udddf\u200d\u2642\ufe0f"));
    add(new Pair<>("\u26f9\ufe0f", "\u26f9\ufe0f\u200d\u2642\ufe0f"));
  }};
}
