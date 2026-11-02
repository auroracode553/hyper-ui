---
name: hyper-ui-glass-design
description: Apply HyperUI's approved HyperOS-inspired frosted-glass visual language when creating, refactoring, or reviewing visual Jetpack Compose components in this repository. Use for HyperUI component surfaces, interaction states, previews, and their documentation; do not use for consumer-app business UI or non-visual maintenance.
---

# HyperUI Glass Design

Use `HyperGlassStyles` and `hyperControlSurface` as the material anchor. Read the requested component's current source, documentation and Preview before choosing its existing internal role. Keep the material calm and preserve its semantic state boundaries.

## Material language

- Build glass from a white translucent base in both themes. Let dark backgrounds mix that white into neutral gray; do not switch the material itself to a near-black fill.
- Use one continuous face and at most one contextual shadow.
- Do not add decorative hard borders, nested rings, isolated specular arcs, caustic streaks, or multiple stacked shadows. Preserve Checkbox/Radio state borders, field boundaries, Toast tone borders and caller-provided borders; these replace the default outer highlight.
- Semantic focus and error indication should use one restrained color cue rather than stacked outlines.
- Keep icons and text high contrast. Tint the material layer rather than reducing foreground legibility.
- Avoid stacking a light glass surface directly on another light glass surface. Glass should normally float over page content or a stable opaque background.

## Hierarchy and state

- Button keeps its existing 38dp geometry and press-dependent style. Compact marks, fields, tracks, panels, drawers and modal dialogs use their own shared role parameters in `HyperGlassStyle.kt` / `HyperControlSurface.kt`; do not copy button optics onto them.
- A role with zero shadow omits shadow drawing completely. Drawer stays rectangular, attached to the window edge and full height; a progress ring stays hollow.
- Preserve theme-token alpha when applying a material multiplier; track and selection tokens are already translucent. Explicit caller colors retain their alpha.
- Pointer-down, focus, validation, disabled, and read-only feedback must appear immediately. Do not wait for click release and do not add decorative looping motion.
- Focus should be obvious without adding a second outline. Error state takes precedence over focus color. Disabled state removes lift and lowers material/foreground contrast.
- Preserve layout stability across states. Focus and validation must not change measured size or padding.

## Compose implementation

- Keep public components responsible for layout, state selection, semantics, and slots. Put reusable material drawing and internal visual data in a focused sibling file.
- Prefer `drawWithCache` for size-dependent brushes. Clip once to the public `shape`, draw the base and material layers inside it, then draw content.
- Keep visual configuration immutable and internal unless callers have a real product need to control it. Do not expose rendering internals as a large styling API.
- Preserve controlled component state and slot-first composition. Do not move business state, validation rules, networking, or navigation into the library.
- Follow the repository `AGENTS.md`, including RGBA color construction, file-size limits, Preview coverage, and documentation synchronization.

## Refactor checklist

1. Inspect the component's existing public API, state ownership, call sites, Preview, and documentation.
2. Decide whether the component is floating, structural, or modal, then set material opacity and shadow accordingly.
3. Keep public styling overrides and semantic borders. Remove an API only within the user's authorized scope, then update all call sites without compatibility aliases.
4. Provide interactive Preview coverage for normal, active/focused, custom-color, error, disabled, and relevant size/layout variants.
5. Synchronize `README.md`, `vitepress/docs/component-index.md`, the component page, and Preview demo metadata.
6. Perform static consistency checks and run the relevant preview or build to verify visual and API changes when the task calls for it.
