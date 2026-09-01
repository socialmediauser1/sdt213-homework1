1. Specification

The goal of the app is to create a simple calculator for a younger student. It should be easy to understand and support the four basic operations: addition, subtraction, multiplication, and division. The user should also be able to enter decimal numbers, clear the calculator, delete a digit, and use either the mouse or keyboard. If someone tries to divide by zero, the calculator should show a clear error message instead of crashing.

2. Product Design and Implementation

I wanted the calculator to be simple and not confusing for a younger user. The interface has a large display and big buttons for numbers and operations. Different types of buttons are visually separated, but they also have clear symbols so the user does not have to depend only on color.

I created the program in Java using Swing. The calculator is separated into the GUI and the calculation logic. The GUI controls the window, buttons, and keyboard input, while the calculator engine performs the actual calculations. I used BigDecimal so decimal calculations give cleaner and more accurate results.

3. Validation

I tested all of the main functions of the calculator. I checked addition, subtraction, multiplication, division, decimal numbers, division by zero, clearing the calculator, and deleting digits. For example, 12 + 8 correctly returned 20, and 9 ÷ 0 displayed Cannot divide by zero. The automated tests passed successfully. I would also manually check that the buttons, keyboard controls, and layout work correctly when the program is opened.

4. Possible Evolution

In the future, I think the most useful improvement would be a practice mode. The calculator could give the student simple questions like 8 × 7 = ? and tell them if their answer is correct. Other possible improvements could include calculation history, larger text settings, and support for brackets. I would still try to keep the app simple because too many features could make it harder for a younger student to use.