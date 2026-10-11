"""Combine Flutter Web's Roboto Latin with the existing Noto Sans SC glyphs.

Compose selects one font file per weight on Wasm. A single character map keeps
Chinese fallback and Latin widths consistent without depending on system fonts.
Requires fontTools; the generated TTF is checked in and builds need no Python.
"""

from pathlib import Path

from fontTools.pens.recordingPen import DecomposingRecordingPen
from fontTools.pens.transformPen import TransformPen
from fontTools.pens.ttGlyphPen import TTGlyphPen
from fontTools.ttLib import TTFont
from fontTools.varLib.instancer import instantiateVariableFont


def generate_preview_font() -> None:
    font_directory = Path(__file__).resolve().parents[1] / "src/commonMain/composeResources/font"
    chinese = TTFont(font_directory / "noto_sans_sc_wght.ttf")
    # Flutter's downloaded fallback is regular; bold text uses synthetic weight.
    chinese = instantiateVariableFont(chinese, {"wght": 400}, inplace=True)
    latin = TTFont(font_directory / "roboto_regular.ttf")
    latin_glyphs = latin.getGlyphSet()
    scale = chinese["head"].unitsPerEm / latin["head"].unitsPerEm
    glyph_order = list(chinese.getGlyphOrder())

    for codepoint, source_name in latin.getBestCmap().items():
        name = f"preview_latin_{codepoint:04x}"
        recording = DecomposingRecordingPen(latin_glyphs)
        latin_glyphs[source_name].draw(recording)
        pen = TTGlyphPen(None)
        recording.replay(TransformPen(pen, (scale, 0, 0, scale, 0, 0)))
        chinese["glyf"][name] = pen.glyph()
        advance, bearing = latin["hmtx"][source_name]
        chinese["hmtx"][name] = (round(advance * scale), round(bearing * scale))
        if "vmtx" in chinese:
            chinese["vmtx"][name] = (chinese["head"].unitsPerEm, 0)
        glyph_order.append(name)
        for table in chinese["cmap"].tables:
            if table.isUnicode() and (table.format in (12, 13) or codepoint <= 0xffff):
                table.cmap[codepoint] = name

    chinese.setGlyphOrder(glyph_order)
    # This is a modified font: use a distinct family instead of a reserved name.
    for record in chinese["name"].names:
        names = {1: "Hyper UI Preview", 2: "Regular", 3: "HyperUIPreview-Regular",
                 4: "Hyper UI Preview Regular", 6: "HyperUIPreview-Regular",
                 16: "Hyper UI Preview", 17: "Regular"}
        if record.nameID in names:
            record.string = names[record.nameID].encode(record.getEncoding())
    output = font_directory / "hyper_ui_preview.ttf"
    chinese.save(output)
    print(f"Generated {output.name}: {len(chinese.getBestCmap())} characters")


if __name__ == "__main__":
    generate_preview_font()
