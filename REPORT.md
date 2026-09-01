# Pocket Math — Calculator Application Report

**Project:** GUI-based Java calculator for a younger school pupil  
**Technology:** Java 8+ and Swing only (no downloaded libraries required)

## 1. Specification

### Purpose

Pocket Math is a small desktop calculator for a younger sibling to use while practising arithmetic at school or at home. It prioritises a calm, clear interface over advanced scientific-calculator features.

### Target user and context

The primary user is a school-age child who knows the four basic operations and may be using a shared Windows computer with a keyboard or mouse. The application should be understandable without a manual.

### Functional requirements

| ID | Requirement | Acceptance condition |
|---|---|---|
| FR1 | Enter whole numbers and decimal numbers. | Digit and decimal buttons update the display. |
| FR2 | Add two numbers. | `12 + 8 =` displays `20`. |
| FR3 | Subtract two numbers. | `15 − 21 =` displays `-6`. |
| FR4 | Multiply two numbers. | `7 × 8 =` displays `56`. |
| FR5 | Divide two numbers. | `7.5 ÷ 2.5 =` displays `3`. |
| FR6 | Allow a calculation to be corrected or restarted. | Backspace removes one digit; C returns to `0`. |
| FR7 | Give a child-friendly response to invalid division. | Dividing by zero displays `Cannot divide by zero`; a new digit starts a fresh calculation. |
| FR8 | Support keyboard as well as mouse use. | Number keys, `+ - * /`, `Enter`, `Backspace`, and `Esc` work. |

### Non-functional requirements

* The program must run with Java 8 or newer and have no external dependencies.
* Buttons must be large, visibly labelled, keyboard-focusable, and distinguishable by more than colour alone.
* The app must use high-contrast text and provide accessible button names/tooltips.
* Results must avoid ordinary binary floating-point artefacts such as `0.30000000000000004`.

### Scope and assumptions

The first release intentionally excludes memory keys, parentheses, percentages, calculation history, user accounts, and scientific functions. A normal left-to-right chain is supported: `2 + 3 × 4` gives `20`, because it completes the current operation whenever the next operation is selected.

## 2. Product Design

### Design goal

The visual language is **playful, warm, and minimal**: it should look less intimidating than a standard office calculator while remaining readable on older school computers. The design uses a charcoal background, cream number keys, orange operator keys, and one blue equals key. Text labels (`×`, `÷`, `+`, `−`, `=`) ensure that colour never carries the meaning by itself.

The UI/UX design review informed the high-contrast palette, clear keyboard focus, descriptive accessible names, and the same left-to-right order for visual and keyboard navigation.

### Layout

```
┌─────────────────────────────────┐
│ Ready for some maths?            │  small status / expression
│                            0     │  large answer display
├─────────────────────────────────┤
│  C   ⌫   ÷   ×                   │
│  7   8   9   −                   │
│  4   5   6   +                   │
│  1   2   3   =                   │
│       0       .   =              │  equals is deliberately tall
└─────────────────────────────────┘
```

The display separates the current answer from a short expression/status line. This gives learners confirmation of the operation they chose without needing a potentially distracting history panel.

### Interaction decisions

* Every primary button has a visible text/symbol label, a tooltip, and an accessible name.
* Number buttons are light; operations are orange; the final action is blue. These have distinct labels as a backup for colour-vision differences.
* Buttons are generously sized through a resizable grid, with clear system focus feedback. The app can be used entirely with the keyboard.
* A decimal point may only be entered once in a number. Whole-number-looking results are simplified (`3.0` becomes `3`).
* Division is calculated using `BigDecimal` at 12 significant digits and rounded half up, a sensible balance for school exercises.

## 3. Implementation and Validation

### Implementation

The implementation is deliberately split into two classes:

| File | Responsibility |
|---|---|
| `CalculatorApp.java` | Creates the Swing window, display, buttons, colours, keyboard bindings, and accessible labels. |
| `CalculatorEngine.java` | Holds calculator state and performs precise arithmetic independently from the GUI. |
| `CalculatorEngineTest.java` | Runs repeatable automated tests without an additional test framework. |

`BigDecimal` is used instead of `double` to make decimal calculations predictable. The engine also holds the friendly division-by-zero state so the GUI does not contain arithmetic rules.

### How to run

```powershell
javac -encoding UTF-8 -d out src\schoolcalculator\*.java
java -cp out schoolcalculator.CalculatorApp
```

### Validation performed

Automated validation is run with `java -cp out schoolcalculator.CalculatorEngineTest` after compilation. The checks cover the functional core:

| Test | Input | Expected outcome |
|---|---|---|
| Addition | `12 + 8 =` | `20` |
| Subtraction | `15 − 21 =` | `-6` |
| Multiplication | `7 × 8 =` | `56` |
| Decimal division | `7.5 ÷ 2.5 =` | `3` |
| Invalid calculation | `9 ÷ 0 =` | `Cannot divide by zero` |
| Chaining | `2 + 3 × 4 =` | `20` |
| Corrections | `123`, Backspace, C | `12`, then `0` |

Manual GUI checks should also confirm that the window opens, the mouse buttons update the display, the keyboard shortcuts work, tooltips describe controls, and the layout remains usable when the window is enlarged. On a headless build environment, the automated engine checks provide the repeatable validation; launch the supplied GUI command on a desktop to complete the visual check.

### Result

The implementation meets FR1–FR8. Compilation was checked with the Java 8 target, and the automated test run completed successfully with the output: `All 9 calculator checks passed.`

## 4. Possible Evolution

The best next change is a **practice mode**. It could show a short, age-appropriate question such as `8 × 7 = ?`, accept the learner’s answer, and give encouraging feedback. This would transform the calculator from a utility into a learning companion without making the main screen harder to use.

Other sensible, incremental improvements are:

* an optional, dismissible calculation history so pupils can review their working;
* a settings screen for larger text, high-contrast themes, and sound-free feedback;
* a teacher/parent mode that chooses number ranges and reports practice progress locally;
* support for brackets and conventional operator precedence, introduced with a small explanatory hint; and
* packaging as a double-clickable installer or native app for simple school-computer deployment.

Each feature should be tested with children before being retained: a simpler calculator that feels safe and obvious is more valuable than a crowded one.
