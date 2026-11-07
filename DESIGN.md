# 🌿 HealthKu - Design System & Token Specifications
*Managed by Impeccable & UI/UX Pro Max*

## 1. Color Palette (Asclepios Theme)
| Token | Hex | Role | Usage |
| :--- | :--- | :--- | :--- |
| `AsclepiosBg` | `#F7F4EB` | Canvas / Base | Full-screen background (Warm Ivory) |
| `AsclepiosCard` | `#FFFFFF` | Primary Surface | Elevated metric cards & containers |
| `AsclepiosCardBorder` | `#EBE5D8` | Structural Border | 1px border on all cards |
| `AsclepiosSearchBg` | `#EFE8DB` | Secondary Surface | Search pill and inactive badges |
| `SageDark` | `#265135` | Primary Brand | Primary CTA buttons & active icons |
| `SagePillBg` | `#D8EAD9` | Accent Surface | Highlighted score badge & optimal tags |
| `SagePillGreen` | `#2E5B3D` | Accent Text | Text inside score pill & positive badges |
| `MetricHeartBlue` | `#5A8DF4` | Semantic Accent | Heart rate icon & sparkline wave |
| `MetricBpPink` | `#EB607A` | Semantic Accent | Blood pressure icon & sparkline wave |
| `MetricCalorieOrange` | `#EA8641` | Semantic Accent | Calories burned badge |
| `MetricOxygenCyan` | `#38B2AC` | Semantic Accent | Oxygen saturation (SpO2) badge |
| `AsclepiosTextPrimary` | `#1B231D` | Typography | Headings, large numbers, values |
| `AsclepiosTextSecondary` | `#758178` | Typography | Subtitles, labels, units |
| `BlackCapsule` | `#1A1C1A` | High-Impact CTA | Bottom swipe/start reading pill |

## 2. Geometry & Corner Radii
- **Hero / Asclepios Score Card:** `26.dp`
- **Standard Metric Cards:** `20.dp – 22.dp`
- **Search & Filter Bars:** `20.dp`
- **Status Pills / Chips:** `12.dp – 14.dp`
- **Bottom Floating Action Pill:** `30.dp` (Capsule)

## 3. Typography Scale
- **Display Big Readings:** `44.sp` (Bold, letterSpacing = -1.sp)
- **Hero Scores:** `22.sp` (Bold)
- **Section Headings:** `19.sp` (Bold)
- **Card Titles:** `14.sp – 16.sp` (SemiBold / Bold)
- **Body & Subtitles:** `12.sp – 13.sp` (Medium)
- **Micro Badges & Captions:** `10.sp – 11.sp` (Medium)

## 4. Interaction & Touch Guidelines
- Minimum interactive touch bounding box: `44 x 44 dp`
- Smooth Bézier wave interpolation for all health metric sparklines
- Zero dummy data in production; show graceful `--` when awaiting sensor sync
