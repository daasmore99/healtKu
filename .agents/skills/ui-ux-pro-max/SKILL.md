---
name: ui-ux-pro-max
description: Comprehensive UI/UX design intelligence encyclopedia. Provides proven color harmonies (60-30-10 rule), design styles (Warm Organic, Apple HIG, Neumorphic), typography pairings, chart visualizations, and mobile touch ergonomics.
---

# UI/UX Pro Max - Design Intelligence System
*Based on the open-source knowledge repository by NextLevelBuilder (`nextlevelbuilder/ui-ux-pro-max-skill`)*

UI/UX Pro Max injects battle-tested design patterns, color harmonies, and interaction guidelines directly into the agent's reasoning process.

---

## 1. Color Architecture: The 60-30-10 Rule
Never create visual chaos with conflicting hues. Structure color distribution as follows:
- **60% Dominant Base:** The foundation tone (Background & Canvas). For HealthKu: Soft warm ivory/cream (`#F7F4EB`).
- **30% Secondary Structure:** Cards, surfaces, navigation bars, and text containers (`#FFFFFF`, `#EFE8DB`, `#1B231D`).
- **10% Intentional Accent:** High-impact semantic elements (Status pills, CTAs, Hero score highlight, Heart Rate pulse, SpO2 cyan).

---

## 2. Design Style Guidelines: Warm Organic Minimalist
*(The Asclepios Health Aesthetic)*
- **Philosophy:** Calm, clinical precision blended with human warmth. Evokes high-end wellness clinics, not intimidating hospital screens.
- **Surface Geometry:** Deep rounded corners:
  - Hero Cards: `26dp` radius
  - Standard Metric Cards: `20dp – 22dp` radius
  - Action Pills & Chips: `12dp – 16dp` radius / Full Capsule (`30dp`)
- **Card Framing:** 1px subtle optical border (`#EBE5D8`) over pure white surfaces on warm off-white canvas.
- **Accents:** Earthy sage greens (`#265135`, `#D8EAD9`), warm terracotta/coral (`#EB607A`), serene gold/amber (`#EA8641`).

---

## 3. Mobile Touch Ergonomics & Accessibility
- **Thumb Zone Optimization:** Primary actions (Sync data, live reading triggers, bottom tabs) must be placed in the lower 40% of the mobile viewport.
- **Target Sizing:** Every interactive touch target MUST be at least `44x44dp` (preferably `48x48dp`) bounding box, even if the visible icon is smaller.
- **Contrast Guardrails:**
  - Body text: Minimum 4.5:1 contrast against surface.
  - Large headings & icons: Minimum 3:1 contrast against surface.
  - Never place light gray text on light cream surfaces.

---

## 4. Data Visualization Patterns
- **Sparkline Waves:** Smooth cubic Bézier interpolation curves instead of jagged polylines.
- **Circular Dials & Timers:** Rounded stroke caps with clear beginning/end milestone indicators (`0s`, `15s`, `20s`).
- **Empty States:** When health data is pending from sensors, render subtle placeholder dashes (`--`) with calm helper text, never jarring red error boxes.
