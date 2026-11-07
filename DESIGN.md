# 🔮 LUMI - AI Health Assistant Design System
*Based on the original concept by Afterglow (`dribbble.com/shots/27837777-UI-UX-Design-for-LUMI-AI-Health-Assistant`)*
*Enforced via Anti-Slop, Impeccable, and UI/UX Pro Max*

---

## 1. Design Philosophy
- **Anti-Clinical & Approachable:** Eliminates cold, sterile hospital graphs in favor of a friendly, emotional daily wellness companion.
- **Dynamic Breathing Orb:** The luminous LUMI AI Orb pulses softly to reflect biometric readiness and vitality.
- **Modular Bite-Sized Bento Cards:** Sleep, Heart Rate, Movement, and Vitals organized into clean, scannable containers.

---

## 2. Color Palette (LUMI Official Tokens)
| Token | Hex | Role | Usage |
| :--- | :--- | :--- | :--- |
| `LumiCanvasDark` | `#090A0E` | Deep Canvas | Full-screen background (Obsidian Night) |
| `LumiCardDark` | `#13151D` | Primary Surface | Elevated bento cards & containers |
| `LumiCardBorderDark` | `#222634` | Optical Border | 1px fine container outline |
| `LumiPeriwinkle` | `#7D8EC2` | Mind & Sleep | Sleep architecture, AI accents, primary CTAs |
| `LumiAmber` | `#E2A55E` | Activity & Steps | Movement card, step goals, energy burn |
| `LumiCoral` | `#F16751` | Vitality & Heart | Heart rate pulse, blood pressure, rhythm |
| `LumiSage` | `#5E8C4A` | Recovery Index | Readiness pill, optimal recovery status |
| `LumiLilac` | `#C3B5B4` | Ambient Orb | Inner ethereal highlight of LUMI Orb |
| `LumiTextPrimary` | `#F5F6FA` | Primary Text | Headings, big readings, key values |
| `LumiTextSecondary` | `#8D96A8` | Supporting Text | Body copy, metric units, labels |
| `LumiTextMuted` | `#525968` | Subtle Captions | Timestamps, inactive states |

---

## 3. Motion & Animation Standards
- **LUMI Orb Breathing:** Infinite sinusoidal scaling between `0.92f` and `1.08f` with ambient radial alpha shifts (`0.35f` to `0.75f`).
- **Progress Animation:** All biometric bars animate smoothly (`animateFloatAsState`, tween 900ms).
- **Tactile Feedback:** Cards react with micro-states and smooth rounded corners (`26.dp – 30.dp`).
- **Touch Ergonomics:** All buttons maintain a minimum `44x44dp` tap bounding area.
