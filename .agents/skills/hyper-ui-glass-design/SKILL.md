---
name: hyper-ui-glass-design
description: Apply HyperUI's approved HyperOS-inspired frosted-glass visual language when creating, refactoring, or reviewing visual Jetpack Compose components in this repository. Use for HyperUI component surfaces, interaction states, previews, and their documentation; do not use for consumer-app business UI or non-visual maintenance.
---

# HyperUI Glass Design

Use the accepted `HyperIconButton` implementation as the material anchor, then adapt its visual weight to the component's size and role. The result should feel calm, soft, and physical—not glossy, outlined, or neumorphic.

## Material language

- Build glass from a white translucent base in both themes. Let dark backgrounds mix that white into neutral gray; do not switch the material itself to a near-black fill.
- Use one continuous face: a broad weak top light, a slight bottom shade, an in-surface fading edge, and at most one contextual shadow.
- Do not draw hard borders, dark outlines, nested rings, isolated specular arcs, caustic streaks, or multiple stacked shadows.
- A glass edge is a soft change within the material, not a separately configurable `border`. Semantic focus and error indication may temporarily replace the neutral edge with one restrained color cue.
- Keep icons and text high contrast. Tint the material layer rather than reducing foreground legibility.
- Avoid stacking a light glass surface directly on another light glass surface. Glass should normally float over page content or a stable opaque background.

## Hierarchy and state

- Small action controls may float more clearly. The accepted icon-button anchor is 38dp with an 18dp icon, white base alpha `0.72f` in light mode and `0.34f` in dark mode, a 6dp resting shadow, and immediate `0.97f` press scale.
- Do not copy those values literally onto larger surfaces. Larger or structural components need calmer edges and lower apparent lift so they do not look like oversized buttons.
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
3. Remove obsolete hard-border APIs and update all repository call sites when a clean breaking refactor is warranted.
4. Provide interactive Preview coverage for normal, active/focused, custom-color, error, disabled, and relevant size/layout variants.
5. Synchronize `README.md`, `vitepress/docs/component-index.md`, the component page, and Preview demo metadata.
6. Perform static consistency checks. Do not automatically run or build this repository.
