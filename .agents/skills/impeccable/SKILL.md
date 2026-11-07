---
name: impeccable
description: Professional design system craftsman and frontend design director. Enforces persistent design context through DESIGN.md, systematic visual polish, ergonomic typography, and multi-device UI hardening.
---

# Impeccable - Design Craftsmanship & Art Direction
*Based on the open-source design toolkit by Paul Bakaus (`pbakaus/impeccable`)*

Impeccable acts as your digital Art Director and Frontend Design Lead. It bridges the gap between raw code implementation and agency-grade design polish.

---

## 1. Core Operating Principles
- **Design System Fidelity:** Always consult and maintain `DESIGN.md`. Visual updates must strictly align with the established design tokens (palettes, radiuses, shadows, type scales).
- **Subtle Elevation Over Heavy Shadows:** Prefer crisp 1px borders with optical opacity over blurry drop shadows.
- **Intentional Spacing:** Adhere to an 8pt spatial grid with 4pt micro-increments. Every pixel of padding and margin must be deliberate.
- **Ergonomic Hierarchy:** The eye should instinctively know what to look at first (Primary Hero), second (Supporting Metrics), and third (Secondary Navigation).

---

## 2. Design Workflows & Commands

### 🔍 Audit (`audit`)
Systematically inspect screens for:
1. **Hierarchy:** Is the hero element truly prominent, or are multiple cards competing for attention?
2. **Contrast & Legibility:** Does body copy meet WCAG AA standards (minimum 4.5:1)?
3. **Alignment:** Are labels, icons, and values vertically centered and horizontally aligned with strict baselines?
4. **Consistency:** Are icon line weights, border radii, and pill styles uniform throughout?

### ✨ Polish (`polish`)
Apply the final 10% micro-details that make an interface feel premium:
- Refine container paddings (e.g. 16dp vs 20dp).
- Balance icon badges with optical centering inside their circular/pill backgrounds.
- Add subtle border outlines (`#EBE5D8` or 5-10% alpha of primary tint) to cards.
- Smooth out transitions and state changes with natural easing curves.

### 📐 Typeset (`typeset`)
- Ensure clear typographic contrast: Bold titles (e.g., 20–24sp) paired with refined body text (13–15sp) and muted labels (11–12sp).
- Enforce strict `lineHeight` proportional to `fontSize` to prevent text crowding.
- Restrict active font families to a single coherent system (or paired serif/sans with proven harmony).

### 🛡️ Harden (`harden`)
Make interfaces bulletproof across devices and extreme states:
- **Small Screens:** Verify layout on narrow screens (320dp width) without text clipping or horizontal overflow.
- **Large Screens / Tablets:** Ensure cards constrain maximum widths rather than stretching awkwardly.
- **Edge-Case Data States:**
  - Empty state (no data yet): Graceful `--` or helpful empty illustration.
  - Loading state: Smooth skeleton or discreet indicator.
  - Extreme values: Large numbers (e.g., 99,999 steps) truncate or scale down cleanly.

---

## 3. Persistent Project Memory
Whenever new design choices are established (new color accents, card styles, animation timings), immediately record them in `DESIGN.md` at the workspace root so that future iterations maintain unbroken consistency.
