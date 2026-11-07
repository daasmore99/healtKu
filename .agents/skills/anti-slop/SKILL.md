---
name: anti-slop
description: Anti-slop quality filter that strips generic AI tropes, predictable UI templates, robotic copywriting, and redundant code comments. Enforces human-grade design texture, intentional palettes, and clean architecture.
---

# Anti-Slop Filter & Quality Guardrails
*Based on the open-source standard by Miqdad Badjuber (`miqdadbadjuber/anti-slop`)*

This skill acts as a deterministic subtraction layer to eliminate "AI slop"—the mathematical averages and predictable defaults of AI agents. It ensures that output feels human, intentional, and crafted with high taste.

---

## 1. Hard Gates (Absolute Prohibitions)
Breaking any rule in this tier is an automatic **FAIL**, regardless of context:

### 🚫 UI & Visual Design
- **No Cliché AI Gradients:** Strictly ban default saturated indigo-to-purple (`#6366F1` to `#8B5CF6`) and generic cyan-to-violet gradients.
- **No Floating Pill Badges Everywhere:** Never slap random rounded pill chips with generic labels ("New", "AI-Powered", "Smart") over every card without a structural purpose.
- **No Formulaic Bento Grids:** Do not create bento grids just because they are trendy. Grids must map strictly to data hierarchy.
- **No Glowing Neon Borders on Plain Cards:** Never add artificial glow shadows or border gradients unless explicitly designing a specialized cyberpunk/HUD theme.

### 🚫 Copywriting & Language
- **No Robotic Buzzwords:** Ban words like: *unleash, elevate, cutting-edge, empower, seamless, game-changer, dive in, testament, bespoke, navigate, tapestry, realm*.
- **No Fake/Generic Social Proof:** No "Trusted by 10,000+ teams" or artificial placeholder testimonials without real data.
- **No Obvious AI Sentence Structure:** Avoid formulaic headers like *"Unlock the Power of [X]"* or *"Say Goodbye to [Y]"*.

### 🚫 Code & Documentation
- **No Redundant "Echo" Comments:** Never write comments that merely rephrase the code syntax:
  ```kotlin
  // Bad AI Slop:
  // increment counter by 1
  counter++
  // return user list
  return userList
  ```
  Only write comments that explain the **"WHY"** (architectural intent, business constraints, or edge-case handling), never the "WHAT".
- **No Premature Over-Abstraction:** No creating layers of wrappers, handlers, or managers for single trivial function calls.

---

## 2. Purpose-Gates (Conditional Allowed Techniques)
These patterns are strictly forbidden **UNLESS** an explicit functional rationale is provided:
- **Glassmorphism / Backdrop Blur:** Only allowed if content flows underneath during scrolling to preserve depth.
- **Micro-Animations & Motion:** Must serve a feedback purpose (e.g., confirming an action, indicating progress), never purely decorative distraction.
- **Custom Modals / Sheets:** Only use when the user's current context must be preserved, not as an excuse to avoid building a proper screen.

---

## 3. Quality Locks (Standards & Craftsmanship)

### 🎨 Visual Texture & Color
- **Intentional Palettes:** Pick purposeful, curated palettes (e.g., warm earthy organic neutrals, deep slate architectural tones, or high-contrast editorial mono).
- **Subtle Borders & Elevation:** Use fine 1px borders with optical contrast over heavy, muddy drop shadows.
- **Accessibility Hard Lock:** Text must meet WCAG AA contrast ratio (minimum 4.5:1 for body text, 3:1 for large headers).
- **Mobile Touch Targets:** Minimum 44x44dp / 48x48dp for all interactive buttons and tap areas.

### 💻 Code Craftsmanship
- Follow official platform idiomatic patterns (e.g., Jetpack Compose best practices, clean immutable state, unidirectional data flow).
- Fail gracefully with clear, human-understandable error messages instead of generic crash dialogs.
- Zero mock/dummy data leaks in production flows.

---

## 4. Delivery Gate (Pre-Ship Verification)
Before finalizing any code, UI layout, or copy, run this checklist:
- [ ] Is the design free of cliché AI purple/cyan glows and unearned pill badges?
- [ ] Does the copy sound like a knowledgeable human engineer, not an AI marketing bot?
- [ ] Are all code comments meaningful and free of syntax-echoing slop?
- [ ] Are colors, typography, and contrast tuned for real-world readability?
- [ ] Is the code free of unnecessary wrappers and boilerplate?
